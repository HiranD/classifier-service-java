package DL4JApproach;

import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.deeplearning4j.models.embeddings.loader.WordVectorSerializer.writeParagraphVectors;

public class DL4JParaVecTrainer {
    ParagraphVectors paragraphVectors;
    LabelAwareIterator iterator;
    TokenizerFactory tokenizerFactory;

    final Logger log = LoggerFactory.getLogger(DL4JParaVecTrainer.class);

    public DL4JParaVecTrainer(LabelAwareIterator iterator, TokenizerFactory tokenizerFactory){
        this.iterator = iterator;
        this.tokenizerFactory = tokenizerFactory;
    }

    public void makeParagraphVectors(String modelFile) throws Exception {

        // ParagraphVectors training configuration
        paragraphVectors = new ParagraphVectors.Builder()
                .learningRate(0.025)
                .minLearningRate(0.001)
                .batchSize(1000)
                .epochs(12)
                .iterate(iterator)
                .trainWordVectors(true)
                .tokenizerFactory(tokenizerFactory)
                .build();


        // Start model training
        paragraphVectors.fit();

        writeParagraphVectors(paragraphVectors, modelFile);
        log.info("Model successfully saved on '" + modelFile + "' location");
    }
}
