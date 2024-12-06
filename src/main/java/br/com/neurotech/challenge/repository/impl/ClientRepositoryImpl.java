package br.com.neurotech.challenge.repository.impl;

import br.com.neurotech.challenge.entity.ClientEntity;
import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.repository.ClientRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    private List<ClientEntity> clientEntityList;
    private ObjectMapper mapper;
    private Integer idCounter;

    public ClientRepositoryImpl() {
        this.clientEntityList = new ArrayList<>();
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.idCounter = 0;
    }

    @Override
    public String saveUser(NeurotechClient client) {
        ClientEntity entity = this.mapper.convertValue(client, ClientEntity.class);
        entity.setId(++this.idCounter);

        this.clientEntityList.add(entity);
        return this.idCounter.toString();
    }

    @Override
    public Optional<NeurotechClient> getUser(String id) {
        Optional<ClientEntity> entity = this.clientEntityList.stream()
                .filter(clientEntity -> clientEntity.getId().toString().equals(id))
                .findFirst();

        return entity.map(ent -> this.mapper.convertValue(ent, NeurotechClient.class));
    }

    @Override
    public List<NeurotechClient> getAllUsers() {
        return this.clientEntityList
                .stream()
                .map(entity -> this.mapper.convertValue(entity, NeurotechClient.class))
                .toList();
    }
}
