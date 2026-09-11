package es.jguimar.tinybankAPI.infrastructure.exception;

import es.jguimar.tinybankAPI.adapter.rest.dto.ErrorResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class GlobalExceptionHandlerTest {

    private static final String SAMPLE_URI = "/wallet/id14/money";
    private static final String SAMPLE_METHOD = "PUT";

    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @Before
    public void setup() {
        handler = new GlobalExceptionHandler();
        given(request.getRequestURI()).willReturn(SAMPLE_URI);
        given(request.getMethod()).willReturn(SAMPLE_METHOD);
    }

    @Test
    public void handleResourceNotFound_shouldReturn404WithBody() {
        // When
        ResponseEntity<ErrorResponseDto> response =
                handler.handleResourceNotFound(new ResourceNotFoundException(), request);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.getStatus());
        assertEquals(SAMPLE_URI, body.getPath());
        assertNotNull(body.getMessage());
        assertFalseEmpty(body.getMessage());
    }

    @Test
    public void handleResourceExists_shouldReturn409WithBody() {
        // When
        ResponseEntity<ErrorResponseDto> response =
                handler.handleResourceExists(new ResourceExistsException(), request);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(409, body.getStatus());
        assertEquals(SAMPLE_URI, body.getPath());
        assertNotNull(body.getMessage());
        assertFalseEmpty(body.getMessage());
    }

    @Test
    public void handleAccessDenied_shouldReturn403WithBody() {
        // When
        ResponseEntity<ErrorResponseDto> response =
                handler.handleAccessDenied(new AccessDeniedException("denied"), request);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(403, body.getStatus());
        assertEquals(SAMPLE_URI, body.getPath());
        assertNotNull(body.getMessage());
        assertFalseEmpty(body.getMessage());
    }

    @Test
    public void handleGeneric_shouldReturn500WithGenericMessage() {
        // Given
        String rawMessage = "Very specific internal failure detail";

        // When
        ResponseEntity<ErrorResponseDto> response =
                handler.handleGeneric(new RuntimeException(rawMessage), request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getStatus());
        assertEquals(SAMPLE_URI, body.getPath());
        assertNotNull(body.getMessage());
        assertFalseEmpty(body.getMessage());
        // The generic handler must not leak the raw exception message to the client.
        assertNotEquals(rawMessage, body.getMessage());
    }

    private static void assertFalseEmpty(String value) {
        assertTrue("message should not be empty", value != null && !value.isEmpty());
    }

}
