import java.io.*;
        import java.util.ArrayList;
        import java.util.Scanner;

public class Utility {


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

    public static ArrayList<String> readPosWords(String filepath) throws IOException {
        FileReader fr = new FileReader(filepath);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        br.close();
        ArrayList<String> words = parseChosenWords(line);
        return words;

    }

    public static void findPosSearches(ArrayList<String> posWords, ArrayList<SearchTerm> terms, String outputDir) {
        ArrayList<ArrayList<String>> columns = new ArrayList<>();
        ArrayList<String> rows = new ArrayList<>();
        for (String word : posWords) {
            ArrayList<String> column = new ArrayList<>();
            column.add(word);
            for (SearchTerm st : terms) {
                String term = st.getTerm();
                if (term.contains(word)) {
                    column.add(term);

                }
            }
            columns.add(column);
        }
        int maxLength = Integer.MIN_VALUE;
        for (ArrayList<String> al : columns) {
            if (al.size() > maxLength) {
                maxLength = al.size();
            }
        }
        for (int i = 0; i < maxLength; i++) {
            StringBuilder sb = new StringBuilder();
            for (ArrayList<String> col : columns) {
                if (col.size() > i) {
                    sb.append(col.get(i));
                    sb.append(",");
                } else {
                    sb.append(",");
                }
            }
            rows.add(sb.toString());
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputDir + "/Search_Term_Appearances.csv"))) {
            for (String line : rows) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Output saved to: " + outputDir + "/output.csv");
    }
}