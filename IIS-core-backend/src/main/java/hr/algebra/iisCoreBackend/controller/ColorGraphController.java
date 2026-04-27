package hr.algebra.iisCoreBackend.controller;

import hr.algebra.iisCoreBackend.dto.ColorDTO;
import hr.algebra.iisCoreBackend.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ColorGraphController {

    private final ColorService colorService;

    @QueryMapping
    public List<ColorDTO> getAllColors() {
        return colorService.getAll();
    }

    @QueryMapping
    public ColorDTO getColorById(@Argument Long id) {
        return colorService.getById(id);
    }

    @MutationMapping
    public ColorDTO createColor(@Argument ColorDTO input) {
        return colorService.save(input);
    }

    @MutationMapping
    public ColorDTO updateColor(@Argument Long id, @Argument ColorDTO input) {
        return colorService.update(id, input);
    }

    @MutationMapping
    public Boolean deleteColor(@Argument Long id) {
        colorService.delete(id);
        return true;
    }
}