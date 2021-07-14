package com.iktakademija.ednevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.ednevnik.entities.TeacherEntity;

public interface TeacherRepository extends CrudRepository<TeacherEntity, Integer> {

}
