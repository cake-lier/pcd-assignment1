package it.unibo.pcd.assignment1.wrapper;

import org.apache.pdfbox.pdmodel.PDDocument;
import java.util.Objects;

public class PageImpl extends PDFPartImpl implements Page {
    private final int pageIndex;
    private final PDDocument entireDocument;

    public PageImpl(final PDDocument page,final PDDocument entireDocument, final int pageIndex) {
        super(page);
        this.entireDocument = entireDocument;
        this.pageIndex = pageIndex;
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
        return this.pageIndex == page.pageIndex && this.getData().equals(page.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getData(), this.pageIndex);
    }

    @Override
    public String toString() {
        return "PageImpl{document=" + this.getData() + ", pageIndex=" + this.pageIndex + '}';
    }
}
