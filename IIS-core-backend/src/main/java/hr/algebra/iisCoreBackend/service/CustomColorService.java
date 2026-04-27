package hr.algebra.iisCoreBackend.service;

import hr.algebra.iisCoreBackend.dto.ColorDTO;
import hr.algebra.iisCoreBackend.model.Color;
import hr.algebra.iisCoreBackend.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class CustomColorService implements ColorService {

    private final ColorRepository colorRepository;

    @Override
    public List<ColorDTO> getAll() {
        return colorRepository
                .findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ColorDTO getById(Long id) {
        return colorRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Color not found with id: " + id));
    }

    @Override
    public ColorDTO save(ColorDTO colorDto) {
        Color entity = mapToEntity(colorDto);
        Color saved = colorRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public ColorDTO update(Long id, ColorDTO colorDto) {
        Color existing = colorRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Color not found with id: " + id));

        existing.setName(colorDto.getName());
        existing.setYear(colorDto.getYear());
        existing.setColor(colorDto.getColor());
        existing.setPantone_value(colorDto.getPantone_value());

        return mapToDTO(colorRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Color existing = colorRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Color not found"));
        colorRepository.delete(existing);
    }

    private ColorDTO mapToDTO(Color entity) {
        ColorDTO dto = new ColorDTO();
        dto.setInternalId(entity.getInternalId());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setYear(entity.getYear());
        dto.setColor(entity.getColor());
        dto.setPantone_value(entity.getPantone_value());
        return dto;
    }

    private Color mapToEntity(ColorDTO dto) {
        Color entity = new Color();
        entity.setInternalId(dto.getInternalId());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setYear(dto.getYear());
        entity.setColor(dto.getColor());
        entity.setPantone_value(dto.getPantone_value());
        return entity;
    }
}
