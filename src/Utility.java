import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Utility {

    /**
     * Return list of words from comma separated list of words
     *
     * @param line
     * @return words
     */
    private static ArrayList<String> parseChosenWords(String line) {
        Scanner s = new Scanner(line);
        s.useDelimiter(",");
        ArrayList<String> words = new ArrayList<>();
        while (s.hasNext()) {
            words.add(s.next());
        }
        s.close();
        return words;
    }

    public static ArrayList<String> readPosWords(String filepath) throws IOException{
        FileReader fr = new FileReader(filepath);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        br.close();
        ArrayList<String> words = parseChosenWords(line);
        return words;

    }


}
