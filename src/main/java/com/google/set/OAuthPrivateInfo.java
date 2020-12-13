package com.google.set;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import com.google.util.ConvertObjectFile;

public class OAuthPrivateInfo {
	private String id;
	private String pw;
	
	private OAuthPrivateInfo() {
		loadInfo();
	}
	
	public static OAuthPrivateInfo getInstance() {
		return classHolder.INSTANCE;
	}
	
	private static class classHolder {
		private static final OAuthPrivateInfo INSTANCE = new OAuthPrivateInfo();
	}
	
	private void loadInfo() {
		ConvertObjectFile cof = new ConvertObjectFile();
		ObjectInputStream objInputStream = null;
		FileInputStream inputStream = null;
		try{
			inputStream = new FileInputStream(cof.O_PATH);
			objInputStream = new ObjectInputStream (inputStream);
			Map<String, Object> data = (Map<String, Object>)objInputStream.readObject();
			this.id = String.valueOf(data.get("clientId"));
			this.pw = String.valueOf(data.get("clientPw"));
			objInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			if (objInputStream != null){
				try{objInputStream.close();}catch (Exception e){}
			}
			else if (inputStream != null){
				try{inputStream.close();}catch (Exception e){}
			}
		}
	}
	
	public String getId() {
		return id;
	}
	
	public String getPw() {
		return pw;
	}
}
