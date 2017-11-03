import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import OpenNLPApproach.OpenNLPDocumentCategorizer;

public class OpenNLPTestClass {
    public static void main(String args[]) throws IOException {

        String modelFile = new File("src/test/resources/models/open_nlp_model.bin").getAbsolutePath();
        String inputFile = new File("src/test/resources/data/tweets.txt").getAbsolutePath();

        OpenNLPDocumentCategorizer detector = new OpenNLPDocumentCategorizer();

        detector.trainModel(inputFile, modelFile);

        detector.initModel(modelFile);


        ArrayList<String> testData = new ArrayList<String>();
        String testFile = new File("src/test/resources/data/testdata.txt").getAbsolutePath();
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                testData.add(line);
            }
        }
        System.out.println("-------------------");
        for (String line : testData){
            System.out.println(detector.getCategory(line) + "\t" + line);
        }
    }
}
