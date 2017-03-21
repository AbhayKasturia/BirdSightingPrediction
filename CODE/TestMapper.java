package main;


import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TestMapper extends Mapper<Object, Text, IntWritable, SightingDetails> {

	@Override
	public void map(Object key, Text value, Context context) 
		throws IOException, InterruptedException {
		
	
		Random rand=new Random();
		String[] columns = value.toString().split(",");			
		
		
		System.out.println("value of length :   "+columns.length);
		System.out.println("value of colmn 1658 :   "+columns[1657]);
		// filtering 
		if(columns[18].equals("1")&&!columns[0].startsWith("Samp")){
			
			String result = "";
			if(columns[26].equals("?") || columns[26].equals("0")) 
				result="false"; 
			else 
				result="true";
			// selecting specific columns
			String[] filtered={rand.nextInt(Integer.MAX_VALUE)+"",result , columns[2],columns[3],columns[4],columns[5],columns[6],columns[7],columns[10],columns[18],columns[958],columns[959],columns[956],columns[957],columns[1090],columns[1091],columns[1092],columns[1093],columns[1094],columns[1095],columns[1096],columns[1097],columns[1098],columns[1099],columns[1100],columns[1101],columns[0]};
			SightingDetails details= new SightingDetails(filtered);
		
			context.write(new IntWritable(Integer.parseInt(columns[1657])), details);
		}	
	}
}