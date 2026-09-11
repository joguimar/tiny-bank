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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class TranferMoneyUseCaseTest {

    @Mock
    private WalletReadRepository walletReadRepository;

    @Mock
    private WalletWriteRepository walletWriteRepository;

    private TransferMoneyUseCase transferMoneyUseCase;

    @BeforeEach
    public void setup() {
        transferMoneyUseCase = new WalletService(walletReadRepository, walletWriteRepository);
    }

    @Test
    public void moneyTranferToWalletEmpty_shouldReturnOk() throws ResourceNotFoundException {

        String walletIdSource = "id1234";
        String walletIdTarget = "idtarget1234";
        Double amount = 5.4;

        // Given
        given(walletReadRepository.findById(walletIdSource))
                .willReturn(Optional.of(Wallet.builder().id(walletIdSource)
                        .movements(new ArrayList<>(Arrays.asList(10.0)))
                        .totalAmount(10.0).build()));

        given(walletReadRepository.findById(walletIdTarget))
                .willReturn(Optional.of(Wallet.builder().id(walletIdTarget)
                        .movements(new ArrayList<>())
                        .totalAmount(0.0).build()));

        Wallet resultExpected = Wallet.builder().id(walletIdSource)
                .movements(new ArrayList<>(Arrays.asList(4.6)))
                .totalAmount(4.6).build();

        given(walletWriteRepository.save(resultExpected))
                .willReturn(resultExpected);

        Wallet resultExpectedTarget = Wallet.builder().id(walletIdTarget)
                .movements(Arrays.asList(5.4))
                .totalAmount(5.4).build();

        given(walletWriteRepository.save(resultExpectedTarget))
                .willReturn(resultExpectedTarget);

        // When
        Wallet wallet = transferMoneyUseCase.transferMoney(walletIdSource, walletIdTarget, amount);

        // Then
        verify(walletReadRepository, times(1)).findById(walletIdSource);
        verify(walletReadRepository, times(1)).findById(walletIdTarget);
        assertThat(wallet.getId()).isEqualTo(walletIdTarget);
        assertThat(wallet.getTotalAmount()).isEqualTo(amount);
        assertThat(wallet.getMovements().get(0)).isEqualTo(amount);
    }

    @Test
    public void moneyTranferToWallet_shouldReturnOk() throws ResourceNotFoundException {

        String walletIdSource = "id1234";
        String walletIdTarget = "idtarget1234";
        Double amount = 5.4;

        // Given
        given(walletReadRepository.findById(walletIdSource))
                .willReturn(Optional.of(Wallet.builder().id(walletIdSource)
                        .movements(new ArrayList<>(Arrays.asList(10.0)))
                        .totalAmount(10.0).build()));

        given(walletReadRepository.findById(walletIdTarget))
                .willReturn(Optional.of(Wallet.builder().id(walletIdTarget)
                        .movements(new ArrayList<>(Arrays.asList(1.0)))
                        .totalAmount(1.0).build()));

        Wallet resultExpected = Wallet.builder().id(walletIdSource)
                .movements(new ArrayList<>(Arrays.asList(4.6)))
                .totalAmount(4.6).build();

        given(walletWriteRepository.save(resultExpected))
                .willReturn(resultExpected);

        Wallet resultExpectedTarget = Wallet.builder().id(walletIdTarget)
                .movements(Arrays.asList(1.0, 5.4))
                .totalAmount(6.4).build();

        given(walletWriteRepository.save(resultExpectedTarget))
                .willReturn(resultExpectedTarget);

        // When
        Wallet wallet = transferMoneyUseCase.transferMoney(walletIdSource, walletIdTarget, amount);

        // Then
        verify(walletReadRepository, times(1)).findById(walletIdSource);
        verify(walletReadRepository, times(1)).findById(walletIdTarget);
        assertThat(wallet.getId()).isEqualTo(walletIdTarget);
        assertThat(wallet.getTotalAmount()).isEqualTo(1 + amount);
        assertThat(wallet.getMovements().get(1)).isEqualTo(amount);
    }

    @Test
    public void putMoneyToWalletNotExits_shouldReturnKO() throws ResourceNotFoundException {

        String walletId = "id1234";

        // Given
        given(walletReadRepository.findById(walletId))
                .willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            Wallet wallet = transferMoneyUseCase.transferMoney(walletId, walletId, 1.5);
            assertThat(wallet).isNull();
        });

        // Then
        verify(walletReadRepository, times(1)).findById(any());
        verify(walletWriteRepository, times(0)).save(any());
    }

}