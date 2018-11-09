import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Utility {

    /**
     * Return list of words from comma separated list of words
     * @param line
     * @return words
     */
    public static ArrayList<String> parseChosenWords(String line) {
        Scanner s = new Scanner(line);
        s.useDelimiter(",");
        ArrayList<String> words = new ArrayList<>();
        while (s.hasNext()) {
            words.add(s.next());
        }
        return words;
    }

    public static String getUserString(){
        String userString = "";
        try{
            BufferedReader filepathReader = new BufferedReader(new InputStreamReader(System.in));
            userString = filepathReader.readLine();
            filepathReader.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
        return userString;
    }


}
