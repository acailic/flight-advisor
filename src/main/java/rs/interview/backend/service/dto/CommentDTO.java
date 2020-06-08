package rs.interview.backend.service.dto;

import rs.interview.backend.domain.Comment;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class CommentDTO implements Serializable {

    private Long id;

    private Long author;

    @Size(min = 1, max = 1000)
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public CommentDTO() {

    }

    public CommentDTO(Long id, Long author, @Size(min = 1, max = 1000) String text) {
        this.id = id;
        this.author = author;
        this.text = text;
    }


    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getAuthor().getId();
        this.text = comment.getText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentDTO that = (CommentDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        return text != null ? text.equals(that.text) : that.text == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
