package me.chester.trucomineiro.android;

import me.chester.trucomineiro.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import java.lang.reflect.Field;
import java.text.DecimalFormat;


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
 * Processa menus e diálogos comuns à tela de título (
 * <code>TituloActivity</code>) e à tela de jogo (<code>TrucoActivity</code>).
 * 
 * 
 */
public abstract class BaseActivity extends Activity {

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.base, menu);
		return true;
	}
	
	public boolean exibindoSinal = false;
	
	int contaSequencia = 0;
	int imagemSequencia = 0;
	int rapidez = 501;
	String sequencia;

	public Thread exibirSinal;
	
	Spinner cartaEscolhida, jogador, velocidadeExibicao;
	
	long contando = System.currentTimeMillis();
	
	Spinner[] todosSinais = new Spinner[10];
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuitem_regras:
			mostraAlertBox(this.getString(R.string.titulo_regras),
					this.getString(R.string.texto_regras));
			return true;
		case R.id.menuitem_ajuda:
			mostraAlertBox(this.getString(R.string.titulo_ajuda),
					this.getString(R.string.texto_ajuda));
			return true;		
		case R.id.menuitem_creditos:
			mostraAlertBox(this.getString(R.string.titulo_creditos),
					this.getString(R.string.texto_creditos));
			return true;		
		
		case R.id.menuitem_sobre:
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			int partidas = preferences.getInt("statPartidas", 0);
			int vitorias = preferences.getInt("statVitorias", 0);
			int derrotas = preferences.getInt("statDerrotas", 0);
			int abandonou = partidas - (vitorias + derrotas);

			String porcento = "";
			String abandono = "";
			int partidasTotal = partidas;
			
			if (abandonou>0) {
				abandono = " e abandonou " + abandonou + ".";
			} else {
				abandono = " e não abandonou nenhuma.";
			}
			
			DecimalFormat formato = new DecimalFormat("0.00");
			double dpartidas = partidas;
			double dvitorias = vitorias; 			
			if (partidas>0) {
				porcento = formato.format(dvitorias/dpartidas*100);
			}
			
			String stats_versao = "Você já iniciou " + plural("partida", "partidas", partidas) 
					+ " , ganhou " + vitorias + ", perdeu " + derrotas + abandono
					+ "<br/>Aproveitamento de " + porcento + "%"
					+ ".<br/><br/>Seu aproveitamento com os jogadores foi o seguinte:<br/><br/>";
			// pegar por jogador
			
			Resources res = getResources();
			
			String[] nomeJogador = res.getStringArray(res.getIdentifier("jogadorTitulo", "array", "me.chester.trucomineiro"));
			for (int j=0;j<4;j++){
				stats_versao = stats_versao + "Com  " + nomeJogador[j];
				partidas = preferences.getInt("statPartidas" + j, 0);
				vitorias = preferences.getInt("statVitorias" + j, 0);
				derrotas = preferences.getInt("statDerrotas" + j, 0);
				if (partidas>0) {
					dpartidas = partidas;
					dvitorias = vitorias; 
					porcento = formato.format(dvitorias/dpartidas*100);
					abandonou = partidas - (vitorias + derrotas);			
					if (abandonou>0) {
						abandono = " e " + plural("abandono", "abandonos", abandonou) + ".";
					} else {
						abandono = " e não abandonou nenhuma.";
					}
					stats_versao = stats_versao + " foram " + 
							plural("partida", "partidas",  partidas) + ", " + 
							plural("vitoria", "vitorias", vitorias) + ", " +
							plural("derrota", "derrotas", derrotas) + " " + 
							abandono + " Teve " + porcento + "% de aproveitamento";
					dvitorias = partidasTotal;
					porcento = formato.format(dpartidas/dvitorias*100);
					stats_versao = stats_versao + " e disputou " + porcento + "% do total de partidas.<br/><br/>";
				} else stats_versao = stats_versao + " não disputou nenhuma partida<br/><br/>";
			}
			stats_versao = stats_versao + "by ReLuBia";
			mostraAlertBox(this.getString(R.string.titulo_sobre), stats_versao);
			return true;
		case R.id.menuitem_historia:
			mostraAlertBox(this.getString(R.string.titulo_historia),
					this.getString(R.string.texto_historia));			
			return true;
		case R.id.menuitem_sair_titulo:
			if (this.exibindoSinal) {
				setContentView(R.layout.titulo);
				this.exibindoSinal = false;
			} else {
				exibirDialogo("Deseja fechar o Truco Mineiro?", "Sair do jogo?");
			}
			return true;
		case R.id.menuitem_sair_truco:
			exibirDialogo("Deseja fechar a partida atual? (Se a partida estiver em andamento ela será considerada como abandono)", "Encerrar Partida?");
			return true;
		case R.id.menuitem_sinais_truco:
			Intent it = new Intent(BaseActivity.this, SinalActivity.class);
			it.putExtra("nome", this.getClass().getSimpleName().toString());
			startActivity(it);
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void mostraAlertBox(String titulo, String texto) {
		new AlertDialog.Builder(this).setTitle(titulo)
				.setMessage(Html.fromHtml(texto))
				.setIcon(R.drawable.icon)
				.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}
	
	private String plural(String singular, String plural, int valor){
		if (valor==1) {
			return "1 " + singular;
		} else {
			return "" + valor + " " + plural;		
		}
	}
	public void exibirDialogo(String mensagem, String titulo) {
		exibirDialogo(mensagem, titulo, 1);
	}
	public void exibirDialogo(String mensagem, String titulo, int tipo) {
		DialogInterface.OnClickListener dialogClickListener;
		if (tipo==2) {
			dialogClickListener = new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int which) {
			           switch (which){
			           case DialogInterface.BUTTON_POSITIVE:
			               //Ação quando o usuário clicar no botão sim
			        		    
			               break;
		
			           case DialogInterface.BUTTON_NEGATIVE:
			               //Ação quando o usuário clicar no botão Não
			        	   finish();
			               break;
			           }
			       }
			   };			
		} else {
			dialogClickListener = new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int which) {
		           switch (which){
		           case DialogInterface.BUTTON_POSITIVE:
		               //Ação quando o usuário clicar no botão sim
		        		   finish(); 
		               break;
	
		           case DialogInterface.BUTTON_NEGATIVE:
		               //Ação quando o usuário clicar no botão Não
		               break;
		           }
		       }
		   };
		}
		  
		   AlertDialog.Builder builder = new AlertDialog.Builder(this);
		   builder.setTitle(titulo);
		   builder.setIcon(R.drawable.icon);
		   builder.setMessage(mensagem).setPositiveButton("Sim", dialogClickListener)
		       .setNegativeButton("Não", dialogClickListener).show();
	}	
	public void carregarSiais(){
		SharedPreferences preferences;		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		int codigoJogador = (jogador.getSelectedItemPosition() + 2) % 4 + 1;
		String cartaSinal = preferences.getString("cartaSinal" + codigoJogador + "" + cartaEscolhida.getSelectedItemPosition(), "0000000000"); 
		velocidadeExibicao.setSelection(preferences.getInt("rapidez", 3));
		rapidez = (preferences.getInt("rapidez", 3)+1) * 100;
		if (cartaSinal.length()<11) cartaSinal = cartaSinal + "00000000000";
		
		this.sequencia = cartaSinal;
		this.contaSequencia = 0;
		
		for (int i=0;i<10;i++){
			todosSinais[i].setSelection(Integer.parseInt(cartaSinal.substring(i, i+1)));
		}	
	}
	
	public void gravarSinais(){
		String comando = "";
		for (int i=0;i<10;i++) comando=comando+"" + 
				todosSinais[i].getSelectedItemPosition();
		
		this.sequencia = comando;
		this.contaSequencia = 0;
		
		int codigoJogador = (jogador.getSelectedItemPosition() + 2) % 4 + 1;
		
		String cartaSinal = "cartaSinal" + codigoJogador + "" + cartaEscolhida.getSelectedItemPosition();
		
		SharedPreferences preferences;		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = preferences.edit();
		
		e.putString(cartaSinal, comando);
		
		e.commit();
		
	}
	
	public void setVelocidade(){
		if (rapidez!=501){
			rapidez = (velocidadeExibicao.getSelectedItemPosition() + 1) * 100;
			SharedPreferences preferences;		
			preferences = PreferenceManager.getDefaultSharedPreferences(this);
			Editor e = preferences.edit();
			e.putInt("rapidez", velocidadeExibicao.getSelectedItemPosition());
			e.commit();
		}
	}
	
	public static int getResId(String variableName, Class<?> с) {

        Field field = null;
        int resId = 0;
        try {
            field = с.getField(variableName);
            try {
                resId = field.getInt(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resId;

    }	
	
	public void exibirDescricao(View v){
		Resources res = getResources();
		String[] textoJogador = res.getStringArray(res.getIdentifier("jogadorDescricao", "array", "me.chester.trucomineiro"));
				
		jogador = (Spinner) findViewById(R.id.nomeJogador);
		
		this.mostraAlertBox("Informações do jogador", textoJogador[this.jogador.getSelectedItemPosition()]);
	}
}

	