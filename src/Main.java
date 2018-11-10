import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    public static void findPosWords(BufferedReader br, ArrayList<SearchTerm> terms, String outputDir) {
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
    }

    public static void main(String[] args) {

        String outputDir;
        ArrayList<SearchTerm> terms = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String fileName;
        String inputDir;

        while (true) { // Set main data location and process
            try {
                System.out.println("Enter filepath to search data (must have one search per row with one heading row):");
                String filepath = br.readLine();
                SearchTerm.readSearchFile(filepath, terms);
                System.out.println("Number of search terms input: " + terms.size());
                Path p = Paths.get(filepath);
                inputDir = p.getParent().toString();
                fileName = p.getFileName().toString();
                break;
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("Search data could not be processed, check file path and data format and try again.");
            }
        }
        String fileNameNoEx = fileName.substring(0, fileName.indexOf("."));
        outputDir = inputDir + "/" + fileNameNoEx + "_Output";
        File dir = new File(outputDir);
        dir.mkdir();
        findPosWords(br, terms, outputDir);



    }
}