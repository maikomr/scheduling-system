package org.mk.scheduling.repositories.handlers;

import org.mk.scheduling.domain.Class;
import org.mk.scheduling.exceptions.UniqueConstraintViolationException;
import org.mk.scheduling.repositories.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * Created by maiko on 23/12/2016.
 */
@Component
@RepositoryEventHandler
public class ClassRepositoryEventHandler {

    @Autowired
    private ClassRepository classRepository;

    @HandleBeforeCreate
    public void handleClassBeforeCreate(Class aClass) throws UniqueConstraintViolationException {
        String code = aClass.getCode();
        if (classRepository.exists(code)) {
            throw new UniqueConstraintViolationException("code");
        }
        Iterator<Class> it = classRepository.findAll().iterator();
        while (it.hasNext()) {
            Class current = it.next();
            if (current.getTitle().equals(aClass.getTitle())) {
                throw new UniqueConstraintViolationException("title");
            }
        }
    }
}
