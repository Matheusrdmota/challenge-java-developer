package br.com.neurotech.challenge.controller;

import br.com.neurotech.challenge.dto.AvailableCreditHatchUsersDTO;
import br.com.neurotech.challenge.dto.AvailableCreditResponseDTO;
import br.com.neurotech.challenge.dto.ErrorResponseDTO;
import br.com.neurotech.challenge.exception.InvalidParamException;
import br.com.neurotech.challenge.exception.UserNotFoundException;
import br.com.neurotech.challenge.service.CreditService;
import br.com.neurotech.challenge.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/credit")
@RequiredArgsConstructor
public class CreditController {
    private final CreditService creditService;

    @Operation(summary = "Verifica se cliente está apto a receber o crédito para o veículo informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna se o usuário está apto ou não",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AvailableCreditResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Os parâmetros informados são inválidos!",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Houve um erro interno! Tente novamente mais tarde!",
                    content = @Content)})
    @GetMapping("/{id}/{vehicleType}")
    public ResponseEntity<Object> checkCredit(@PathVariable("id") String id,
                                              @PathVariable("vehicleType") String vehicleModel) {
        try{
            boolean isAble = this.creditService.checkCredit(id, vehicleModel.toUpperCase());
            return ResponseEntity
                    .ok(new AvailableCreditResponseDTO(isAble));
        }
        catch(UserNotFoundException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        catch(InvalidParamException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponseDTO(Constants.INVALID_PARAMS_ERROR_MESSAGE));
        }
        catch(Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponseDTO(Constants.INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

    @Operation(summary = "Retorna listagem de usuários, com idade entre 23 e 49 anos, com crédito com juros fixos estão aptos para receber crédito de veículos hatch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna se o usuário está apto ou não",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AvailableCreditHatchUsersDTO.class))) }),
            @ApiResponse(responseCode = "500", description = "Houve um erro interno! Tente novamente mais tarde!",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<Object> checkAvailableUsersWithFixedFeeForHatchCredit() {
        try{
            return ResponseEntity
                    .ok(this.creditService
                            .getUsersWithFixedFeeAndHatchCreditAvailable());
        }
        catch(Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponseDTO(Constants.INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }
}
