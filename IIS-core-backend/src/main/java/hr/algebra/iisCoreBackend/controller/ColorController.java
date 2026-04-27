package hr.algebra.iisCoreBackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import hr.algebra.iisCoreBackend.dto.ColorDTO;
import hr.algebra.iisCoreBackend.service.ColorService;
import hr.algebra.iisCoreBackend.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
public class ColorController {

    private final ValidationService validationService;

    private final ColorService customColorService;
    private final ColorService publicColorService;

    public ColorController(
            ValidationService validationService,
            @Qualifier("customColorService") ColorService customColorService,
            @Qualifier("publicColorService") ColorService publicColorService
    ) {
        this.validationService = validationService;
        this.customColorService = customColorService;
        this.publicColorService = publicColorService;
    }

    @GetMapping
    public ResponseEntity<List<ColorDTO>> getAllColors(
            @RequestHeader(value = "X-API-Mode", defaultValue = "public") String apiMode
    ) {
        if ("public".equalsIgnoreCase(apiMode)) {
            return ResponseEntity.ok(publicColorService.getAll());
        } else {
            return ResponseEntity.ok(customColorService.getAll());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColorDTO> getColorById(
            @RequestHeader(value = "X-API-Mode", defaultValue = "public") String apiMode,
            @PathVariable Long id
    ) {
        if ("public".equalsIgnoreCase(apiMode)) {
            return ResponseEntity.ok(publicColorService.getById(id));
        } else {
            return ResponseEntity.ok(customColorService.getById(id));
        }
    }

    @PostMapping
    public ResponseEntity<ColorDTO> createColor(
            @RequestHeader(value = "X-API-Mode", defaultValue = "public") String apiMode,
            @RequestBody ColorDTO colorDTO
    ) {
        if ("public".equalsIgnoreCase(apiMode)) {
            return new ResponseEntity<>(publicColorService.save(colorDTO), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(customColorService.save(colorDTO), HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColorDTO> updateColor(
            @RequestHeader(value = "X-API-Mode", defaultValue = "public") String apiMode,
            @PathVariable Long id,
            @RequestBody ColorDTO colorDTO
    ) {
        if ("public".equalsIgnoreCase(apiMode)) {
            return ResponseEntity.ok(publicColorService.update(id,colorDTO));
        } else {
            return ResponseEntity.ok(customColorService.update(id,colorDTO));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColorById(
            @RequestHeader(value = "X-API-Mode", defaultValue = "public") String apiMode,
            @PathVariable Long id
    ) {
        if ("public".equalsIgnoreCase(apiMode)) {
            publicColorService.delete(id);
        } else {
            customColorService.delete(id);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/validate-and-save", consumes = {"application/json", "application/xml"})
    public ResponseEntity<String> validateAndSave(
            @RequestBody String body,
            @RequestHeader("Content-Type") String contentType
    ) {
        try {
            ColorDTO colorToSave;

            if (contentType.contains("application/json")) {
                colorToSave = getColorFromJson(body);

            } else if (contentType.contains("application/xml")) {
                colorToSave = getColorFromXml(body);

            } else {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("Error: Only application/json or application/xml are supported.");
            }

            customColorService.save(colorToSave);

            return ResponseEntity.ok("Success: Data validated and saved!");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Validation Failed: " + e.getMessage());
        }
    }

    private ColorDTO getColorFromXml(String body) throws Exception {
        validationService.validateXml(body);
        return new XmlMapper().readValue(body, ColorDTO.class);
    }

    private ColorDTO getColorFromJson(String body) throws Exception {
        validationService.validateJson(body);
        return new ObjectMapper().readValue(body, ColorDTO.class);
    }
}
