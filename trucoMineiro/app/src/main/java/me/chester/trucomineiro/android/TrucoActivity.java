package me.chester.trucomineiro.android;

import me.chester.trucomineiro.R;
import me.chester.trucomineiro.core.JogadorCPU;
import me.chester.trucomineiro.core.SoundManager;
import me.chester.trucomineiro.core.Jogo;
import me.chester.trucomineiro.core.JogoLocal;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.Context; 
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import android.view.KeyEvent;  
import android.media.AudioManager;

/*
 * Copyright © 2005-2012 Carlos Duarte do Nascimento "Chester" <cd@pobox.com>
 * Todos os direitos reservados.
 *
 * A redistribuição e o uso nas formas binária e código fonte, com ou sem
 * modificações, são permitidos contanto que as condições abaixo sejam
 * cumpridas:
 * 
 * - Redistribuições do código fonte devem conter o aviso de direitos
 *   autorais acima, esta lista de condições e o aviso de isenção de
 *   garantias subseqüente.
 * 
 * - Redistribuições na forma binária devem reproduzir o aviso de direitos
 *   autorais acima, esta lista de condições e o aviso de isenção de
 *   garantias subseqüente na documentação e/ou materiais fornecidos com
 *   a distribuição.
 *   
 * - Nem o nome do Chester, nem o nome dos contribuidores podem ser
 *   utilizados para endossar ou promover produtos derivados deste
 *   software sem autorização prévia específica por escrito.
 * 
 * ESTE SOFTWARE É FORNECIDO PELOS DETENTORES DE DIREITOS AUTORAIS E
 * CONTRIBUIDORES "COMO ESTÁ", ISENTO DE GARANTIAS EXPRESSAS OU TÁCITAS,
 * INCLUINDO, SEM LIMITAÇÃO, QUAISQUER GARANTIAS IMPLÍCITAS DE
 * COMERCIABILIDADE OU DE ADEQUAÇÃO A FINALIDADES ESPECÍFICAS. EM NENHUMA
 * HIPÓTESE OS TITULARES DE DIREITOS AUTORAIS E CONTRIBUIDORES SERÃO
 * RESPONSÁVEIS POR QUAISQUER DANOS, DIRETOS, INDIRETOS, INCIDENTAIS,
 * ESPECIAIS, EXEMPLARES OU CONSEQUENTES, (INCLUINDO, SEM LIMITAÇÃO,
 * FORNECIMENTO DE BENS OU SERVIÇOS SUBSTITUTOS, PERDA DE USO OU DADOS,
 * LUCROS CESSANTES, OU INTERRUPÇÃO DE ATIVIDADES), CAUSADOS POR QUAISQUER
 * MOTIVOS E SOB QUALQUER TEORIA DE RESPONSABILIDADE, SEJA RESPONSABILIDADE
 * CONTRATUAL, RESTRITA, ILÍCITO CIVIL, OU QUALQUER OUTRA, COMO DECORRÊNCIA
 * DE USO DESTE SOFTWARE, MESMO QUE HOUVESSEM SIDO AVISADOS DA
 * POSSIBILIDADE DE TAIS DANOS.
 * 
 */

/**
 * Activity onde os jogos (partidas) efetivamente acontecem..
 * <p>
 * Ela inicializa o jogo e exibe sa cartas, "balões" de texto e diálogos através
 * de uma <code>MesaView</code>.
 * 
 * 
 */
@SuppressWarnings("unused")
public class TrucoActivity extends BaseActivity {

	private static final String[] TEXTO_BOTAO_AUMENTO = { "Truco", "Seis!",
			"Nove!", "Doze!!!" };
	
	private AudioManager audio;
	private SoundManager sons;
	private MesaView mesa;
	private View layoutFimDeJogo;
	private static boolean mIsViva = false;
	boolean jogoAbortado = false;
	
	JogadorHumano jogadorHumano;

	Jogo jogo;

	int[] placar = new int[2];
	
	static final int MSG_ATUALIZA_PLACAR = 0;
	static final int MSG_TIRA_DESTAQUE_PLACAR = 1;
	static final int MSG_OFERECE_NOVA_PARTIDA = 2;
	static final int MSG_REMOVE_NOVA_PARTIDA = 3;
	static final int MSG_MOSTRA_BOTAO_AUMENTO = 4;
	static final int MSG_ESCONDE_BOTAO_AUMENTO = 5;
	static final int MSG_MOSTRA_BOTAO_ABERTA_FECHADA = 6;
	static final int MSG_ESCONDE_BOTAO_ABERTA_FECHADA = 7;
	static final int MSG_ESCONDE_PATROCINIO = 8;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			TextView tvNos = (TextView) findViewById(R.id.textview_nos);
			TextView tvEles = (TextView) findViewById(R.id.textview_eles);
			Button btnAumento = (Button) findViewById(R.id.btnAumento);
			Button btnAbertaFechada = (Button) findViewById(R.id.btnAbertaFechada);
			switch (msg.what) {
			case MSG_ATUALIZA_PLACAR:
				tvEles.setText("\n\n\n");
				tvNos.setText("\n\n\n");
				placar[0] = msg.arg1;
				placar[1] = msg.arg2;
				break;
			case MSG_TIRA_DESTAQUE_PLACAR:
				tvNos.setBackgroundColor(Color.TRANSPARENT);
				tvEles.setBackgroundColor(Color.TRANSPARENT);
				break;
			case MSG_OFERECE_NOVA_PARTIDA:
				if (jogo instanceof JogoLocal) {
					layoutFimDeJogo.setVisibility(View.VISIBLE);
				}
				break;
			case MSG_REMOVE_NOVA_PARTIDA:
				layoutFimDeJogo.setVisibility(View.INVISIBLE);
				break;
			case MSG_MOSTRA_BOTAO_AUMENTO:
				btnAumento
						.setText(TEXTO_BOTAO_AUMENTO[(jogadorHumano.valorProximaAposta / 3) - 1]);
				btnAumento.setVisibility(Button.VISIBLE);
				break;
			case MSG_ESCONDE_BOTAO_AUMENTO:
				btnAumento.setVisibility(Button.GONE);
				break;
			case MSG_MOSTRA_BOTAO_ABERTA_FECHADA:
				btnAbertaFechada.setText(mesa.vaiJogarFechada ? "Aberta"
						: "Fechada");
				btnAbertaFechada.setVisibility(Button.VISIBLE);
				break;
			case MSG_ESCONDE_BOTAO_ABERTA_FECHADA:
				btnAbertaFechada.setVisibility(Button.GONE);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * Cria um novo jogo e dispara uma thread para ele. Para jogos multiplayer,
	 * a criação é terceirizada para a classe apropriada.
	 * <p>
	 * Este método é chamada pela primeira vez a partir da MesaView (para
	 * garantir que o jogo só role quando ela estiver inicializada) e dali em
	 * diante pelo botão de nova partida.
	 */
	public void criaEIniciaNovoJogo() {
		jogadorHumano = new JogadorHumano(this, mesa);
		int jogadorAbriu = 1;
		if (jogo!=null) jogadorAbriu = jogo.jogadorAbriu + 1;
		if (jogadorAbriu==5) jogadorAbriu = 1;
		jogo = criaNovoJogoSinglePlayer(jogadorHumano, jogadorAbriu);
		(new Thread(jogo)).start();
	}
	
	/* emite um som da lista de sons de acordo com o que foi cadastrado */
	public void falaMensagem(String mensagem,int posicao){
		if (sons.CarregouSons&&tipoGrito!=1) {
			if ((mensagem.indexOf("1")        > 0) && (mensagem.indexOf("mento") > 0)) sons.playSound(posicao); 
			if ((mensagem.indexOf("2")        > 0) && (mensagem.indexOf("mento") > 0)) sons.playSound(posicao + 4);
			if ((mensagem.indexOf("3")        > 0) && (mensagem.indexOf("mento") > 0)) sons.playSound(posicao + 8);
			if ((mensagem.indexOf("4")        > 0) && (mensagem.indexOf("mento") > 0)) sons.playSound(posicao + 8);
			if ((mensagem.indexOf("sim")      > 0) && (mensagem.indexOf("mento") > 0)) sons.playSound(posicao + 12);
			if ((mensagem.indexOf("nao")      > 0) && (mensagem.indexOf("mento") > 0)) sons.playSound(posicao + 16);
			if ((mensagem.indexOf("sim")      > 0) && (mensagem.indexOf("11")    > 0)) sons.playSound(posicao + 20);
			if ((mensagem.indexOf("nao")      > 0) && (mensagem.indexOf("11")    > 0)) sons.playSound(posicao + 24);
			if (mensagem.indexOf("oria")      > 0)                                     sons.playSound(posicao + 28);
			if (mensagem.indexOf("pata_jogo") > 0)                                     sons.playSound(posicao + 32);
			if (mensagem.indexOf("nha_jogo")  > 0) sons.playSound(posicao + 36);
		}
	}	
	public int valorSinal = 0;
	public int sinalForca = 20;
	public int tipoGrito = 0;
	
	private Jogo criaNovoJogoSinglePlayer(JogadorHumano humano, int jogadorAbriu) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// pegar os dados dos jogadores
		int meuJogador = Integer.parseInt(preferences.getString("meuJogador", "0")) ;
		int codJogador = meuJogador + 1;
		// pegar os sinais definidos pelo usuário
		for (int j=0;j<10;j++) {
			String cartaSinal = preferences.getString("cartaSinal"+ codJogador + "" + j, "0000000000");
			for (int i=0;i<10;i++){
				mesa.codigoSinal[j][i]=Integer.parseInt(cartaSinal.substring(i, i+1))-1;
			}		
		}

		valorSinal = Integer.parseInt(preferences.getString("tipoSinal", "4"));
		sinalForca = Integer.parseInt(preferences.getString("listaForcaSinal", "20"));
		tipoGrito = Integer.parseInt(preferences.getString("gritar", "0"));
		int forca1 = Integer.parseInt(preferences.getString("forca1", "3"));
		int forca2 = Integer.parseInt(preferences.getString("forca2", "3"));
		int forca3 = Integer.parseInt(preferences.getString("forca3", "3"));

		
		if (forca1==0) forca1=(new Random()).nextInt(5) + 1;
		if (forca2==0) forca2=(new Random()).nextInt(5) + 1;
		if (forca3==0) forca3=(new Random()).nextInt(5) + 1;
		
		
		forca1=forca1-1;
		forca2=forca2-1;
		forca3=forca3-1;
		
		int[] forcas = {forca1, forca2, forca3};

		boolean maoDezAberta = preferences.getBoolean("maoDezAberta", true);
		boolean doisFechada = preferences.getBoolean("doisFechada", true);
		boolean amarraAbre = preferences.getBoolean("amarraAbre", true);

		// até aqui ok, está pegando os dados e criando as forças
		
		Jogo novoJogo = new JogoLocal(meuJogador, jogadorAbriu, maoDezAberta, doisFechada, amarraAbre );
		
		novoJogo.adiciona(jogadorHumano);

		for (int i = 2; i <= 4; i++) {
			novoJogo.adiciona(new JogadorCPU(forcas[i-2]));
		}

		return novoJogo;
	}

	public void carregarSons(int indice){
		
		if (indice==0) {
			sons = new SoundManager();
	        sons.initSounds(getBaseContext());	
		}
		
		try {
			switch(indice) {
			case 1:
				Sinais sinais = new Sinais(MesaView.larguraSinal);
				for (int cont=1;cont<10;cont++) 
					MesaView.sinais[cont-1] = sinais.pegar((jogo.meuJogador + 2) % 4 + 1, cont - 1);
					
				mesa.carregouSinais = true;
				for (int cont=1;cont<5;cont++) sons.addSound(cont, getResId("truco"+cont,R.raw.class));
		        break;
			case 2:
				for (int cont=1;cont<5;cont++) sons.addSound(cont+4, getResId("seis"+cont,R.raw.class));
		        break;
			case 3:
				for (int cont=1;cont<5;cont++) sons.addSound(cont+8, getResId("nove"+cont,R.raw.class));
		        break;
			case 4:
				for (int cont=1;cont<5;cont++) sons.addSound(cont+12, getResId("aceita"+cont,R.raw.class));
		        break;
			case 5:
				for (int cont=1;cont<5;cont++) sons.addSound(cont+16, getResId("recusa"+cont,R.raw.class));
		        break;
			case 6:
				for (int cont=1;cont<5;cont++) sons.addSound(cont+20, getResId("onze_sim"+cont,R.raw.class));
		        break;
			case 7:
				for (int cont=1;cont<5;cont++) sons.addSound(cont+24, getResId("onze_nao"+cont,R.raw.class));
		        break;
			case 8:
				for (int cont=1;cont<5;cont++) sons.addSound(cont+28, getResId("vitoria"+cont,R.raw.class));
		        break;
			case 9:
				for (int cont=1;cont<5;cont++) sons.addSound(cont+32, getResId("empata_jogo"+cont,R.raw.class));
		        break;
			case 10:
				for (int cont=1;cont<5;cont++) sons.addSound(cont+36, getResId("ganha_jogo"+cont,R.raw.class));
		        sons.CarregouSons = true;
			};
		}catch (Exception e) {
			// deixa arder
		}	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//carregarSons();
		audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.truco);
		
		mIsViva = true;
		mesa = ((MesaView) findViewById(R.id.MesaView01));
		layoutFimDeJogo = findViewById(R.id.layoutFimDeJogo);
		mesa.carregouSinais = false;
		mesa.setTrucoActivity(this);
		// Inicializa componentes das classes visuais que dependem de métodos
		// disponíveis exclusivamente na Activity
		if (MesaView.iconesRodadas == null) {
			MesaView.iconesRodadas = new Bitmap[4];
		}
		// iniciar os icones de sinais
		if (MesaView.sinais == null) {
			MesaView.sinais = new Bitmap[12];
		}
		
		if (CartaVisual.resources == null) {
			CartaVisual.resources = getResources();
		}
		
		Thread abreSons = new Thread() {
			int i = 0;
			@Override
			public void run(){
				try {
					for (i=0;i<11;i++) {
						Thread.sleep(200);
						carregarSons(i);
					}
            	}
					catch (InterruptedException e) {
            	}
			}
		};		
		
		abreSons.start();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.truco, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuitem_sair_truco:
			if (jogo.jogoFinalizado) {
				finish();
			} else {
				exibirDialogo("Se você fechar a partida atual ela será considerada como abandono nas estatísticas. Deseja encerrar a partida?", "Encerrar Partida?");
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch (keyCode) {
	    case KeyEvent.KEYCODE_VOLUME_UP:
	        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
	                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
	        return true;
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
	        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
	                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
	        return true;
	    case KeyEvent.KEYCODE_BACK:
			if (jogo.jogoFinalizado) {
				finish();
			} else {
				exibirDialogo("Se você fechar a partida atual ela será considerada como abandono nas estatísticas. Deseja encerrar a partida?", "Encerrar Partida?");
			}
			return true;

	    default:
	        return false;
	    }
	}
	


	public void novaPartidaClickHandler(View v) {
		Message.obtain(handler, MSG_REMOVE_NOVA_PARTIDA).sendToTarget();
		criaEIniciaNovoJogo();
	}

		 
	public void aumentoClickHandler(View v) {
		handler.sendMessage(Message.obtain(handler, MSG_ESCONDE_BOTAO_AUMENTO));
		mesa.setStatusVez(MesaView.STATUS_VEZ_HUMANO_AGUARDANDO);
		jogo.aumentaAposta(jogadorHumano);
	}

	public void abertaFechadaClickHandler(View v) {
		mesa.vaiJogarFechada = !mesa.vaiJogarFechada;
		handler.sendMessage(Message.obtain(handler,
				MSG_MOSTRA_BOTAO_ABERTA_FECHADA));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mesa.setVisivel(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mesa.setVisivel(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mIsViva = false;
		if (!jogoAbortado) {
			jogo.abortaJogo(1);
		}
	}

	public static boolean isViva() {
		return mIsViva;
	}
	
}
