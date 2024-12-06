package br.com.neurotech.challenge.client;

import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.exception.InvalidParamException;
import br.com.neurotech.challenge.exception.UserNotFoundException;
import br.com.neurotech.challenge.repository.ClientRepository;
import br.com.neurotech.challenge.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClientServiceTest {
    @InjectMocks
    private ClientServiceImpl clientService;
    @Mock
    private ClientRepository repository;

    @Test
    void whenServiceReceivesValidClientRequestForSaveThenShouldReturnSuccess() {
        NeurotechClient client = new NeurotechClient("Joao", 28, 10000.0);
        when(this.repository.saveUser(client)).thenReturn("1");

        assertEquals("1", this.clientService.save(client));
    }

    @Test
    void whenServiceReceivesRequestWithoutNameForSaveThenShouldThrowAnError() {
        NeurotechClient client = new NeurotechClient(null, 28, 10000.0);

        assertThrows(InvalidParamException.class, () -> this.clientService.save(client));
    }

    @Test
    void whenServiceReceivesRequestWithAgeLowerThanEighteenForSaveThenShouldThrowAnError() {
        NeurotechClient client = new NeurotechClient("Joao", 17, 10000.0);

        assertThrows(InvalidParamException.class, () -> this.clientService.save(client));
    }

    @Test
    void whenServiceReceivesRequestWithoutAgeForSaveThenShouldThrowAnError() {
        NeurotechClient client = new NeurotechClient("Joao", null, 10000.0);

        assertThrows(InvalidParamException.class, () -> this.clientService.save(client));
    }

    @Test
    void whenServiceReceivesRequestWithNegativeIncomeForSaveThenShouldThrowAnError() {
        NeurotechClient client = new NeurotechClient("Joao", 18, -10000.0);

        assertThrows(InvalidParamException.class, () -> this.clientService.save(client));
    }

    @Test
    void whenServiceReceivesRequestWithoutIncomeForSaveThenShouldThrowAnError() {
        NeurotechClient client = new NeurotechClient("Joao", 18, -10000.0);

        assertThrows(InvalidParamException.class, () -> this.clientService.save(client));
    }

    @Test
    void whenServiceReceivesValidIdForGetUserThenShouldReturnSuccess() {
        NeurotechClient client = new NeurotechClient("Joao", 28, 10000.0);
        when(this.repository.getUser("1")).thenReturn(Optional.of(client));

        assertEquals(client, this.clientService.get("1"));
    }

    @Test
    void whenServiceReceivesRequestWithoutIdForGetUserThenShouldThrowAnError() {
        assertThrows(InvalidParamException.class, () -> this.clientService.get(null));
    }

    @Test
    void whenServiceReceivesRequestWithInvalidIdForGetUserThenShouldThrowAnError() {
        assertThrows(InvalidParamException.class, () -> this.clientService.get("as"));
    }

    @Test
    void whenServiceReceivesRequestForGetUserThatDoesntExistsThenShouldThrowAnError() {
        when(this.repository.getUser("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> this.clientService.get("1"));
    }
}
