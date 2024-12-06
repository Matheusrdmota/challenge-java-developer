package br.com.neurotech.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableCreditResponseDTO {
    private boolean creditAvailable;
}
