package br.com.neurotech.challenge.controller;

import br.com.neurotech.challenge.dto.ErrorResponseDTO;
import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.exception.InvalidParamException;
import br.com.neurotech.challenge.exception.UserNotFoundException;
import br.com.neurotech.challenge.service.ClientService;
import br.com.neurotech.challenge.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @Operation(summary = "Realiza o cadastro de um novo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna status code ok e um location no header com url de busca de informações do cliente",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Os parâmetros informados são inválidos!",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Houve um erro interno! Tente novamente mais tarde!",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody NeurotechClient client) {
        String baseUrl = "http://localhost:5000/api/client";
        try {
            String clientId = this.clientService.save(client);
            String location = String.format("%s/%s", baseUrl, clientId);

            return ResponseEntity
                    .noContent()
                    .header("Location", location)
                    .build();
        }
        catch (InvalidParamException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponseDTO(Constants.INVALID_PARAMS_ERROR_MESSAGE));
        }
        catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponseDTO(Constants.INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

    @Operation(summary = "Busca as informações de um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna as informações do cliente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NeurotechClient.class)) }),
            @ApiResponse(responseCode = "400", description = "Os parâmetros informados são inválidos!",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Houve um erro interno! Tente novamente mais tarde!",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") String id) {
        try {
            NeurotechClient client = this.clientService.get(id);

            return ResponseEntity
                    .ok(client);
        }
        catch (UserNotFoundException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        catch (InvalidParamException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponseDTO(Constants.INVALID_PARAMS_ERROR_MESSAGE));
        }
        catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponseDTO(Constants.INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }
}
