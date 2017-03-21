package main;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class TestPartition extends Partitioner<IntWritable, SightingDetails> {

	private static int oneTenthSize;
	int size;
	public void setup(Configuration conf){
		
		size=conf.getInt("count", -10);
		 if (size == -10) {
	            throw new Error("Can not find count");	              
	        }
	}
	
	@Override
	public int getPartition(IntWritable key, SightingDetails value, int numPartitions) {

	  oneTenthSize=size/(numPartitions+1);
	  return Math.abs(key.get()/oneTenthSize);
	}
}