import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

public class Test {
	
	public static void main(String[] args) throws IOException{
	
		/* BufferedReader br = new BufferedReader(new InputStreamReader(
			        new BZip2CompressorInputStream(new FileInputStream(args[0]))));		 
		 String line= br.readLine();				 
		 String[] columns = line.split(",");		  
		 int i=0;
		 for(String column:columns){			 
			 System.out.println(column+"	"+i);			 
			 i++;
		 }*/
		
		
		String check="(1,false,(24.61967,-81.53504,2014,05,131,13.5,Florida,1),"
				+ "(1,.28,77.48083695877356536150818313652372073752,.2018716577540106951871657754010695187166),"
				+ "(9,?,9,?,9,?,3,?,4,?,?,1))";
		check = check.replace("(", "").replace(")", "");
		String[] columns = check.split(",");
		//System.out.println(check);
		int i=0;
		 for(String column:columns){			 
			 System.out.println("index  "+i+" 	:"+column);		
			 i++;
		 }
	
		
		
		
	}
	
	
	public boolean contains(final Integer[] array, final Integer key) {
	    return Arrays.asList(array).contains(key);
	}

}
