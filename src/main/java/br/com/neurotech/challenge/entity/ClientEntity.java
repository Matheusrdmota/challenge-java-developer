package br.com.neurotech.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {
    private Integer id;
    private String name;
    private Integer age;
    private Double income;
}
