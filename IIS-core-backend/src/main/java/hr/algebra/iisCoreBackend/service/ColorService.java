package hr.algebra.iisCoreBackend.service;

import hr.algebra.iisCoreBackend.dto.ColorDTO;

import java.util.List;

public interface ColorService {
    List<ColorDTO> getAll();
    ColorDTO getById(Long id);
    ColorDTO save(ColorDTO color);
    ColorDTO update(Long id, ColorDTO color);
    void delete(Long id);
}
