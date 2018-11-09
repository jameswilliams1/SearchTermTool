import java.io.BufferedReader;
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
        double conversions;
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

    public static void readSearchFile(String filepath, ArrayList<SearchTerm> terms) throws IOException {
        FileReader fr = new FileReader(filepath);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        br.readLine();
        while ((line = br.readLine()) != null) {
            try {
                SearchTerm term = parseLine(line);
                terms.add(term);
            } catch (NoSuchElementException e) {
                System.out.println("Row could not be parsed: " + line);
            }
        }
        br.close();
    }


}
