package com.io.javabrains.movie_catalog_service.resources;

import com.io.javabrains.movie_catalog_service.models.CatalogItem;
import com.io.javabrains.movie_catalog_service.models.Movie;
import com.io.javabrains.movie_catalog_service.models.Rating;
import com.io.javabrains.movie_catalog_service.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        // Get all rated movie IDs - Hardcoded for now:
        UserRating userRatings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId,
                UserRating.class
                );

        return userRatings.getUserRating().stream().map(rating -> {

            // For each movie ID, call the movie info service and get its details:
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

            // Put them all together:
            return new CatalogItem(movie.getName(), "Description", rating.getRating());
        }).collect(Collectors.toList());
    }
}



// Getting the instance of a movie using webClientBuilder:
/*Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
*/
