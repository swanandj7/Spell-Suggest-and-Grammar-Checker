import java.util.*;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.*;

public class NgramModel {

	public static int nGram=0;
	public static List<String> testPosTList;
	public static List<String> trainPosTList; 
	private static HashMap<String, Integer> languageModel = new HashMap<String, Integer>();
	
	public NgramModel(int n,List<String> trainData ) {
		// TODO Auto-generated constructor stub
		nGram=n;
		List<String> returnNgrams =new ArrayList<String>();
		trainPosTList=new ArrayList<String>();
		
		/*
		try{
			
			MaxentTagger tagger = new MaxentTagger("taggers/bidirectional-distsim-wsj-0-18.tagger");
			Iterator<String> iter = trainData.iterator();
			while(iter.hasNext())
			{
				String temp=iter.next();
				String tagged = tagger.tagString(temp);
				String splitter=null;
				StringTokenizer st = new StringTokenizer(tagged);
				     while (st.hasMoreTokens()) {
				    	 splitter=st.nextToken();
				    	 String[] parts = splitter.split("/");
				    	 
				    	 if(parts[1]!=null)
				    		 trainPosTList.add(parts[1]);
				    
				     }
					
			}
			returnNgrams=makeNgrams(trainPosTList,nGram);
			
			for(int i=0;i<returnNgrams.size();i++){
				String s =returnNgrams.get(i);
				if(s.equals("<e> <s>")==false)
				{
					if(languageModel.containsKey(s)==true)
					{
						int count=languageModel.get(s);
						count++;
						languageModel.put(s, count);
					}
					else
						languageModel.put(s, 1);	
				}
			}
			
			File writeModel = new File("writeModel.txt"); 
			BufferedWriter bw = new BufferedWriter(new FileWriter(writeModel));
			for(String p:languageModel.keySet())
			{
			    bw.write(p + "," + languageModel.get(p));
			    bw.newLine();
			}
			bw.flush();
			bw.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		*/
		try
		{
			   BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("writeModel.txt")));
			   String l;
			   while((l = br.readLine()) != null)
			   {
			      String[] args = l.split(",");
			      String p = args[0];
			      String b = args[1];
			      languageModel.put(p, Integer.parseInt(b));
			   }
			   br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static List<String> readFile(String filename){
		  List<String> input =new ArrayList<String>();
		  
		  String temp= null;
		  try{	   
			  FileReader fileReader = new FileReader(filename);
	          BufferedReader bufferedReader =new BufferedReader(fileReader);
	          while((temp= bufferedReader.readLine()) != null){
	        	temp=temp.replace(",", "");
	  			temp=temp.replace(".", "");// <e> <s>
	  			temp=temp.replace(";", "");
	  			temp=temp.replace("!", "");	
	  			temp=temp.replace("?", "");
	  			temp=temp.toLowerCase();
	  			StringTokenizer st = new StringTokenizer(temp);
	  		     while (st.hasMoreTokens()) {
	  		    	 input.add(st.nextToken());
	  		    
	  		     }
	          }
	          bufferedReader.close();            
	      }
	      catch(IOException ex){
	    	     ex.printStackTrace();
	   	        
	      }
		   return input;
	}
	
	private static List<String> makeNgrams(List<String> testData,int n) {
		// TODO Auto-generated method stub
		List<String> ret =new ArrayList<String>();
		  
		for(int i=0;i<testData.size();i++){
			String s ="";
			if(i<=(testData.size()-n))
			{
				for(int j=0;j<n;j++)
				{
					s=s.concat(testData.get(i+j));
					if(j<n-1)
						s=s+" ";
				}
				ret.add(s);
			}
		}
		return ret;
	}

	
	private static List<String> checkGrammar() {
		List<String> resultSet =new ArrayList<String>();
		Iterator<String> iter = testPosTList.iterator();
		
		
		while(iter.hasNext()){
			String s =iter.next();
			if(languageModel.containsKey(s)&&languageModel.get(s)>50)
				resultSet.add("T");
			else
				resultSet.add("F");
		}
		
		// TODO Auto-generated method stub
		return resultSet ;
	}
	
	public static void main(String[] args) {
		
		String filename = null;
		List<String> trainData =new ArrayList<String>();
		List<String> testData =new ArrayList<String>();
		List<String> resultSet =new ArrayList<String>();
		
		filename = args[0];
		
		trainData=readFile(filename);
		testData=readFile("input.txt");
		
		
		NgramModel ngram = new NgramModel(3,trainData);
		
		testPosTList=new ArrayList<String>();
		
		try{
			
			MaxentTagger tagger = new MaxentTagger("taggers/bidirectional-distsim-wsj-0-18.tagger");
			Iterator<String> iter = testData.iterator();
			while(iter.hasNext())
			{
				String temp=iter.next();
				String tagged = tagger.tagString(temp);
				String splitter=null;
				StringTokenizer st = new StringTokenizer(tagged);
				     while (st.hasMoreTokens()) {
				    	 splitter=st.nextToken();
				    	 String[] parts = splitter.split("/");
				    	 
				    	 if(parts[1]!=null)
				    		 testPosTList.add(parts[1]);
				    
				     }
					
			}
			testPosTList=makeNgrams(testPosTList,nGram);
			
			
			System.out.println(testPosTList);		
			resultSet=checkGrammar();
			System.out.println(resultSet);
			}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		}
	
}
