package com.google.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ConvertObjectFile {
	// 객체 파일 생성 경로
	public String O_PATH = "C:/RHIE/spring-workspace/GooglePj/private-info.obj";
	
	public static void main(String[] args) throws IOException {
		ConvertObjectFile cof = new ConvertObjectFile();
		ConvertObjectData cod = ConvertObjectData
				.builder()
				.clientId("") // 클라이언트 아이디
				.clientPw("").build(); // 클라이언트 시크릿 비밀번호
		ObjectOutputStream objOut = null;
		FileOutputStream fileOut = null;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("clientId", cod.getClientId());
		data.put("clientPw", cod.getClientPw());

		try {
			fileOut = new FileOutputStream(cof.O_PATH);
			objOut = new ObjectOutputStream (fileOut);
			objOut.writeObject(data);
			System.out.println("---SUCCESS---");
		} finally {
			if (objOut!=null) objOut.close();
			else if (fileOut!=null) fileOut.close();
		}
	}
}
