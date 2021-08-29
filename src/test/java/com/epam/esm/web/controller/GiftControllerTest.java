package com.epam.esm.web.controller;

import com.epam.esm.TestConfig;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.persistence.util.search.QueryOrder;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.web.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@Sql({"classpath:sql/db-schema.sql", "classpath:sql/db-test-data.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GiftControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private GiftController giftController;

    @BeforeEach
    public void initialize() {
        mockMvc = MockMvcBuilders.standaloneSetup(giftController).build();
    }

    @Test
    void findAllGiftsWithoutSearchFilterParams() {
        try {
            mockMvc.perform(
                    get("/gifts"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(4)));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsWithTagExists() {
        try {
            GiftSearchFilter giftSearchFilter = GiftSearchFilter
                    .builder()
                    .tag("TAG_TEST_1")
                    .build();

            mockMvc.perform(get("/gifts")
                            .content(objectMapper.writeValueAsString(giftSearchFilter))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsWithTagNotExists() {
        try {
            String tagName = "Tag bad request";
            GiftSearchFilter giftSearchFilter = GiftSearchFilter
                    .builder()
                    .tag(tagName)
                    .build();

            mockMvc.perform(get("/gifts")
                    .content(objectMapper.writeValueAsString(giftSearchFilter))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsWithAllParamsExists() {
        try {
            String tagName = "TAG_TEST_2";
            String namePart = "TEST1";
            String descriptionPart = "_";
            GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
                    .giftName(namePart)
                    .tag(tagName)
                    .giftDescription(descriptionPart)
                    .giftsByNameOrder(QueryOrder.ASC)
                    .giftsByDateOrder(QueryOrder.DESC)
                    .build();

            mockMvc.perform(get("/gifts")
                    .content(objectMapper.writeValueAsString(giftSearchFilter))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[?(@.name == 'TEST1')]").exists());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findGiftByIdExists() {
        try {
            mockMvc.perform(
                    get("/gifts/"+1))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("TEST1"));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findGiftByIdFailNotFound() {
        try {
            mockMvc.perform(
                    get("/gifts/"+123))
                    .andExpect(status().isNotFound())
                    .andExpect(mvcResult -> mvcResult.getResolvedException()
                            .getClass()
                            .equals(EntityNotFoundException.class));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createSuccess() {
        try {
            String name = "Just for test";
            String description = "Description for test";
            double price = 100;
            int duration = 200;
            GiftCertificateDto giftCertificateDto = GiftCertificateDto
                    .builder()
                    .id(5L)
                    .name(name)
                    .description(description)
                    .price(price)
                    .duration(duration)
                    .build();

            mockMvc.perform(post("/gifts")
                    .content(objectMapper.writeValueAsString(giftCertificateDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(giftCertificateDto)));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createFailBadInput() {
        try {
            GiftCertificateDto giftCertificateDto = GiftCertificateDto
                    .builder()
                    .name("asd")
                    .build();

            mockMvc.perform(post("/gifts")
                    .content(objectMapper.writeValueAsString(giftCertificateDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void updateSuccess() {
        try {
            String name = "New name";
            int duration = 11;
            GiftCertificateDto giftCertificateDto = GiftCertificateDto
                    .builder()
                    .id(4L)
                    .name(name)
                    .duration(duration)
                    .build();

            mockMvc.perform(put("/gifts/4")
                    .content(objectMapper.writeValueAsString(giftCertificateDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void updateFailNotFound() {
        try {
            String name = "New name";
            int duration = 11;
            GiftCertificateDto giftCertificateDto = GiftCertificateDto
                    .builder()
                    .name(name)
                    .duration(duration)
                    .build();

            mockMvc.perform(put("/gifts/5")
                    .content(objectMapper.writeValueAsString(giftCertificateDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteSuccess() {
        try {
            mockMvc.perform(delete("/gifts/1"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteFailNotFound() {
        try {
            mockMvc.perform(delete("/gifts/5"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }
}