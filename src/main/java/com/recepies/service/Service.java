package com.recepies.service;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.recepies.model.foodType;
import com.recepies.model.recepie;
import com.recepies.repository.Repos;

import java.util.*;
import java.util.regex.Pattern;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class Service {

    @Autowired
    public MongoTemplate mongoTemplate;

    @Autowired
    private final Repos sRepository;

    public ResponseEntity<Map<String, Object>> getPaginatedResult(Object obj1, boolean includeFlag, int page, int size) {
        try {
			List<recepie> tutorials = new ArrayList<>();
			Pageable paging = PageRequest.of(page, size);

			Page<recepie> pageRecepies = null;

            if(obj1 instanceof foodType) {
                foodType foodType = (foodType) obj1;
                pageRecepies = sRepository.findByTypeContainingIgnoreCase(foodType, paging);
            } else if (obj1 instanceof Integer) {
                Integer numberOfServings = (Integer) obj1;
                pageRecepies = sRepository.findByServings(numberOfServings, paging);
            } else if (obj1 instanceof List<?> && !includeFlag) {
                List<String> excludes = (List<String>) obj1;
                pageRecepies = sRepository.findByIngredientsNotInIgnoreCase(excludes, paging);
            } else if (obj1 instanceof List<?>){
                List<String> includes = (List<String>) obj1;
                pageRecepies = sRepository.findByIngredientsContainingIgnoreCase(includes, paging);
            } else if (obj1 == null) {
                pageRecepies = sRepository.findAll(paging);
            } 

			tutorials = pageRecepies.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("result", tutorials);
			response.put("currentPage", pageRecepies.getNumber());
			response.put("totalItems", pageRecepies.getTotalElements());
			response.put("totalPages", pageRecepies.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
            System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    // read operation
    public List<recepie> fetchRecepies() {
        return (List<recepie>) sRepository.findAll();
    }

    public Optional<recepie> fetchRecepieById(String id) {
        return  sRepository.findById(id);
    }

    // save operation
    public recepie saveRecepie(recepie recepie) {
        return sRepository.save(recepie);
    }

    // update operation
    @Transactional
    public void updateRecepie(recepie recepie, String email) {
        recepie std = sRepository.findById(email).get();

        if (Objects.nonNull(recepie.getDishName())) {
            std.setDishName(recepie.getDishName());
        }
        if (Objects.nonNull(recepie.getType())) {
            std.setType(recepie.getType());
        }
        if (Objects.nonNull(recepie.getIngredients())) {
            std.setIngredients(recepie.getIngredients());
        }
        if (Objects.nonNull(recepie.getInstructions())) {
            std.setInstructions(recepie.getInstructions());
        }
        if (Objects.nonNull(recepie.getServings())) {
            std.setServings(recepie.getServings());
        }
        sRepository.save(std);
    }

    // delete operation
    public void deleteRecepieById(String id) {
        boolean exist = sRepository.existsById(id);
        if(!exist){
            throw new IllegalStateException("recepie with " + id + " does not exist");
        }
        sRepository.deleteById(id);
    }

    // search with different recepies
    public ResponseEntity<Map<String, Object>> getSearchResult(foodType type, Integer servings, List<String> includes, List<String> excludes, List<String> instructions, Integer page, Integer size) {
        try {
			List<recepie> tutorials = new ArrayList<>();
			Pageable paging = PageRequest.of(page, size);

			// Page<recepie> pageRecepies = sRepository.findAll(type, servings, includes, excludes,paging);

            List<Criteria> criterias = new ArrayList<>();

            if(Objects.nonNull(type)) {
                Criteria dynamicCriteria = Criteria.where("type").is(type);
                criterias.add(dynamicCriteria);
            } 
            if (Objects.nonNull(servings)) {
                Criteria dynamicCriteria = Criteria.where("servings").is(servings);
                criterias.add(dynamicCriteria);
            }  
            if (Objects.nonNull(includes)) {
                Criteria dynamicCriteria = Criteria.where("ingredients").in(includes);
                criterias.add(dynamicCriteria);
            } 
            if (Objects.nonNull(excludes)) {
                Criteria dynamicCriteria = Criteria.where("ingredients").nin(excludes);
                criterias.add(dynamicCriteria);
            }
            if (Objects.nonNull(instructions)) {
                String regexString = '(' + String.join("|", instructions) + ')';
                Criteria dynamicCriteria = Criteria.where("instructions").regex( Pattern.compile(regexString, Pattern.CASE_INSENSITIVE));
                criterias.add(dynamicCriteria);
            } 

            Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
            Query searchQuery = new Query(criteria);
            System.out.println(searchQuery);

            List<recepie> pageRecepies = mongoTemplate.find(searchQuery, recepie.class);
            if(pageRecepies.size() > (page + size)){
                tutorials = pageRecepies.subList(page, page+size);
            } else {
                tutorials = pageRecepies;
            }

			// tutorials = pageRecepies.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("result", tutorials);
			response.put("currentPage", page + 1);
			response.put("totalItems", pageRecepies.size());
			response.put("totalPages", pageRecepies.size() / size);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
