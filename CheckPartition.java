package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class CheckPartition extends Partitioner<IntWritable, SightingDetails> {

	@Override
	public int getPartition(IntWritable key, SightingDetails value, int numPartitions) {
	  // multiply by 127 to perform some mixing
	  return Math.abs(key.get() % numPartitions);
	}
}
