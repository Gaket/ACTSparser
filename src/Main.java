import java.io.IOException;

/**
 * Created by Artur Badretdinov on 20.02.2016.
 * artursletter@gmail.com
 */
public class Main {

    public static void main(String[] args) {
        int argsNum = args.length;
        String inputFile = argsNum < 1 ? "MegaShapeClassifier-output.csv": args[0];
        String outputFile = argsNum < 2 ? "output.txt": args[1];

        ACTSParser parser = new ACTSParser(inputFile, outputFile);
        try {
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
