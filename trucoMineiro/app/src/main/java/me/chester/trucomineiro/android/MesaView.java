package me.chester.trucomineiro.android;

import java.util.Vector;

import me.chester.trucomineiro.core.Carta;
import me.chester.trucomineiro.core.Jogador;
import me.chester.trucomineiro.core.Jogo;
import me.chester.trucomineiro.core.Pontos;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
 * Representa visualmente o andamento de um jogo, permitindo que o usuário
 * interaja.
 * <p>
 * Para simplificar o acesso, alguns métodos/propriedades são static - o que só
 * reitera que só deve existir uma instância desta View.
 * 
 * 
 */
@SuppressWarnings("unused")
@SuppressLint({ "DrawAllocation", "DrawAllocation", "ClickableViewAccessibility" })
public class MesaView extends View {

	private int posicaoVez;
	private Bitmap pontos1;
	private Bitmap pontos2;
	private Pontos pontos;
	private int jogoPrimeiro = -1;
	private int jogos1 = 0;
	private int jogos2 = 0;
	
	int codigoSinal[][] = {
		// 0 normal - 1 olhar cima - 2 olhar baixo - 3 olhar direita - 4 olhar esquerda
		// 5 fechar direito - 6 fechar esquerdo - 7 fechar os dois - 8 mexer boca
		{0, 0, 1, 0, 0, -1, 0, 0, 0, -1 }, // zap
		{0, 0, 2, 0, 0, -1, 0, 0, 0, -1 }, // escopeta
		{0, 0, 3, 0, 0, -1, 0, 0, 0, -1 }, // espadilha
		{0, 0, 4, 0, 0, -1, 0, 0, 0, -1 }, // pica fumo
		{0, 0, 5, 0, 0, -1, 0, 5, 0, -1 }, // tres
		{0, 0, 6, 0, 0, -1, 0, 6, 0, -1 }, // dois
		{0, 0, 7, 0, 0, -1, 0, 7, 0, -1 }, // az
		{0, 0, 5, 0, 3, 0, 0, -1, 0, -1 }, // rei
		{0, 0, 6, 0, 4, 0, 0, -1, 0, -1 }, // valete
		{0, 0, 3, 0, 4, 0, 0, -1, 0, -1 }, // dama
		{0, 6, 5, 6, 5, 6, 5, 0, -1, -1 }, // sete
		{0, 0, 3, 0, 7, 0, -1, 0, 0, -1 }, // seis
		{0, 0, 2, 0, 7, 0, -1, 0, 0, -1 }, // cinco
		{7, 7, 7, 7, 7, -1, 0, 0, 0, -1 }, // quatro 
	};
	private int contaSinal = 0;
	
	public MesaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	
	}

	public MesaView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MesaView(Context context) {
		super(context);
	}

	public void setTrucoActivity(TrucoActivity trucoActivity) {
		this.trucoActivity = trucoActivity;
		//this.trucoActivity.jogo.
	}

	/**
	 * Informa à mesa que uma animação começou (garantindo refreshes da tela
	 * enquanto ela durar).
	 * 
	 * @param fim
	 *            timestamp de quando a animação vai acabar
	 */
	public static void notificaAnimacao(long fim) {
		if (animandoAte < fim) {
			animandoAte = fim;
		}
	}

	/**
	 * Coloca a thread chamante em sleep até que as animações acabem.
	 */
	public void aguardaFimAnimacoes() {
		long milisecAteFimAnimacao;
		while ((milisecAteFimAnimacao = animandoAte
				- System.currentTimeMillis()) > 0) {
			try {
				Thread.sleep(milisecAteFimAnimacao);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	/**
	 * Ajusta o tamanho das cartas e sua posição de acordo com a resolução.
	 * <p>
	 * Na primeira chamada, incializa as cartas e dá início à partida (tem que
	 * ser feito aqui porque não é uma boa idéia começar sem saber onde as
	 * coisas vão aparecer)
	 */
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		double ratioLargura = w / (180 * 1.5) * 180;
		larguraSinal = (int) (ratioLargura / 1.3);
		// Ajusta o tamanho da carta (tudo depende dele) ao da mesa e os
		// "pontos de referência" importantes (baralho decorativo, tamanho do
		// texto, etc.)
		CartaVisual.ajustaTamanho(w, h);
		double rLargura = w / (180 * 6.0);
		double rAltura = h / (252 * 5.0);
		double ratioCarta = Math.min(rLargura, rAltura);
		int l = (int) (180 * ratioCarta);
		int a = (int) (252 * ratioCarta);
		pontos = new Pontos(l, a);
		for (int i=0;i<4;i++) iconesRodadas[i] = pontos.marcadorRodada(i);
		pontos1 = pontos.atualizaPontos(0, 0);
		pontos2 = pontos.atualizaPontos(0, 0);

		leftBaralho = w - CartaVisual.largura - MARGEM - 4;
		topBaralho = MARGEM + 4;
		this.topBaralho = calcPosTopCarta((3) % 4 + 1, 1);
		this.leftBaralho = calcPosLeftCarta((3) % 4 + 1, 1);

		tamanhoFonte = 12.0f * (h / 270.0f);

		// Na primeira chamada (inicialização), instanciamos as cartas
		if (!inicializada) {
			for (int i = 0; i < cartas.length; i++) {
				cartas[i] = new CartaVisual(this, leftBaralho, topBaralho, null);
				cartas[i].movePara(leftBaralho, topBaralho);
			}
			cartas[0].visible = false;
		}
		// Define posição e tamanho da caixa de diálogo e seus botões
		int alturaDialog = CartaVisual.altura + 8;
		int larguraDialog = CartaVisual.largura * 3;
		int topDialog = (h - alturaDialog) / 2;
		int leftDialog = (w - larguraDialog) / 2;
		rectDialog = new Rect(leftDialog, topDialog,
				leftDialog + larguraDialog, topDialog + alturaDialog);
		int alturaBotao = (int) (tamanhoFonte * 1.8f);
		rectBotaoSim = new RectF(leftDialog + 8, topDialog + alturaDialog
				- alturaBotao - 8, leftDialog + larguraDialog / 2 - 8,
				topDialog + alturaDialog - 8);
		rectBotaoNao = new RectF(leftDialog + larguraDialog / 2 + 8,
				rectBotaoSim.top, leftDialog + larguraDialog - 8,
				rectBotaoSim.bottom);

		// Posiciona o vira e as cartas decorativas do baralho, que são fixos
		cartas[0].movePara(leftBaralho, topBaralho);
		cartas[1].movePara(leftBaralho + 4, topBaralho + 4);
		cartas[2].movePara(leftBaralho + 2, topBaralho + 2);
		cartas[3].movePara(leftBaralho, topBaralho);
		
		// experiência se for desfazer é aqui
		cartas[0].visible = false;
		cartas[1].visible = false;
		cartas[2].visible = false;
		cartas[3].visible = false;
		
		if (!inicializada) {
			// Inicia as threads internas que cuidam de animações e de responder
			// a diálogos e faz a activity começar o jogo
			animacaoJogo.start();
			respondeDialogos.start();
			inicializada = true;
			if (this.trucoActivity != null) {
				this.trucoActivity.criaEIniciaNovoJogo();
			}
		} else {
			// Rolou um resize, reposiciona as cartas não-decorativas
			for (int i = 0; i <= 15; i++) {
				CartaVisual cv = cartas[i];
				if (cv != null) {
					cv.resetBitmap();
					if (i >= 4) {
						int numJogador = (i - 1) / 3;
						if (trucoActivity.jogo.jogoFinalizado) {
							cv.movePara(leftBaralho, topBaralho);
						} else if (cv.descartada) {
							cv.movePara(calcPosLeftDescartada(numJogador),
									calcPosTopDescartada(numJogador));
						} else {
							int pos = (i - 1) % 3;
							cv.movePara(calcPosLeftCarta(numJogador, pos),
									calcPosTopCarta(numJogador, pos));
						}
					}
				}
			}
		}
	}

	/**
	 * Recupera a carta visual correspondente a uma carta do jogo.
	 * 
	 * @param c
	 *            carta do jogo
	 * @return Carta visual com o valor desta, ou <code>null</code> se não achar
	 */
	public CartaVisual getCartaVisual(Carta c) {
		for (CartaVisual cv : cartas) {
			if (c != null && c.equals(cv)) {
				return cv;
			}
		}
		return null;
	}

	/**
	 * Atualiza o resultado de uma rodada, destacando a carta vencedora e
	 * piscando a rodada atual por um instante.
	 * 
	 * @param numRodada
	 *            rodada que finalizou
	 * @param resultado
	 *            (0 a 3, vide {@link #resultadoRodada}
	 * @param jogadorQueTorna
	 *            jogador cuja carta venceu a rodada
	 */
	public void atualizaResultadoRodada(int numRodada, int resultado,
			Jogador jogadorQueTorna) {
		aguardaFimAnimacoes();
		if (resultado != 3) {
			cartaQueFez = getCartaVisual(trucoActivity.jogo
					.getCartasDaRodada(numRodada)[jogadorQueTorna.getPosicao() - 1]);
			cartaQueFez.destacada = true;
		}
		for (CartaVisual c : cartas) {
			c.escura = c.descartada;
		}
		resultadoRodada[numRodada - 1] = resultado;
		numRodadaPiscando = numRodada;
		rodadaPiscaAte = System.currentTimeMillis() + 1600;
		notificaAnimacao(rodadaPiscaAte);
	}

	/**
	 * Torna as cartas da mão de 11 visíveis
	 * 
	 * @param cartasParceiro
	 *            cartas do seu parceiro
	 */
	public void mostraCartasMao10(Carta[] cartasParceiro) {
		if (this.trucoActivity.jogo.maoDezAberta) {
			for (int i = 0; i <= 2; i++) {
				cartas[10 + i].setCarta(cartasParceiro[i]);
			}
		}
	}

	/**
	 * Faz com que o balão mostre uma frase por um tempo para um jogador.
	 * <p>
	 * As frases estão no strings.xml no formato balao_<chave>, e são arrays de
	 * strings (das quais uma será sorteada para exibição).
	 * 
	 * @param chave
	 *            diz o tipo de texto que aparece no balão. Ex.: "aumento_3"
	 *            para pedido de truco.
	 * @param posicao
	 *            posição (1 a 4) do jogador que "dirá" a frase
	 * @param tempoMS
	 *            tempo em que ela aparecerá
	 */
	public void diz(String chave, int posicao, int tempoMS) {
		aguardaFimAnimacoes();
		calculaJogosPlacar();
		/* definir a posicao do meu jogador para falar as mensagnes */
		int posicaoMensagem = posicao + trucoActivity.jogo.meuJogador;
		if (posicaoMensagem>4) posicaoMensagem = posicaoMensagem - 4;
		
		trucoActivity.falaMensagem(chave, posicaoMensagem);
			
		mostraBalaoAte = System.currentTimeMillis() + tempoMS;
		
		Resources res = getResources();
		
		String[] frasesBalao = res.getStringArray(res.getIdentifier("balao_"
				+ chave, "array", "me.chester.trucomineiro"));
		if (trucoActivity.tipoGrito==2) {
			fraseBalao = null;
		} else {
			fraseBalao = frasesBalao[posicaoMensagem - 1];
		}
		posicaoBalao = posicao;
		
		notificaAnimacao(mostraBalaoAte);
		
	}

	/**
	 * Verifica qual elemento foi tocado (ex.: uma das cartas do jogador ou um
	 * dos botões) e executa a ação associada a ele.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return true;
		case MotionEvent.ACTION_UP:
			if (mostrarPerguntaMao10) {
				if (rectBotaoSim.contains((int) event.getX(),
						(int) event.getY())) {
					mostrarPerguntaMao10 = false;
					aceitarMao10 = true;
				}
				if (rectBotaoNao.contains((int) event.getX(),
						(int) event.getY())) {
					mostrarPerguntaMao10 = false;
					recusarMao10 = true;
				}
			}
			if (mostrarPerguntaAumento) {
				if (rectBotaoSim.contains((int) event.getX(),
						(int) event.getY())) {
					mostrarPerguntaAumento = false;
					aceitarAumento = true;
				}
				if (rectBotaoNao.contains((int) event.getX(),
						(int) event.getY())) {
					mostrarPerguntaAumento = false;
					recusarAumento = true;
				}
			}
			if (statusVez == STATUS_VEZ_HUMANO_OK) {
				for (int i = 6; i >= 4; i--) {
					if ((!cartas[i].descartada)
							&& cartas[i].isDentro(event.getX(), event.getY())) {
						statusVez = STATUS_VEZ_OUTRO;
						cartas[i].setFechada(vaiJogarFechada);
						trucoActivity.jogo.jogaCarta(
								trucoActivity.jogadorHumano, cartas[i]);
					}
				}
			}
			return true;
		default:
			return super.onTouchEvent(event);
		}
	}

	private TrucoActivity trucoActivity;

	private Rect rectDialog;

	private RectF rectBotaoSim;

	private RectF rectBotaoNao;

	private float tamanhoFonte;

	/**
	 * Cartas que estão na mesa, na ordem de empilhamento. cartas[0] é o vira,
	 * cartas[1..3] são o baralho decorativo, cartas[4..6] são as do jogador na
	 * posição 1 (inferior), cartas[7..9] o jogador 2 e assim por diante para os
	 * jogadores 3 e 4.
	 * 
	 * TODO: refatorar esses magic numbers para algo melhor.
	 */
	public CartaVisual[] cartas = new CartaVisual[16];

	/**
	 * É true se a view já está pronta para responder a solicitações do jogo
	 * (mover cartas, acionar balões, etc)
	 */
	private boolean inicializada = false;

	private long calcTempoAteFimAnimacaoMS() {
		return animandoAte - System.currentTimeMillis();
	}

	/**
	 * Thread/runnable que faz as animações acontecerem.
	 * <p>
	 */
	Thread animacaoJogo = new Thread(new Runnable() {

		// Para economizar CPU/bateria, o jogo trabalha a um máximo de 4 FPS
		// (1000/(200+50)) quando não tem nenhuma animação rolando, e sobe para
		// um máximo de 20 FPS (1000/50) quando tem (é sempre um pouco menos
		// porque periga não ter dado tempo de redesenhar a tela entre um
		// postInvalidate() e outro.
		public void run() {
			// Aguarda o jogo existir
			while (trucoActivity.jogo == null) {
				sleep(200);
			}
			// Roda até a activity-mãe se encerrar
			while (!trucoActivity.isFinishing()) {
				sleep(200);
				do {
					if (visivel) {
						postInvalidate();
					}
					sleep(50);
				} while (calcTempoAteFimAnimacaoMS() >= 0);
			}
		}

		private void sleep(int tempoMS) {
			try {
				Thread.sleep(tempoMS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	});

	Thread respondeDialogos = new Thread() {
		@Override
		public void run() {
			// Aguarda o jogo existir
			while (trucoActivity.jogo == null) {
				try {
					sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// Roda até a activity-mãe se encerrar
			while (!trucoActivity.isFinishing()) {
				try {
					sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Jogo jogo = trucoActivity.jogo;
				if (recusarMao10) {
					recusarMao10 = false;
					jogo.decideMao10(trucoActivity.jogadorHumano, false);
				}
				if (aceitarMao10) {
					aceitarMao10 = false;
					jogo.decideMao10(trucoActivity.jogadorHumano, true);
				}
				if (recusarAumento) {
					recusarAumento = false;
					jogo.respondeAumento(trucoActivity.jogadorHumano, false);
				}
				if (aceitarAumento) {
					aceitarAumento = false;
					jogo.respondeAumento(trucoActivity.jogadorHumano, true);
				}
			}
		}

	};

	/**
	 * Permite à Activity informar que (não) é a vez de deixar o humano jogar
	 * 
	 * @param vezHumano
	 *            um entre VEZ_HUMANO, VEZ_CPU e VEZ_HUMANO_AGUARDANDO_RESPOSTA
	 */
	public void setStatusVez(int vezHumano) {
		aguardaFimAnimacoes();
		this.statusVez = vezHumano;
	}

	/**
	 * Entrega as cartas iniciais na mão de cada jogador.
	 */
	
	public void calculaJogosPlacar(){
		if (trucoActivity.jogo.pontosEquipe[0]==0&&
				trucoActivity.jogo.pontosEquipe[1]==0)
			jogoPrimeiro = -1;
		
		if (jogoPrimeiro==-1&&trucoActivity.jogo.jogos[0]==1)
			jogoPrimeiro = 0;
		if (jogoPrimeiro==-1&&trucoActivity.jogo.jogos[1]==1)
			jogoPrimeiro = 1;

		jogos1 = trucoActivity.jogo.jogos[0];
		jogos2 = trucoActivity.jogo.jogos[1];
		
		if (jogos1==1&&jogos2==1) {
			if (jogoPrimeiro==0) {
				jogos2 = 2;
				jogos1 = 0;
			} else {
				jogos2 = 0;
				jogos1 = 2;
				
			}
		}	
		int pontosLocal1 = trucoActivity.jogo.pontosEquipe[0];
		int pontosLocal2 = trucoActivity.jogo.pontosEquipe[1];
		if (pontosLocal1>=12) pontosLocal2 = -1;
		if (pontosLocal2>=12) pontosLocal1 = -1;
		pontos1 = pontos.atualizaPontos(pontosLocal1, jogos1);
		pontos2 = pontos.atualizaPontos(pontosLocal2, jogos2);		
	}
	public void distribuiMao() {

		aguardaFimAnimacoes();
		calculaJogosPlacar();
		
		notificaAnimacao(System.currentTimeMillis() + 600);
		aguardaFimAnimacoes();		

		
		int jogadorAbriu = this.trucoActivity.jogo.jogadorAbriu + 2; 
		// Distribui as cartas em círculo
		for (int j = 1; j <= 4; j++) {
			for (int i = 0; i <= 2; i++) {
				CartaVisual c = cartas[4 + i + 3 * (((j + jogadorAbriu) % 4 + 1) - 1)];
				c.setFechada(true);
				entregaCarta(c, (j + jogadorAbriu) % 4 + 1, i);
			}
		}

		// Atribui o valor correto às cartas do jogador e exibe
		for (int i = 0; i <= 2; i++) {
			CartaVisual c = cartas[4 + i];
			c.setCarta(trucoActivity.jogadorHumano.getCartas()[i]);
			c.setFechada(false);
		}
		
		// aqui vou definir o topo do baralho para recolher as cartas
		this.topBaralho = calcPosTopCarta((this.trucoActivity.jogo.jogadorAbriu + 3) % 4 + 1, 1);
		this.leftBaralho = calcPosLeftCarta((this.trucoActivity.jogo.jogadorAbriu + 3) % 4 + 1, 1);
		//carta.movePara(calcPosLeftCarta(numJogador, posicao),
		//calcPosTopCarta(numJogador, posicao), 150);
	}

	public void aceitouAumentoAposta(Jogador j, int valor) {
		if (statusVez == STATUS_VEZ_HUMANO_AGUARDANDO) {
			statusVez = STATUS_VEZ_HUMANO_OK;
		}
	}

	/**
	 * Recolhe o vira e as cartas jogadas de volta para o baralho
	 */
	public void recolheMao() {
		aguardaFimAnimacoes();
		
		// aqui eu vou recolher as cartas do próximo jogador a embalhar primeiro
		// a questão é que a carta do primeiro jogador começa no índice 4 e vai
		// até 15, então vou pegar a posição do jogador, multiplicar por três e somar ao array
		// na carta vou aplicar ao módulo de 15 e somar 4
		
		int ordemRecolher = (this.trucoActivity.jogo.jogadorAbriu + 3)  * 3;
		cartas[0].visible = false;
		// vou fazer o loop duas vezes pegando as descartadas primeiro
		// na verdade as do jogador que abriu primeiro
		for (int i = 0; i <= 11; i++) {
				CartaVisual c = cartas[(i + ordemRecolher) % 12 + 4];
				if (i<3||c.descartada) {
				float c1 = Math.abs(topBaralho - c.top);
				float c2 = Math.abs(leftBaralho - c.left);
				int h = (int)Math.floor(Math.sqrt(c1*c1+c2*c2) / 2);
				
				if ((c.top != topBaralho) || (c.left != leftBaralho)) {
					c.movePara(leftBaralho, topBaralho, h);
					c.setCarta(null);
					c.descartada = false;
					c.escura = false;
					cartasJogadas.remove(c);
				}
			}
		}
		for (int i = 0; i <= 11; i++) {
			CartaVisual c = cartas[(i + ordemRecolher) % 12 + 4];
			if (i>=3&&!c.descartada) {
			float c1 = Math.abs(topBaralho - c.top);
			float c2 = Math.abs(leftBaralho - c.left);
			int h = (int)Math.floor(Math.sqrt(c1*c1+c2*c2) / 2);
			
				if ((c.top != topBaralho) || (c.left != leftBaralho)) {
					c.movePara(leftBaralho, topBaralho, h);
					c.setCarta(null);
					c.descartada = false;
					c.escura = false;
					cartasJogadas.remove(c);
				}
			}
		}
	}

	/**
	 * Joga a carta no meio da mesa
	 * 
	 * @param c
	 */
	public void descarta(Carta c, int posicao) {

		aguardaFimAnimacoes();

		// Coloca a carta no meio da tela, mas "puxando" na direção
		// de quem jogou
		int topFinal = calcPosTopDescartada(posicao);
		int leftFinal = calcPosLeftDescartada(posicao);

		// Pega uma carta visual naquela posição...
		CartaVisual cv = null;
		for (int i = 0; i <= 2; i++) {
			CartaVisual cvCandidata = cartas[i + 1 + posicao * 3];
			// ...que não tenha sido descartada...
			if (cvCandidata.descartada) {
				continue;
			}
			// ...e, no caso de um humano (ou parceiro em mão de 11), que
			// corresponda à carta do jogo
			cv = cvCandidata;
			if (c.equals(cvCandidata)) {
				break;
			}
		}

		// Executa a animação de descarte
		cv.setCarta(c);
		cv.movePara(leftFinal, topFinal, 200);
		cv.descartada = true;
		cartasJogadas.addElement(cv);

	}

	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

	/**
	 * 
	 * @param numJogador
	 * @return posição (x) de uma carta descartada pelo jogador (no meio da
	 *         tela, mas puxando para a direçãod dele com um breve distúrbio
	 *         aleatório)
	 */
	private int calcPosLeftDescartada(int numJogador) {
		int leftFinal;
		leftFinal = getWidth() / 2 - CartaVisual.largura / 2;
		if (numJogador == 2) {
			leftFinal += CartaVisual.largura;
		} else if (numJogador == 4) {
			leftFinal -= CartaVisual.largura;
		}
		leftFinal += System.currentTimeMillis() % 5 - 2;
		return leftFinal;
	}

	/**
	 * Exibe uma carta na posição apropriada, animando
	 * <p>
	 * 
	 * @param numJogador
	 *            Posição do jogador, de 1 a 4 (1 = humano).
	 * @param posicao
	 *            posição da carta na mão do jogador (0 a 2)
	 * 
	 * @carta Carta a distribuir
	 */
	private void entregaCarta(CartaVisual carta, int numJogador, int posicao) {
		if (numJogador == 3 || numJogador == 4) {
			posicao = 2 - posicao;
		}
		float ctopo = calcPosTopCarta(numJogador, posicao);
		float cleft = calcPosLeftCarta(numJogador, posicao);
		float c1 = Math.abs(Math.abs(topBaralho) - Math.abs(ctopo));
		float c2 = Math.abs(Math.abs(leftBaralho) - Math.abs(cleft));
		int h = (int)Math.floor(Math.sqrt(c1*c1+c2*c2) / 3);
		carta.movePara(calcPosLeftCarta(numJogador, posicao),
				calcPosTopCarta(numJogador, posicao), h);
		
	}

	/**
	 * 
	 * @param numJogador
	 * @param i
	 * @return Posição (x) da i-ésima carta na mão do jogador em questão
	 */
	private int calcPosLeftCarta(int numJogador, int i) {
		int deslocamentoHorizontalEntreCartas = CartaVisual.largura * 7 / 8;
		int leftFinal = 0;
		switch (numJogador) {
		case 1:
		case 3:
			leftFinal = (getWidth() / 2) - (CartaVisual.largura / 2) + (i - 1)
					* deslocamentoHorizontalEntreCartas;
			break;
		case 2:
			leftFinal = getWidth() - CartaVisual.largura - MARGEM;
			break;
		case 4:
			leftFinal = MARGEM;
			break;
		}
		return leftFinal;
	}

	/**
	 * 
	 * @param numJogador
	 * @param i
	 * @return Posição (y) da i-ésima carta na mão do jogador em questão
	 */
	private int calcPosTopCarta(int numJogador, int i) {
		int deslocamentoVerticalEntreCartas = 12;
		int topFinal = 0;
		switch (numJogador) {
		case 1:
			topFinal = getHeight() - (CartaVisual.altura + MARGEM);
			break;
		case 2:
		case 4:
			topFinal = getHeight() / 2 - CartaVisual.altura / 2 - (i - 1)
					* deslocamentoVerticalEntreCartas;
			break;
		case 3:
			topFinal = MARGEM;
			break;
		}
		return topFinal;
	}

	/**
	 * 
	 * @param numJogador
	 * @return posição (y) de uma carta descartada pelo jogador (no meio da
	 *         tela, mas puxando para a direçãod dele com um breve distúrbio
	 *         aleatório)
	 */
	private int calcPosTopDescartada(int numJogador) {
		int topFinal;
		topFinal = getHeight() / 2 - CartaVisual.altura / 2;
		if (numJogador == 1) {
			topFinal += CartaVisual.altura / 2;
		} else if (numJogador == 3) {
			topFinal -= CartaVisual.altura / 2;
		}
		topFinal += System.currentTimeMillis() % 5 - 2;
		return topFinal;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Fundo verde
		canvas.drawRGB(27, 142, 60);

		// Desenha as cartas que já foram jogadas (se houverem),
		// na ordem em que foram jogadas
		for (int i = 0; i < cartasJogadas.size(); i++) {
			cartasJogadas.elementAt(i).draw(canvas);
		}
		// Desenha as cartas restantes, e o vira por cima de todas
		for (CartaVisual carta : cartas) {
			if (carta != null && !cartasJogadas.contains(carta)
					&& carta != cartas[0]) {
				carta.draw(canvas);
			}
		}
		cartas[0].draw(canvas);

		// Desliga o destaque da carta que fez a rodada e escurece as cartas já
		// descartadas (para não confundir com as próximas)
		long agora = System.currentTimeMillis();
		if ((agora > rodadaPiscaAte) && (numRodadaPiscando > 0)) {
			if (cartaQueFez != null) {
				cartaQueFez.destacada = false;
			}
			numRodadaPiscando = 0;
		}

		// Ícones das rodadas
		if (iconesRodadas != null) {
			for (int i = 0; i <= 2; i++) {
				// Desenha se não for a rodada piscando, ou, se for, alterna o
				// desenho a cada 250ms
				if (i != (numRodadaPiscando - 1) || (agora % 250) % 2 == 0) {
					canvas.drawBitmap(iconesRodadas[resultadoRodada[i]],
							canvas.getWidth() - iconesRodadas[0].getWidth() - MARGEM,
							MARGEM + i * (1 + iconesRodadas[0].getHeight()),
							new Paint());
				}
			}
		}
		
		/*
		 * Exibe os sinais conforme definido nas variáveis
		 * exibirSinal
		 * sinal;
		 */
		if (sinais != null&&carregouSinais) {
			if (this.exibirSinal>agora) {
				// aqui vou fazer uma modificacao para melhorar o sinal
				// vou criar uma sequencia de sinais para cada caso ao invés de um sinal simples
				// Ex. Zap, Olha para cima e pisca olho direito, ficará assim
				// 0 0 1 0 5 0 0
				mostraOcultaSinal++;
				if (mostraOcultaSinal>trucoActivity.valorSinal){
					mostraOcultaSinal=0;
					this.contaSinal++;
					if (contaSinal>9) {
							contaSinal = 0;
							exibirSinal = agora;
					}
				}
				
				int left = (int) canvas.getWidth() / 2 - sinais[0].getWidth() / 2;
				int top = (int) canvas.getHeight() / 2 - sinais[0].getHeight() / 2;
		
				if (this.codigoSinal[this.sinal][contaSinal] >= 0) {
						canvas.drawBitmap(sinais[
						                         this.codigoSinal[this.sinal][contaSinal]],
						                         left,
						                         top,
						                         new Paint());
						exibirSinal = agora + 1000;
				} else {				
					contaSinal = 0;
					exibirSinal = agora;
					animandoAte = agora;
				}
			}
		}

		// Caixa de diálogo (mão de 11 ou aumento)
		if (mostrarPerguntaMao10 || mostrarPerguntaAumento) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Style.FILL);
			canvas.drawRect(rectDialog, paint);
			paint.setColor(Color.WHITE);
			paint.setStyle(Style.STROKE);
			canvas.drawRect(rectDialog, paint);
			paint.setTextSize(tamanhoFonte);
			paint.setTextAlign(Align.CENTER);
			canvas.drawText(mostrarPerguntaMao10 ? "Bater carta?"
					: "Aceita?", rectDialog.centerX(),
					rectDialog.top + paint.getTextSize() * 1.2f, paint);
			desenhaBotao("Sim", canvas, rectBotaoSim);
			desenhaBotao("Nao", canvas, rectBotaoNao);

		}

		desenhaBalao(canvas);
		desenhaIndicadorDeVez(canvas);
		
		// desenhar os pontos
		
		if (pontos!=null) {
			if (pontos2!=null) canvas.drawBitmap(pontos2, MARGEM * 8, MARGEM, new Paint());
			if (pontos1!=null) canvas.drawBitmap(pontos1,  canvas.getWidth() - pontos.getLargura() - MARGEM, 
							   canvas.getHeight() - pontos.getAltura() - MARGEM, new Paint());
		}
	}

	private void desenhaBotao(String texto, Canvas canvas, RectF outerRect) {
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		paint.setTextSize(tamanhoFonte);
		// Borda
		
		paint.setColor(Color.WHITE);
		canvas.drawRoundRect(outerRect, tamanhoFonte * 2 / 3,
				tamanhoFonte * 2 / 3, paint);
		// Interior
		paint.setColor(Color.BLACK);
		RectF innerRect = new RectF(outerRect.left + 1, outerRect.top + 1,
				outerRect.right - 1, outerRect.bottom - 1);
		canvas.drawRoundRect(innerRect, tamanhoFonte * 2 / 3,
				tamanhoFonte * 2 / 3, paint);
		// Texto
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText(texto, outerRect.centerX(), outerRect.centerY()
				+ tamanhoFonte * 0.3f, paint);
	}

	/**
	 * Cache dos ícones que informam o resultado das rodadas
	 */
	public static Bitmap[] iconesRodadas;
	
	/**
	 * Cahce dos sinais que serão passados para o jogador humano
	 */
	public static Bitmap[] sinais;
	
	/*
	 * Marca qual sinal será exibido
	 */
	public int sinal = 0;
	
	/*
	 *  Mostra se carregou os sinais
	 */
	public boolean carregouSinais = false;
	/*
	 * mostra se é para exibir o sinal agora 
	 */
	public long exibirSinal = 0;
	/**
	 * Resultado das rodadas (0=não jogada; 1=vitória; 2=derrota; 3=empate)
	 */
	protected int[] resultadoRodada = { 0, 0, 0 };

	/**
	 * Margem entre a mesa e as cartas
	 */
	public static final int MARGEM = 2;

	/**
	 * Posição do baralho na mesa
	 */
	private int topBaralho, leftBaralho;

	/**
	 * Timestamp em que as animações em curso irão acabar
	 */
	private static long animandoAte = System.currentTimeMillis();

	/**
	 * Indica que é a vez do humano, e ele pode jogar
	 */
	public static final int STATUS_VEZ_HUMANO_OK = 1;

	/**
	 * Indica que é a vez de outro jogador
	 */
	public static final int STATUS_VEZ_OUTRO = 0;

	/**
	 * Indica que é a vez do humano, e ele está aguardando resposta (ex.: de
	 * aumento) para jogar
	 */
	public static final int STATUS_VEZ_HUMANO_AGUARDANDO = -1;

	/**
	 * Diz se é a vez do jogador humano dessa mesa ou de outro, e, no primeiro
	 * caso se está aguardando resposta de truco
	 */
	private int statusVez = 0;

	/**
	 * Guarda as cartas que foram jogadas pelos jogadores (para saber em que
	 * ordem desenhar)
	 */
	private Vector<CartaVisual> cartasJogadas = new Vector<CartaVisual>(12);

	private int numRodadaPiscando = 0;

	private long rodadaPiscaAte = System.currentTimeMillis();

	/**
	 * Carta que "fez" a última rodada (para fins de destaque)
	 */
	private CartaVisual cartaQueFez;

	public boolean mostrarPerguntaMao10 = false;

	private boolean recusarMao10 = false;
	private boolean aceitarMao10 = false;
	
	public boolean mostrarPerguntaAumento = false;

	private boolean recusarAumento = false;
	private boolean aceitarAumento = false;

	private int posicaoBalao = 1;

	private long mostraBalaoAte = System.currentTimeMillis();

	private String fraseBalao = null;
	
	public void perguntaSimNao(String pergunta) {
		
	}
	
	private void desenhaIndicadorDeVez(Canvas canvas) {
		if (statusVez == STATUS_VEZ_HUMANO_AGUARDANDO) {
			return;
		}
		Paint paintSetaVez = new Paint();
		paintSetaVez.setColor(Color.YELLOW);
		paintSetaVez.setTextAlign(Align.CENTER);
		paintSetaVez.setTextSize(CartaVisual.altura / 3);
		switch (posicaoVez) {
		case 1:
			canvas.drawText("\u21E9", getWidth() / 2, getHeight()
					- CartaVisual.altura * 14 / 12, paintSetaVez);
			break;
		case 2:
			canvas.drawText("\u21E8", getWidth() - CartaVisual.largura * 15
					/ 12, getHeight() / 2, paintSetaVez);
			break;
		case 3:
			canvas.drawText("\u21E7", getWidth() / 2,
					CartaVisual.altura * 16 / 12, paintSetaVez);
			break;
		case 4:
			canvas.drawText("\u21E6", CartaVisual.largura * 15 / 12,
					getHeight() / 2, paintSetaVez);
			break;
		}
	}

	/**
	 * Desenha a parte gráfica do balão (sem o texto). O nome é meio mentiroso,
	 * porque também desenha a ponta. É chamada várias vezes para compor o
	 * contorno, antes de estampar o texto
	 * 
	 * @param canvas
	 *            onde ele será desenhado
	 * @param x
	 *            esquerda
	 * @param y
	 *            topo
	 * @param largBalao
	 *            largura
	 * @param altBalao
	 *            altura
	 * @param quadrantePonta
	 *            Quadrante (cartesiano) onde aparece a ponta do balão (com
	 *            relação a ele mesmo)
	 */
	private void desenhaElipseBalao(Canvas canvas, int x, int y, int largBalao,
			int altBalao, int quadrantePonta, Paint paint) {
		// Elipse principal
		paint.setAntiAlias(true);
		canvas.drawArc(new RectF(x, y, x + largBalao - 1, y + altBalao - 1), 0,
				360, false, paint);
		// Ponta (é um triângulo que desenhamos linha a linha)
		paint.setAntiAlias(false);
		int xi;
		for (int i = 0; i < altBalao; i++) {
			if (quadrantePonta == 2 || quadrantePonta == 3) {
				xi = x + altBalao * 3 / 2 - i;
			} else {
				xi = x - altBalao * 3 / 2 + i + largBalao;
			}
			int sinaly = quadrantePonta < 3 ? -1 : 1;
			canvas.drawLine(xi, y + altBalao / 2, xi, y + altBalao / 2 + i
					* sinaly, paint);
		}
	}

	/**
	 * Desenha o balão no lugar certo, se ele estiver visível
	 * 
	 * @param canvas
	 *            canvas onde ele será (ou não) desenhado.
	 */
	private void desenhaBalao(Canvas canvas) {
		if (fraseBalao != null && mostraBalaoAte > System.currentTimeMillis()) {

			// Determina o tamanho e a posição do balão e o quadrante da
			// ponta
			final int MARGEM_BALAO_LEFT = (int) tamanhoFonte;
			final int MARGEM_BALAO_TOP = (int) tamanhoFonte / 1;
			Paint paintFonte = new Paint();
			paintFonte.setAntiAlias(true);
			paintFonte.setTextSize(tamanhoFonte);
			Rect bounds = new Rect();
			paintFonte.setColor(Color.BLACK);
			paintFonte
					.getTextBounds(fraseBalao, 0, fraseBalao.length(), bounds);

			int largBalao = bounds.width() + 2 * MARGEM_BALAO_LEFT;
			int altBalao = bounds.height() + 2 * MARGEM_BALAO_TOP;
			int x = 0, y = 0;
			int quadrantePonta = 0;
			switch (posicaoBalao) {
			case 1:
				x = (canvas.getWidth() - largBalao) / 2 - CartaVisual.largura;
				y = canvas.getHeight() - altBalao * 3 - MARGEM - 3;
				quadrantePonta = 4;
				break;
			case 2:
				x = canvas.getWidth() - largBalao - MARGEM - 3;
				y = (canvas.getHeight() / 2) - (altBalao / 2);
				quadrantePonta = 1;
				break;
			case 3:
				x = (canvas.getWidth() - largBalao) / 2 + CartaVisual.largura;
				y = MARGEM + 3 + altBalao * 2;
				quadrantePonta = 2;
				break;
			case 4:
				x = MARGEM + 3;
				y = (canvas.getHeight() / 2) - (altBalao / 2);
				quadrantePonta = 3;
				break;
			}

			// O balão tem que ser branco, com uma borda preta. Como
			// ele só aparece em um refresh, vamos pela força bruta,
			// desenhando ele deslocado em torno da posição final em
			// preto e em seguida desenhando ele em branco na posição
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setColor(Color.BLACK);
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					desenhaElipseBalao(canvas, x + i, y + j, largBalao,
							altBalao, quadrantePonta, paint);
				}
			}
			paint.setColor(Color.WHITE);
			desenhaElipseBalao(canvas, x, y, largBalao, altBalao,
					quadrantePonta, paint);

			// Finalmente, escreve o texto do balão
			paint.setAntiAlias(true);
			canvas.drawText(fraseBalao, x + MARGEM_BALAO_LEFT, y + altBalao
					- MARGEM_BALAO_TOP - 2, paintFonte);

		} else {
			fraseBalao = null;
		}

	}

	private boolean visivel = false;

	public boolean vaiJogarFechada;

	public void setPosicaoVez(int posicaoVez) {
		this.posicaoVez = posicaoVez;
	}
	
	public static int larguraSinal = 0;
	private long mostraOcultaSinal = 0;

	public static Bitmap redimensionarBitmap(Bitmap bm) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) larguraSinal) / width;
		float scaleHeight = ((float) larguraSinal) / height;
		// CREATE A MATRIX FOR THE MANIPULATION

		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		
		matrix.postScale(scaleWidth, scaleHeight);
		// RECREATE THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
		
	}
	
}
