package org.commoncrawl.examples;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.maicoin.*;

/**
 * A raw example of how to process a WARC file using the org.archive.io package.
 * Common Crawl S3 bucket without credentials using JetS3t.
 *
 * @author Stephen Merity (Smerity)
 */
public class WARCReaderTest {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static HashSet<String> getFinished()throws Exception{
		File finishedFile = new File("finished");
        HashSet<String> finished = new HashSet<String>();
        if (!finishedFile.exists()) return finished;
        BufferedReader reader = new BufferedReader(new FileReader(finishedFile));
        String line;
        while((line = reader.readLine()) != null){
        	String fn = line.split("[\t ]")[0];
            finished.add(fn);
        }
        reader.close();
        return finished;
	}
	public static void main(String[] args) throws Exception {
		HashSet<String> finished = getFinished();
		BTCWriter writer = new BTCWriter("data");
        PrintWriter finishedWriter  = new PrintWriter(new FileWriter("finished", true));
        PrintWriter summaryWriter  = new PrintWriter(new FileWriter("output", true));
		S3Service s3s = new RestS3Service(null);
		for (String fn: args){
			if (finished.contains(fn)){
				System.out.println(fn + " has finished");
				continue;
			}
            S3Object f = s3s.getObject("aws-publicdatasets", fn, null, null, null, null, null, null);
//            ArchiveReader ar = WARCReaderFactory.get(fn, is, true);
//            FileInputStream is = new FileInputStream(fn);
            ArchiveReader ar = WARCReaderFactory.get(fn, f.getDataInputStream(), true);

            int count = 0;
            int count_addrpages = 0;
            for(ArchiveRecord r : ar) {
            	count++;
                byte[] rawData = null;
            	try{
                    rawData = IOUtils.toByteArray(r, r.available());
            	}catch(EOFException e){
            		e.printStackTrace();
            	}
                String content = new String(rawData);
                Matcher see = BTCAddressValidator.REGEX_BITCOIN_ADDRESS.matcher(content);
                while (see.find()){
                    String addr = see.group();
                    for (int i = 27; i <= Math.min(addr.length(),34); i++){
                        String addrTemp = addr.substring(0, i);
                        String url = r.getHeader().getUrl();
                        if (BTCAddressValidator.isValid(addrTemp)){
                            summaryWriter.println(addrTemp + "\t" + url);
                            summaryWriter.flush();
                            writer.write(addrTemp, url, content);
                            count_addrpages++;
                            break;
                        }
                    }
                }
            }
            finishedWriter.println(fn + "\t" + count + "\t" + count_addrpages);
            finishedWriter.flush();
		}
	}
}