package main;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import java.io.BufferedReader;
import java.io.FileInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;

public class FinalProject {

	
	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		BasicConfigurator.configure();
		
		Path input;
		if (otherArgs.length > 0) {
			input = new Path(otherArgs[0]);
		} else {		
			throw new Exception("Parameters wrong");
		}
		
		FileSystem fs =  FileSystem.get(new Configuration());
		// preprocessing and building model
		// buildModel(conf, otherArgs[0],otherArgs[1],Integer.parseInt(otherArgs[3]));
		System.out.println("Model Ready");
		// true stands for recursively deleting the folder you gave
		// fs.delete(new Path(otherArgs[1]), true);
		// validate model
		// valModel(conf, otherArgs);
		System.out.println("Validation Done");
		// add indexing to unlabelled data
		// int unlabel_count = test_add_index(otherArgs);
		//System.out.println("Count = " + unlabel_count);
		// test model on unlabelled data
		test(conf, otherArgs,189502);
		//System.out.println("Testing Done");
	}

	// sequential indexing of the unlabelled data
	public static int test_add_index(String []otherArgs) throws Exception{

		
		BufferedReader br = new BufferedReader(new InputStreamReader(
		        new BZip2CompressorInputStream(new FileInputStream(otherArgs[4]+"/unlabeled.csv.bz2"))));		 
		Integer i=0;
		String line;
		
		Writer writer = new  FileWriter(otherArgs[4]+"/indexed/indexed");
		
		while ((line = br.readLine()) != null) {
			line= line+","+i;		
			writer.write(line);
			writer.write("\n");
			i++;
		}
		writer.close();
		return i;
	}
	
	public static void buildModel(Configuration conf, String input,String output,int n) throws Exception {
		
		conf.setInt("n",n);
		conf.setStrings("model",output);
		Job job = Job.getInstance(conf, "FinalProject");
		
		//job.addCacheFile(new Path(output+"/index").toUri());
		
		job.setJarByClass(FinalProject.class);
		
		job.setMapperClass(CheckMapper.class);	
		job.setPartitionerClass(CheckPartition.class);	
		job.setReducerClass(CheckReducer.class);		
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(SightingDetails.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		// job.setNumReduceTasks(0);
        MultipleOutputs.addNamedOutput(job, "training", SequenceFileOutputFormat.class,IntWritable.class, SightingDetails.class);
        MultipleOutputs.addNamedOutput(job, "validation", SequenceFileOutputFormat.class,IntWritable.class, SightingDetails.class);

				
		//job.setInputFormatClass(SequenceFileInputFormat.class);
		
		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		boolean ok = job.waitForCompletion(true);
		if (!ok) {
			throw new Exception("Job failed");
		}
		else
		{
			
			return;
		}					
	}

	private static void valModel(Configuration conf, String[] args) throws Exception {
		conf.setInt("n",Integer.parseInt(args[3]));
		Job job = Job.getInstance(conf, "FinalProject");
		job.setJarByClass(FinalProject.class);
		job.setMapperClass(ValMapper.class);
		//job.setReducerClass(ValReducer.class);
		job.setNumReduceTasks(0);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		// job.setOutputKeyClass(Text.class);
		// job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[2]));
		FileOutputFormat.setOutputPath(job, new Path(args[6]));
		job.setInputFormatClass(SequenceFileInputFormat.class);
		boolean ok = job.waitForCompletion(true);
		if (!ok) {
			throw new Exception("Job failed");
		}
		else
		{
			long correct =0;
			long incorrect=0;
			for (Counter counter : job.getCounters().getGroup(ValMapper.VAL_COUNTER_GROUP)) {
                System.out.println(counter.getDisplayName() + "\t" + counter.getValue());
                if(counter.getDisplayName().equals("correct"))
                    correct =  counter.getValue();
                else if(counter.getDisplayName().equals("incorrect"))
                	incorrect =  counter.getValue(); 
            }
            System.out.println("Accuracy = "+ ((double)correct/(correct+incorrect))*100);
		}	
	}

	private static void test(Configuration conf, String[] args,int count) throws Exception {
		conf.setInt("n",Integer.parseInt(args[3]));
		conf.setInt("count",count);
		Job job = Job.getInstance(conf, "FinalProject");
		job.setJarByClass(FinalProject.class);
		job.setMapperClass(TestMapper.class);
		job.setPartitionerClass(TestPartition.class);
		job.setReducerClass(TestReducer.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(SightingDetails.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		System.out.println("INPUT=" + args[4]+"/indexed");
		System.out.println("OUTPUT=" + args[5]);
		FileInputFormat.addInputPath(job, new Path(args[4]+"/indexed"));
		FileOutputFormat.setOutputPath(job, new Path(args[5]));
		boolean ok = job.waitForCompletion(true);
		if (!ok) {
			throw new Exception("Job failed");
		}
		else
		{
			return;
		}	
	}
	
}
