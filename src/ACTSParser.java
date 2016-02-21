import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Artur Badretdinov on 20.02.2016.
 * artursletter@gmail.com
 */
public class ACTSParser {
    private final File outputFile;
    private final File JUnitFile;
    private final int NUMBER_OF_PARAMETERS = 7;
    private String formattedACTS = null;

    public static List<String> fileList(String directory) {
        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path path : directoryStream) {
                fileNames.add(path.toString());
            }
        } catch (IOException ex) {}
        return fileNames;
    }

    ACTSParser(String outputFile, String jUnitFile){
        this.outputFile = new File (outputFile);
        this.JUnitFile = new File(jUnitFile);
    }

    private void writeToFile(String output, File outputFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(output);
        writer.close();
    }

    private String readAndConvertData(String fileName) throws IOException {

        // Read existing data from input file and convert to a new view
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        StringBuilder output = new StringBuilder();
        String currentLine;

        while ((currentLine = reader.readLine()) != null) {
            if (currentLine.contains("Value1")) {
                break;
            }
        }
        while ((currentLine = reader.readLine()) != null) {
            output.append(parseOneLine(currentLine))
                    .append("\n");
        }
        reader.close();
        formattedACTS = output.toString();
        return formattedACTS;
    }

    public void createJUnitTestFromDir(String directory) throws IOException {
        List<String> files = fileList(directory);
        createJUnitTestFile(files);
    }

    public void createJUnitTestFile(List<String> fileNames) throws IOException {
        StringBuilder jUnitText = new StringBuilder();
        jUnitText.append(header);

        int testCaseNum = 0;
        for (String fileName : fileNames) {
            jUnitText.append("\n\n" + "// File: ")
                    .append(fileName)
                    .append("\n\n");
            readAndConvertData(fileName);
            BufferedReader reader = new BufferedReader(new StringReader(formattedACTS));
            String line;
            boolean good = fileName.contains("Bad") ? false:true;

            while ((line = reader.readLine()) != null) {
                jUnitText.append("    @Test\n" + "    public void")
                        //.append("testcase" + testCaseNum++)
                        .append(fileName.replace(",","COMMA"))
                        .append("() throws Exception {\n")
                        .append("    ShapeClassifier classifier = new ShapeClassifier();\n")
                        .append("    assertEquals(\"")
                        .append(good ? "Yes" : "No")
                        .append("\", classifier.evaluateGuess(\"")
                        .append(line)
                        .append("\"));\n")
                        .append("}\n\n");
            }

        }
        jUnitText.append(finish);
        writeToFile(jUnitText.toString(), JUnitFile);
    }

    private String parseOneLine(String line) {
        String[] params = line.split(",");
        StringBuilder newLine = new StringBuilder();
        newLine.append(params[NUMBER_OF_PARAMETERS-1])
                .append(",");
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
                .append(",");
        if (params[NUMBER_OF_PARAMETERS-3]
                .toLowerCase()
                .equals("true")) {
            parameter = "Yes";
        }
        else {
            parameter = "No";
        }
        newLine.append(parameter)
                .append(",");

        for (int valueNum = 0; valueNum < 4; valueNum++) {
            if (!params[valueNum].equals("-1")) {
                newLine.append(params[valueNum])
                        .append(",");
            }
        }
        newLine.deleteCharAt(newLine.length()-1);
        return newLine.toString();
    }

    private String header = "package analysis;\n" +
            "\n" +
            "import junit.framework.TestCase;\n" +
            "import org.junit.Test;\n" +
            "\n" +
            "public class ShapeClassifierTest extends TestCase {\n "
            ;

    private String finish = "}\n";
}
