package AEP.ClothIn.console;

import AEP.ClothIn.models.Roupa;
import AEP.ClothIn.services.RoupaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class MenuConsole implements CommandLineRunner{

    private final RoupaService roupaService;
    private final Scanner scanner = new Scanner(System.in);

    public MenuConsole(RoupaService roupaService){
        this.roupaService = roupaService;
    }

    @Override
    public void run(String... args){

        int opcao;

        do {
            mostrarMenu();
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1:
                    criarRoupa();
                    break;

                case 2:
                    listarRoupas();
                    break;

                case 3:
                    buscarRoupa();
                    break;

                case 4:
                    atualizarRoupa();
                    break;

                case 5:
                    deletarRoupa();
                    break;

                case 0:
                    sair();
                    break;

                default:
                    System.out.println("\nOpção inválida!");
                    pressionarEnter();
            }
        }while(opcao!=0);
    }

    private void mostrarMenu() {

        System.out.println("\n================================");
        System.out.println("            CLOTHIN");
        System.out.println("   DOE ROUPAS PARA UM PROXIMO!");
        System.out.println("================================");
        System.out.println("1 - Criar roupa");
        System.out.println("2 - Listar roupas");
        System.out.println("3 - Buscar roupa por ID");
        System.out.println("4 - Atualizar roupa");
        System.out.println("5 - Deletar roupa");
        System.out.println("0 - Sair");
        System.out.println("================================");
        System.out.print("Escolha uma opção: ");
    }

    private void criarRoupa() {
        System.out.println("\n--- CADASTRAR ROUPA ---");

        System.out.print("Tipo: ");
        String tipo = scanner.nextLine();

        System.out.print("Tamanho: ");
        String tamanho = scanner.nextLine();

        System.out.print("Cor: ");
        String cor = scanner.nextLine();

        System.out.print("Estado: ");
        String estado = scanner.nextLine();

        Roupa roupa = new Roupa(
                null,
                tipo,
                tamanho,
                cor,
                estado
        );

        Roupa roupaSalva = roupaService.criar(roupa);

        System.out.println("\nRoupa cadastrada com sucesso!");
        System.out.println("ID gerado: " + roupaSalva.getId());

        pressionarEnter();
    }

    private void listarRoupas() {
        System.out.println("\n--- ROUPAS CADASTRADAS ---");

        List<Roupa> roupas = roupaService.listarTodas();

        if (roupas.isEmpty()) {
            System.out.println("Nenhuma roupa cadastrada.");
            pressionarEnter();
            return;
        }

        for (Roupa roupa : roupas) {

            System.out.println("--------------------------------");
            System.out.println("ID: " + roupa.getId());
            System.out.println("Tipo: " + roupa.getTipo());
            System.out.println("Tamanho: " + roupa.getTamanho());
            System.out.println("Cor: " + roupa.getCor());
            System.out.println("Estado: " + roupa.getEstado());
        }

        System.out.println("--------------------------------");

        pressionarEnter();
    }

    private void buscarRoupa() {
        System.out.println("\n--- BUSCAR ROUPA ---");

        System.out.print("Digite o ID: ");
        String id = scanner.nextLine();

        roupaService.buscarPorId(id).ifPresentOrElse(

                roupa -> {
                    System.out.println("\nRoupa encontrada!");
                    System.out.println("ID: " + roupa.getId());
                    System.out.println("Tipo: " + roupa.getTipo());
                    System.out.println("Tamanho: " + roupa.getTamanho());
                    System.out.println("Cor: " + roupa.getCor());
                    System.out.println("Estado: " + roupa.getEstado());
                },

                () -> System.out.println("\nRoupa não encontrada.")
        );

        pressionarEnter();
    }

    private void atualizarRoupa() {
        System.out.println("\n--- ATUALIZAR ROUPA ---");

        System.out.print("Digite o ID da roupa: ");
        String id = scanner.nextLine();

        System.out.print("Novo tipo: ");
        String tipo = scanner.nextLine();

        System.out.print("Novo tamanho: ");
        String tamanho = scanner.nextLine();

        System.out.print("Nova cor: ");
        String cor = scanner.nextLine();

        System.out.print("Novo estado: ");
        String estado = scanner.nextLine();

        Roupa roupaAtualizada = new Roupa(
                null,
                tipo,
                tamanho,
                cor,
                estado
        );

        Roupa resultado = roupaService.atualizar(id, roupaAtualizada);

        if (resultado == null) {
            System.out.println("\nRoupa não encontrada.");
        } else {
            System.out.println("\nRoupa atualizada com sucesso!");
        }

        pressionarEnter();
    }

    private void deletarRoupa() {
        System.out.println("\n--- DELETAR ROUPA ---");

        System.out.print("Digite o ID da roupa: ");
        String id = scanner.nextLine();

        Roupa roupa = roupaService.buscarPorId(id).orElse(null);

        if (roupa == null) {
            System.out.println("\nRoupa não encontrada.");
            pressionarEnter();
            return;
        }

        System.out.println("\nRoupa encontrada:");
        System.out.println("ID: " + roupa.getId());
        System.out.println("Tipo: " + roupa.getTipo());
        System.out.println("Tamanho: " + roupa.getTamanho());
        System.out.println("Cor: " + roupa.getCor());
        System.out.println("Estado: " + roupa.getEstado());

        System.out.print("\nDeseja realmente deletar esta roupa? (S/N): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {

            boolean deletado = roupaService.deletar(id);

            if (deletado) {
                System.out.println("\nRoupa deletada com sucesso!");
            } else {
                System.out.println("\nNão foi possível deletar a roupa.");
            }

        } else {
            System.out.println("\nOperação cancelada.");
        }

        pressionarEnter();
    }

    private void sair(){
        System.out.print("\nDeseja realmente sair do ClothIn? (S/N): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            System.out.println("\nEncerrando o ClothIn...");
            scanner.close();
            System.exit(0);
        }
    }

    private void pressionarEnter() {
        System.out.print("\nPrecisone ENTER para continuar: ");
        scanner.nextLine();
    }
}
