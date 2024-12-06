package br.com.neurotech.challenge.credit;

import br.com.neurotech.challenge.dto.AvailableCreditHatchUsersDTO;
import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.exception.InvalidParamException;
import br.com.neurotech.challenge.exception.UserNotFoundException;
import br.com.neurotech.challenge.repository.ClientRepository;
import br.com.neurotech.challenge.service.impl.CreditServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class CreditServiceTest {
    @InjectMocks
    private CreditServiceImpl creditService;
    @Mock
    private ClientRepository repository;

    @Test
    void whenReceivesValidRequestForCheckAvailableCreditForHatchThenShouldReturnTrue() {
        NeurotechClient client = new NeurotechClient("Joao", 24, 10000.0);
        when(this.repository.getUser("1")).thenReturn(Optional.of(client));

        assertEquals(true, this.creditService.checkCredit("1", "HATCH"));
    }

    @Test
    void whenReceivesValidRequestForCheckAvailableCreditForSUVThenShouldReturnTrue() {
        NeurotechClient client = new NeurotechClient("Joao", 24, 10000.0);
        when(this.repository.getUser("1")).thenReturn(Optional.of(client));

        assertEquals(true, this.creditService.checkCredit("1", "SUV"));
    }

    @Test
    void whenReceivesValidRequestForCheckAvailableCreditForHatchAndTheUserDoesntMatchTheConditionsThenShouldReturnFalse() {
        NeurotechClient client = new NeurotechClient("Joao", 24, 3000.0);
        when(this.repository.getUser("1")).thenReturn(Optional.of(client));

        assertEquals(false, this.creditService.checkCredit("1", "HATCH"));
    }

    @Test
    void whenReceivesValidRequestForCheckAvailableCreditForSUVAndTheUserDoesntMatchTheConditionsOfAgeThenShouldReturnFalse() {
        NeurotechClient client = new NeurotechClient("Joao", 18, 10000.0);
        when(this.repository.getUser("1")).thenReturn(Optional.of(client));

        assertEquals(false, this.creditService.checkCredit("1", "SUV"));
    }

    @Test
    void whenReceivesValidRequestForCheckAvailableCreditForSUVAndTheUserDoesntMatchTheConditionsOfIncomeThenShouldReturnFalse() {
        NeurotechClient client = new NeurotechClient("Joao", 21, 7000.0);
        when(this.repository.getUser("1")).thenReturn(Optional.of(client));

        assertEquals(false, this.creditService.checkCredit("1", "SUV"));
    }

    @Test
    void whenRequestWithInvalidVehicleModelForCheckAvailableCreditForSUVThenShouldThrowAnError() {
        assertThrows(InvalidParamException.class,
                () -> this.creditService.checkCredit("1", "hs"));
    }

    @Test
    void whenRequestWithNullVehicleModelForCheckAvailableCreditForSUVThenShouldThrowAnError() {
        assertThrows(InvalidParamException.class,
                () -> this.creditService.checkCredit("1", null));
    }

    @Test
    void whenRequestForCheckAvailableCreditForUserThatDoesntExistsThenShouldThrowAnError() {
        when(this.repository.getUser("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> this.creditService.checkCredit("1", "HATCH"));
    }

    @Test
    void whenGettingUsersWithFixedFeeAndAvailableCreditForHatchThenShouldReturnSuccess() {
        List<NeurotechClient> clients = new ArrayList<>();
        clients.add(new NeurotechClient("Joao", 21, 10000.0));
        clients.add(new NeurotechClient("Maria", 24, 10000.0));
        clients.add(new NeurotechClient("Jose", 27, 10000.0));
        clients.add(new NeurotechClient("Francisco", 25, 3000.0));
        clients.add(new NeurotechClient("Francisca", 24, 10000.0));

        when(this.repository.getAllUsers()).thenReturn(clients);

        List<AvailableCreditHatchUsersDTO> clientsThatMatchAllConditions = new ArrayList<>();
        clientsThatMatchAllConditions.add(new AvailableCreditHatchUsersDTO("Maria", 10000.0));
        clientsThatMatchAllConditions.add(new AvailableCreditHatchUsersDTO("Francisca", 10000.0));

        assertEquals(clientsThatMatchAllConditions,
                this.creditService.getUsersWithFixedFeeAndHatchCreditAvailable());
    }
}
