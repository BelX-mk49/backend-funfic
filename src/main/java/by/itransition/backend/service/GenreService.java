package by.itransition.backend.service;

import by.itransition.backend.dto.GenreDto;
import by.itransition.backend.exceptions.FunficException;
import by.itransition.backend.mapper.GenreMapper;
import by.itransition.backend.model.Genre;
import by.itransition.backend.repo.GenreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Transactional
    public GenreDto save(GenreDto genreDto) {
        Genre save = genreRepository.save(genreMapper.mapDtoToGenre(genreDto));
        genreDto.setId(save.getId());
        return genreDto;
    }

    @Transactional(readOnly = true)
    public List<GenreDto> getAll() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::mapGenreToDto)
                .collect(toList());
    }

    public GenreDto getGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new FunficException("No genre found with ID - " + id));
        return genreMapper.mapGenreToDto(genre);
    }
}
