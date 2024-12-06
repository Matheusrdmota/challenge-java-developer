package br.com.neurotech.challenge.service.impl;

import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.exception.InvalidParamException;
import br.com.neurotech.challenge.exception.UserNotFoundException;
import br.com.neurotech.challenge.repository.ClientRepository;
import br.com.neurotech.challenge.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;

    @Override
    public String save(NeurotechClient client) {
        if(!isValidInputs(client))
            throw new InvalidParamException();

        return this.repository.saveUser(client);
    }

    @Override
    public NeurotechClient get(String id) {
        if(Objects.isNull(id) || !id.matches("\\d+"))
            throw new InvalidParamException();

        return this.repository
                .getUser(id)
                .orElseThrow(UserNotFoundException::new);
    }

    private boolean isValidInputs(NeurotechClient client) {
        return Optional.ofNullable(client.getAge()).orElse(0) >= 18
                && client.getIncome() >= 0
                && StringUtils.hasLength(client.getName());
    }
}
