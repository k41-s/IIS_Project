package hr.algebra.iisCoreBackend.repository;

import hr.algebra.iisCoreBackend.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> findById(int id);
    void deleteById(int id);
}
