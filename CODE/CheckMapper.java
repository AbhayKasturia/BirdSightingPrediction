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
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class CheckMapper extends Mapper<Object, Text, IntWritable, SightingDetails>{
	 private Random rand,rand1;
	 MultipleOutputs mos;
	 int n,count;
	 
	public void setup(Context context) {
		n = context.getConfiguration().getInt("n", -10);
    	
        if (n == -10) {
            throw new Error("Can not find n");
        }

        rand = new Random();
        rand1 = new Random();

	    mos = new MultipleOutputs<IntWritable, SightingDetails>(context);

	    count = 0;

    }

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		
		String val=value.toString();		
		//val = val.replace("(", "").replace(")", "");
		String[] columns = val.split(",");
		//System.out.println("Size = " + columns.length);
		//System.out.println("Column = " + columns[18] + "Samp ID = " + columns[0]);
		if(columns[18].equals("1")&&!columns[0].startsWith("Samp")){
			count++;
			String result = "";
			if(columns[26].equals("?") || columns[26].equals("0")) 
				result="false"; 
			else 
				result="true";
			String[] filtered={rand.nextInt(Integer.MAX_VALUE)+"",result , columns[2],columns[3],columns[4],columns[5],columns[6],columns[7],columns[10],columns[18],columns[958],columns[959],columns[956],columns[957],columns[1090],columns[1091],columns[1092],columns[1093],columns[1094],columns[1095],columns[1096],columns[1097],columns[1098],columns[1099],columns[1100],columns[1101],columns[0]};
			SightingDetails details= new SightingDetails(filtered); 
			//if(details.getRed_Bird().get())
			//	System.out.println("TRUE");
			//System.out.println(details.getRed_Bird().toString());
			//context.write(new IntWritable(Integer.parseInt(columns[5])), details);

			if(rand.nextInt(10)%10<8){
				mos.write("training" , new IntWritable(0) , details , "train/training");
				for(int i=0;i<n;i++){
					context.write(new IntWritable(rand1.nextInt(n)), details);
				}
			}
			else
				mos.write("validation" , new IntWritable(0), details ,"val/validation");
		}
	}

	public void cleanup(Context context) throws IOException, InterruptedException{
		System.out.println("Number of records converted = " + count);
		mos.close();
	}

}
