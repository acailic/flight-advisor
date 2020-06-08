package rs.interview.backend.service.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class AirportDTO implements Serializable {

    @Size(min = 1, max = 50)
    private String name;


}
