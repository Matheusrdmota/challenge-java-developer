package br.com.neurotech.challenge.repository;

import br.com.neurotech.challenge.entity.NeurotechClient;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
     String saveUser(NeurotechClient client);
     Optional<NeurotechClient> getUser(String id);
     List<NeurotechClient> getAllUsers();
}
