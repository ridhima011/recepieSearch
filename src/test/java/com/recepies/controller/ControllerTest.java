package com.recepies.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONObject;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createSampleData() throws Exception {
        String jsonContent = "{\"dishName\": \"name\",\"instructions\": [\"string\"],\"servings\": -1,\"type\": \"VEGETARIAN\"}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/recepies/")
                    .contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Created"));

        MvcResult result = this.mockMvc
                .perform(MockMvcRequestBuilders.get("/recepies/servings")
                    .contentType(MediaType.APPLICATION_JSON).param("servings", "-1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, List<Map<String, String>>> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);  

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/recepies/{id}", map.get("result").get(0).get("id")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
