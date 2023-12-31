package dev.vailati.vibrewery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vailati.vibrewery.mappers.BeerMapper;
import dev.vailati.vibrewery.model.BeerDTO;
import dev.vailati.vibrewery.services.BeerService;
import dev.vailati.vibrewery.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl(Mappers.getMapper(BeerMapper.class));
    }

    @Test
    void testPatchBeerById() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers().get(0);

        given(beerService.patchBeerById(eq(beer.getId()), any(BeerDTO.class))).willReturn(Optional.of(
                BeerDTO.builder().build()
        ));

        String testName = "Test Name";

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", testName);

        ResultActions response = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap)));

        response.andExpect(status().isOk());
        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(testName).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void testDeleteBeerById() throws Exception {
        // Arrange
        BeerDTO beer = beerServiceImpl.listBeers().get(0);

        given(beerService.deleteBeerById(beer.getId())).willReturn(true);

        // Act
        ResultActions response = mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON));

        // Assert
        response.andExpect(status().isOk());
        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeerById() throws Exception {
        // Arrange
        BeerDTO beer = beerServiceImpl.listBeers().get(0);

        given(beerService.updateBeerById(eq(beer.getId()), any(BeerDTO.class)))
                .willReturn(Optional.of(BeerDTO.builder().build()));

        //Act
        ResultActions response = mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)));

        // Assert
        response.andExpect(status().isOk());
        verify(beerService).updateBeerById(eq(beer.getId()), any(BeerDTO.class));
    }

    @Test
    void testUpdateBeerNullBeerName() throws Exception {
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);
        testBeer.setBeerName(null);

        given(beerService.updateBeerById(any(UUID.class), any(BeerDTO.class)))
                .willReturn(Optional.of(beerServiceImpl.listBeers().get(1)));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testCreateNewBeer() throws Exception {
        // Arrange
        BeerDTO beer = beerServiceImpl.listBeers().get(0);
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.saveBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers().get(1));

        // Act
        ResultActions response = mockMvc.perform(post(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)));

        // Assert
        response.andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testCreateBeerNullBeerName() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.saveBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post(BeerController.BEER_PATH).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(4)));
    }

    @Test
    void testListBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        ResultActions response = mockMvc.perform(get(BeerController.BEER_PATH).accept(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testGetBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerById() throws Exception {
        // Arrange
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId()))
                .willReturn(Optional.of(testBeer));

        // Act
        ResultActions response = mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }
}
