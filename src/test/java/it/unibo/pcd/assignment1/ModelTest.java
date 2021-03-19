package it.unibo.pcd.assignment1;

import it.unibo.pcd.assignment1.model.Model;
import it.unibo.pcd.assignment1.model.ModelImpl;
import it.unibo.pcd.assignment1.model.update.Update;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ModelTest {
    private static final int WORDS_NUMBER = 5;
    private static final String PDFS_FOLDER_NAME = "pdfs";
    private static final String STOPWORDS_FILE_NAME = "stopwords.txt";

    private Map<String, Long> frequencies;
    private int processedWords;

    @BeforeEach
    void setUp() {
        this.processedWords = 2581;
        this.frequencies = new LinkedHashMap<>();
        this.frequencies.put("dolor", 610L);
        this.frequencies.put("ipsum", 325L);
        this.frequencies.put("lorem", 315L);
        this.frequencies.put("di", 200L);
        this.frequencies.put("frase", 200L);
    }

    @Test
    void testExampleCase() throws IOException, URISyntaxException {
        final Model model = new ModelImpl();
        model.startCalculation(Paths.get(ClassLoader.getSystemResource(PDFS_FOLDER_NAME).toURI()),
                               Paths.get(ClassLoader.getSystemResource(STOPWORDS_FILE_NAME).toURI()),
                               WORDS_NUMBER);
        Update lastUpdate = null;
        do {
            final Optional<Update> update = model.getLatestUpdate();
            if (!update.isPresent()) {
                break;
            }
            lastUpdate = update.get();
        } while (true);
        Assertions.assertNotNull(lastUpdate);
        Assertions.assertEquals(this.frequencies, lastUpdate.getFrequencies());
        Assertions.assertEquals(this.processedWords, lastUpdate.getProcessedWords());
    }
}
