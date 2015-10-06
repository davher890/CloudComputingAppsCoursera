import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
 
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
        List<String> stopWordsSet = new ArrayList<String>(Arrays.asList(stopWordsArray));
        List<Integer> indexes = new ArrayList<Integer>(Arrays.asList(getIndexes()));
        List<String> fileWords = new ArrayList<String>();
       	
       	BufferedReader br = new BufferedReader(new FileReader(this.inputFileName));
       	try {
       	    StringBuilder sb = new StringBuilder();
       	    String line = br.readLine();

       	    int lineIndex =0;
       	    while (line != null){
       	        StringTokenizer tokenizer = new StringTokenizer(line, delimiters);
       	        
       	        while (tokenizer.hasMoreTokens()){
       	        	String token = tokenizer.nextToken().toLowerCase();
       	        	if (token != null && !stopWordsSet.contains(token)){
       	        		fileWords.add(token);
	       	        }
       	        }
       	        line = br.readLine(); 
       	        lineIndex++;
       	    }
       	} finally {
       	    br.close();
       	}
       	
       	for (int i=0; i<fileWords.size(); i++){
       		if (indexes.contains(i)){ 
       			String token = fileWords.get(i);
		       	if (wordMap.get(token) == null){
		       		wordMap.put(token, 1);
		       	}
		       	else {
		       		wordMap.put(token, wordMap.get(token)+1);
		       	}
       		}
       	}
       	
		ret = sortMap(wordMap);
		return ret;

	}
    
    /**
     * Sort de map passed as argument and return an string array with the 20 top elements 
     */
    private static String[] sortMap(Map<String, Integer> map){
    
	    Object[] a = map.entrySet().toArray();
	    Arrays.sort(a, new Comparator() {
	        public int compare(Object o1, Object o2) {
	            return ((Map.Entry<String, Integer>) o2).getValue().compareTo(
	                    ((Map.Entry<String, Integer>) o1).getValue());
	        }
	    });
	    
	    String[] ret = new String[20];
	    for (int i=0; i<20; i++) {
	    	if (a.length>i && a[i] != null){
	    		ret[i] = ((Map.Entry<String, Integer>) a[i]).getKey();
	    		//System.out.println(((Map.Entry<String, Integer>) a[i]).getKey() + " : " + ((Map.Entry<String, Integer>) a[i]).getValue());
	    	}
	    }
	    return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
