package com.io.javabrains.movie_catalog_service.resources;

import com.io.javabrains.movie_catalog_service.models.CatalogItem;
import com.io.javabrains.movie_catalog_service.models.Movie;
import com.io.javabrains.movie_catalog_service.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        // Get all rated movie IDs - Hardcoded for now:
        List<Rating> ratingsList = Arrays.asList(
                new Rating ("1234", 4),
                new Rating ("5678", 3)
        );

        // For each movie ID, call the movie info service and get its details and put them all together:
        return ratingsList.stream().map(rating -> {

            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), "Description", rating.getRating());

        }).collect(Collectors.toList());
    }
}
