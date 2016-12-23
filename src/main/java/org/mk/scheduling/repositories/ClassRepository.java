package org.mk.scheduling.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.mk.scheduling.domain.Class;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by maiko on 22/12/2016.
 */
public interface ClassRepository extends PagingAndSortingRepository<Class, String> {
    @RestResource(path="search", rel="search")
    List<Class> findByCodeContainingOrTitleContainingOrDescriptionContainingAllIgnoreCase(@Param("code")String code, @Param("title")String title, @Param("description")String description);
}
