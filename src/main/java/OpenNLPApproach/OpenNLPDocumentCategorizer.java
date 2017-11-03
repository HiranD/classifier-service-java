package OpenNLPApproach;

import java.io.*;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import java.io.FileInputStream;
import java.io.InputStream;


public class OpenNLPDocumentCategorizer {
    public void trainModel(String inputFile, String modelFile) throws IOException {
        DoccatModel model = null;
        try {

            MarkableFileInputStreamFactory factory = new MarkableFileInputStreamFactory(new File(inputFile));
            ObjectStream<String> lineStream = new PlainTextByLineStream(factory, "UTF-8");

            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            TrainingParameters mlParams = new TrainingParameters();
            mlParams.put("Iterations", 200);
            mlParams.put("Cutoff", 2);
            model = DocumentCategorizerME.train("en", sampleStream, mlParams, new DoccatFactory());

            OutputStream modelOut = null;
            File modelFileTmp = new File(modelFile);
            modelOut = new BufferedOutputStream(new FileOutputStream(modelFileTmp));
            model.serialize(modelOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private InputStream inputStream;
    private DoccatModel docCatModel;
    private DocumentCategorizerME myCategorizer;

    public void initModel(String modelFile) {
        try {
            inputStream = new FileInputStream(modelFile);
            docCatModel = new DoccatModel(inputStream);
            myCategorizer = new DocumentCategorizerME(docCatModel);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public String getCategory(String text) {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);
        double[] outcomes = myCategorizer.categorize(tokens);
        String category = myCategorizer.getBestCategory(outcomes);
        return category;
    }
}
