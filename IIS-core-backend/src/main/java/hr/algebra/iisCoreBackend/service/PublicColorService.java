package hr.algebra.iisCoreBackend.service;

import hr.algebra.iisCoreBackend.dto.ColorDTO;
import hr.algebra.iisCoreBackend.dto.ColorResponse;
import hr.algebra.iisCoreBackend.dto.SingleColorResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class PublicColorService implements ColorService {

    private final RestTemplate restTemplate;
    private final String REQRES_URL = "https://reqres.in/api/unknown";

    public PublicColorService(@Qualifier("reqresRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ColorDTO> getAll() {
        ColorResponse response = restTemplate.getForObject(REQRES_URL, ColorResponse.class);
        return response != null
                ? response.getData()
                : Collections.emptyList();
    }

    @Override
    public ColorDTO getById(Long id) {
        var response = restTemplate.getForObject(REQRES_URL + "/" + id, SingleColorResponse.class);
        return (response != null) ? response.getData() : null;
    }

    @Override
    public ColorDTO save(ColorDTO color) {
        return restTemplate.postForObject(REQRES_URL, color, ColorDTO.class);
    }

    @Override
    public ColorDTO update(Long id, ColorDTO color) {
        return restTemplate.patchForObject(REQRES_URL + "/" + id, color, ColorDTO.class);
    }

    @Override
    public void delete(Long id) {
        restTemplate.delete(REQRES_URL + "/" + id);
    }
}
