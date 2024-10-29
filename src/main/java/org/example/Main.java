package org.example;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        Repositorio repositorio = new Repositorio();
        List<Parada> itinerario = repositorio.leExibeArquivoCsv("Itinerario");

        Integer opcaoEscolhida = 0;
        Scanner scannerNum = new Scanner(System.in);

        while (opcaoEscolhida != 2) {
            System.out.println(
                    " ---------------- Menu ---------------" + "\n" +
                            "| [1] Iniciar Itinerario             |" + "\n" +
                            "| [2] Finalizar                      |" + "\n" +
                            "| [3] Exibir Relatório               |" + "\n" +
                            " -------------------------------------");
            System.out.println("Digite a opção desejada:");
            opcaoEscolhida = scannerNum.nextInt();
            switch (opcaoEscolhida) {
                case 1:
                    iniciarItinerario(itinerario, scannerNum);
                    break;
                case 2:
                    System.out.println("Finalizando itinerario...");
                    repositorio.gravaArquivoCsv(itinerario, "Itinerario");
                    break;
                case 3:
                    exibirRelatorio(itinerario);
                    break;
                default:
                    System.out.println("Opção inválida, digite novamente:");
                    opcaoEscolhida = scannerNum.nextInt();
            }
        }
    }

    private static void iniciarItinerario(List<Parada> itinerario, Scanner scannerNum) {
        Integer opcao = 0;
        String periodo = "";
        System.out.println(
                " ---------------- Menu ---------------" + "\n" +
                        "| [1] Manhã                          |" + "\n" +
                        "| [2] Tarde                          |" + "\n" +
                        "| [3] Noite                          |" + "\n" +
                        " -------------------------------------");
        System.out.println("Digite o período desejado:");
        opcao = scannerNum.nextInt();
        switch (opcao) {
            case 1:
                periodo = "Manhã";
                break;
            case 2:
                periodo = "Tarde";
                break;
            case 3:
                periodo = "Noite";
                break;
            default:
                System.out.println("Opção inválida, digite novamente:");
                return;
        }

        // Se o período for "Manhã", marcar todas as paradas como "Não"
        if ("Manhã".equalsIgnoreCase(periodo)) {
            itinerario.forEach(parada -> parada.setFaltou("Não"));
        }

        System.out.println("Iniciando itinerário para o período: " + periodo);

        for (Parada parada : itinerario) {
            if (parada.getPeriodoItinerario().contains(removerAcentos(periodo)) &&
                    !(parada.getViagem().equalsIgnoreCase("deixar") && "Sim".equalsIgnoreCase(parada.getFaltou()))) {

                System.out.printf("%-3s | %-25s | %-35s | %-5s\n",
                        parada.getId(),
                        removerAcentos(parada.getNome()),  // Remove acentos ao exibir no terminal
                        removerAcentos(parada.getRua()),   // Remove acentos ao exibir no terminal
                        parada.getViagem());

                System.out.println(
                        " ---------------- Menu ---------------" + "\n" +
                                "| [1] Faltou                         |" + "\n" +
                                "| [2] Presente                       |" + "\n" +
                                "| [3] Próximo                        |" + "\n" +
                                " -------------------------------------");
                System.out.println("Digite a opção desejada:");
                opcao = scannerNum.nextInt();

                if (opcao == 1) {
                    marcarFaltou(parada, itinerario);
                }
                switch (opcao) {
                    case 1:
                        marcarFaltou(parada, itinerario);
                        break;
                    case 2:
                        parada.setFaltou("Não");
                        break;
                    case 3:
                        parada.setFaltou("");
                        continue;
                    default:
                        System.out.println("Opção inválida, digite novamente:");
                        opcao = scannerNum.nextInt();
                }
            }
        }
    }

    private static void marcarFaltou(Parada parada, List<Parada> itinerario) {
        parada.setFaltou("Sim");

        if ("pegar".equalsIgnoreCase(parada.getViagem())) {
            Parada proximaDeixar = itinerario.stream()
                    .filter(p -> p.getNome().equals(parada.getNome()) && "deixar".equalsIgnoreCase(p.getViagem()))
                    .findFirst()
                    .orElse(null);

            if (proximaDeixar != null) {
                proximaDeixar.setFaltou("Sim");
            }
        }
    }

    private static void exibirRelatorio(List<Parada> itinerario) {
        System.out.printf("%-3s | %-25s | %-35s | %-8s | %-8s | %-4s\n",
                "ID",
                "Nome",
                "Rua",
                "Viagem",
                "Período",
                "Faltou");
        for (Parada parada : itinerario) {
            System.out.printf("%-3s | %-25s | %-35s | %-8s | %-8s | %-4s\n",
                    parada.getId(),
                    removerAcentos(parada.getNome()),
                    removerAcentos(parada.getRua()),
                    parada.getViagem(),
                    parada.getPeriodoItinerario(),
                    parada.getFaltou());
        }
    }

    private static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }
}
