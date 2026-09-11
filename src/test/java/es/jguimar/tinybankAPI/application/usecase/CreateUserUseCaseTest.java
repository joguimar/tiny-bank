package es.jguimar.tinybankAPI.application.usecase;

import es.jguimar.tinybankAPI.application.port.inbound.UserReadRepository;
import es.jguimar.tinybankAPI.application.port.outbound.UserWriteRepository;
import es.jguimar.tinybankAPI.application.service.CreateUserService;
import es.jguimar.tinybankAPI.domain.model.User;
import es.jguimar.tinybankAPI.infrastructure.exception.ResourceExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTest {

    @Mock
    private UserReadRepository userReadRepository;

    @Mock
    private UserWriteRepository userWriteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CreateUserUseCase createUserUserCase;

    @BeforeEach
    public void setup() {
        createUserUserCase = new CreateUserService(userReadRepository,
                userWriteRepository, passwordEncoder);
    }

    @Test
    public void createNewSafebox_shouldReturnOk() throws Exception {

        // Given
        given(userReadRepository.findByName(any()))
                .willReturn(Optional.empty());

        given(userWriteRepository.save(any()))
                .willReturn(User.builder().id("abc1234").build());

        given(passwordEncoder.encode(any())).willReturn("passenco1234");

        // When
        User user = createUserUserCase.create(User.builder()
                .name("abc1234").password("pass1234").build());

        // Then
        verify(userReadRepository, times(1)).findByName(eq("abc1234"));
        verify(userWriteRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(eq("pass1234"));
        assertThat(user.getId()).isEqualTo("abc1234");
    }

    @Test
    public void createNewSafebox_shouldReturnKO() throws Exception {

        // Given
        given(userReadRepository.findByName(any()))
                .willReturn(Optional.of(User.builder().id("abc1234").build()));

        // When
        Exception exception = assertThrows(ResourceExistsException.class, () -> {
            createUserUserCase.create(User.builder().name("abc1234").build());
         });

        // Then
        verify(userReadRepository, times(1)).findByName(eq("abc1234"));
        assertThat(exception.getMessage()).isNull();
    }

}