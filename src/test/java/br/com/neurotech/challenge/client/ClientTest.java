package br.com.neurotech.challenge.client;

import br.com.neurotech.challenge.controller.ClientController;
import br.com.neurotech.challenge.dto.ErrorResponseDTO;
import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.exception.InvalidParamException;
import br.com.neurotech.challenge.exception.UserNotFoundException;
import br.com.neurotech.challenge.service.ClientService;
import br.com.neurotech.challenge.utils.Constants;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClientTest {
    @InjectMocks
    private ClientController controller;
    @Mock
    private ClientService service;

    @Test
    void whenValidRequestForSavingUserIsMadeThenShouldReturnSuccess() {
        NeurotechClient client = new NeurotechClient("Joao", 28, 10000.0);
        when(this.service.save(client)).thenReturn("1");

        ResponseEntity response = this.controller.saveUser(client);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("http://localhost:5000/api/client/1",
                response.getHeaders().get("location").getFirst());
    }

    @Test
    void whenServiceReturnsInvalidParamExceptionForSavingUserThenShouldReturnError() {
        NeurotechClient client = new NeurotechClient(null, 28, 10000.0);
        when(this.service.save(client)).thenThrow(InvalidParamException.class);

        ResponseEntity response = this.controller.saveUser(client);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new ErrorResponseDTO(Constants.INVALID_PARAMS_ERROR_MESSAGE), response.getBody());
    }

    @Test
    void whenServiceReturnsUncheckedExceptionForSavingUserThenShouldReturnError() {
        NeurotechClient client = new NeurotechClient(null, 28, 10000.0);
        when(this.service.save(client)).thenThrow(RuntimeException.class);

        ResponseEntity response = this.controller.saveUser(client);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(new ErrorResponseDTO(Constants.INTERNAL_SERVER_ERROR_MESSAGE), response.getBody());
    }

    @Test
    void whenValidRequestForGetUserIsMadeThenShouldReturnSuccess() {
        NeurotechClient client = new NeurotechClient("Joao", 28, 10000.0);
        when(this.service.get("1")).thenReturn(client);

        ResponseEntity response = this.controller.getUser("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void whenServiceReturnsUserNotFoundForGetUserThenShouldReturnError() {
        when(this.service.get("1")).thenThrow(UserNotFoundException.class);

        ResponseEntity response = this.controller.getUser("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void whenServiceReturnsInvalidParamForGetUserThenShouldReturnError() {
        when(this.service.get("1")).thenThrow(InvalidParamException.class);

        ResponseEntity response = this.controller.getUser("1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void whenServiceReturnsUncheckedExceptionForGetUserThenShouldReturnError() {
        when(this.service.get("1")).thenThrow(RuntimeException.class);

        ResponseEntity response = this.controller.getUser("1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(new ErrorResponseDTO(Constants.INTERNAL_SERVER_ERROR_MESSAGE), response.getBody());
    }
}
