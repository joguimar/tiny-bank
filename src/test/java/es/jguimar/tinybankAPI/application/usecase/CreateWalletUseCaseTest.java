package es.jguimar.tinybankAPI.application.usecase;

import es.jguimar.tinybankAPI.application.port.inbound.WalletReadRepository;
import es.jguimar.tinybankAPI.application.port.outbound.WalletWriteRepository;
import es.jguimar.tinybankAPI.application.service.WalletService;
import es.jguimar.tinybankAPI.domain.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class CreateWalletUseCaseTest {

    @Mock
    private WalletReadRepository walletReadRepository;

    @Mock
    private WalletWriteRepository walletWriteRepository;

    private CreateWalletUseCase createWalletUseCase;

    @BeforeEach
    public void setup() {
        createWalletUseCase = new WalletService(walletReadRepository, walletWriteRepository);
    }

    @Test
    public void createNewWallet_shouldReturnOk() {

        // Given
        given(walletWriteRepository.save(any()))
                .willReturn(Wallet.builder().id("abc1234").build());

        // When
        Wallet wallet = createWalletUseCase.create(Arrays.asList("us12345"));

        // Then
        verify(walletWriteRepository, times(1)).save(any());
        assertThat(wallet.getId()).isEqualTo("abc1234");
    }

    //TODO: End up test cases

}