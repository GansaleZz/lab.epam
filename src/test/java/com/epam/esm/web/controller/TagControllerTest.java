package com.epam.esm.web.controller;

import com.epam.esm.TestConfig;
import com.epam.esm.service.dto.TagDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@Sql(scripts = "classpath:sql/db-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:sql/db-clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TagControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TagController tagController;

    @BeforeEach
    public void initialize() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
    }

    @Test
    void findAllTagsSuccess() {
        try {
            mockMvc.perform(get("/tags"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(6)));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByIdExists() {
        try {
            mockMvc.perform(get("/tags/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("TAG_TEST_1"));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByIdFailNotFound() {
        try {
            mockMvc.perform(get("/tags/8"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByNameExists() {
        try {
            mockMvc.perform(get("/tags/name/TAG_TEST_1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByNameFailNotFound() {
        try {
            mockMvc.perform(get("/tags/name/Random_Name"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createSuccess() {
        try {
            TagDto tagDto = TagDto.builder().name("TestName").build();

            mockMvc.perform(post("/tags")
                    .content(objectMapper.writeValueAsString(tagDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value(tagDto.getName()));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createFailBadInput() {
        try {
            TagDto tagDto = TagDto.builder().build();

            mockMvc.perform(post("/tags")
                    .content(objectMapper.writeValueAsString(tagDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteSuccess() {
        try {
            mockMvc.perform(delete("/tags/1"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteFailNotFound() {
        try {
            mockMvc.perform(delete("/tags/100"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }
}
