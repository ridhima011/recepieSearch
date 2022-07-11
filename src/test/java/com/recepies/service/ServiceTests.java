package com.recepies.service;
import static org.mockito.Mockito.verify;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;

import com.recepies.model.recepie;
import com.recepies.repository.Repos;
import com.recepies.model.foodType;

import java.util.regex.Pattern;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ServiceTests {

    @Mock
    private Repos repos;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private Service service;

    Page<recepie> mockResponsePage;
    List<recepie> mockResponse;
    List<String> ingredients;

    @Before
    public void setUp() {
        ingredients = new ArrayList<>(Arrays.asList("maggi"));
        recepie newRecepie = new recepie("name", foodType.VEGETARIAN, ingredients, ingredients, 2);
        mockResponsePage = new PageImpl(Arrays.asList(newRecepie));
        mockResponse = new ArrayList<>(Arrays.asList(newRecepie));
    }

    @Test
    public void getPaginatedResultTestsForCallingFindBYType() throws Exception {
        Pageable paging = PageRequest.of(1, 3);

        Mockito.when(repos.findByTypeContainingIgnoreCase(foodType.VEGETARIAN, paging)).thenReturn(mockResponsePage);


        service.getPaginatedResult(foodType.VEGETARIAN, true, 1, 3);
        verify(repos).findByTypeContainingIgnoreCase(foodType.VEGETARIAN, paging);

    }

    @Test
    public void getPaginatedResultTestsForCallingFindBYServings() throws Exception {
        Pageable paging = PageRequest.of(1, 3);

        Mockito.when(repos.findByServings(2, paging)).thenReturn(mockResponsePage);


        service.getPaginatedResult(2, true, 1, 3);
        verify(repos).findByServings(2, paging);
    }

    @Test
    public void getPaginatedResultTestsForCallingFindByIngredientsIncludes() throws Exception {
        Pageable paging = PageRequest.of(1, 3);

        Mockito.when(repos.findByIngredientsContainingIgnoreCase(ingredients, paging)).thenReturn(mockResponsePage);


        service.getPaginatedResult(ingredients, true, 1, 3);
        verify(repos).findByIngredientsContainingIgnoreCase(ingredients, paging);
    }

    @Test
    public void getPaginatedResultTestsForCallingFindByIngredientsExcludes() throws Exception {
        Pageable paging = PageRequest.of(1, 3);

        Mockito.when(repos.findByIngredientsNotInIgnoreCase(ingredients, paging)).thenReturn(mockResponsePage);


        service.getPaginatedResult(ingredients, false, 1, 3);
        verify(repos).findByIngredientsNotInIgnoreCase(ingredients, paging);
    }

    @Test
    public void getPaginatedResultTestsForCallingFindByTypeAndServings() throws Exception {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where("type").is(foodType.VEGETARIAN));
        criterias.add(Criteria.where("servings").is(2));
        Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
        Query searchQuery = new Query(criteria);

        Mockito.when(mongoTemplate.find(searchQuery, recepie.class)).thenReturn(mockResponse);
        

        service.getSearchResult(foodType.VEGETARIAN, 2, null,null,null, 1, 3);
        verify(mongoTemplate).find(searchQuery, recepie.class);
    }


    @Test
    public void getPaginatedResultTestsForCallingFindByIncludesAndExcludes() throws Exception {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where("ingredients").in("rice"));
        criterias.add(Criteria.where("ingredients").nin("egg"));
        Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
        Query searchQuery = new Query(criteria);

        Mockito.when(mongoTemplate.find(searchQuery, recepie.class)).thenReturn(mockResponse);
        

        service.getSearchResult(null, null, new ArrayList<>(Arrays.asList("rice")),new ArrayList<>(Arrays.asList("egg")),null, 1, 3);
        verify(mongoTemplate).find(searchQuery, recepie.class);
    }

    // @Test
    // public void getPaginatedResultTestsForCallingFindByInstructions() throws Exception {
    //     String regexString = "(rice)";
    //     Criteria dynamicCriteria = Criteria.where("instructions").regex( Pattern.compile(regexString, Pattern.CASE_INSENSITIVE));
    //     List<Criteria> criterias = new ArrayList<>();
    //     criterias.add(dynamicCriteria);
    //     Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
    //     Query searchQuery = new Query(criteria);

    //     Mockito.when(mongoTemplate.find(searchQuery, recepie.class)).thenReturn(mockResponse);
        

    //     service.getSearchResult(null, null, null,null,new ArrayList<>(Arrays.asList("rice")), 1, 3);
    //     verify(mongoTemplate).find(searchQuery, recepie.class);
    // }
}
