package answers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;



public class Penalizer {
	private final String delimiter="~";
	private HashMap<String, Boolean> stopWords;
	/**
	 * @param args
	 */
	public Penalizer() {
		// TODO Auto-generated method stub
		stopWords=new HashMap<String, Boolean>();
		try {
			FileInputStream fstream=new FileInputStream("stopwords.txt");
			DataInputStream input = new DataInputStream(fstream);
			BufferedReader buffer= new BufferedReader(new InputStreamReader(input));
			String line;
			while((line=buffer.readLine())!=null){
				stopWords.put(line.trim(), true);
			}
		}catch (FileNotFoundException e) {
			System.err.println("Error:"+e.getMessage());
		} catch (IOException e) {
			System.err.println("Error:"+e.getMessage());
		}
		String keyword ="tallest man";
//		String questions= "Who is the worlds tallest man?-Who is the tallest man to play in NBA?-What size of shoe does the tallest man wear";
		
	}
	public HashMap<String,Double> penalize(String questions,String queryString){
		//String questions="who is our first president of india?~Who was the first president of india?";
		double score=0;
		HashMap<String, Boolean> keywordMap=new HashMap<String, Boolean>();
		for(String keywords:queryString.split("\\s+")){
			keywordMap.put(keywords, true);
		}
		HashMap<String,Double> questScore=new HashMap<String, Double>();
		for(String question:questions.split(delimiter)){
			score=0;
			String ques=filterStopWords(question).replaceAll("[^a-zA-Z0-9]+"," ");
//			String ques =question;
			for(String word:ques.split("\\s+")){
				if(keywordMap.containsKey(word)){
					score+=1;
				}else{
					score-=0.25;
				}
			}
			questScore.put(question, score);
		}
		return questScore;
	}
	public HashMap<String,Double> penalize1(String questions,String queryString){
	       
        double score=0;
       
        int totalWords=0;
       
        HashMap<String,Double> questScore=new HashMap<String, Double>();
       
        HashMap<String, Double> keywordMap=new HashMap<String, Double>();
        for(String keywords:filterStopWords(questions).replaceAll("[^a-zA-Z0-9]+"," ").split("\\s+")){

            if(keywordMap.keySet().contains(keywords)){
                keywordMap.put(keywords, keywordMap.get(keywords)+1);
            }
            else
                keywordMap.put(keywords, 1d);
            totalWords+=1;
        }
       
               
        for(String key:keywordMap.keySet()){
            keywordMap.put(key, Math.log(totalWords/keywordMap.get(key)));
        }
       
       
       
       
        for(String question:questions.split(delimiter)){
           
            double idfSum=0;
            String ques=filterStopWords(question).replaceAll("[^a-zA-Z0-9]+"," ");           
           
            for(String word:ques.split("\\s+")){
                idfSum+=keywordMap.get(word);               
            }
           
            score=Math.exp(-idfSum);
            questScore.put(question, score);
        }
       
       
        return questScore;
    }
	private String filterStopWords(String question){ 
		StringBuilder strb= new StringBuilder();
		String[] questionWords=question.split("\\s+");
		for(String word:questionWords){
			if(!stopWords.containsKey(word)){
				strb.append(word).append(" ");
			}
		}
		return strb.toString().trim();
	}

}
