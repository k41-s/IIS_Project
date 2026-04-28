package hr.algebra.iisCoreBackend.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import hr.algebra.iisCoreBackend.dto.ColorDTO;
import hr.algebra.iisCoreBackend.model.Color;
import hr.algebra.iisCoreBackend.repository.ColorRepository;
import hr.algebra.iisCoreBackend.service.ValidationService;
import hr.algebra.iisCoreBackend.soap.GetColorRequest;
import hr.algebra.iisCoreBackend.soap.GetColorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Endpoint
public class SoapEndpoint {
    private static final String NAMESPACE_URI = "http://algebra.hr/iisbackend/soap";
    private static final String REST_URL = "http://localhost:8080/api/colors";
    private static final String TEMP_FILE = "temp/backend_generated_data.xml";

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private ValidationService validationService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetColorRequest")
    @ResponsePayload
    public GetColorResponse getColor(@RequestPayload GetColorRequest request) {

        GetColorResponse response = new GetColorResponse();
        try {
            RestTemplate restTemplate = new RestTemplate();

            String apiMode = httpRequest.getHeader("X-API-Mode");
            if (apiMode == null) {
                apiMode = "public";
            }
            String authHeader = httpRequest.getHeader("Authorization");

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-Mode", apiMode);

            if (authHeader != null) {
                headers.set("Authorization", authHeader);
            }

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ColorDTO[]> res = restTemplate.exchange(
                    REST_URL,
                    HttpMethod.GET,
                    entity,
                    ColorDTO[].class
            );

            ColorDTO[] responseBody = res.getBody();
            if (responseBody == null) {
                throw new RuntimeException("REST API returned a null body. Cannot generate XML.");
            }

            List<ColorDTO> colors = Arrays.asList(responseBody);

            File tempDir = new File("temp");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            File xmlFile = new File(TEMP_FILE);

            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.writer().withRootName("Colors").writeValue(xmlFile, colors);

            List<String> validationMessages = validationService.validateXmlFile(xmlFile);
            if (!validationMessages.isEmpty()) {
                String errorResult = "XML Validation Failed:\n" + String.join("\n", validationMessages);
                response.setResult(errorResult);
                return response;
            }

            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(xmlFile);

            XPath xpath = XPathFactory.newInstance().newXPath();

            String term = request.getSearchTerm();
            String xpathExpression = String.format("//item[contains(name, '%s')]/name/text()", term);
            String result = xpath.evaluate(xpathExpression, doc);
            if (result == null || result.isEmpty()) {
                response.setResult("No colors found matching term: " + term);
            } else {
                response.setResult(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setResult("Error executing SOAP request: " + e.getMessage());
        }
        return response;
    }
}