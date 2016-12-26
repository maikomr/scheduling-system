package org.mk.scheduling.repositories;

import org.mk.scheduling.domain.Class;
import org.mk.scheduling.domain.Student;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by maiko on 22/12/2016.
 */
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    @RestResource(path="search", rel="search")
    List<Student> findByFirstNameContainingOrLastNameContainingAllIgnoreCase(@Param("firstName")String firstName, @Param("lastName")String lastName);
}
