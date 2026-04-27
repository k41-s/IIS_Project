package hr.algebra.iisCoreBackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ColorResponse {
    private int page;
    @JsonProperty("per_page")
    private int perPage;
    private int total;
    @JsonProperty("total_pages")
    private int totalPages;
    private List<ColorDTO> data;
}
