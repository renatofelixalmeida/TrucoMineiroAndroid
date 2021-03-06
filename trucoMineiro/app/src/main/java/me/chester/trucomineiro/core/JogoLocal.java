package me.chester.trucomineiro.core;

import android.util.Log;


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
 * Jogo rodando no celular.
 * <p>
 * Um jogador só passa a fazer parte do jogo se for adicionado a ele pelo método
 * <code>adiciona()</code>.
 * <p>
 * A classe notifica aos jogadores participantes os eventos relevantes (ex.:
 * início da partida, vez passando para um jogador, carta jogada, pedido de
 * truco), e os jogadores podem usar os métodos de entrada (ex.:
 * <code>jogaCarta()</code>, <code>aumentaAposta</code>, etc.) para interagir
 * com o jogo.
 * <p>
 * 
 * 
 */
public class JogoLocal extends Jogo {

	/**
	 * Cria um novo jogo.
	 * <p>
	 * O jogo é criado, mas apenas inicia quando forem adicionados jogadores
	 * 
	 * @param manilhaVelha
	 *            true para jogo com manilhas fixas, false para jogar com "vira"
	 * @param baralhoLimpo
	 *            true para baralho sem os 4, 5, 6, 7, false para baralho
	 *            completo (sujo)
	 */
	public JogoLocal(int meuJogador, int jogadorAbriu, boolean maoDezAberta, boolean doisFechada, boolean amarraAbre) {
		this.manilhaVelha = true;
		this.baralhoLimpo = true;
		this.meuJogador = meuJogador;
		this.jogadorAbriu = jogadorAbriu;
		this.jogos[0] = 0;
		this.jogos[1] = 0;
		this.tento = new TentoMineiro();
		this.baralho = new Baralho(baralhoLimpo);
		this.maoDezAberta = maoDezAberta;
		this.doisFechada = doisFechada;
		this.amarraAbre = amarraAbre;

	}

	/**
	 * Cria um novo jogo.
	 * <p>
	 * O jogo é criado, mas apenas inicia quando forem adicionados jogadores
	 * 
	 * @param manilhaVelha
	 *            true para jogo com manilhas fixas, false para jogar com "vira"
	 * @param baralho
	 *            Instância de baralho a ser utilizado no jogo.
	 */
	public JogoLocal(Baralho baralho, boolean manilhaVelha, int jogadorAbriu) {
		this.manilhaVelha = manilhaVelha;
		this.baralhoLimpo = baralho.isLimpo();
		this.baralho = baralho;
		this.jogos[0] = 0;
		this.jogos[1] = 0;
		this.jogadorAbriu = jogadorAbriu;
	}

	/**
	 * Forma de tento que será usado durante esse jogo
	 */
	private Tento tento;

	/**
	 * Baralho que será usado durante esse jogo
	 */
	private Baralho baralho;

	/**
	 * Resultados de cada rodada (1 para vitória da equipe 1/3, 2 para vitória
	 * da equipe 2/4 e 3 para empate)
	 */
	private int resultadoRodada[] = new int[3];

	/**
	 * Valor atual da mão (1, 3, 6, 9 ou 12)
	 */
	private int valorMao;

	/**
	 * Jogador que está pedindo aumento de aposta (pedindo truco, 6, 9 ou 12).
	 * Se for null, ninguém está pedindo
	 */
	private Jogador jogadorPedindoAumento;

	/**
	 * Status das respsotas para um pedido de aumento de aposta para cada
	 * jogador.
	 * <p>
	 * false signfica que não respondeu ainda, true que respondeu recusando
	 */
	private boolean[] recusouAumento = new boolean[4];

	/**
	 * Posição (1 a 4) do jogador da vez
	 */
	private int posJogadorDaVez;

	/**
	 * Jogador que abriu a rodada
	 */
	private Jogador jogadorAbriuRodada;

	/**
	 * Jogador que abriu a mão
	 */
	private Jogador jogadorAbriuMao;

	/**
	 * Indica, para cada jogador, se estamos aguardando a resposta para uma mão
	 * de 11
	 */
	private boolean[] aguardandoRespostaMaoDe10 = new boolean[4];

	/**
	 * Sinaliza para o loop principal que alguém jogou uma carta
	 */
	boolean alguemJogou = false;

	/**
	 * Se alguemJogou = true, é o alguém que jogou
	 */
	public Jogador jogadorQueJogou;
	/**
	 * Se alguemJogou = true, é a carta jogada
	 */
	private Carta cartaJogada;

	private boolean manilhaVelha, baralhoLimpo,  doisFechada, amarraAbre;

	/*
	 * (non-Javadoc)
	 * 
	 * @see mt.JogoGenerico#run()
	 */
	public void run() {

		// Avisa os jogadores que o jogo vai começar
		for (Jogador interessado : jogadores) {
			interessado.inicioPartida(pontosEquipe[0], pontosEquipe[1]);
		}


		// Inicia a primeira rodada, usando o jogador na posição 1 caso seja a primeira partida
		// depois vai rodando o baralho normalmente e processa 
		// as jogadas até alguém ganhar ou o jogo ser abortado (o que pode
		// ocorrer em paralelo, daí os múltiplos checks a jogoFinalizado)
		Log.d("irrf", "jogadorabriu: " + this.jogadorAbriu);
		iniciaMao(getJogador(this.jogadorAbriu));
		while (pontosEquipe[0] < 12 && pontosEquipe[1] < 12 && !jogoFinalizado) {
			while ((!alguemJogou) && (!jogoFinalizado)) {
				sleep(100);
			}
			if (!jogoFinalizado) {
				processaJogada();
				alguemJogou = false;
			}
		}
	}

	/**
	 * Inicia uma mão (i.e., uma distribuição de cartas)
	 * 
	 * @param jogadorQueAbre
	 *            Jogador que abre a rodada
	 */
	private void iniciaMao(Jogador jogadorQueAbre) {
		this.jogadorAbriu = jogadorQueAbre.getPosicao();
		// Embaralha as cartas e reinicia a mesa
		baralho.embaralha();
		cartasJogadasPorRodada = new Carta[3][4];
	
		// Distribui as cartas de cada jogador
		for (int j = 1; j <= 4; j++) {
			Jogador jogador = getJogador(j);
			Carta[] cartas = new Carta[3];
			for (int i = 0; i <= 2; i++) {
				cartas[i] = baralho.sorteiaCarta();
			}
			jogador.setCartas(cartas);
		}

		// Inicializa a mão
		valorMao = tento.inicializaMao();

		jogadorPedindoAumento = null;
		numRodadaAtual = 1;
		jogadorAbriuMao = jogadorAbriuRodada = jogadorQueAbre;

		// Abre a primeira rodada, informando a carta da mesa e quem vai abrir
		posJogadorDaVez = jogadorQueAbre.getPosicao();
		for (Jogador interessado : jogadores) {
			interessado.inicioMao();
		}

		if (pontosEquipe[0] == tento.valorPenultimaMao()
				^ pontosEquipe[1] == tento.valorPenultimaMao()) {
			// Se apenas uma das equipes tiver 11 pontos, estamos numa
			// "mão de 11": os membros da equipe podem ver as cartas do parceiro
			// e decidir se querem jogar (valendo 3 pontos) ou desistir
			// (perdendo 1)
			if (pontosEquipe[0] == tento.valorPenultimaMao()) {
				setEquipeAguardandoMao11(1);
				getJogador(1).informaMao10(getJogador(3).getCartas());
				getJogador(3).informaMao10(getJogador(1).getCartas());
				for (Jogador interessado : jogadores) {
					// Interessados que não sejam Jogador (ex.: a Partida na
					// versão Android) devem ser notificados também
					if (!(interessado instanceof Jogador)) {
						interessado.informaMao10(getJogador(3).getCartas());
					}
				}
			} else {
				setEquipeAguardandoMao11(2);
				getJogador(2).informaMao10(getJogador(4).getCartas());
				getJogador(4).informaMao10(getJogador(2).getCartas());
			}
		} else {
			// Se for uma mão normal, passa a vez para o jogador que abre
			setEquipeAguardandoMao11(0);
			notificaVez();
		}

	}

	/**
	 * Processa uma jogada e passa a vez para o próximo jogador (ou finaliza a
	 * rodoada/mão/jogo), notificando os jogadores apropriadamente
	 * 
	 * @param j
	 *            Jogador que efetuou a jogada
	 * @param c
	 *            Carta que foi jogada
	 */
	private void processaJogada() {

		Jogador j = this.jogadorQueJogou;
		Carta c = this.cartaJogada;

		// Se o jogo acabou, a mesa não estiver completa, já houver alguém
		// trucando, estivermos aguardando ok da mão de 11 ou não for a vez do
		// cara, recusa
		if (jogoFinalizado || numJogadores < 4 || jogadorPedindoAumento != null
				|| (isAguardandoRespostaMao11())
				|| !j.equals(getJogadorDaVez())) {
			return;
		}

		// Verifica se a carta já não foi jogada anteriormente (normalmente não
		// deve acontecer - mesmo caso do check anterior)
		for (int i = 0; i <= 2; i++) {
			for (int k = 0; k <= 3; k++) {
				if (c.equals(cartasJogadasPorRodada[i][k])) {
					return;
				}
			}
		}

		// Garante que a regra para carta fechada seja respeitada
		if (!isPodeFechada()) {
			c.setFechada(false);
		}

		// Dá a carta como jogada, notificando os jogadores
		cartasJogadasPorRodada[numRodadaAtual - 1][j.getPosicao() - 1] = c;
		for (Jogador interessado : jogadores) {
			interessado.cartaJogada(j, c);
		}

		// Passa a vez para o próximo jogador
		posJogadorDaVez++;
		if (posJogadorDaVez == 5) {
			posJogadorDaVez = 1;
		}
		if (posJogadorDaVez == jogadorAbriuRodada.getPosicao()) {

			// Completou a volta da rodada - acha o valor da maior carta da mesa
			Carta[] cartas = getCartasDaRodada(numRodadaAtual);
			int valorMaximo = 0;
			for (int i = 0; i <= 3; i++) {
				valorMaximo = Math.max(valorMaximo, getValorTruco(cartas[i]));
			}

			// Determina a equipe vencedora (1/2= equipe 1 ou 2; 3=empate) e o
			// jogador que vai "tornar", i.e., abrir a próxima rodada
			setResultadoRodada(numRodadaAtual, 0);
			// vou mudar o loop para pegar o jogador que empatou para definir o que torna
			Jogador jogadorQueTorna = null;
			for (int i = 0; i <= 3; i++) {
				int n = (i + posJogadorDaVez - 1) % 4;
				if (getValorTruco(cartas[n]) == valorMaximo) {
					jogadorQueTorna = getJogador(n + 1);
					if (n == 0 || n == 2) {
						setResultadoRodada(numRodadaAtual,
								getResultadoRodada(numRodadaAtual) | 1);
					} else {
						setResultadoRodada(numRodadaAtual,
								getResultadoRodada(numRodadaAtual) | 2);
					}
				}
			}

			// Se houve vencedor, passa a vez para o jogador que fechou a
			// vitória, senão deixa quem abriu a mão anterior abrir a próxima
			// Se amarrar na primeira rodada o jogador que amarrou que abre
			if (getResultadoRodada(numRodadaAtual) != 3||(numRodadaAtual==1&&amarraAbre)) {
				posJogadorDaVez = jogadorQueTorna.getPosicao();
			} else {
				jogadorQueTorna = getJogadorDaVez();
			}

			// Notifica os interessados que a mão foi feita
			for (Jogador interessado : jogadores) {
				interessado.rodadaFechada(numRodadaAtual,
						getResultadoRodada(numRodadaAtual), jogadorQueTorna);
			}
			
			// Verifica se já temos vencedor na rodada
			int resultadoRodada = 0;
			if (numRodadaAtual == 2) {
				if (getResultadoRodada(1) == 3 && getResultadoRodada(2) != 3) {
					// Empate na 1a. mão, quem fez a 2a. leva
					resultadoRodada = getResultadoRodada(2);
				} else if (getResultadoRodada(1) != 3
						&& getResultadoRodada(2) == 3) {
					// Empate na 2a. mão, quem fez a 1a. leva
					resultadoRodada = getResultadoRodada(1);
				} else if (getResultadoRodada(1) == getResultadoRodada(2)
						&& getResultadoRodada(1) != 3) {
					// Quem faz as duas primeiras leva
					resultadoRodada = getResultadoRodada(2);
				}
			} else if (numRodadaAtual == 3) {
				if (getResultadoRodada(3) != 3) {
					// Quem faz a 3a. leva
					resultadoRodada = getResultadoRodada(3);
				} else {
					// Se a 3a. empatou, a 1a. decide
					resultadoRodada = getResultadoRodada(1);
				}
			}

			// Se já tivermos vencedor (ou empate final), notifica e abre uma
			// nova mao, senão segue a vida na mão seguinte
			if (resultadoRodada != 0) {
				// Soma os pontos (se não deu emptate)
				if (resultadoRodada != 3) {
					pontosEquipe[resultadoRodada - 1] += valorMao;
				}
				fechaMao();
			} else {
				numRodadaAtual++;
				jogadorAbriuRodada = jogadorQueTorna;
				notificaVez();
			}
		} else {
			notificaVez();
		}

	}

	/**
	 * Conclui a mão atual, e, se o jogo não acabou, inicia uma nova.
	 * 
	 * @param jogadorQueTorna
	 *            Jogador que irá abrir a próxima mão, se houver
	 */
	private void fechaMao() {

		// Notifica os interessados que a rodada acabou, e, se for o caso, que o
		// jogo acabou também
		fezJogo[0] = false;
		fezJogo[1] = false;
		
		for (Jogador interessado : jogadores) {
			if (pontosEquipe[0] > tento.valorPenultimaMao()) {
				if (jogos[0]==0) {
					jogos[0]=1;
					fezJogo[0] = true;
					pontosEquipe[0] = 0;
					pontosEquipe[1] = 0;
				}
			}
			
			if (pontosEquipe[1] > tento.valorPenultimaMao()) {
				if (jogos[1]==0) {
					jogos[1]=1;
					fezJogo[1] = true;
					pontosEquipe[0] = 0;
					pontosEquipe[1] = 0;
				}
			}
			
			interessado.maoFechada(pontosEquipe);
			
			if (pontosEquipe[0] > tento.valorPenultimaMao()) {
				interessado.jogoFechado(1);
				jogoFinalizado = true;
			} else if (pontosEquipe[1] > tento.valorPenultimaMao()) {
				interessado.jogoFechado(2);
				jogoFinalizado = true;
			}			
		}

		// Se ainda estivermos em jogo, incia a nova mao
		if (pontosEquipe[0] <= tento.valorPenultimaMao()
				&& pontosEquipe[1] <= tento.valorPenultimaMao()) {
			int posAbre = jogadorAbriuMao.getPosicao() + 1;
			if (posAbre == 5)
				posAbre = 1;
			iniciaMao(getJogador(posAbre));
		}

		return;
	}

	// /// NOTIFICAÇÕES RECEBIDAS DOS JOGADORES

	/*
	 * (non-Javadoc)
	 * 
	 * @see mt.JogoGenerico#jogaCarta(mt.Jogador, mt.Carta)
	 */
	public synchronized void jogaCarta(Jogador j, Carta c) {

		// Se alguém tiver jogado e ainda não foi processado, segura a onda
		while (alguemJogou) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// No worries
			}
		}
		this.jogadorQueJogou = j;
		this.cartaJogada = c;
		this.alguemJogou = true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mt.JogoGenerico#decideMao11(mt.Jogador, boolean)
	 */
	public synchronized void decideMao10(Jogador j, boolean aceita) {

		// Só entra se estivermos jogando e se estivermos agurardando resposta
		// daquele jogador para a pergunta (isso é importante para evitar duplo
		// início)
		if (jogoFinalizado || !aguardandoRespostaMaoDe10[j.getPosicao() - 1])
			return;

		// Avisa os outros jogadores da decisão
		for (Jogador interessado : jogadores) {
			interessado.decidiuMao10(j, aceita);
		}

		aguardandoRespostaMaoDe10[j.getPosicao() - 1] = false;

		if (aceita) {
			// Se aceitou, desencana da resposta do parceiro e pode tocar o
			// jogo, valendo 3
			aguardandoRespostaMaoDe10[j.getParceiro() - 1] = false;
			valorMao = tento.inicializaPenultimaMao();
			notificaVez();
		} else {
			// Se recusou (e o parceiro também), a equipe perde um ponto e
			// recomeça a mao
			if (!aguardandoRespostaMaoDe10[j.getParceiro() - 1]) {
				pontosEquipe[j.getEquipeAdversaria() - 1] += tento
						.inicializaMao();
				fechaMao();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mt.JogoGenerico#aumentaAposta(mt.Jogador)
	 */
	public void aumentaAposta(Jogador j) {

		// Se o jogo estiver fianlizado, a mesa não estiver completa, já houver
		// alguém trucando, estivermos aguardando a mão de 11 ou não for a vez
		// do cara, recusa
		if ((jogoFinalizado) || (numJogadores < 4)
				|| (jogadorPedindoAumento != null)
				|| isAguardandoRespostaMao11() || !j.equals(getJogadorDaVez())) {
			return;
		}

		// Atualiza o status e notifica os outros jogadores do pedido
		jogadorPedindoAumento = j;
		for (int i = 0; i <= 3; i++)
			recusouAumento[i] = false;

		int valor = tento.calcValorTento(valorMao);

		// Notifica os interessados
		for (Jogador interessado : jogadores) {
			interessado.pediuAumentoAposta(j, valor);
		}

		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mt.JogoGenerico#respondeAumento(mt.Jogador, boolean)
	 */
	public synchronized void respondeAumento(Jogador j, boolean aceitou) {
		// Apenas os adversários de quem trucou respondem
		if (jogadorPedindoAumento == null
				|| jogadorPedindoAumento.getEquipeAdversaria() != j.getEquipe()) {
			return;
		}

		if (aceitou) {
			// Se o jogador aceitou, seta o novo valor, notifica a galera e tira
			// o jogo da situtação de truco
			valorMao = tento.calcValorTento(valorMao);
			jogadorPedindoAumento = null;
			for (Jogador interessado : jogadores) {
				interessado.aceitouAumentoAposta(j, valorMao);
			}
		} else {
			// Primeiro notifica todo mundo
			for (Jogador interessado : jogadores) {
				interessado.recusouAumentoAposta(j);
			}
			int posParceiro = (j.getPosicao() + 1) % 4 + 1;
			if (recusouAumento[posParceiro - 1]) {
				// Se o parceiro também recusou, derrota da dupla
				pontosEquipe[jogadorPedindoAumento.getEquipe() - 1] += valorMao;
				fechaMao();
			} else {
				// Sinaliza a recusa, deixando a decisão na mão do parceiro
				recusouAumento[j.getPosicao() - 1] = true;
			}
		}
	}

	/**
	 * Determina qual a equipe que está aguardando mão de 11
	 * 
	 * @param i
	 *            1 ou 2 para a respectiva equipe, 0 para ninguém aguardando mão
	 *            de 11 (jogo normal)
	 */
	private void setEquipeAguardandoMao11(int i) {
		aguardandoRespostaMaoDe10[0] = aguardandoRespostaMaoDe10[2] = (i == 1);
		aguardandoRespostaMaoDe10[1] = aguardandoRespostaMaoDe10[3] = (i == 2);
	}

	private int getResultadoRodada(int mao) {
		return resultadoRodada[mao - 1];
	}

	private void setResultadoRodada(int mao, int valor) {
		resultadoRodada[mao - 1] = valor;
	}

	/**
	 * Informa aos jogadores participantes que é a vez de um deles.
	 */
	private void notificaVez() {

		// Esses dados têm que ser coletados *antes* de chamar as Threads.
		// Motivo: se uma delas resolver jogar, a informação para as outras pode
		// ficar destaualizada.
		Jogador j = getJogadorDaVez();
		boolean pf = isPodeFechada();

		for (Jogador interessado : jogadores) {
			interessado.vez(j, pf);
		}

	}

	/**
	 * Informa se o jogador da vez pode jogar carta fechada (se mudar a regra,
	 * basta alterar aqui).
	 * <p>
	 * Regra atual: só vale carta fechada se não for a 1a. rodada e não for
	 * mão de 10
	 * 
	 * @return
	 */
	private boolean isPodeFechada() {
		if (this.doisFechada) {
			return (numRodadaAtual > 1 &&
					pontosEquipe[0] < 10 &&
					pontosEquipe[1] < 10);
		} else {
			Carta cartaParceiro = cartasJogadasPorRodada[numRodadaAtual - 1][getJogadorDaVez()
					.getParceiro() - 1];
			return (numRodadaAtual > 1 && (cartaParceiro == null || !cartaParceiro
					.isFechada()));
		}
	}

	/**
	 * Recupera o jogador cuja vez é a atual
	 * 
	 * @return
	 */
	private Jogador getJogadorDaVez() {
		return getJogador(posJogadorDaVez);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mt.JogoGenerico#atualizaSituacao(mt.SituacaoJogo, mt.Jogador)
	 */
	public void atualizaSituacao(SituacaoJogo s, Jogador j) {
		s.baralhoSujo = !this.baralhoLimpo;
		if (manilhaVelha) {
			s.manilha = SituacaoJogo.MANILHA_INDETERMINADA;
		} else {
			s.manilha = this.getManilha();
		}
		s.numRodadaAtual = this.numRodadaAtual;
		s.maoDezAberta = this.maoDezAberta;
		s.posJogador = j.getPosicao();
		s.posJogadorQueAbriuRodada = this.jogadorAbriuRodada.getPosicao();
		if (this.jogadorPedindoAumento != null)
			s.posJogadorPedindoAumento = this.jogadorPedindoAumento
					.getPosicao();
		s.valorMao = this.valorMao;

		for (int i = 0; i <= 1; i++) {
			s.pontosEquipe[i] = this.pontosEquipe[i];
		}
		for (int i = 0; i <= 2; i++)
			s.resultadoRodada[i] = this.resultadoRodada[i];

		for (int i = 0; i <= 2; i++)
			for (int k = 0; k <= 3; k++) {
				Carta c = cartasJogadasPorRodada[i][k];
				if (c == null) {
					s.cartasJogadas[i][k] = null;
				} else if (s.cartasJogadas[i][k] == null) {
					s.cartasJogadas[i][k] = new Carta(c.getLetra(),
							c.getNaipe());
				} else {
					s.cartasJogadas[i][k].setLetra(c.getLetra());
					s.cartasJogadas[i][k].setNaipe(c.getNaipe());
				}
				// Se for uma carta fechada, limpa letra/naipe na cópia (pra
				// evitar que uma estratégia maligna tente espiar uma carta
				// fechada)
				if (c != null && c.isFechada()) {
					s.cartasJogadas[i][k].setFechada(true);
					s.cartasJogadas[i][k].setLetra(Carta.LETRA_NENHUMA);
					s.cartasJogadas[i][k].setNaipe(Carta.NAIPE_NENHUM);
				}
			}

	}

	/**
	 * @return True para jogo sem os 4,5,6 e 7.
	 */
	public boolean isBaralhoLimpo() {
		return baralhoLimpo;
	}

	/**
	 * @return True para manilhas fixas (sem "vira")
	 */
	public boolean isManilhaVelha() {
		return manilhaVelha;
	}

	/**
	 * Verifica se estamos aguardando resposta para mão de 11
	 * 
	 * @return true se falta alguém responder, false caso contrário
	 */
	private boolean isAguardandoRespostaMao11() {
		for (int i = 0; i <= 3; i++) {
			if (aguardandoRespostaMaoDe10[i]) {
				return true;
			}
		}
		return false;
	}

	private void sleep(int i) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Seta o placar atual (apenas para fins de testes)
	 * 
	 * @param pontosEquipe1
	 * @param pontosEquipe2
	 */
	public void setPlacar(int pontosEquipe1, int pontosEquipe2) {
		pontosEquipe[0] = pontosEquipe1;
		pontosEquipe[1] = pontosEquipe2;
	}

}
