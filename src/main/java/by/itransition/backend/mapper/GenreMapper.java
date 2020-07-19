package by.itransition.backend.mapper;

import by.itransition.backend.dto.GenreDto;
import by.itransition.backend.model.Genre;
import by.itransition.backend.model.Post;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(genre.getPosts()))")
    GenreDto mapGenreToDto(Genre genre);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Genre mapDtoToGenre(GenreDto genreDto);
}
