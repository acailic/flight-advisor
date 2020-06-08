package rs.interview.backend.service.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityDTO implements Serializable {

    private Long id;
    @Size(min = 1, max = 50)
    private String name;
    @Size(min = 1, max = 50)
    private String country;
    @Size(min = 1, max = 1000)
    private String description;


    private List<CommentDTO> comments= new ArrayList<>();

    public CityDTO(Long id, @Size(min = 1, max = 50) String name, @Size(min = 1, max = 50) String country, @Size(min
            = 1, max = 1000) String description) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.description = description;
    }


    public CityDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}
