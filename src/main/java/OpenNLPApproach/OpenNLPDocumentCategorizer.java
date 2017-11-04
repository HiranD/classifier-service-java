package OpenNLPApproach;

import java.io.*;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;


public class OpenNLPDocumentCategorizer {
    public void trainModel(ObjectStream<String> lineStream, String modelFile) throws IOException {
        DoccatModel model = null;
        try {
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

    public void trainModelfromMarkedFile(String inputFile, String modelFile) throws IOException {

        MarkableFileInputStreamFactory factory = new MarkableFileInputStreamFactory(new File(inputFile));
        ObjectStream<String> lineStream = new PlainTextByLineStream(factory, "UTF-8");
        this.trainModel(lineStream, modelFile);
    }

    public void trainModelfromDocuments(String contentDir, String modelFile) throws IOException {

        InputStream trainingData = ClassifierDataUtil.readClassifierSourceData(contentDir);
        InputStreamFactory inputStreamFactory = new DocumentFileInputStreamFactory(trainingData);
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");
        this.trainModel(lineStream, modelFile);
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

    public HashMap<String,Double> getWeights(String text) {
        HashMap<String,Double> results = new HashMap<String,Double>();
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);
        double[] outcomes = myCategorizer.categorize(tokens);
        for(int i = 0; i < outcomes.length; i++){
            results.put(myCategorizer.getCategory(i), outcomes[i]);
        }
        return results;
    }
}
