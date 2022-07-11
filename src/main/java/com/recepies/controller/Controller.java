package com.recepies.controller;

import java.util.*;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.recepies.model.foodType;
import com.recepies.model.recepie;
import com.recepies.repository.Repos;
import com.recepies.service.Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/recepies")
@Api("Configure The Swagger")
@AllArgsConstructor
public class Controller {
	private final Service Service;
	Repos repo;

	// Save operation
	@PostMapping("/")
	@ApiOperation(value="create recepie", tags = "Create")
	public String saveRecepie(@RequestBody recepie recepie) {
		//sliderService.put(slider.getId(), slider);
		//return slider;
		Service.saveRecepie(recepie);
		return "Created";
	}

	// Read operation
	@GetMapping("/")
	@ApiOperation(value="return all recepies", tags = "Read")
	public List<recepie> fetchAllRecepies() {
		//return new ArrayList<slider>(sliderService.values());
		return Service.fetchRecepies();
	}

	@GetMapping("/{id}")
	@ApiOperation(value="return recepie by id", tags = "Read")
	public Optional<recepie> fetchRecepiesById(@PathVariable String id) {
		//return sliderService.get(id);
		return Service.fetchRecepieById(id);
	}

	@GetMapping("/type")
	@ApiOperation(value="return recepie by type(VEGETARIAN or NONVEGETARIAN)", tags = "Read")
	public ResponseEntity<Map<String, Object>> fetchRecepiesByType(
		@RequestParam(required = false) foodType type,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "3") int size) {
		//return sliderService.get(id);
		return Service.getPaginatedResult(type, true, page, size);
	}

	@GetMapping("/servings")
	@ApiOperation(value="return recepie by number of servings)", tags = "Read")
	public ResponseEntity<Map<String, Object>> fetchRecepiesByServings(
		@RequestParam(required = false) Integer servings,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "3") int size) {
		//return sliderService.get(id);
		return Service.getPaginatedResult(servings, true, page, size);
	}

	@GetMapping("/ingredients/includes")
	@ApiOperation(value="return recepie by included ingredients)", tags = "Read")
	public ResponseEntity<Map<String, Object>> fetchRecepiesByIngredientsIncluded(
		@RequestParam(required = false) List<String> includes,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "3") int size) {
		//return sliderService.get(id);
		return Service.getPaginatedResult(includes, true, page, size);
	}

	@GetMapping("/ingredients/excludes")
	@ApiOperation(value="return recepie by excluded ingredients)", tags = "Read")
	public ResponseEntity<Map<String, Object>> fetchRecepiesByIngredientsExcluded(
		@RequestParam(required = false) List<String> excludes,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "3") int size) {
		//return sliderService.get(id);
		return Service.getPaginatedResult(excludes, false, page, size);
	}

	@GetMapping("/search")
	@ApiOperation(value="Search recepies with mix of different inputs)", tags = "Search")
	public ResponseEntity<Map<String, Object>> searchRecepies(
		@RequestParam(required = false) foodType type,
		@RequestParam(required = false) Integer servings,
		@RequestParam(required = false) List<String> excludes,
		@RequestParam(required = false) List<String> includes,
		@RequestParam(required = false) List<String> instructions,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "3") int size) {
		//return sliderService.get(id);
		return Service.getSearchResult(type, servings, includes, excludes, instructions, page, size);
	}

	// Update operation
	@PutMapping("/{id}")
	@ApiOperation(value="update recepies by id", tags = "Update")
	public String updateRecepie(@RequestBody recepie recepie,
							   @PathVariable("id") String id) {
		//sliderService.replace(id, slider);
		Service.updateRecepie(recepie, id);
		return "Updated Successfully";
	}

	// Delete operation
	@DeleteMapping("/{id}")
	@ApiOperation(value="delete recepies by id", tags = "Delete")
	public void deleteRecepieById(@PathVariable("id") String id) {
		//sliderService.clear();
		Service.deleteRecepieById(id);
	}
}
