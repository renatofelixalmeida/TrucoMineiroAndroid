package me.chester.trucomineiro.android;

public class KmlFile {
	public String criar(String texto){
//		String saida = "<html><head><title>Links para as localizacoes</title><\head><body>" +
//						"<table>";
		String[] linhas = texto.split("|");
		for (int i=0;i<linhas.length;i++){
			String[] campos = linhas[i].split(" ");
			if (campos.length>2){
				
			}
		}
		return "";
	}
}
