import java.io.IOException;

/**
 * Created by Artur Badretdinov on 20.02.2016.
 * artursletter@gmail.com
 */
public class Main {

    public static void main(String[] args) {
        int argsNum = args.length;
        String inputFile = argsNum < 1 ? "MegaShapeClassifier-output-YES.csv": args[0];
        String outputFile = argsNum < 2 ? "output.txt": args[1];
        String jUnitFile = argsNum < 3 ? "ShapeClassifierTest.java": args[2];

        ACTSParser parser = new ACTSParser(outputFile,jUnitFile);
        try {
            parser.createJUnitTestFromDir("Classifier");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
