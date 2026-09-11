package es.jguimar.tinybankAPI.adapter.rest;


import es.jguimar.tinybankAPI.adapter.rest.tranform.WalletMapperImpl;
import es.jguimar.tinybankAPI.application.service.WalletService;
import es.jguimar.tinybankAPI.domain.model.Wallet;
import es.jguimar.tinybankAPI.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new WalletController(walletService, new WalletMapperImpl())).build();
    }

    @Test
    public void createNewWallet_shouldReturnOk() throws Exception {
        // Given
        given(walletService.create(any()))
                .willReturn(Wallet.builder().id("abc1234").build());

        // When
        final ResultActions result = mockMvc.perform(
                post("/wallet/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"userIds\": [\"us1544\"]\n" +
                                "}"));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("abc1234"));
    }

    @Test
    public void createWalletWrontInput_shouldReturnKO() throws Exception {

        // When
        final ResultActions result = mockMvc.perform(
                post("/wallet/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void showWallet_shouldReturnOk() throws Exception {
        // Given
        String walletId = "id14";
        given(walletService.showWallet(walletId))
                .willReturn(Wallet.builder().id(walletId).build());

        // When
        final ResultActions result = mockMvc.perform(
                get("/wallet/" + walletId)
                        .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(walletId));
    }

    @Test
    public void showWalletNotExits_shouldReturnKO() throws Exception {
        // Given
        String walletId = "id14";
        given(walletService.showWallet(walletId))
                .willThrow(new ResourceNotFoundException());

        // When
        final ResultActions result = mockMvc.perform(
                get("/wallet/" + walletId)
                        .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isNotFound());
    }

    //TODO: End up more test cases for all endpoints
}