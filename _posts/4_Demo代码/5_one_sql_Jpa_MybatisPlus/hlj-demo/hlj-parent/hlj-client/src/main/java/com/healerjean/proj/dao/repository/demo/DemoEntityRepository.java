package com.healerjean.proj.dao.repository.demo;

import com.healerjean.proj.pojo.demo.DemoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DemoEntityRepository extends CrudRepository<DemoEntity, Long> {

    List<DemoEntity> findAll();

    @Override
    Optional<DemoEntity> findById(Long id);

}
