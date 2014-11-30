package org.maicoin;

import java.io.File;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class BTCWriter {
	File dataDir = null;
	final static int fileMaxLength = 250;
	public BTCWriter(String dataDirStr)throws Exception{
		dataDir =  new File(dataDirStr);
		if (dataDir.exists()){
			if (dataDir.isFile()){
				throw new Exception("it should be folder instead of file");
			}
		}else{
			dataDir.mkdir();
		}
	}
	public void write(String BTCAddr, String url, String content){
		System.out.println(BTCAddr + "\t" + url);
		String addrDirStr = dataDir.getPath() + "/" + BTCAddr;
		String htmlStr = addrDirStr  + "/" + url;
		File addrDir = new File(addrDirStr);
		addrDir.mkdir();
		try{
			String filename = url.replace("/", "\\");
            File htmlFile = new File(addrDir, filename.substring(0, Math.min(fileMaxLength, filename.length())));
            htmlFile.createNewFile();
            PrintWriter writer  = new PrintWriter(htmlFile);
            writer.println(url);
            writer.println(content);
            writer.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
