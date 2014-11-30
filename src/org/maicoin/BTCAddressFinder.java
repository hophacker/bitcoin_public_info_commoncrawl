package org.maicoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;

public class BTCAddressFinder extends Configured implements Tool{

	public int run(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		Job job = new Job(conf);
		job.setJarByClass(BTCAddressFinder.class);
		job.setNumReduceTasks(1);

		return 0;
	}

}
