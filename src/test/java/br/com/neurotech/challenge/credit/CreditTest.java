package br.com.neurotech.challenge.credit;

import br.com.neurotech.challenge.controller.CreditController;
import br.com.neurotech.challenge.dto.AvailableCreditHatchUsersDTO;
import br.com.neurotech.challenge.dto.AvailableCreditResponseDTO;
import br.com.neurotech.challenge.dto.ErrorResponseDTO;
import br.com.neurotech.challenge.exception.InvalidParamException;
import br.com.neurotech.challenge.exception.UserNotFoundException;
import br.com.neurotech.challenge.service.CreditService;
import br.com.neurotech.challenge.utils.Constants;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CreditTest {
    @InjectMocks
    private CreditController controller;
    @Mock
    private CreditService creditService;

    @Test
    void whenReceivesValidRequestForGetCreditAvailableThenShouldReturnSuccess() {
        when(this.creditService.checkCredit("1", "SUV")).thenReturn(true);

        ResponseEntity response = this.controller.checkCredit("1", "SUV");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new AvailableCreditResponseDTO(true), response.getBody());
    }

    @Test
    void whenServiceReturnsInvalidParamThenShouldReturnSuccess() {
        when(this.creditService.checkCredit("1", "SUV")).thenThrow(InvalidParamException.class);

        ResponseEntity response = this.controller.checkCredit("1", "SUV");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new ErrorResponseDTO(Constants.INVALID_PARAMS_ERROR_MESSAGE), response.getBody());
    }

    @Test
    void whenServiceReturnsUserNotFoundThenShouldReturnSuccess() {
        when(this.creditService.checkCredit("1", "SUV")).thenThrow(UserNotFoundException.class);

        ResponseEntity response = this.controller.checkCredit("1", "SUV");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void whenServiceReturnsUncheckedExceptionThenShouldReturnSuccess() {
        when(this.creditService.checkCredit("1", "SUV")).thenThrow(RuntimeException.class);

        ResponseEntity response = this.controller.checkCredit("1", "SUV");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(new ErrorResponseDTO(Constants.INTERNAL_SERVER_ERROR_MESSAGE), response.getBody());
    }

    @Test
    void whenReceivesValidRequestForGetUsersWithHatchCreditAvailableThenShouldReturnSuccess() {
        List<AvailableCreditHatchUsersDTO> usersDTOList = new ArrayList<>();
        usersDTOList.add(new AvailableCreditHatchUsersDTO("Joao", 5000.0));
        usersDTOList.add(new AvailableCreditHatchUsersDTO("Maria", 7000.0));

        when(this.creditService.getUsersWithFixedFeeAndHatchCreditAvailable())
                .thenReturn(usersDTOList);

        ResponseEntity response = this.controller.checkAvailableUsersWithFixedFeeForHatchCredit();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usersDTOList, response.getBody());
    }

    @Test
    void whenServiceReturnsUncheckedExceptionForGetUsersWithHatchCreditAvailableThenShouldThrowAnError() {
        when(this.creditService.getUsersWithFixedFeeAndHatchCreditAvailable())
                .thenThrow(RuntimeException.class);

        ResponseEntity response = this.controller.checkAvailableUsersWithFixedFeeForHatchCredit();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(new ErrorResponseDTO(Constants.INTERNAL_SERVER_ERROR_MESSAGE), response.getBody());
    }
}
