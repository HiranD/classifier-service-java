package com.ClassifierService;

import com.ClassifierService.DL4JApproach.DL4JDocumentCategorizer;
import com.ClassifierService.OpenNLPApproach.OpenNLPDocumentCategorizer;
import com.google.gson.Gson;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.primitives.Pair;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import static org.deeplearning4j.models.embeddings.loader.WordVectorSerializer.readParagraphVectors;


@Path("/service")
public class ClassifierService {
    private static final Logger log = Logger.getLogger(ClassifierService.class.getName());

    private static boolean dl4jClassifier = false;
    private static boolean OpenNlpClassifier = false;
    private static String previous_classifier = "";
    private static String modelFile = null;
    private static OpenNLPDocumentCategorizer detector = null;
    private static DL4JDocumentCategorizer categorizer = null;
    private static String labelListFile = null;


    private void RunOnFirstRequest(String classifier){
        ClassLoader classLoader = getClass().getClassLoader();
        if(!previous_classifier.equals(classifier) || previous_classifier.equals("")){
            if(dl4jClassifier){
                modelFile = new File(classLoader.getResource("models/dl4j_para_vec.pv").getFile()).getAbsolutePath();
                labelListFile = new File(classLoader.getResource("labelsList.txt").getFile()).getAbsolutePath();
                try {
                    TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
                    tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
                    categorizer = new DL4JDocumentCategorizer(readParagraphVectors(modelFile), tokenizerFactory);
                    dl4jClassifier = false;
                }catch(Exception e){
                    log.severe("Exception: " + e);
                }
            }
            if(OpenNlpClassifier){
                modelFile = new File(classLoader.getResource("models/open_nlp_model.bin").getFile()).getAbsolutePath();
                detector = new OpenNLPDocumentCategorizer();
                detector.initModel(modelFile);
                OpenNlpClassifier = false;
            }
            previous_classifier = classifier;
        }
    }

    @POST
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        return Response.status(200).entity("health is Ok..").build();
    }

    @POST
    @Path("/dl4jClassifier")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response dl4jClassifier(String msg) throws FileNotFoundException {
        dl4jClassifier = true;
        RunOnFirstRequest("dl4jClassifier");
        msg = msg.toLowerCase();
        long tStart = System.currentTimeMillis();

        List<Pair<String, Double>> scores = categorizer.checkUnlabeledData(msg, labelListFile);
        Gson gson = new Gson();
        String json = gson.toJson(scores);

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        log.info(msg + " : " + json + " classified in " + tDelta + "ms");
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/openNlpClassifier")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response openNlpClassifier(String msg) throws FileNotFoundException {
        OpenNlpClassifier = true;
        RunOnFirstRequest("OpenNlpClassifier");
        msg = msg.toLowerCase();
        long tStart = System.currentTimeMillis();

        HashMap<String,Double> results = detector.getWeights(msg);
        Gson gson = new Gson();
        String json = gson.toJson(results);

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        log.info(msg + " : " + json + " classified in " + tDelta + "ms");
        return Response.status(200).entity(json).build();
    }

}
