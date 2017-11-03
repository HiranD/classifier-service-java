package DL4JApproach.tools;

import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MeansBuilder {
    private VocabCache<VocabWord> vocabCache;
    private InMemoryLookupTable<VocabWord> lookupTable;
    private TokenizerFactory tokenizerFactory;

    public MeansBuilder(InMemoryLookupTable<VocabWord> lookupTable, TokenizerFactory tokenizerFactory) {
        this.lookupTable = lookupTable;
        this.vocabCache = lookupTable.getVocab();
        this.tokenizerFactory = tokenizerFactory;
    }

    public INDArray documentAsVector(LabelledDocument document) {
        List<String> documentAsTokens = tokenizerFactory.create(document.getContent()).getTokens();
        AtomicInteger cnt = new AtomicInteger(0);
        for (String word: documentAsTokens) {
            if (vocabCache.containsWord(word)) cnt.incrementAndGet();
        }
        INDArray allWords = Nd4j.create(cnt.get(), lookupTable.layerSize());

        cnt.set(0);
        for (String word: documentAsTokens) {
            if (vocabCache.containsWord(word))
                allWords.putRow(cnt.getAndIncrement(), lookupTable.vector(word));
        }

        INDArray mean = allWords.mean(0);

        return mean;
    }

    public INDArray stringAsVector(String str) {
        List<String> tokens = tokenizerFactory.create(str).getTokens();

        if (tokens.size() > 1) {
            AtomicInteger cnt = new AtomicInteger(0);
            for (String word : tokens) {
                if (vocabCache.containsWord(word)) cnt.incrementAndGet();
            }
            INDArray allWords = Nd4j.create(cnt.get(), lookupTable.layerSize());

            cnt.set(0);
            for (String word : tokens) {
                if (vocabCache.containsWord(word))
                    allWords.putRow(cnt.getAndIncrement(), lookupTable.vector(word));
            }

            INDArray mean = allWords.mean(0);
//	        System.out.println(mean);
            return mean;
        } else {
            AtomicInteger cnt = new AtomicInteger(1);

            INDArray allWords = Nd4j.create(cnt.incrementAndGet(), lookupTable.layerSize());

            cnt.set(0);
            allWords.putRow(cnt.getAndIncrement(), lookupTable.vector(str));

            INDArray mean = allWords.mean(0);
//	        System.out.println(mean);
            return mean;
        }
    }
}
