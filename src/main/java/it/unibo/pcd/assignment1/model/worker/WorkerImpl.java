package it.unibo.pcd.assignment1.model.worker;

import it.unibo.pcd.assignment1.model.WordCounter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

class WorkerImpl extends Thread implements Worker {
    public static final String CANNOT_EXTRACT_TEXT = "You do not have permission to extract text";

    private final List<Path> filesToProcess;
    private final List<String> stopwords;
    private final WordCounter counter;

    protected WorkerImpl(final List<Path> filesToProcess, final List<String> stopwords, final WordCounter counter) {
        this.filesToProcess = new ArrayList<>(filesToProcess);
        this.stopwords = new ArrayList<>(stopwords);
        this.counter = Objects.requireNonNull(counter);
    }

    @Override
    public void run() {
        try {
            for (final Path path : this.filesToProcess) {
                final PDDocument document = PDDocument.load(path.toFile());
                if (!document.getCurrentAccessPermission().canExtractContent()) {
                    throw new IllegalStateException(CANNOT_EXTRACT_TEXT);
                }
                final PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(true);
                for (int page = 1; page <= document.getNumberOfPages(); page++) {
                    stripper.setStartPage(page);
                    stripper.setEndPage(page);
                    Pattern.compile("\\W+")
                           .splitAsStream(stripper.getText(document))
                           .forEach(w -> {
                               final String lowercaseWord = w.toLowerCase();
                               this.counter.countWord(lowercaseWord, !this.stopwords.contains(lowercaseWord));
                           });
                }
                document.close();
            }
        } catch (final IOException ex) {
            ex.printStackTrace();
            //TODO: what to do?
        }
    }

    @Override
    public boolean hasCompleted() {
        //TODO: better...
        return !this.isAlive();
    }

    @Override
    public void await() {
        try {
            this.join();
        } catch (final InterruptedException ex) {
            ex.printStackTrace();
            //TODO: what to do?
        }
    }
}
