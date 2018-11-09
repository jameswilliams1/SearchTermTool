import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SearchTerm {

    //<editor-fold desc="basics">
    private final String term;
    private final int clicks;
    private final double conversions;


    public SearchTerm(String term, int clicks, double conversions) {
        this.term = term;
        this.clicks = clicks;
        this.conversions = conversions;
    }

    public SearchTerm(String term) {
        this.term = term;
        this.clicks = -1;
        this.conversions = -1;
    }

    public String getTerm() {
        return term;
    }

    public double getConversions() {
        return conversions;
    }

    public int getClicks() {
        return clicks;
    }

    @Override
    public String toString() {
        return "SearchTerm{" +
                "term='" + term + '\'' +
                ", clicks=" + clicks +
                ", conversions=" + conversions +
                '}';
    }

    //</editor-fold>

    public static SearchTerm parseLine(String line) throws NoSuchElementException {
        String term;
        double conversions = 0.0;
        int clicks;
        Scanner s = new Scanner(line);
        s.useDelimiter(",");
        term = s.next();
        if (s.hasNext()) {
            clicks = s.nextInt();
            String num = s.findInLine("\"([^\"]*)\"");
            num = num.replaceAll("\"", "");
            num = num.replaceAll(",", ".");
            System.out.println(num);
            conversions = Double.parseDouble(num);
            return new SearchTerm(term, clicks, conversions);
        } else {
            return new SearchTerm(term);
        }
    }

    public static void readSearchFile(String filepath, ArrayList<SearchTerm> terms) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String line = "";
        try {
            br.readLine();
        } catch (IOException e) {
            System.out.println(e);
        }
        try {
            while ((line = br.readLine()) != null) {
                try {
                    SearchTerm term = parseLine(line);
                    terms.add(term);
                } catch (NoSuchElementException e) {
                    System.out.println(e);
                    System.out.println("Row could not be parsed: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Enter filepath for search data (one term per line with one heading row:");
        String filepath = Utility.getUserString();
        ArrayList<SearchTerm> terms = new ArrayList<>();
        try {
            readSearchFile(filepath, terms);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        System.out.println(terms);
        System.out.println(terms.size());


    }
}
