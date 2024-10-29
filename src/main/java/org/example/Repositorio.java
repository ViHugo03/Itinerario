package org.example;

import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class Repositorio {

    public List<Parada> leExibeArquivoCsv(String nomeArq) {
        FileReader arq = null;
        Scanner entrada = null;

        nomeArq += ".csv";

        try {
            arq = new FileReader(nomeArq);
            entrada = new Scanner(arq).useDelimiter(";|\\n");
        } catch (FileNotFoundException erro) {
            System.exit(1);
        }

        try {
            List<Parada> itinerario = new ArrayList<>();
            int idNovo = 1;
            while (entrada.hasNext()) {
                String id = entrada.next();
                String nome = entrada.next();
                String rua = entrada.next();
                String viagem = entrada.next();
                String periodoItinerario = entrada.next();
                String faltou = entrada.next();


                itinerario.add(new Parada(idNovo, nome, rua, viagem, periodoItinerario, faltou));
                idNovo++;
            }
            return itinerario;
        } catch (NoSuchElementException | IllegalStateException erro) {
        } finally {
            entrada.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.exit(1);
            }
        }
        return null;
    }

    public void gravaArquivoCsv(List<Parada> lista, String nomeArq) throws IOException {
        if (lista.isEmpty()) {
            return;
        } else {

            nomeArq += ".csv";

            FileWriter arq = new FileWriter(nomeArq);
            Formatter saida = new Formatter(arq);

            try {
                for (Parada parada : lista) {
                    saida.format("%d;%s;%s;%s;%s;%s\n",
                            parada.getId(),
                            parada.getNome(),
                            parada.getRua(),
                            parada.getViagem(),
                            parada.getPeriodoItinerario(),
                            parada.getFaltou());
                }

            } catch (FormatterClosedException erro) {
            } finally {
                saida.close();
                try {
                    arq.close();
                } catch (IOException erro) {
                    System.exit(1);
                }
            }
        }
    }

}
