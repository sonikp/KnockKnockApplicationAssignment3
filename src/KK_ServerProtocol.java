

import java.security.SecureRandom;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class KK_ServerProtocol {
    
	//Key for 2nd part of array
	final static int CLUE = 0;
	final static int ANSWER = 1;
	private String[][] knockknockInfo;
	
	private static final int WAITING = 0;
    private static final int SENTKNOCKKNOCK = 1;
    private static final int SENTCLUE = 2;
    private static final int ANOTHER = 3;

    private static final int NUMJOKES = 19;

    private int state = WAITING;
    private SecureRandom rand = new SecureRandom();
    private int currentJoke = rand.nextInt(19);			
     
    public KK_ServerProtocol() throws FileNotFoundException, IOException {
 
    	//get properties file
    	Properties prop = new Properties();
    	prop.load(new FileInputStream("KnockKnockJokesData.properties"));

    	//get two dimensional array from the properties file that has been delineated
    	knockknockInfo = fetchArrayFromPropFile("knockknockInfo", prop);
    }
    
    public String processInput(String theInput) throws FileNotFoundException, IOException {
    	
    	//INFO: demonstrates state by printing out all the knock knock CLUE and ANSWER details
    	for (int i = 0; i < knockknockInfo.length; i++) {
    		System.out.print("\n" + i + ":");	
    		System.out.print("\n");
    		System.out.print("Clue: " + knockknockInfo[i][CLUE]);
    		System.out.print("\n");
    		System.out.print("Answer: " + knockknockInfo[i][ANSWER]);
    	}
    	

        String theOutput = null;
        System.out.println("\n===========================\nrandomizeJoke: " + currentJoke + "\n");
        
        if (currentJoke == (NUMJOKES - 1)) {
        	currentJoke = 0;
        }

        if (state == WAITING) {
            theOutput = "Knock! Knock!";
            state = SENTKNOCKKNOCK;
        } else if (state == SENTKNOCKKNOCK) {
            if (theInput.equalsIgnoreCase("Who's there?")) {
                theOutput = knockknockInfo[currentJoke][CLUE];
                state = SENTCLUE;
            } else {
                theOutput = "You're supposed to say \"Who's there?\"! " +
			    "Try again. Knock! Knock!";
            }
        } else if (state == SENTCLUE) {
            if (theInput.equalsIgnoreCase(knockknockInfo[currentJoke][CLUE] + " who?")) {
                theOutput = knockknockInfo[currentJoke][ANSWER] + " Want another? (y/n)";
                state = ANOTHER;
            } else {
                theOutput = "You're supposed to say \"" + 
                knockknockInfo[currentJoke][CLUE] + 
			    " who?\"" + 
			    "! Try again. Knock! Knock!";
                state = SENTKNOCKKNOCK;
            }
        } else if (state == ANOTHER) {
        	currentJoke = rand.nextInt(19);
        	System.out.println("randomizeJoke: " + currentJoke);
            if (theInput.equalsIgnoreCase("y")) {
                theOutput = "Knock! Knock!";
                if (currentJoke == (NUMJOKES))
                    currentJoke = 0;
                else
                    currentJoke++;
                state = SENTKNOCKKNOCK;
            } else {
                theOutput = "Bye.";
                state = WAITING;
            }
        }
        return theOutput;
    }
    
    /**
	 * Creates two dimensional array from delineated string in properties file
	 * @param propertyName name of the property as in the file
	 * @param propFile the instance of the Properties file that has the property
	 * @return two dimensional array
	 */
	private static String[][] fetchArrayFromPropFile(String propertyName, Properties propFile) {

		//get array split up by the semicolin
		String[] a = propFile.getProperty(propertyName).split(";");
		
		Collections.shuffle(Arrays.asList(a));		// 

		//create the two dimensional array with correct size
		String[][] array = new String[a.length][a.length];

		//combine the arrays split by semicolin and comma 
		for(int i = 0;i < a.length;i++) {
			array[i] = a[i].split(":");
		}
		return array;
	}
	
	
}
