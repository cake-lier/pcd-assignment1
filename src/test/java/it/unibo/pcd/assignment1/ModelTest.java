package it.unibo.pcd.assignment1;

import it.unibo.pcd.assignment1.model.Model;
import it.unibo.pcd.assignment1.model.ModelImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModelTest {
    private static final int WORDS_NUMBER = 5;
    private static final String PDFS_FOLDER_NAME = "pdfs";
    private static final String STOPWORDS_FILE_NAME = "stopwords.txt";

    private Map<String, Integer> frequencies;
    private int processedWords;

    @BeforeEach
    void setUp() {
        this.processedWords = 2581;
        this.frequencies = new LinkedHashMap<>();
        this.frequencies.put("dolor", 610);
        this.frequencies.put("ipsum", 325);
        this.frequencies.put("lorem", 315);
        this.frequencies.put("di", 200);
        this.frequencies.put("frase", 200);
    }

    @Test
    void testExampleCase() throws IOException, URISyntaxException {
        final Model model = new ModelImpl();
        model.startCalculation(Paths.get(ClassLoader.getSystemResource(PDFS_FOLDER_NAME).toURI()),
                               Paths.get(ClassLoader.getSystemResource(STOPWORDS_FILE_NAME).toURI()),
                               WORDS_NUMBER);
        model.awaitResult();
        Assertions.assertEquals(this.frequencies, model.getUpdate().getFrequencies());
        Assertions.assertEquals(this.processedWords, model.getUpdate().getProcessedWords());
    }
}
