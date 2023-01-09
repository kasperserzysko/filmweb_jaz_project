package com.kasperserzysko.web.configs;

import com.kasperserzysko.web.dtos.*;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
public class EhCacheConfig {

    private class RoleCharacterList extends ArrayList<RoleCharacterDto> implements Serializable {
        public RoleCharacterList(final Collection<? extends RoleCharacterDto> c) {
            super(c);
        }
    }
    private class GenreList extends ArrayList<GenreDto> implements Serializable{
        public GenreList(final Collection<? extends GenreDto> c) {
            super(c);
        }
    }
    private class PersonList extends ArrayList<PersonDto> implements Serializable {
        public PersonList(final Collection<? extends PersonDto> c) {
            super(c);
        }
    }
    private class MovieList extends ArrayList<MovieDto> implements Serializable {
        public MovieList(final Collection<? extends MovieDto> c) {
            super(c);
        }
    }

    @Bean
    CacheManager movieDetailsCacheManager(){
        CacheConfiguration<Long, MovieDetailsDto> movieDetailsDtoCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                MovieDetailsDto.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();

        CacheConfiguration<Long, RoleCharacterList> roleCharacterDtoCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                        RoleCharacterList.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();
        CacheConfiguration<Long, GenreList> genreDtoListCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                        GenreList.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();
        CacheConfiguration<Long, PersonList> producerListCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                        PersonList.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();
        CacheConfiguration<Long, PersonDetailedDto> personDetailCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                        PersonDetailedDto.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();
        CacheConfiguration<Long, MovieList> movieListCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                        MovieList.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();
        CacheConfiguration<Long, UserUsernameDto> userUsernameCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                        UserUsernameDto.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        javax.cache.configuration.Configuration<Long, MovieDetailsDto> movieDetailsConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(movieDetailsDtoCacheConfiguration);
        javax.cache.configuration.Configuration<Long, RoleCharacterList> roleCharacterListConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(roleCharacterDtoCacheConfiguration);
        javax.cache.configuration.Configuration<Long, GenreList> genreListConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(genreDtoListCacheConfiguration);
        javax.cache.configuration.Configuration<Long, PersonList> personListConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(producerListCacheConfiguration);
        javax.cache.configuration.Configuration<Long, PersonDetailedDto> personDetailConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(personDetailCacheConfiguration);
        javax.cache.configuration.Configuration<Long, MovieList> movieListConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(movieListCacheConfiguration);
        javax.cache.configuration.Configuration<Long, UserUsernameDto> userUsernameConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(userUsernameCacheConfiguration);


        cacheManager.createCache("cacheMovieDetails", movieDetailsConfiguration);
        cacheManager.createCache("cacheMovieRolesList", roleCharacterListConfiguration);
        cacheManager.createCache("cacheMovieGenreList", genreListConfiguration);
        cacheManager.createCache("cacheMovieProducerList", personListConfiguration);
        cacheManager.createCache("cachePersonDetails", personDetailConfiguration);
        cacheManager.createCache("cachePersonRoleList", personListConfiguration);
        cacheManager.createCache("cachePersonProductionsList", movieListConfiguration);
        cacheManager.createCache("cacheUserUsername", userUsernameConfiguration);
        cacheManager.createCache("cacheUserMovieList", movieListConfiguration);

        return cacheManager;
    }


}
