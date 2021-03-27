package it.unibo.pcd.assignment1.model.entities;
import java.util.Objects;

public class PageImpl implements Page {
    private final String text;

    public PageImpl(final String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
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
        return this.text.equals(page.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text);
    }

    @Override
    public String toString() {
        return "PageImpl{text=" + this.text + '}';
    }
}
