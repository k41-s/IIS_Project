package hr.algebra.iisweatherservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherDataService {
    private final String DHMZ_URL = "https://vrijeme.hr/hrvatska_n.xml";

    public Map<String, String> fetchTemperatures() {
        Map<String, String> weatherMap = new HashMap<>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            String xmlData = restTemplate.getForObject(DHMZ_URL, String.class);

            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8)));

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xpath.evaluate("//Grad", doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                String cityName = xpath.evaluate("GradIme", nodes.item(i));
                String temp = xpath.evaluate("Podatci/Temp", nodes.item(i));
                weatherMap.put(cityName, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weatherMap;
    }

    public List<CityData> getWeatherData(String searchName) {
        List<CityData> matches = new ArrayList<>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            String xml = restTemplate.getForObject(DHMZ_URL, String.class);

            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xpath.evaluate("//Grad", doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                String cityName = xpath.evaluate("GradIme", nodes.item(i));

                if (cityName.toLowerCase().contains(searchName.toLowerCase())) {
                    String temp = xpath.evaluate("Podatci/Temp", nodes.item(i));
                    matches.add(new CityData(cityName, temp));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matches;
    }

    public record CityData(String name, String temp) {}
}