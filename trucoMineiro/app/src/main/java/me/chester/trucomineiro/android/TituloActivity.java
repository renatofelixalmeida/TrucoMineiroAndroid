package me.chester.trucomineiro.android;

import me.chester.trucomineiro.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.KeyEvent;  
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;  
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
 * Tela inicial do jogo. Permite mudar opções e inciar uma partida (
 * <code>TrucoActivity</code>).
 */

public class TituloActivity extends BaseActivity {

	SharedPreferences preferences;
	private AudioManager audio;
	public void handleSelectionChange(Object sender){
	    //handle event
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.titulo);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		boolean mostraInstrucoes = preferences.getBoolean("mostraInstrucoes",
				true);
		if (mostraInstrucoes) {
			Editor e = preferences.edit();
			e.putBoolean("mostraInstrucoes", false);
			// aqui eu vou inicializar as variaveis de sinal para todos os jogadores
			for (int i=1;i<5;i++){
				e.putString("cartaSinal" + i + "0", "1121100000"); // zap
				e.putString("cartaSinal" + i + "1", "1131100000"); // escopeta
				e.putString("cartaSinal" + i + "2", "1141100000"); // espadilha
				e.putString("cartaSinal" + i + "3", "1151100000"); // pica fumo
				e.putString("cartaSinal" + i + "4", "1161100000"); // tres
				e.putString("cartaSinal" + i + "5", "1171100000"); // dois
				e.putString("cartaSinal" + i + "6", "1181100000"); // as
				e.putString("cartaSinal" + i + "7", "1191100000"); // rei
				e.putString("cartaSinal" + i + "8", "1123231100"); // valete
				e.putString("cartaSinal" + i + "9", "1145451100"); // dama
				e.putString("cartaSinal" + i + "10", "1167671100"); // sete
				e.putString("cartaSinal" + i + "11", "1121611000"); // seis
				e.putString("cartaSinal" + i + "12", "1131711000"); // cinco
				e.putString("cartaSinal" + i + "13", "1888881100"); // quatro
			}
			e.commit();
			mostraAlertBox(this.getString(R.string.titulo_ajuda),
					this.getString(R.string.texto_ajuda));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.titulo, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
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
	    	if (this.exibindoSinal) {
	    		exibirSinal.interrupt();
	    		setContentView(R.layout.titulo);
	    		this.exibindoSinal = false;
	    	} else {
	    		exibirDialogo("Deseja fechar o Truco Mineiro?", "Sair do jogo?");
	    	}
			return true;
	    default:
	        return false;
	    }
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuitem_opcoes:
			Intent settingsActivity = new Intent(getBaseContext(),
					OpcoesActivity.class);
			startActivity(settingsActivity);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
		

	public void jogarClickHandler(View v) {
		Intent intent = new Intent(TituloActivity.this, TrucoActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}