import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {


    public static void printHelp() {
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("INFORMATION:");
        System.out.println();
        System.out.println("This program processes search term data into useful outputs. The search data should be a CSV file with one");
        System.out.println("search term on each line and exactly one heading/title line in row 1. The correct format for the search");
        System.out.println("data is:");
        System.out.println();
        System.out.println("[Search Term,Clicks,Conversions]");
        System.out.println();
        System.out.println("Alternatively you can use 1 search term on each line with exactly one heading line in row 1 in the format:");
        System.out.println();
        System.out.println("[Search term]");
        System.out.println();
        System.out.println("This will produce output without clicks or conversions. All output is saved next to the input file in a");
        System.out.println("separate folder.");
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("SUPPORTED COMMANDS:");
        System.out.println();
        System.out.println("help :     Display help (can be used at any time)");
        System.out.println();
        System.out.println("keywords : Input a list of keywords to find search terms that contain them. The keywords file should be a");
        System.out.println("           CSV with each keyword in one column on the first row in the following format:");
        System.out.println();
        System.out.println("           [Keyword,Keyword,Keyword]...");
        System.out.println();
        System.out.println("split :    Split each search term from the input data into multiple terms (word length of each term is set");
        System.out.println("           by typing a number after choosing this command). Appearances, clicks and conversions are then ");
        System.out.println("           aggregated for each term and saved to a CSV in the output folder.");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println();
    }


    public static boolean isHelp(String line) { // Finds if user asks for help
        if ( line == null){
            return false;
        }
        boolean result = (line.equalsIgnoreCase("help")) ? true : false;
        return result;
    }

    public static void findPosWords(BufferedReader br, ArrayList<SearchTerm> terms, String outputDir) {
        while (true) {
            try {
                ArrayList<String> posWords;
                System.out.println("Enter filepath to keywords list or type \"help\":");
                String filepath = br.readLine();
                if (isHelp(filepath)) {
                    printHelp();
                    break;
                }
                posWords = Utility.readPosWords(filepath);
                System.out.println("Number of keywords entered: " + posWords.size());
                Utility.findPosSearches(posWords, terms, outputDir);
                break;
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("Selected words file could not be processed, check file path and data format then try again.");
                System.out.println();
            }
        }
    }

    public static void splitWords(ArrayList<SearchTerm> terms, String outputDir, int xValue) {
        ArrayList<SplitTerm> splitTerms = SplitTerm.splitTermList(terms, xValue);
        SplitTerm.writeFile(splitTerms, outputDir, xValue);
    }

    public static void getCommand(BufferedReader br, ArrayList<SearchTerm> terms, String outputDir) {
        outerloop:
        while (true) {
            System.out.println("Enter a command or type \"help\":");
            try {
                String command = br.readLine().toLowerCase();
                switch (command) {
                    case "help":
                        printHelp();
                        break;
                    case "keywords":
                        findPosWords(br, terms, outputDir);
                        break;
                    case "split":
                        while (true) {
                            System.out.println("Enter number of words to split each search term into or type \"help\":");
                            String levelString = br.readLine();
                            if (isHelp(levelString)) {
                                printHelp();
                                break;
                            }
                            int level = 0;
                            try {
                                level = Integer.parseInt(levelString);
                                if (level < 1) {
                                    throw new NumberFormatException();
                                }
                                splitWords(terms, outputDir, level);
                                break outerloop;
                            } catch (NumberFormatException e) {
                                System.out.println("You must enter a valid integer greater than 0.");
                                System.out.println();
                            }

                        }
                    default:
                        System.out.println(command + " is not a valid command. Type \"help\" for a list of commands.");
                        System.out.println();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static String setInput(BufferedReader br, ArrayList<SearchTerm> terms) {
        String outputDir = "";
        while (true) { // Set input data location, process and create output dir
            try {
                System.out.println("Enter filepath to search data CSV or type \"help\":");
                String filepath = br.readLine();
                if (isHelp(filepath)) {
                    printHelp();
                    break;
                }
                SearchTerm.readSearchFile(filepath, terms);
                System.out.println("Number of search terms input: " + terms.size());
                Path p = Paths.get(filepath);
                String inputDir = p.getParent().toString();
                String fileName = p.getFileName().toString();
                String fileNameNoEx = fileName.substring(0, fileName.indexOf("."));
                outputDir = inputDir + "/" + fileNameNoEx + "_Output";
                File dir = new File(outputDir);
                dir.mkdir();
                break;
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("Search data could not be processed, check file path and data format then try again.");
                System.out.println();
            }
        }
        return outputDir;
    }

    public static void main(String[] args) {

        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("  /$$$$$$  /$$$$$$$$  /$$$$$$        /$$$$$$$$                  /$$          ");
        System.out.println(" /$$__  $$| $$_____/ /$$__  $$      |__  $$__/                 | $$          ");
        System.out.println("| $$  \\__/| $$      | $$  \\ $$         | $$  /$$$$$$   /$$$$$$ | $$  /$$$$$$$");
        System.out.println("|  $$$$$$ | $$$$$   | $$  | $$         | $$ /$$__  $$ /$$__  $$| $$ /$$_____/");
        System.out.println("\\____   $$| $$__/   | $$  | $$         | $$| $$  \\ $$| $$  \\ $$| $$|  $$$$$$ ");
        System.out.println("/$$  \\  $$| $$      | $$  | $$         | $$| $$  | $$| $$  | $$| $$ \\____  $$");
        System.out.println("|  $$$$$$/| $$$$$$$$|  $$$$$$/         | $$|  $$$$$$/|  $$$$$$/| $$ /$$$$$$$/");
        System.out.println(" \\______/ |________/ \\______/          |__/ \\______/  \\______/ |__/|_______/    ");
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println();

        String outputDir = "";
        ArrayList<SearchTerm> terms = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String output = setInput(br, terms);
            if (output.length() != 0) {
                outputDir = output;
                break;
            }
        }

        while (true) {
            getCommand(br, terms, outputDir);
        }


        //findPosWords(br, terms, outputDir);
        //splitWords(terms, outputDir, 1);


    }
}
