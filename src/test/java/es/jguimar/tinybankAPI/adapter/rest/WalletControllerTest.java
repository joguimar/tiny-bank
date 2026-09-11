package es.jguimar.tinybankAPI.adapter.rest;


import es.jguimar.tinybankAPI.adapter.rest.tranform.WalletMapperImpl;
import es.jguimar.tinybankAPI.application.service.WalletService;
import es.jguimar.tinybankAPI.domain.model.Wallet;
import es.jguimar.tinybankAPI.infrastructure.exception.GlobalExceptionHandler;
import es.jguimar.tinybankAPI.infrastructure.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new WalletController(walletService, new WalletMapperImpl()))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
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

    @Test
    public void putMoney_shouldReturnOk() throws Exception {
        // Given
        String walletId = "id14";
        given(walletService.putMoney(anyString(), anyDouble()))
                .willReturn(Wallet.builder().id(walletId).build());

        // When
        final ResultActions result = mockMvc.perform(
                put("/wallet/" + walletId + "/money")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 100.0\n" +
                                "}"));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(walletId));
    }

    @Test
    public void putMoneyWalletNotExists_shouldReturnKO() throws Exception {
        // Given
        String walletId = "id14";
        given(walletService.putMoney(anyString(), anyDouble()))
                .willThrow(new ResourceNotFoundException());

        // When
        final ResultActions result = mockMvc.perform(
                put("/wallet/" + walletId + "/money")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 100.0\n" +
                                "}"));

        // Then
        result.andExpect(status().isNotFound());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void putMoneyWrongInput_shouldReturnKO() throws Exception {
        // Given
        String walletId = "id14";

        // When (body missing the required 'amount' field -> validation 400)
        final ResultActions result = mockMvc.perform(
                put("/wallet/" + walletId + "/money")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"));

        // Then
        result.andExpect(status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void transferMoney_shouldReturnOk() throws Exception {
        // Given
        given(walletService.transferMoney(anyString(), anyString(), anyDouble()))
                .willReturn(Wallet.builder().id("target1").build());

        // When
        final ResultActions result = mockMvc.perform(
                post("/wallet/money-tranfer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 50.0,\n" +
                                "  \"walletSource\": \"source1\",\n" +
                                "  \"walletTarget\": \"target1\"\n" +
                                "}"));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("target1"));
    }

    @Test
    public void transferMoneyWalletNotExists_shouldReturnKO() throws Exception {
        // Given
        given(walletService.transferMoney(anyString(), anyString(), anyDouble()))
                .willThrow(new ResourceNotFoundException());

        // When
        final ResultActions result = mockMvc.perform(
                post("/wallet/money-tranfer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 50.0,\n" +
                                "  \"walletSource\": \"source1\",\n" +
                                "  \"walletTarget\": \"target1\"\n" +
                                "}"));

        // Then
        result.andExpect(status().isNotFound());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void transferMoneyWrongInput_shouldReturnKO() throws Exception {

        // When (body missing the required 'walletSource' field -> validation 400)
        final ResultActions result = mockMvc.perform(
                post("/wallet/money-tranfer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 50.0,\n" +
                                "  \"walletTarget\": \"target1\"\n" +
                                "}"));

        // Then
        result.andExpect(status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
    }
}
