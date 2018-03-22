package com.hlj.springboot.dome.common.repository;

import com.hlj.springboot.dome.common.bean.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface PersonRepository extends CrudRepository<Person,Long> {

    List<Person> findAll();
}
