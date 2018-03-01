import com.ClassifierService.DL4JApproach.DL4JDocumentCategorizer;
import com.ClassifierService.DL4JApproach.DL4JParaVecTrainer;
import com.google.gson.Gson;
import org.apache.log4j.PropertyConfigurator;
import org.deeplearning4j.text.documentiterator.FileLabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.primitives.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.deeplearning4j.models.embeddings.loader.WordVectorSerializer.readParagraphVectors;

public class DL4JTestClass {

    private static Logger log = LoggerFactory.getLogger(DL4JTestClass.class);
    static String modelFile = null;
    static String dataDirectory = "src/test/resources/data/labeled";
    static String testDataDir = "src/test/resources/data/unlabeled";

    public static void main(String[] args) {
    try {
        String log4jConfPath = new File("src/main/resources/log4j.properties").getAbsolutePath();
        PropertyConfigurator.configure(log4jConfPath);

        modelFile = new File("src/main/resources/models/dl4j_para_vec.pv").getAbsolutePath();

        File resource = new File(dataDirectory);
        LabelAwareIterator iterator = new FileLabelAwareIterator.Builder().addSourceFolder(resource).build();
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        DL4JParaVecTrainer trainer = new DL4JParaVecTrainer(iterator, tokenizerFactory);
        trainer.makeParagraphVectors(modelFile);

        DL4JDocumentCategorizer categorizer = new DL4JDocumentCategorizer(readParagraphVectors(modelFile), tokenizerFactory);

////       to test accuracy load unlabeled docs...
//        File unClassifiedResource = new File(testDataDir);
//        categorizer.checkUnlabeledData(unClassifiedResource);

//      or use ur own strings...
        ArrayList<String> testData = new ArrayList<String>();
        testData.add("The Russian Trading System (RTS) was a stock market established in 1995 in Moscow, consolidating various regional trading floors into one exchange.\n" +
                "Originally RTS was modeled on NASDAQ's trading and settlement software; in 1998 the exchange went on line with its own in-house system.\n" +
                "Initially created as a non-profit organisation, it was transformed into a joint-stock company.\n" +
                "In 2011 MICEX merged with RTS creating Moscow Exchange.\n" +
                "The RTS Index, RTSI, the official Moscow Exchange indicator, was first calculated on September 1, 1995.\n" +
                "The RTS is a capitalization-weighted composite index calculated based on prices of the 50 most liquid Russian stocks listed on Moscow Exchange.\n" +
                "The Index is calculated in real time and denominated in US dollars.");
        testData.add("Human immunodeficiency virus infection and acquired immune deficiency syndrome (HIV/AIDS) is a spectrum of conditions caused by infection with the human immunodeficiency virus (HIV).\n" +
                "It may also be referred to as HIV disease or HIV infection.\n" +
                "Following initial infection, a person may experience a brief period of influenza-like illness.\n" +
                "This is typically followed by a prolonged period without symptoms.\n" +
                "As the infection progresses, it interferes more and more with the immune system, making the person much more susceptible to common infections, like tuberculosis, as well as opportunistic infections and tumors that do not usually affect people who have working immune systems.\n" +
                "The late symptoms of the infection are referred to as AIDS.\n" +
                "This stage is often complicated by an infection of the lung known as pneumocystis pneumonia, severe weight loss, skin lesions caused by Kaposi's sarcoma, or other AIDS-defining conditions.\n" +
                "HIV is transmitted primarily via unprotected sexual intercourse (including anal and oral sex), contaminated blood transfusions, hypodermic needles, and from mother to child during pregnancy, delivery, or breastfeeding.\n" +
                "Some bodily fluids, such as saliva and tears, do not transmit HIV.\n" +
                "Common methods of HIV/AIDS prevention include encouraging and practicing safe sex, needle-exchange programs, and treating those who are infected.\n" +
                "There is no cure or vaccine; however, antiretroviral treatment can slow the course of the disease and may lead to a near-normal life expectancy.\n" +
                "While antiretroviral treatment reduces the risk of death and complications from the disease, these medications are expensive and have side effects.\n" +
                "Treatment is recommended as soon as the diagnosis is made.\n" +
                "Without treatment, the average survival time after infection with HIV is estimated to be 9 to 11 years, depending on the HIV subtype.");

        String labelListFile = new File("src/main/resources/labelsList.txt").getAbsolutePath();
        for (String line : testData) {
            List<Pair<String, Double>> scores = categorizer.checkUnlabeledData(line, labelListFile);

            log.info("Content falls into the following categories: ");
            for (Pair<String, Double> score: scores) {
                log.info("        " + score.getFirst() + ": " + score.getSecond());
            }
        }


        } catch (Exception e){
            System.out.println(e.toString());
            log.error(e.toString());
        }
    }
}
