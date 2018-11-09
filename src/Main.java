import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {


        ArrayList<String> words;
        boolean fileSet = false;
        while (!fileSet) {
            System.out.println("Enter filepath for selected words list (one per column):");
            try {
                BufferedReader filepathReader = new BufferedReader(new InputStreamReader(System.in));
                String filepath = filepathReader.readLine();
                BufferedReader lineReader = new BufferedReader(new FileReader(filepath));
                String line = lineReader.readLine();
                words = Utility.parseChosenWords(line);
                fileSet = true;
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("There was a problem setting the word list. Check the filepath and word list format then try again.");
            }
        }


    }
}
