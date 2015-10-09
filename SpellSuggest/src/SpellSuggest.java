import java.io.*;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.*;
import java.util.regex.*;


public class SpellSuggest {

	
	private static HashMap<String, Integer> languageModel = new HashMap<String, Integer>();
	private static ArrayList<String> inputText= new ArrayList<String>();
	
	public SpellSuggest(String file1,String file2) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file1));
		Pattern p = Pattern.compile("\\w+");
		for(String temp = ""; temp != null; temp = in.readLine()){
			Matcher m = p.matcher(temp.toLowerCase());
			while(m.find()) 
			{	
				temp = m.group();
				if(languageModel.containsKey(temp)==true)
				{
					int count=languageModel.get(temp);
					count++;
					languageModel.put(temp, count);
				}
				else
					languageModel.put(temp, 1);
		
			}
		}
		in.close();
		
		in = new BufferedReader(new FileReader(file2));
		/*for(String temp = ""; temp != null; temp = in.readLine()){
			temp.replaceAll("\\p{P}", "");
			inputText.add(temp);
		}*/
		String temp="";
		
		while((temp = in.readLine())!=null){
			temp=temp.replace(",", "");
			temp=temp.replace(".", "");
			temp=temp.replace(";", "");
			temp=temp.replace("!", "");
			temp=temp.replace("?", "");
			temp=temp.toLowerCase();
			StringTokenizer st = new StringTokenizer(temp);
		     while (st.hasMoreTokens()) {
		    	 inputText.add(st.nextToken());
		     }
		     
			
		}
		in.close();
		
	}
	
	public ArrayList<String> editDistance(String errorSpell) {
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0; i < errorSpell.length(); i++) 
				result.add(errorSpell.substring(0, i) + errorSpell.substring(i+1));
		for(int i=0; i < errorSpell.length()-1; i++) 
			result.add(errorSpell.substring(0, i) + errorSpell.substring(i+1, i+2) + errorSpell.substring(i, i+1) + errorSpell.substring(i+2));
		for(int i=0; i < errorSpell.length(); i++){
			for(char j='a'; j<= 'z'; j++) 
				result.add(errorSpell.substring(0, i) + String.valueOf(j) + errorSpell.substring(i+1));
			
		}
		for(int i=0; i <= errorSpell.length(); i++){
			for(char j='a'; j <= 'z'; ++j) 
				result.add(errorSpell.substring(0, i) + String.valueOf(j) + errorSpell.substring(i));
		}
		return result;
	}
	
	public final String suggestCorrection(String errorSpell) {
		if(languageModel.containsKey(errorSpell)) return errorSpell;
		ArrayList<String> list = editDistance(errorSpell);
		HashMap<Integer, String> probables = new HashMap<Integer, String>();
		
		
		for(String s : list){
			if(languageModel.containsKey(s)) 
				probables.put(languageModel.get(s),s);
		}
		if(probables.size() > 0) 
			return probables.get(Collections.max(probables.keySet()));
		
		for(String s : list){
			for(String w : editDistance(s)){
				if(languageModel.containsKey(w)) 
					probables.put(languageModel.get(w),w);
			}
		}
		
		if(probables.size()>0){
			return probables.get(Collections.max(probables.keySet()));
		}
		else
			return errorSpell;
	}
	
	public static void main(String args[]) throws IOException {
		
			SpellSuggest ss= new SpellSuggest("words.txt","inputFile.txt");
			int errorCount=0;
			ArrayList<String> outputText= new ArrayList<String>();
			for(String errorSpell : inputText){
				boolean value=languageModel.containsKey(errorSpell);
				if(value ==false){
					String suggestion=ss.suggestCorrection(errorSpell);
					if(suggestion.equals(errorSpell)==false){
						errorCount++;
						int index=inputText.indexOf(errorSpell);
						suggestion="**"+suggestion;
						outputText.add(index, suggestion);
					}	
				}
				else
					outputText.add(errorSpell);
			}
			outputText.add(0, "Total errors: "+errorCount);
			for(String s : outputText)
					System.out.print(s+" ");
	}

}	
