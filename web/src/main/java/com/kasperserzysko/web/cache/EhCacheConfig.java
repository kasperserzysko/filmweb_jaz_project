package com.kasperserzysko.web.cache;

import com.kasperserzysko.web.cache.list_wrappers.GenreListWrapper;
import com.kasperserzysko.web.cache.list_wrappers.MovieListWrapper;
import com.kasperserzysko.web.cache.list_wrappers.PersonListWrapper;
import com.kasperserzysko.web.cache.list_wrappers.RoleCharacterListWrapper;
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

    @Bean
    CacheManager ehCacheManager(){
        CacheConfiguration<Long, MovieDetailsDto> movieDetailsDtoCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                MovieDetailsDto.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();

        CacheConfiguration<Long, RoleCharacterListWrapper> roleCharacterDtoCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                        RoleCharacterListWrapper.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();
        CacheConfiguration<Long, GenreListWrapper> genreDtoListCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                        GenreListWrapper.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();
        CacheConfiguration<Long, PersonListWrapper> producerListCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                        PersonListWrapper.class,
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
        CacheConfiguration<Long, MovieListWrapper> movieListCacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
                        MovieListWrapper.class,
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
        javax.cache.configuration.Configuration<Long, RoleCharacterListWrapper> roleCharacterListConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(roleCharacterDtoCacheConfiguration);
        javax.cache.configuration.Configuration<Long, GenreListWrapper> genreListConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(genreDtoListCacheConfiguration);
        javax.cache.configuration.Configuration<Long, PersonListWrapper> personListConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(producerListCacheConfiguration);
        javax.cache.configuration.Configuration<Long, PersonDetailedDto> personDetailConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(personDetailCacheConfiguration);
        javax.cache.configuration.Configuration<Long, MovieListWrapper> movieListConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(movieListCacheConfiguration);
        javax.cache.configuration.Configuration<Long, UserUsernameDto> userUsernameConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(userUsernameCacheConfiguration);


        cacheManager.createCache("cacheMovieDetails", movieDetailsConfiguration);
        cacheManager.createCache("cacheMovieRolesList", roleCharacterListConfiguration);
        cacheManager.createCache("cacheMovieGenreList", genreListConfiguration);
        cacheManager.createCache("cacheMovieProducerList", personListConfiguration);
        cacheManager.createCache("cachePersonDetails", personDetailConfiguration);
        cacheManager.createCache("cachePersonRoleList", roleCharacterListConfiguration);
        cacheManager.createCache("cachePersonProductionsList", movieListConfiguration);
        cacheManager.createCache("cacheUserUsername", userUsernameConfiguration);

        return cacheManager;
    }


}
