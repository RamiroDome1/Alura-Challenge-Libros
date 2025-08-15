package Alura_Challenge.demo.repository;

import Alura_Challenge.demo.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM Libro l WHERE LOWER(l.languages) LIKE LOWER(concat('%', :idioma, '%'))")
    List<Libro> findByLanguageContaining(@Param("idioma") String idioma);
}