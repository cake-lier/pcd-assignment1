package it.unibo.pcd.assignment1.model.worker;

import it.unibo.pcd.assignment1.model.Page;
import it.unibo.pcd.assignment1.model.WordCounterImpl;
import it.unibo.pcd.assignment1.model.concurrency.GeneratorPipe;
import it.unibo.pcd.assignment1.model.update.UpdateImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class WorkerImpl extends Thread implements Worker {
    public static final String CANNOT_EXTRACT_TEXT = "You do not have permission to extract text";

    private final GeneratorPipe<Path> documentQueue;
    private final FilterPipe<Page> pageQueue;
    private final Set<String> stopwords;
    private final SharedWorkerState state;
    private final WordCounterImpl counter;

    protected WorkerImpl(final GeneratorPipe<Path> documentQueue,
                         final FilterPipe<Page> pageQueue,
                         final Set<String> stopwords,
                         final SharedWorkerState state,
                         final WordCounterImpl counter) {
        this.documentQueue = Objects.requireNonNull(documentQueue);
        this.pageQueue = Objects.requireNonNull(pageQueue);
        this.stopwords = new HashSet<>(stopwords);
        this.state = Objects.requireNonNull(state);
        this.counter = Objects.requireNonNull(counter);
    }

    @Override
    public void run() {
        try {
            do {
                final Optional<Path> document = this.documentQueue.dequeue();
                if (!document.isPresent()) {
                    break;
                }
                final PDDocument pdfDocument = PDDocument.load(document.get().toFile());
                if (!pdfDocument.getCurrentAccessPermission().canExtractContent()) {
                    throw new IllegalStateException(CANNOT_EXTRACT_TEXT);
                }
                for (int page = 1; page <= pdfDocument.getNumberOfPages(); page++) {
                    this.state.checkForSuspension();
                    this.pageQueue.enqueue(new PageImpl(pdfDocument, page));
                }
            } while (true);
            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            do {
                final Optional<Page> optionalPage = this.pageQueue.dequeue();
                if (!optionalPage.isPresent()) {
                    break;
                }
                final Page page = optionalPage.get();
                stripper.setStartPage(page.getPageIndex());
                stripper.setEndPage(page.getPageIndex());
                final String[] words = Pattern.compile("\\W+").split(stripper.getText(page.getDocument()));
                this.counter.enqueue(new UpdateImpl(Arrays.stream(words)
                                                          .map(String::toLowerCase)
                                                          .filter(w -> !this.stopwords.contains(w))
                                                          .collect(Collectors.groupingBy(s -> s, Collectors.counting())),
                                     words.length));
            } while (true);
            //TODO: document.close()
        } catch (final IOException ex) {
            ex.printStackTrace();
            //TODO: what to do?
        }
    }
}
