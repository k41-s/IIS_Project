package hr.algebra.iisCoreBackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;
import org.springframework.stereotype.Service;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    private final String schemasDir = "/schemas";

    public void validateJson(String jsonContent) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonContent);

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);

        try (InputStream is = getClass().getResourceAsStream(schemasDir + "/color-input-schema.json")) {
            JsonSchema schema = factory.getSchema(is);
            Set<ValidationMessage> errors = schema.validate(node);

            if (!errors.isEmpty()) {
                String errorMsg = errors.stream()
                        .map(ValidationMessage::getMessage)
                        .collect(Collectors.joining(" | "));
                throw new RuntimeException("JSON Schema Error: " + errorMsg);
            }
        }
    }

    public void validateXml(String xmlContent) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try (InputStream is = getClass().getResourceAsStream(schemasDir + "/color-input-schema.xsd")) {
            if (is == null) throw new FileNotFoundException("XSD file not found in resources/schemas/");

            Schema schema = factory.newSchema(new StreamSource(is));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(xmlContent.getBytes())));
        }
    }

    public List<String> validateXmlFile(File xmlFile) throws Exception {
        List<String> errors = new ArrayList<>();
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try (InputStream is = getClass().getResourceAsStream(schemasDir + "/color-export-schema.xsd")) {
            if (is == null) throw new FileNotFoundException("Export XSD not found!");

            Schema schema = factory.newSchema(new StreamSource(is));
            Validator validator = schema.newValidator();

            validator.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException e) { errors.add("Line " + e.getLineNumber() + ": " + e.getMessage()); }
                @Override
                public void error(SAXParseException e) { errors.add("Line " + e.getLineNumber() + ": " + e.getMessage()); }
                @Override
                public void fatalError(SAXParseException e) { errors.add("FATAL: " + e.getMessage()); }
            });

            validator.validate(new StreamSource(xmlFile));
        }
        return errors;
    }
}
