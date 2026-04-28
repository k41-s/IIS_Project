package hr.algebra.iisCoreBackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"internalId"})
@JacksonXmlRootElement(localName = "item")
public class ColorDTO {

    private Long internalId;

    private int id;
    private String name;
    private int year;
    private String color;
    private String pantone_value;
}
