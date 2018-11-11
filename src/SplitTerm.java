import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SplitTerm implements Comparable<SplitTerm> {

    //<editor-fold desc="base">
    private final String term;
    private int count;
    private int clicks;
    private double conversions;

    public String getTerm() {
        return term;
    }

    public int getCount() {
        return count;
    }

    public int getClicks() {
        return clicks;
    }

    public double getConversions() {
        return conversions;
    }

    @Override
    public String toString() {
        return "SplitTerm{" +
                "term='" + term + '\'' +
                ", count=" + count +
                ", clicks=" + clicks +
                ", conversions=" + conversions +
                '}';
    }

    @Override
    public int compareTo(SplitTerm o) {
        return Double.compare(this.getClicks(), o.getClicks());
    }

    public SplitTerm(String term) {
        this.term = term;
        this.count = 0;
        this.clicks = 0;
        this.conversions = 0;
    }
    //</editor-fold>

    /**
     * Takes ArrayList of Search terms and int Xvalue, returns ArrayList of SplitTerm objects (sorted by highest clicks)
     * where each SplitTerm has xValue words
     *
     * @param searchTerms
     * @param xValue
     * @return output
     */
    public static ArrayList<SplitTerm> splitTermList(ArrayList<SearchTerm> searchTerms, int xValue) {
        ArrayList<SplitTerm> output = new ArrayList<>();
        Set<String> noDups = new HashSet<String>();
        for (SearchTerm st : searchTerms) {
            String searchTerm = st.getTerm();
            Scanner s = new Scanner(searchTerm);
            ArrayList<String> words = new ArrayList<>(); // List of separate words from search term
            while (s.hasNext()) {
                words.add(s.next());
            }
            s.close();
            ArrayList<ArrayList<String>> splitTerms = splitList(words, xValue); // List of lists of words each of size = xValue (last item possibly shorter)
            if (splitTerms.get(splitTerms.size() - 1).size() < xValue) { // Checks if last item is shorter than xValue and removes
                splitTerms.remove(splitTerms.size() - 1);
            }
            for (ArrayList<String> listSizeX : splitTerms) {
                String xLenTerm = listToString(listSizeX);
                noDups.add(xLenTerm);
            }
        }
        for (String xLenTerm : noDups) {
            output.add(new SplitTerm(xLenTerm));
        }
        for (SplitTerm spTerm : output) {
            for (SearchTerm st : searchTerms) {
                if (st.getTerm().contains(spTerm.getTerm())) { // If search term has split term inside it
                    spTerm.count += 1;
                    spTerm.clicks += st.getClicks();
                    spTerm.conversions += st.getConversions();
                }
            }
        }
        boolean dataSet = (output.get(0).getClicks() > 0) ? true:false; // Checks if data negative (= not set)
        if (dataSet) {
            Collections.sort(output, Collections.reverseOrder());
        }
        return output;
    }

    // Splits an ArrayList into sub lists of length L
    private static <E> ArrayList<ArrayList<E>> splitList(ArrayList<E> list, final int L) {
        ArrayList<ArrayList<E>> parts = new ArrayList<>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<E>(list.subList(i, Math.min(N, i + L))));
        }
        return parts;
    }

    // Returns a string of each item in ArrayList (of strings) with space between each
    private static String listToString(ArrayList<String> al) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < al.size(); i++) {
            sb.append(al.get(i));
            if (i < al.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static void writeFile(ArrayList<SplitTerm> terms, String outputDir, int xValue) {
        boolean dataSet = true; //Ensures negative (unset) data doesn't get printed
        if (terms.get(0).getClicks() < 0) {
            dataSet = false;
        }
        BufferedWriter bwZero = null;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputDir + "/Search_Term_Data_" + Integer.toString(xValue) + "_Words.csv"))) {

            if (dataSet) {
                bwZero = new BufferedWriter(new FileWriter(outputDir + "/Search_Term_Data_" + Integer.toString(xValue) + "_Words_0_Conversions.csv"));
                bw.write("Word,Appearances,Clicks,Conversion");
                bwZero.write("Word,Appearances,Clicks,Conversion");
                bwZero.newLine();
            } else {
                bw.write("Word,Appearances");
            }
            bw.newLine();
            for (SplitTerm st : terms) {
                if (dataSet) {
                    String conversions = Double.toString(st.getConversions()).replaceAll("\\.", ",");
                    bw.write(st.getTerm() + "," + st.getCount() + "," + st.getClicks() + "," + "\"" + conversions + "\"");
                    if (st.getConversions() == 0) {
                        bwZero.write(st.getTerm() + "," + st.getCount() + "," + st.getClicks() + "," + "\"" + conversions + "\"");
                        bwZero.newLine();
                    }
                } else {
                    bw.write(st.getTerm() + "," + st.getCount());
                }
                bw.newLine();
            }
            System.out.println("Full output saved to: " + outputDir + "/Search_Term_Data_" + Integer.toString(xValue) + "_Words.csv");
            if (dataSet) {
                System.out.println("Filtered output saved to: " + outputDir + "/Search_Term_Data_" + Integer.toString(xValue) + "_Words_0_Conversions.csv");
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                bwZero.close();
            }
            catch (IOException | NullPointerException e){

            }
        }
    }
}
