package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Genre;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.GenreDetailedDto;
import com.kasperserzysko.web.services.interfaces.IGenreService;
import org.springframework.stereotype.Service;

@Service
public class GenreService implements IGenreService {

    private final DataRepository db;

    public GenreService(DataRepository db) {
        this.db = db;
    }

    @Override
    public void addGenreMovie(Long movieId) {

    }

    @Override
    public void addGenre(GenreDetailedDto dto) {
        var genreEntity = new Genre();
        genreEntity.setName(dto.getName());
        db.getGenres().save(genreEntity);
    }
}
