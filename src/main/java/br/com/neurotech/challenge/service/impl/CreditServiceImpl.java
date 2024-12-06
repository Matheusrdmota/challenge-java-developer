package br.com.neurotech.challenge.service.impl;

import br.com.neurotech.challenge.dto.AvailableCreditHatchUsersDTO;
import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.entity.VehicleModel;
import br.com.neurotech.challenge.exception.InvalidParamException;
import br.com.neurotech.challenge.exception.UserNotFoundException;
import br.com.neurotech.challenge.repository.ClientRepository;
import br.com.neurotech.challenge.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final ClientRepository repository;

    @Override
    public boolean checkCredit(String clientId, String model) {
        VehicleModel vehicleModel = this.verifyIfIsAValidVehicleModel(model);
        NeurotechClient client = this.repository.getUser(clientId)
                .orElseThrow(UserNotFoundException::new);

        return vehicleModel.equals(VehicleModel.HATCH) ?
                this.verifyConditionForHatch(client)
                : this.verifyConditionForSUV(client);
    }

    @Override
    public List<AvailableCreditHatchUsersDTO> getUsersWithFixedFeeAndHatchCreditAvailable() {
        List<NeurotechClient> clients = this.repository.getAllUsers();

        return clients
                .stream()
                .filter(
                        client -> client.getAge() >= 23
                                && client.getAge() <= 25
                                && this.verifyConditionForHatch(client)
                )
                .map(client ->
                        new AvailableCreditHatchUsersDTO(client.getName(), client.getIncome())
                )
                .toList();
    }

    private VehicleModel verifyIfIsAValidVehicleModel(String model) {
        List<VehicleModel> models = Arrays.asList(VehicleModel.values());
        boolean isValidVehicleModel = models.stream()
                .anyMatch(vehicleModel -> vehicleModel.toString().equals(model));

        if(!isValidVehicleModel)
            throw new InvalidParamException();

        return Enum.valueOf(VehicleModel.class, model);
    }

    private boolean verifyConditionForHatch(NeurotechClient client) {
        return client.getIncome() >= 5000 && client.getIncome() <= 15000;
    }

    private boolean verifyConditionForSUV(NeurotechClient client) {
        return client.getIncome() >= 8000 && client.getAge() > 20;
    }
}
