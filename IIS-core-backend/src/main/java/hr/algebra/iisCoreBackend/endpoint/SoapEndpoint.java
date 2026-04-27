package hr.algebra.iisCoreBackend.endpoint;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import hr.algebra.iisCoreBackend.model.Color;
import hr.algebra.iisCoreBackend.repository.ColorRepository;
import hr.algebra.iisCoreBackend.soap.GetColorRequest;
import hr.algebra.iisCoreBackend.soap.GetColorResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ColorRepository colorRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetColorRequest")
    @ResponsePayload
    public GetColorResponse getColor(@RequestPayload GetColorRequest request) {

        GetColorResponse response = new GetColorResponse();
        try {
            RestTemplate restTemplate = new RestTemplate();
            Color[] colorsArray = restTemplate.getForObject(REST_URL, Color[].class);
            List<Color> colors = Arrays.asList(colorsArray);

            XmlMapper xmlMapper = new XmlMapper();
            File xmlFile = new File(TEMP_FILE);

            if (xmlFile.getParentFile() != null && !xmlFile.getParentFile().exists()) {
                if(!xmlFile.getParentFile().mkdirs()) throw new Exception("Error creating directory");
            }

            xmlMapper.writer().withRootName("Colors").writeValue(xmlFile, colors);
            System.out.println(">>> Physical XML file generated at: " + xmlFile.getAbsolutePath());

            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(xmlFile);

            XPath xpath = XPathFactory.newInstance().newXPath();

            String result = xpath.evaluate(request.getXpathQuery(), doc);

            response.setResult(result);

        } catch (Exception e) {
            response.setResult("Error executing XPath: " + e.getMessage());
        }
        return response;
    }
}