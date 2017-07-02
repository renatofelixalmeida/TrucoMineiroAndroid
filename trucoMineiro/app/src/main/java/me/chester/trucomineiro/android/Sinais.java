package me.chester.trucomineiro.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;

public class Sinais {
	int largura;
	private int[] l = new int[801];
	Paint paint = new Paint();
	RectF rectf; 
	public Sinais(int largura) {
		this.largura=largura;
		for (int i = 1; i < 501; i ++){
			l[i] = largura * i / 500;
		}			
	}
	
	public Bitmap pegar(int jogador, int sinal){
		
		switch (jogador){
		case 1 : return homem1(sinal);
		case 2 : return homem2(sinal);
		case 3 : return mulher1(sinal);
		case 4 : return mulher2(sinal);
		}
		return null;
	}
	
	
	public Bitmap homem1(int sinal){
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap bitmap = Bitmap.createBitmap(largura, largura, conf);
		Canvas canvas = new Canvas(bitmap);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(l[7]);
		Path path = new Path();
		rectf = new RectF();
		int[] to = {0,0};
		int[] bo = {0,0};
		int[] lo = {0,0};
		int[] ro = {0,0};
		int[] ts = {0,0};
		int[] bs = {0,0};	
		
		if (sinal==1) {
			to[0] = to[1] = -5;
			bo[0] = bo[1] = -5;
			ts[0] = ts[1] = -10;
			bs[0] = bs[1] = 15;
		}
		if (sinal==2) {
			to[0] = to[1] = 5;
			bo[0] = bo[1] = 5;
			ts[0] = ts[1] = 5;
			bs[0] = bs[1] = -40;
		}
		if (sinal==3) {
			lo[0] = lo[1] = -7;
			ro[0] = ro[1] = -7;
		}
		if (sinal==4) {
			lo[0] = lo[1] = 7;
			ro[0] = ro[1] = 7;
		}
		if (sinal==5||sinal==7) {
			to[0] = 13;
			bo[0] = -13;
			ts[0] = 15;
			bs[0] = -100;
		}
		
		if (sinal==6||sinal==7) {
			to[1] = 13;
			bo[1] = -13;
			ts[1] = 15;
			bs[1] = -100;
		}		
		
		// cabeça
		rectf.set(l[398], l[249], l[447], l[329]);
		path.arcTo(rectf, 280, 90, false);		
		rectf.set(l[385], l[311], l[431], l[357]);
		path.arcTo(rectf, 0, 90, false);
		rectf.set(l[93], l[185], l[407], l[497]);
		path.arcTo(rectf, 10, 160, false);	
		rectf.set(l[70], l[311], l[115], l[357]);
		path.arcTo(rectf, 90, 90, false);
		rectf.set(l[53], l[249], l[102], l[329]);
		path.arcTo(rectf, 170, 90, false);
		rectf.set(l[70], l[38], l[430], l[379]);
		path.arcTo(rectf, 170, 200, false);		
		path.close();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		canvas.drawPath(path, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		canvas.drawPath(path, paint);

		// cabelo
		path = new Path();
		paint.setStrokeWidth(1);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		canvas.drawPath(path, paint);		
		rectf.set(l[353], l[137], l[425], l[297]);
		path.arcTo(rectf, 20, 50, false);	
		rectf.set(l[344], l[221], l[405], l[283]);
		path.arcTo(rectf, 0, -25, false);
		rectf.set(l[383], l[170], l[444], l[233]);
		path.arcTo(rectf, 160, 25, false);			
		rectf.set(l[366], l[166], l[389], l[187]);
		path.arcTo(rectf, 10, -70, false);	
		rectf.set(l[355], l[103], l[407], l[161]);
		path.arcTo(rectf, 110, 70, false);
		rectf.set(l[263], l[93], l[344], l[152]);
		path.arcTo(rectf, 350, -100, false);			
		rectf.set(l[188], l[46], l[301], l[105]);
		path.arcTo(rectf, 60, 60, false);	
		rectf.set(l[136], l[98], l[248], l[156]);
		path.arcTo(rectf, 300, -110, false);
		rectf.set(l[131], l[104], l[196], l[184]);
		path.arcTo(rectf, 200, -30, false);	
		path.lineTo(l[132], l[160]);
		path.lineTo(l[120], l[140]);
		rectf.set(l[120], l[94], l[224], l[209]);
		path.arcTo(rectf, 170, -20, false);	
		path.lineTo(l[110], l[157]);
		rectf.set(l[104], l[231], l[207], l[324]);
		path.arcTo(rectf, 180, -10, false);			
		rectf.set(l[78], l[202], l[181], l[295]);
		path.arcTo(rectf, 120, 40, false);	
		rectf.set(l[63], l[158], l[168], l[250]);
		path.arcTo(rectf, 170, 10, false);	
		rectf.set(l[79], l[66], l[173], l[179]);
		path.arcTo(rectf, 200, 10, false);	
		rectf.set(l[128], l[4], l[234], l[116]);
		path.arcTo(rectf, 220, 40, false);	
		path.lineTo(l[198], 0);
		path.lineTo(l[218], 0);
		rectf.set(l[208], l[5], l[274], l[68]);
		path.arcTo(rectf, 290, 50, false);
		rectf.set(l[273], l[12], l[344], l[76]);
		path.arcTo(rectf, 240, 55, false);				
		rectf.set(l[290], l[39], l[422], l[186]);
		path.arcTo(rectf, 305, 45, false);		
		path.lineTo(l[428], l[122]);
		rectf.set(l[304], l[134], l[436], l[281]);
		path.arcTo(rectf, 0, 15, false);
		path.close();
		canvas.drawPath(path, paint);
		
		// sombracelhas
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(l[10]);
		rectf.set(l[110], l[208 + ts[0]], l[245], l[347 + bs[0]]);		
		canvas.drawArc(rectf, 200, 90, false, paint);
		rectf.set(l[255], l[208 + ts[1]], l[390], l[347 + bs[1]]);		
		canvas.drawArc(rectf, 250, 90, false, paint);
		
		// olhos
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(l[10]);
		rectf.set(l[166 + lo[0]], l[251 + to[0]], l[201 + ro[0]], l[285 + bo[0]]);
		canvas.drawOval(rectf, paint);
		rectf.set(l[299 + lo[1]], l[251 + to[1]], l[334 + ro[1]], l[285 + bo[1]]);
		canvas.drawOval(rectf, paint);
		
		// nariz
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(l[10]);
		rectf.set(l[213], l[294], l[279], l[358]);
		canvas.drawArc(rectf, 320, 180, false, paint);		

		// boca
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(l[10]);
		if (sinal==8) {
			rectf.set(l[171], l[394], l[331], l[405]);
		} else {
			rectf.set(l[171], l[321], l[331], l[416]);
		}
		canvas.drawArc(rectf, 45, 90, false, paint);		
		
		return bitmap;
		
	}
	
	public Bitmap homem2(int sinal){
		int[] to = {0,0};
		int[] bo = {0,0};
		int[] lo = {0,0};
		int[] ro = {0,0};
		int[] ts = {0,0};
		int[] bs = {0,0};
		

		if (sinal==1) {
			to[0] = to[1] = -5;
			bo[0] = bo[1] = -5;
			ts[0] = ts[1] = -10;
			bs[0] = bs[1] = 15;
		}
		if (sinal==2) {
			to[0] = to[1] = 5;
			bo[0] = bo[1] = 5;
			ts[0] = ts[1] = 5;
			bs[0] = bs[1] = -40;
		}
		if (sinal==3) {
			lo[0] = lo[1] = -7;
			ro[0] = ro[1] = -7;
			to[0] = to[1] = 3;
		}
		if (sinal==4) {
			lo[0] = lo[1] = 7;
			ro[0] = ro[1] = 7;
			to[0] = to[1] = 3;
		}
		if (sinal==5||sinal==7) {
			to[0] = 13;
			bo[0] = -13;
			ts[0] = 15;
			bs[0] = -100;
		}
		
		if (sinal==6||sinal==7) {
			to[1] = 13;
			bo[1] = -13;
			ts[1] = 15;
			bs[1] = -100;
		}

		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap bitmap = Bitmap.createBitmap(largura, largura, conf);
		Canvas canvas = new Canvas(bitmap);
		paint.setStrokeWidth(l[10]);
		Path path = new Path();
		
		// cabeça
		rectf = new RectF();
		path.moveTo(l[78], l[230]);
		rectf.set(l[78], l[5], l[420], l[360]);
		path.arcTo(rectf, 180, 180, false);
		path.lineTo(l[420], l[228]);
		rectf.set(l[330], l[215], l[450], l[330]);
		path.arcTo(rectf, 330, 60, false);
		rectf.set(l[350], l[270], l[440], l[350]);
		path.arcTo(rectf, 0, 70, false);
		rectf.set(l[110], l[195], l[395], l[490]);
		path.arcTo(rectf, 30, 120, false);
		
		rectf.set(l[60], l[270], l[150], l[350]);
		path.arcTo(rectf, 110, 70, false);
		rectf.set(l[50], l[215], l[170], l[330]);
		path.arcTo(rectf, 150, 60, false);
		path.close();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		canvas.drawPath(path, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		canvas.drawPath(path, paint);
		
		// olho esquerdo
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(l[5]);
		rectf.set(l[130], l[230 + ts[0]], l[240], l[390 + bs[0]]);
		
		canvas.drawArc(rectf, 230, 85, false, paint);
		paint.setStyle(Style.FILL);
		rectf.set(l[167 + lo[0]], l[227 + to[0]], l[203 + ro[0]], l[263 + bo[0]]);
		canvas.drawOval(rectf, paint);
		
		// sombracelha esquerda
		path = new Path();
		paint.setStyle(Style.FILL);
		rectf.set(l[137], l[210], l[170], l[240]);
		path.arcTo(rectf, 190, 80, false);
		rectf.set(l[140], l[207], l[202], l[237]);
		path.arcTo(rectf, 260, 70, false);
		rectf.set(l[195], l[200], l[215], l[216]);
		path.arcTo(rectf, 100, -180, false);
		rectf.set(l[144], l[195], l[210], l[218]);
		path.arcTo(rectf, 280, -90, false);
		rectf.set(l[135], l[202], l[170], l[234]);
		path.arcTo(rectf, 240, -70, false);	
		path.close();
		canvas.drawPath(path, paint);
		
		
		// olho direito
		rectf.set(l[270], l[230 + ts[1]], l[380], l[390 + bs[1]]);
		paint.setStyle(Style.STROKE);
		canvas.drawArc(rectf, 230, 85, false, paint);
		paint.setStyle(Style.FILL);
		rectf.set(l[307 + lo[1]], l[227 + to[1]], l[343 + ro[1]], l[263 + bo[1]]);
		canvas.drawOval(rectf, paint);		
		
		// sombracelha direita
		path = new Path();
		rectf.set(l[297], l[201], l[314], l[217]);
		path.arcTo(rectf, 50, 200, false);
		rectf.set(l[303], l[195], l[368], l[217]);
		path.arcTo(rectf, 220, 100, false);	
		rectf.set(l[343], l[203], l[378], l[232]);
		path.arcTo(rectf, 300, 80, false);	
		rectf.set(l[305], l[208], l[376], l[239]);
		path.arcTo(rectf, 0, -110, false);	
		path.close();
		canvas.drawPath(path, paint);
		
		// nariz
		path = new Path();
		paint.setStrokeWidth(l[5]);
		paint.setStyle(Style.STROKE);
		rectf.set(l[201], l[314], l[231], l[341]);
		canvas.drawArc(rectf, 20, 180, false, paint);
		rectf.set(l[269], l[314], l[299], l[341]);
		canvas.drawArc(rectf, 330, 180, false, paint);
		paint.setStrokeWidth(l[10]);
		rectf.set(l[230], l[316], l[275], l[347]);
		canvas.drawArc(rectf, 350, 200, false, paint);	
		
		// queixo
		paint.setStrokeWidth(l[5]);
		rectf.set(l[221], l[409], l[276], l[440]);
		canvas.drawArc(rectf, 30, 120, false, paint);
		
		// boca
		path = new Path();
		paint.setStrokeWidth(1);
		paint.setStyle(Style.FILL);
		
		if (sinal!=8){
			rectf.set(l[180], l[362], l[234], l[394]);
			path.arcTo(rectf, 160, -80, false);	
			rectf.set(l[211], l[393], l[243], l[418]);
			path.arcTo(rectf, 265, 40, false);
			rectf.set(l[228], l[373], l[271], l[398]);
			path.arcTo(rectf, 110, -40, false);
			
			rectf.set(l[257], l[393], l[289], l[418]);
			path.arcTo(rectf, 255, 40, false);			
			rectf.set(l[266], l[362], l[320], l[394]);
			path.arcTo(rectf, 90, -80, false);
			rectf.set(l[287], l[361], l[331], l[387]);
			path.arcTo(rectf, 110, 20, false);	
			rectf.set(l[253], l[376], l[293], l[401]);
			path.arcTo(rectf, 300, -60, false);
			rectf.set(l[230], l[356], l[270], l[381]);
			path.arcTo(rectf, 70, 40, false);
			
			rectf.set(l[207], l[376], l[247], l[401]);
			path.arcTo(rectf, 300, -60, false);
			rectf.set(l[169], l[361], l[213], l[387]);
			path.arcTo(rectf, 90, 20, false);	
			path.close();
			canvas.drawPath(path, paint);
		} else {
			rectf.set(l[190], l[384], l[310], l[392]);
			canvas.drawOval(rectf, paint);
		}

		rectf.set(l[173], l[373], l[199], l[398]);
		canvas.drawArc(rectf, 135, 100, false, paint);
		rectf.set(l[301], l[373], l[327], l[398]);
		canvas.drawArc(rectf, 315, 100, false, paint);	
		
		// cabelo
		path = new Path();
		paint.setStrokeWidth(1);
		paint.setStyle(Style.FILL);
		path.moveTo(l[423], l[225]);
		path.lineTo(l[412], l[234]);
		rectf.set(l[316], l[157], l[406], l[261]);
		path.arcTo(rectf, 350, -80, false);	
		rectf.set(l[297], l[70], l[375], l[180]);
		path.arcTo(rectf, 20, -110, false);
		rectf.set(l[189], l[39], l[323], l[83]);
		path.arcTo(rectf, 60, 60, false);
		rectf.set(l[125], l[70], l[203], l[180]);
		path.arcTo(rectf, 270, -110, false);
		rectf.set(l[94], l[157], l[184], l[261]);
		path.arcTo(rectf, 270, -80, false);	
		path.lineTo(l[88], l[234]);
		path.lineTo(l[77], l[225]);
		rectf.set(l[78], l[5], l[420], l[360]);
		path.arcTo(rectf, 180, 180, false);
		path.close();
		canvas.drawPath(path, paint);
		return bitmap;
	}
	public Bitmap mulher1(int sinal){
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap bitmap = Bitmap.createBitmap(largura, largura, conf);
		Canvas canvas = new Canvas(bitmap);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(l[7]);
		Path path = new Path();
		rectf = new RectF();
		int[] to = {0,0};
		int[] bo = {0,0};
		int[] lo = {0,0};
		int[] ro = {0,0};
		int[] ts = {0,0};
		int[] bs = {0,0};	
		
		if (sinal==1) {
			to[0] = to[1] = -5;
			bo[0] = bo[1] = -5;
			ts[0] = ts[1] = -10;
			bs[0] = bs[1] = 15;
		}
		if (sinal==2) {
			to[0] = to[1] = 5;
			bo[0] = bo[1] = 5;
			ts[0] = ts[1] = 5;
			bs[0] = bs[1] = -40;
		}
		if (sinal==3) {
			lo[0] = lo[1] = -7;
			ro[0] = ro[1] = -7;
		}
		if (sinal==4) {
			lo[0] = lo[1] = 7;
			ro[0] = ro[1] = 7;
		}
		if (sinal==5||sinal==7) {
			to[0] = 10;
			bo[0] = -9;
			ts[0] = 20;
			bs[0] = -30;
		}
		
		if (sinal==6||sinal==7) {
			to[1] = 10;
			bo[1] = -9;
			ts[1] = 20;
			bs[1] = -30;
		}		
		// cabelo fundo	
		rectf.set(l[46], l[360], l[156], l[467]);
		path.arcTo(rectf, 90, 70, false);		
		rectf.set(l[32], l[116], l[346], l[538]);
		path.arcTo(rectf, 160, 20, false);
		rectf.set(l[39], l[0], l[461], l[500]);
		path.arcTo(rectf, 190, 160, false);	
		rectf.set(l[154], l[116], l[468], l[538]);
		path.arcTo(rectf, 0, 20, false);	
		rectf.set(l[344], l[360], l[454], l[467]);
		path.arcTo(rectf, 20, 70, false);
		
		canvas.drawPath(path, paint);
		
		// cabeça
		path = new Path();
		rectf.set(l[393], l[254], l[420], l[344]);
		path.arcTo(rectf, 310, 150, false);			
		rectf.set(l[99], l[195], l[398], l[495]);
		path.arcTo(rectf, 5, 170, false);	
		rectf.set(l[80], l[254], l[107], l[344]);
		path.arcTo(rectf, 80, 150, false);	
		rectf.set(l[88], l[0], l[412], l[411]);
		path.arcTo(rectf, 170, 200, false);	
		
		path.close();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		canvas.drawPath(path, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		canvas.drawPath(path, paint);
		
		// cabelo frente
		paint.setStyle(Style.FILL);
		path = new Path();
		rectf.set(l[57], l[0], l[441], l[398]);
		path.arcTo(rectf, 180, 180, false);
		rectf.set(l[321], l[195], l[423], l[430]);
		path.arcTo(rectf, 0, -45, false);
		rectf.set(l[308], l[143], l[406], l[303]);
		path.arcTo(rectf, 0, 70, false);
		rectf.set(l[288], l[181], l[382], l[333]);
		path.arcTo(rectf, 35, -70, false);		
		rectf.set(l[298], l[51], l[393], l[165]);
		path.arcTo(rectf, 110, 50, false);	
		path.lineTo(l[292], l[112]);
		rectf.set(l[214], l[12], l[309], l[127]);
		path.arcTo(rectf, 60, 30, false);
		rectf.set(l[123], l[149], l[310], l[350]);
		path.arcTo(rectf, 260, -40, false);	
		rectf.set(l[115], l[161], l[294], l[351]);
		path.arcTo(rectf, 200, -50, false);		
		path.close();
		canvas.drawPath(path, paint);
		canvas.drawRect(l[79], l[220], l[98], l[261], paint);
		canvas.drawRect(l[405], l[230], l[416], l[261], paint);
		
		//olhos 
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(l[6]);
		rectf.set(l[144], l[247 + ts[0]], l[215], l[315 + bs[0]]);
		canvas.drawArc(rectf, 190, 140, false, paint);
		rectf.set(l[285], l[247 + ts[1]], l[356], l[315 + bs[1]]);
		canvas.drawArc(rectf, 350, -140, false, paint);
		paint.setStyle(Style.FILL);
		rectf.set(l[167 + lo[0]], l[252 + to[0]], l[194 + ro[0]], l[278 + bo[0]]);
		canvas.drawOval(rectf, paint);
		rectf.set(l[306 + lo[1]], l[252 + to[1]], l[333 + ro[1]], l[278 + bo[1]]);
		canvas.drawOval(rectf, paint);
		
		// nariz
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(l[10]);
		path = new Path();
		path.moveTo(l[237], l[316]);
		rectf.set(l[236], l[312], l[263], l[340]);
		path.arcTo(rectf, 270, 90, false);
		rectf.set(l[243], l[331], l[263], l[349]);
		path.arcTo(rectf, 0, 70, false);		
		canvas.drawPath(path, paint);
		
		// sombrancelha
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(1);
		path = new Path();	
		rectf.set(l[133], l[220], l[205], l[271]);
		path.arcTo(rectf, 190, 120, false);
		rectf.set(l[193], l[219], l[204], l[230]);
		path.arcTo(rectf, 140, -190, false);
		rectf.set(l[132], l[212], l[212], l[272]);
		path.arcTo(rectf, 310, -120, false);
		path.close();	
		canvas.drawPath(path, paint);

		path = new Path();	
		rectf.set(l[295], l[220], l[367], l[271]);
		path.arcTo(rectf, 350, -120, false);
		rectf.set(l[296], l[219], l[307], l[230]);
		path.arcTo(rectf, 40, 190, false);
		rectf.set(l[288], l[212], l[368], l[272]);
		path.arcTo(rectf, 230, 120, false);
		path.close();
		canvas.drawPath(path, paint);
		
		// boca
		path = new Path();	
		paint.setStyle(Style.FILL);
		if (sinal==8) {
			rectf.set(l[190], l[291], l[308], l[420]);
			path.arcTo(rectf, 20, 140, false);		
			rectf.set(l[190], l[312], l[308], l[401]);
			path.arcTo(rectf, 140, -100, false);
		
		} else {
			rectf.set(l[190], l[281], l[308], l[430]);
			path.arcTo(rectf, 20, 140, false);		
			rectf.set(l[190], l[302], l[308], l[411]);
			path.arcTo(rectf, 140, -100, false);
		}
		path.close();
		canvas.drawPath(path, paint);
		
		path = new Path();	
		paint.setStyle(Style.FILL);
		rectf.set(l[172], l[358], l[220], l[383]);
		path.arcTo(rectf, 100, -30, false);
		rectf.set(l[201], l[377], l[252], l[402]);
		path.arcTo(rectf, 230, 70, false);
		rectf.set(l[238], l[358], l[264], l[383]);
		path.arcTo(rectf, 130, -80, false);		
		rectf.set(l[248], l[377], l[299], l[402]);
		path.arcTo(rectf, 230, 70, false);
		rectf.set(l[280], l[358], l[328], l[383]);
		path.arcTo(rectf, 110, -30, false);
		rectf.set(l[231], l[367], l[269], l[392]);
		path.arcTo(rectf, 80, 20, false);
		path.close();
		canvas.drawPath(path, paint);		
		return bitmap;
		
	}
	public Bitmap mulher2(int sinal){
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap bitmap = Bitmap.createBitmap(largura, largura, conf);
		Canvas canvas = new Canvas(bitmap);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(l[7]);
		Path path = new Path();
		rectf = new RectF();
		int[] to = {0,0};
		int[] bo = {0,0};
		int[] lo = {0,0};
		int[] ro = {0,0};
		int[] ts = {0,0};
		int[] bs = {0,0};	
		
		if (sinal==1) {
			to[0] = to[1] = -5;
			bo[0] = bo[1] = -5;
			ts[0] = ts[1] = -10;
			bs[0] = bs[1] = 15;
		}
		if (sinal==2) {
			to[0] = to[1] = 5;
			bo[0] = bo[1] = 5;
			ts[0] = ts[1] = 5;
			bs[0] = bs[1] = -10;
		}
		if (sinal==3) {
			lo[0] = lo[1] = -7;
			ro[0] = ro[1] = -7;
		}
		if (sinal==4) {
			lo[0] = lo[1] = 7;
			ro[0] = ro[1] = 7;
		}
		if (sinal==5||sinal==7) {
			to[0] = 13;
			bo[0] = -13;
			ts[0] = 15;
			bs[0] = -15;
		}
		
		if (sinal==6||sinal==7) {
			to[1] = 13;
			bo[1] = -13;
			ts[1] = 15;
			bs[1] = -15;
		}		
		
		// cabeça
		rectf.set(l[80], l[5], l[420], l[495]);
		//rectf.set(l[398], l[249], l[447], l[329]);
		//path.arcTo(rectf, 280, 90, false);		
		//path.close();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		canvas.drawOval(rectf, paint);
		//canvas.drawPath(path, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		canvas.drawOval(rectf, paint);
		
		//cabelo
		path = new Path();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
			
		rectf.set(l[33], l[0], l[473], l[432]);
		path.arcTo(rectf, 160, 220, false);	
		rectf.set(l[417], l[366], l[468], l[418]);
		path.arcTo(rectf, 185, -100, false);		
		rectf.set(l[403], l[363], l[484], l[414]);
		path.arcTo(rectf, 85, -40, false);
		rectf.set(l[276], l[348], l[464], l[474]);
		path.arcTo(rectf, 40, 75, false);
		rectf.set(l[207], l[125], l[399], l[455]);
		path.arcTo(rectf, 45, -45, false);
		rectf.set(l[209], l[89], l[403], l[452]);
		path.arcTo(rectf, 10, -15, false);
		rectf.set(l[159], 0 - l[150], l[693], l[243]);
		path.arcTo(rectf, 100, 60, false);
		rectf.set(0 - l[201], 0 - l[116], l[188], l[248]);
		path.arcTo(rectf, 25, 20, false);
		rectf.set(l[91], l[3], l[441], l[499]);
		path.arcTo(rectf, 180, -45, false);
		rectf.set(l[20], l[268], l[239], l[471]);
		path.arcTo(rectf, 75, 90, false);
		rectf.set(l[14], l[344], l[88], l[413]);
		path.arcTo(rectf, 110, -45, false);
		rectf.set(l[32], l[366], l[84], l[411]);
		path.arcTo(rectf, 60, -80, false);
		path.close();
		
		canvas.drawPath(path, paint);
		
		// sombracelha
		path = new Path();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(3);
			
		rectf.set(l[194], l[220], l[209], l[231]);
		path.arcTo(rectf, 90, -180, false);
		rectf.set(l[145], l[218], l[177], l[229]);
		path.arcTo(rectf, 270, -45, false);	
		path.lineTo(l[131], l[236]);
		rectf.set(l[141], l[226], l[173], l[237]);
		path.arcTo(rectf, 190, 50, false);			
		path.close();
		canvas.drawPath(path, paint);
		
		rectf.set(l[289], l[219], l[339], l[230]);
		canvas.drawOval(rectf, paint);
		
		// nariz
		path = new Path();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(l[5]);
			
		path.moveTo(l[240],l[318]);		
		rectf.set(l[234], l[315], l[265], l[340]);
		path.arcTo(rectf, 270, 90, false);
		rectf.set(l[230], l[322], l[265], l[353]);
		path.arcTo(rectf, 0, 45, false);		
		canvas.drawPath(path, paint);
		
		// boca
		path = new Path();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(l[5]);
			
		if (sinal==8) {
			rectf.set(l[203], l[389], l[300], l[400]);
			canvas.drawOval(rectf, paint);
		} else {
			rectf.set(l[202], l[350], l[303], l[423]);
			path.arcTo(rectf, 10, 160, false);
			rectf.set(l[217], l[379], l[255], l[416]);
			path.arcTo(rectf, 240, 70, false);
			path.lineTo(l[253], l[387]);
			rectf.set(l[249], l[380], l[293], l[417]);
			path.arcTo(rectf, 230, 70, false);
			path.close();
			canvas.drawPath(path, paint);
		}
		
		
		// olhos
		paint.setStyle(Style.FILL);
		
		rectf.set(l[171 + lo[0]], l[255 + to[0]], l[199 + ro[0]], l[282 + bo[0]]);
		canvas.drawOval(rectf, paint);
		rectf.set(l[304 + lo[1]], l[255 + to[1]], l[332 + ro[1]], l[282 + bo[1]]);
		canvas.drawOval(rectf, paint);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(l[10]);
		rectf.set(l[151], l[252 + ts[0]], l[211], l[290 + bs[0]]);
		canvas.drawArc(rectf, 180, 180, false, paint);
		rectf.set(l[293], l[252 + ts[1]], l[353], l[290 + bs[1]]);
		canvas.drawArc(rectf, 180, 180, false, paint);			
		return bitmap;
		
	}	
}
