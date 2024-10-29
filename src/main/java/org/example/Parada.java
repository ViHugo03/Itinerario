package org.example;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Parada {

    private Integer id;

    private String nome;

    private String rua;

    private String viagem;

    private String periodoItinerario;

    private String faltou;


}
