package hr.algebra.iisCoreBackend.model;

import hr.algebra.iisCoreBackend.dto.ColorDTO;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Entity
@Table(name = "colors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "Color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internalId;

    private int id;
    private String name;
    private int year;
    private String color;
    private String pantone_value;
}