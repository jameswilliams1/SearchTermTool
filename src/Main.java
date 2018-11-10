import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String outputDir = System.getProperty("user.home");
        ArrayList<SearchTerm> terms = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.println("Enter filepath to search data (must have one search per row with one heading row):");
                String filepath = br.readLine();
                SearchTerm.readSearchFile(filepath, terms);
                System.out.println("Number of search terms input: " + terms.size());
                break;
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("Search data could not be processed, check file path and data format and try again.");
            }
        }

        ArrayList<String> posWords;
        while (true) {
            try {
                System.out.println("Enter filepath to positive words (one word in each column on first row):");
                String filepath = br.readLine();
                posWords = Utility.readPosWords(filepath);
                System.out.println("Number of positive words input: " + posWords.size());
                break;
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("Positive words file could not be processed, check file path and data format and try again.");
            }
        }

        Utility.findPosSearches(posWords, terms, outputDir);
        ArrayList<SplitTerm> splitTerms = SplitTerm.splitTermList(terms, 2);
        SplitTerm.writeFile(splitTerms, outputDir, 2);



    }
}