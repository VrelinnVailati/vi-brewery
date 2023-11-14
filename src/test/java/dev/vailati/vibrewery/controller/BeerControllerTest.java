package dev.vailati.vibrewery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vailati.vibrewery.model.Beer;
import dev.vailati.vibrewery.services.BeerService;
import dev.vailati.vibrewery.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
public class BeerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testDeleteBeerById() throws Exception {
        // Arrange
        Beer beer = beerServiceImpl.listBeers().get(0);

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        // Act
        ResultActions response = mockMvc.perform(delete("/api/v1/beer/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON));

        // Assert
        response.andExpect(status().isOk());
        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeerById() throws Exception {
        // Arrange
        Beer beer = beerServiceImpl.listBeers().get(0);

        //Act
        ResultActions response = mockMvc.perform(put("/api/v1/beer/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)));

        // Assert
        response.andExpect(status().isOk());
        verify(beerService).updateBeerById(eq(beer.getId()), any(Beer.class));
    }

    @Test
    void testCreateNewBeer() throws Exception {
        // Arrange
        Beer beer = beerServiceImpl.listBeers().get(0);
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.saveBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));

        // Act
        ResultActions response = mockMvc.perform(post("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)));

        // Assert
        response.andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void listBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        ResultActions response = mockMvc.perform(get("/api/v1/beer").accept(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getBeerById() throws Exception {
        // Arrange
        Beer testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId()))
                .willReturn(testBeer);

        // Act
        ResultActions response = mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())
                .accept(MediaType.APPLICATION_JSON));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }
}
