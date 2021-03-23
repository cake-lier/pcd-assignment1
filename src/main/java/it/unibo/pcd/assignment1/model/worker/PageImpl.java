package it.unibo.pcd.assignment1.model.worker;

import it.unibo.pcd.assignment1.model.Page;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.Objects;

public class PageImpl implements Page {
    private final PDDocument document;
    private final int pageIndex;

    public PageImpl(final PDDocument document, final int pageIndex) {
        this.document = Objects.requireNonNull(document);
        this.pageIndex = pageIndex;
    }

    @Override
    public PDDocument getDocument() {
        return this.document;
    }

    @Override
    public int getPageIndex() {
        return this.pageIndex;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PageImpl page = (PageImpl) o;
        return this.pageIndex == page.pageIndex && this.document.equals(page.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.document, this.pageIndex);
    }

    @Override
    public String toString() {
        return "PageImpl{document=" + this.document + ", pageIndex=" + this.pageIndex + '}';
    }
}
