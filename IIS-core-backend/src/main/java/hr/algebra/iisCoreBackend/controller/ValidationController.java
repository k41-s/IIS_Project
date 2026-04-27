package hr.algebra.iisCoreBackend.controller;

import hr.algebra.iisCoreBackend.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("api/validation")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;

    @GetMapping("/validate-generated-xml")
    public ResponseEntity<List<String>> validateGeneratedXml() {
        try {
            File file = new File("temp/backend_generated_data.xml");
            if (!file.exists()) {
                return ResponseEntity.badRequest().body(List.of("File not found. Run SOAP test first."));
            }

            List<String> messages = validationService.validateXmlFile(file);

            if (messages.isEmpty()) {
                return ResponseEntity.ok(List.of("SUCCESS: File is valid."));
            } else {
                return ResponseEntity.ok(messages);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(List.of("Error: " + e.getMessage()));
        }
    }
}
