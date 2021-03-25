package it.unibo.pcd.assignment1.sequential;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Controller {
    private static final String CANNOT_EXTRACT_TEXT = "You do not have permission to extract text";

    private final View view;

    public Controller(final View view) {
        this.view = Objects.requireNonNull(view);
    }

    public void launch(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) {
        final Map<String, Long> frequencies = new HashMap<>();
        int processedWords = 0;
        try {
            final Set<String> stopwords = new HashSet<>(Files.readAllLines(stopwordsFile));
            final Set<Path> pdfsPaths = Files.list(filesDirectory)
                                             .filter(p -> p.toString().matches(".*pdf$"))
                                             .collect(Collectors.toSet());
            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            for (final Path pdfPath : pdfsPaths) {
                final PDDocument pdfDocument = PDDocument.load(pdfPath.toFile());
                if (!pdfDocument.getCurrentAccessPermission().canExtractContent()) {
                    throw new IllegalStateException(CANNOT_EXTRACT_TEXT);
                }
                for (int page = 1; page <= pdfDocument.getNumberOfPages(); page++) {
                    stripper.setStartPage(page);
                    stripper.setEndPage(page);
                    final String[] words = Pattern.compile("\\W+").split(stripper.getText(pdfDocument));
                    Arrays.stream(words)
                          .map(String::toLowerCase)
                          .filter(w -> !stopwords.contains(w))
                          .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                          .forEach((k, v) -> frequencies.merge(k, v, Long::sum));
                    processedWords += words.length;
                    this.view.displayProgress();
                }
                pdfDocument.close();
            }
            this.view.displayCompletion(
                frequencies.entrySet()
                           .parallelStream()
                           .sorted(Collections.<Map.Entry<String, Long>>reverseOrder(Map.Entry.comparingByValue())
                                              .thenComparing(Map.Entry.comparingByKey()))
                           .limit(wordsNumber)
                           .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum, LinkedHashMap::new)),
                processedWords);
        } catch (final IOException ex) {
            this.view.displayError(ex.getMessage());
        }
    }

    public void exit() {
        System.exit(0);
    }
}
