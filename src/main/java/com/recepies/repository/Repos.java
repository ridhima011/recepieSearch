package com.recepies.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.recepies.model.foodType;
import com.recepies.model.recepie;

@Repository
public interface Repos extends CrudRepository<recepie, String> {
    Page<recepie> findAll(Pageable pageable);
    Page<recepie> findByTypeContainingIgnoreCase(foodType domainID, Pageable pageable);
    Page<recepie> findByServings(Integer num, Pageable pageable);
    Page<recepie> findByIngredientsContainingIgnoreCase(List<String> includes, Pageable pageable);
    Page<recepie> findByIngredientsNotInIgnoreCase(List<String> excludes, Pageable pageable);

    @Query("{'type' : ?0, 'servings' : ?1, 'ingredients' : { $in : ?2}, 'ingredients' : { $nin : ?3}}")
    Page<recepie> findAll(foodType type, Integer servings, List<String> includes, List<String> excludes, Pageable pageable);
}
