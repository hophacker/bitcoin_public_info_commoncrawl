package org.commoncrawl.examples;
import java.io.IOException;
import java.util.regex.Matcher;

import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCWriter;
import org.archive.io.warc.WARCReader;
import org.archive.io.warc.WARCReaderFactory;
import org.archive.io.warc.WARCWriterPool;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.maicoin.BTCAddressValidator;

/**
 * This is a raw example of how you can retrieve a file from the
 * Common Crawl S3 bucket without credentials using JetS3t.
 *
 * @author Stephen Merity (Smerity)
 */
public class S3ReaderTest {
	public static void main(String[] args) throws IOException, S3ServiceException {
		// We're accessing a publicly available bucket so don't need to fill in our credentials
		S3Service s3s = new RestS3Service(null);
		
		// Let's grab a file out of the CommonCrawl S3 bucket
		//String fn = "common-crawl/crawl-data/CC-MAIN-2013-48/segments/1386163035819/warc/CC-MAIN-20131204131715-00000-ip-10-33-133-15.ec2.internal.warc.gz";
        //String fn = "common-crawl/crawl-data/CC-MAIN-2013-48/segments/1386163035819/warc/15-00000-ip-10-33-133-15.ec2.internal.warc.gz";
        String fn = "common-crawl/crawl-data/CC-MAIN-2014-23/segments/1404776422751.47/wat/CC-MAIN-20140707234022-00064-ip-10-180-212-248.ec2.internal.warc.wat.gz";
		S3Object f = s3s.getObject("aws-publicdatasets", fn, null, null, null, null, null, null);
		
		// The file name identifies the ArchiveReader and indicates if it should be decompressed
		ArchiveReader ar = WARCReaderFactory.get(fn, f.getDataInputStream(), true);
		// Once we have an ArchiveReader, we can work through each of the records it contains
		int count = 0;
		for(ArchiveRecord r : ar) {
            byte[] rawData = IOUtils.toByteArray(r, r.available());
			String content = new String(rawData);
            Matcher see = BTCAddressValidator.REGEX_BITCOIN_ADDRESS.matcher(content);
            while (see.find()){
                String addr = see.group();
                for (int i = 27; i <= Math.min(addr.length(),34); i++){
                    String addrTemp = addr.substring(0, i);
                    String url = r.getHeader().getUrl();
                    if (BTCAddressValidator.isValid(addrTemp)){
                        System.out.println(addrTemp);
                    	break;
                    }
                }
            }
			if (count++ > 50000) break; 
		}
	}
}