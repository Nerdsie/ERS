package ERS.Files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import ERS.States.Setting;

public class Stream {
	
	public ArrayList<String> load() throws Exception{
		ArrayList<String> toR = new ArrayList<String>();
		
		BufferedReader inputStream = null;
		
		try {
			inputStream = new BufferedReader(new FileReader("ERSSettings.txt"));
			   
			String l;
			while ((l = inputStream.readLine()) != null) {
			 	toR.add(l);
			}
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		}
		
		return toR;
	}
	
	public void save() throws Exception{
		BufferedReader inputStream = null;
		PrintWriter outputStream = null;
		
		try {
		    outputStream = new PrintWriter(new FileWriter("ERSSettings.txt"));

		    for(Setting s : Setting.values()){
		    	outputStream.println(s.getSave());
		    }
		    
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (outputStream != null) {
		        outputStream.close();
		    }
		}
	}
}
