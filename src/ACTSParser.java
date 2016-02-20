import java.io.*;

/**
 * Created by Artur Badretdinov on 20.02.2016.
 * artursletter@gmail.com
 */
public class ACTSParser {
    private final File inputFile;
    private final File outputFile;
    private final int HEADER_LENGTH = 7;
    private final int NUMBER_OF_PARAMETERS = 7;

    ACTSParser(String inputFile, String outputFile){
        this.outputFile = new File (outputFile);
        this.inputFile = new File(inputFile);
    }

    public void parse() throws IOException {
        // Read existing data from input file and convert to a new view
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        for(int skipLine = 0; skipLine < HEADER_LENGTH; skipLine++) {
            reader.readLine();
        }
        StringBuilder output = new StringBuilder();
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            output.append(parseOneLine(currentLine))
                    .append("\n");
        }
        reader.close();

        // Write to the output file
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(output.toString());
        writer.close();
    }

    private String parseOneLine(String line) {
        String[] params = line.split(",");
        StringBuilder newLine = new StringBuilder();
        newLine.append(params[NUMBER_OF_PARAMETERS-1])
                .append(", ");
        String parameter;
        if (params[NUMBER_OF_PARAMETERS-2]
                .toLowerCase()
                .equals("true")) {
            parameter = "Large";
        }
        else {
            parameter = "Small";
        }
        newLine.append(parameter)
                .append(", ");
        if (params[NUMBER_OF_PARAMETERS-3]
                .toLowerCase()
                .equals("true")) {
            parameter = "Yes";
        }
        else {
            parameter = "No";
        }
        newLine.append(parameter)
                .append(", ");

        for (int valueNum = 0; valueNum < 4; valueNum++) {
            if (!params[valueNum].equals("-1")) {
                newLine.append(params[valueNum])
                        .append(", ");
            }
        }
        newLine.delete(newLine.length()-2, newLine.length()-1);
        return newLine.toString();
    }
}
