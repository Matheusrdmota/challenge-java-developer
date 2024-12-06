package br.com.neurotech.challenge.service;

import br.com.neurotech.challenge.dto.AvailableCreditHatchUsersDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CreditService {
	
	/**
	 * Efetua a checagem se o cliente está apto a receber crédito
	 * para um determinado modelo de veículo
	 */
	boolean checkCredit(String clientId, String model);
	List<AvailableCreditHatchUsersDTO> getUsersWithFixedFeeAndHatchCreditAvailable();
}
