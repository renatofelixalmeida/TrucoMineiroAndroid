package me.chester.trucomineiro.android;

import me.chester.trucomineiro.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

public class SinalActivity extends BaseActivity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.exibindoSinal = true;
		setContentView(R.layout.sinal);
		Intent it = getIntent();
		if (it!=null) {
			Bundle p = it.getExtras();
			if (p!=null) {
				String nome = p.getString("nome");
				if (nome.equals("TrucoActivity"))
					exibirDialogo("As alterações de sinais somente serão aplicadas ao iniciar nova partida. Deseja continuar?", "Truco Mineiro", 2);
			}
		}
		
		cartaEscolhida = (Spinner) findViewById(R.id.carta);
		
		this.todosSinais[0] = (Spinner) findViewById(R.id.sinal1);
		this.todosSinais[1] = (Spinner) findViewById(R.id.sinal2);
		this.todosSinais[2] = (Spinner) findViewById(R.id.sinal3);
		this.todosSinais[3] = (Spinner) findViewById(R.id.sinal4);
		this.todosSinais[4] = (Spinner) findViewById(R.id.sinal5);
		this.todosSinais[5] = (Spinner) findViewById(R.id.sinal6);
		this.todosSinais[6] = (Spinner) findViewById(R.id.sinal7);
		this.todosSinais[7] = (Spinner) findViewById(R.id.sinal8);
		this.todosSinais[8] = (Spinner) findViewById(R.id.sinal9);
		this.todosSinais[9] = (Spinner) findViewById(R.id.sinal10);
		jogador = (Spinner) findViewById(R.id.nomeJogador);
		velocidadeExibicao = (Spinner) findViewById(R.id.velocidadeExibicao);
					
		for (int i=0; i<10;i++) {
			todosSinais[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
			    	if (contando < System.currentTimeMillis()) gravarSinais();
			    } 

			    public void onNothingSelected(AdapterView<?> adapterView) {
			        return;
			    } 
			}); 
			
		}
		
	
		cartaEscolhida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
		       
		    	contando = System.currentTimeMillis() + 1000;
		    	carregarSiais();
		    } 

		    public void onNothingSelected(AdapterView<?> adapterView) {
		        return;
		    } 
		});

		velocidadeExibicao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
		    	setVelocidade();
		    } 

		    public void onNothingSelected(AdapterView<?> adapterView) {
		        return;
		    } 
		});
		
		exibirSinal = new Thread() {
			@Override
			public void run(){
				try {
					while (true) {
						if (!exibindoSinal) exibirSinal.interrupt();
						Thread.sleep(rapidez);							
						SinalActivity.this.runOnUiThread(new Runnable() {
					        public void run() {
					        	Sinais sinais = new Sinais(400);
								if (sequencia.length()<10) sequencia = sequencia + "0000000000";
								
								imagemSequencia = Integer.parseInt(sequencia.substring(contaSequencia, contaSequencia+1)) - 1;
								
								if (imagemSequencia<0||imagemSequencia>9)contaSequencia = 0;
								
									
								if (imagemSequencia>=0){
									imagemSequencia++;
									//int codJogador =  (jogador.getSelectedItemPosition() + 2) % 4 + 1;
									int codJogador =  (jogador.getSelectedItemPosition()) % 4 + 1;
									//int recurso = getResId("jogador"+ codJogador + imagemSequencia, R.drawable.class);
									ImageView foto = (ImageView) findViewById(R.id.foto1);
									//if (recurso>0&&foto!=null) foto.setImageResource(recurso);
									if (foto!=null) foto.setImageBitmap(sinais.pegar(codJogador, imagemSequencia - 1));
								}
								
								contaSequencia++;
								
								if (contaSequencia>9) contaSequencia=0;
					        }
					    });							
						
					}
            	}
					catch (InterruptedException e) {
            	}
			}
		};		
		
		exibirSinal.start();
		
	}
	public void sairSinal(View v){
		finish();
	}
}
