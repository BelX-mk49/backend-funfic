package by.itransition.backend.controller;

import by.itransition.backend.dto.GenreDto;
import by.itransition.backend.service.GenreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/genre")
@AllArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @PostMapping("/create")
    public ResponseEntity<GenreDto> createGenre(@RequestBody GenreDto GenreDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genreService.save(GenreDto));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(genreService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenre(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(genreService.getGenre(id));
    }
}
