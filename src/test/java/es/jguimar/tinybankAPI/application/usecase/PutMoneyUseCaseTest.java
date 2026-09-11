package es.jguimar.tinybankAPI.application.usecase;

import es.jguimar.tinybankAPI.application.port.inbound.WalletReadRepository;
import es.jguimar.tinybankAPI.application.port.outbound.WalletWriteRepository;
import es.jguimar.tinybankAPI.application.service.WalletService;
import es.jguimar.tinybankAPI.domain.model.Wallet;
import es.jguimar.tinybankAPI.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class PutMoneyUseCaseTest {

    @Mock
    private WalletReadRepository walletReadRepository;

    @Mock
    private WalletWriteRepository walletWriteRepository;

    private PutMoneyUseCase putMoneyUseCase;

    @BeforeEach
    public void setup() {
        putMoneyUseCase = new WalletService(walletReadRepository, walletWriteRepository);
    }

    @Test
    public void putMoneyToWallet_shouldReturnOk() throws ResourceNotFoundException {

        String walletId = "id1234";
        Double amount = 2.4;

        // Given
        given(walletReadRepository.findById(walletId))
                .willReturn(Optional.of(Wallet.builder().id(walletId).build()));


        Wallet resultExpected = Wallet.builder().id(walletId)
                .movements(Arrays.asList(amount))
                .totalAmount(amount).build();

        given(walletWriteRepository.save(resultExpected))
                .willReturn(resultExpected);

        // When
        Wallet wallet = putMoneyUseCase.putMoney(walletId, amount);

        // Then
        verify(walletReadRepository, times(1)).findById(any());
        verify(walletWriteRepository, times(1)).save(any());
        assertThat(wallet.getId()).isEqualTo(walletId);
        assertThat(wallet.getTotalAmount()).isEqualTo(amount);
        assertThat(wallet.getMovements().get(0)).isEqualTo(amount);
    }

    @Test
    public void putMoneyToWalletNotExits_shouldReturnKO() throws ResourceNotFoundException {

        String walletId = "id1234";
        Double amount = 2.4;

        // Given
        given(walletReadRepository.findById(walletId))
                .willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            Wallet wallet = putMoneyUseCase.putMoney(walletId, amount);
            assertThat(wallet).isNull();
        });

        // Then
        verify(walletReadRepository, times(1)).findById(any());
        verify(walletWriteRepository, times(0)).save(any());
    }

}