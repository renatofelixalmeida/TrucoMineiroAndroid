package me.chester.trucomineiro.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.Log;

public class Arquivo {
	private String FILENAME = "";
	
	public Arquivo(String nome) {
		this.FILENAME = nome;
	}
	
	public void escrever(String data) {	
        try {
        	FileOutputStream fs = new FileOutputStream(this.FILENAME, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fs);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Arquivo", "Erro ao gravar o arquivo " + e.toString());
        }      
    }
	
	public String ler(){
		String ret = "";
		try {
			FileInputStream fs = new FileInputStream(this.FILENAME);			
            InputStream inputStream = fs;
             
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                 
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("Arquivo", "Arquivo nao encontrado: " + e.toString());
        } catch (IOException e) {
            Log.e("Arquivo", "Impossivel ler o arquivo: " + e.toString());
        }
		return ret;
	}

	public void apagar(){
        try {
        	File file = new File(this.FILENAME);
        	if (file.exists()) file.delete();
        }
        catch (Exception e) {
            Log.e("Arquivo", "Erro ao escrever no arquivo: " + e.toString());
        } 		
	}

}
