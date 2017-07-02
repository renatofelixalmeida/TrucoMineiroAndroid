package me.chester.trucomineiro.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

public class Pontos {
	public static final int COR_MESA = Color.argb(255, 27, 142, 60);
	public static final int COR_PLACAR = Color.argb(255, 47, 190, 80);
	private int largura;
	private int altura;
	public Pontos(int largura, int altura){
		this.largura = largura;
		this.altura = altura;
	}
	public Bitmap marcadorRodada(int valor){
		int nLargura = (int) largura / 5 * 2;
		int nAltura = nLargura;
		String[] caras = {" ", "✔", "✘", "≡"};
		String texto = caras[valor];
		int[] cores = {Color.WHITE, Color.BLUE, Color.RED, Color.YELLOW};
		int cor = cores[valor];
		int[] tamanhos = new int[4];
		tamanhos[0] = (int) Math.floor(nLargura / 1.5);
		tamanhos[1] = (int) Math.floor(nLargura / 1.5);
		tamanhos[2] = (int) Math.floor(nLargura / 1.5);
		tamanhos[3] = (int) Math.floor(nLargura / 1.2);
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap bitmap = Bitmap.createBitmap(nLargura, nAltura, conf);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();	
		paint.setStyle(Style.STROKE);
		paint.setColor(COR_MESA);				
		canvas.drawRect(0, 0, nLargura, nAltura, paint);
		paint.setStyle(Style.FILL);
		paint.setColor(COR_PLACAR);
		canvas.drawRect(2, 
				2, 
				nLargura - 2, 
				nAltura - 2, 
				paint);	
		
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);	
		canvas.drawRect(2, 
						2, 
						nLargura - 2, 
						nAltura - 2, 
						paint);	
			paint.setColor(cor);
			
			alinharTexto(texto, 
						 2,  
						 2, 
						 nLargura - 2, 
						 nAltura - 2,  
						 tamanhos[valor], 
						 cor, 
						 canvas);
		return bitmap;
	}

	public Bitmap atualizaPontos(int pontos, int jogos){
		final String[] fNaipes = {"♥","♦","♠","♣"};
		Rect rect;
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap bitmap = Bitmap.createBitmap(largura, altura, conf);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		
		if (jogos>0){
			// criar a carta de baralho que vai marcar o jogo
			
			boolean isFechada = jogos == 2;
			// começar desenhando a carta
			paint.setAntiAlias(true);
			
			// desenhar o contorno da carta
			rect = new Rect(1, 1,  largura - 1, altura - 1);
			RectF rectf = new RectF(rect);

			if (!isFechada) {
				paint.setColor(Color.WHITE);
			} else {
				paint.setColor(Color.rgb(46, 139, 87));
			}

			// preencher a carta
			paint.setStyle(Paint.Style.FILL);
			
			// desenhar o fundo
			canvas.drawRoundRect(rectf,  largura / 12,  altura / 12, paint);
			
			int fCor;
			
			fCor = Color.RED;
			// agora a borda da carta, será preta
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(1);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectf,  largura / 12,  altura / 12, paint);
			if (!isFechada) {
				// escrever o texto na carta
				alinharTexto("4", 0, 0, largura / 2, altura / 3, altura / 4, fCor, canvas);
				alinharTexto(fNaipes[0], 0, altura / 8, largura, altura - altura / 8, altura / 2, fCor, canvas);
			} else {
				alinharTexto(fNaipes[3], 0, 0, largura, altura,  largura / 2, Color.rgb(26, 100, 57), canvas);			
				// no caso de jogar fechada vou ver se coloco uma porcao de linhas na beirada para ficar
				// mais bonito
				paint.setColor(Color.WHITE);
				//paint.setColor(Color.rgb(46, 139, 87));
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(1);
				Paint np = new Paint();
				np.setColor(Color.WHITE);
				np.setColor(Color.rgb(26, 100, 57));
				np.setStyle(Paint.Style.STROKE);
				np.setStrokeWidth(3);
				
				int espaco = largura / 20; 
				for (int i=1;i<6;i++) {
					rectf.left = rectf.left + espaco;
					rectf.top = rectf.top + espaco;
					rectf.bottom = rectf.bottom - espaco;
					rectf.right = rectf.right - espaco;
					canvas.drawRoundRect(rectf,  largura / 12,  altura / 12, np);
					canvas.drawRoundRect(rectf,  largura / 12,  altura / 12, paint);					
				}			
			}			
		}
		
		// começar a montar o dado
		int nLargura = (int) largura / 5 * 3;
		int nAltura = nLargura;
		int topo = (int) (altura - nAltura) / 2;
		int baixo = topo + nAltura;
		int esquerda = (int) (largura - nLargura) / 2;
		int direita = esquerda + nLargura;
		for (int i=0;i<10;i++) {
			paint.setStyle(Style.STROKE);
			rect = new Rect(esquerda - i, topo - i, direita - i, baixo - i);
			paint.setColor(Color.rgb(140 + i, 140 + i, 140 + i));
			canvas.drawRect(rect, paint);
			paint.setStyle(Style.FILL);
			paint.setColor(Color.rgb(200 + i, 200 + i, 200 + i));
			canvas.drawRect(rect, paint);
		}
		String texto = "" + (pontos);
		if (pontos >= 12) texto = "♔";
		if (pontos < 0) texto = "♘";
		
		alinharTexto(texto, 0, 0, largura - 20, altura - 16,  largura / 2, Color.rgb(26, 26, 26), canvas);
		alinharTexto(texto, 0, 0, largura - 19, altura - 14,  largura / 2, Color.rgb(116, 116, 116), canvas);
		return bitmap;
	}
	private void alinharTexto(String texto, int esquerda, int topo, int largura, int altura, int tamanho, int cor, Canvas canvas){
		//texto = texto.toUpperCase(Locale.ENGLISH);
		Paint paint = new Paint();
		paint.setTextSize(tamanho);
		paint.setTextAlign(Align.CENTER);
		paint.setColor(cor);
		paint.setStyle(Style.FILL_AND_STROKE);
		Rect cantos = new Rect();
		paint.setTextSize(tamanho);
		paint.getTextBounds(texto, 0, texto.length(), cantos);
		int fEsquerda = esquerda + largura / 2;
		int fTopo = topo + altura / 2 + (cantos.bottom - cantos.top) / 2;	
		canvas.drawText(texto, fEsquerda, fTopo, paint);
	}	
	
	public int getLargura(){
		return largura;
	}
	
	public int getAltura(){
		return altura;
	}
}
