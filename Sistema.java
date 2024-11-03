import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


class Emissor {
    private String nome;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String email;

    public Emissor(String nome, String cnpj, String endereco, String telefone, String email) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNome() { return nome; }
    public String getCnpj() { return cnpj; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Emissor: " + nome + " | CNPJ: " + cnpj + " | Endereço: " + endereco +
                " | Telefone: " + telefone + " | Email: " + email;
    }
}


class Cliente {
    private String nome;
    private String cpf;
    private String endereco;
    private String telefone;
    private String email;

    public Cliente(String nome, String cpf, String endereco, String telefone, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Cliente: " + nome + " | CPF: " + cpf + " | Endereço: " + endereco +
                " | Telefone: " + telefone + " | Email: " + email;
    }
}


class NotaFiscal {
    private static final double ISS_PERCENTUAL = 0.05; 
    private Emissor emissor;
    private Cliente cliente;
    private double valor;
    private double valorISS;
    private String descricao;
    private int numeroNota; 

    public NotaFiscal(Emissor emissor, Cliente cliente, double valor, String descricao) {
        this.emissor = emissor;
        this.cliente = cliente;
        this.valor = valor;
        this.descricao = descricao;
        this.valorISS = calcularISS();
        this.numeroNota = gerarNumeroNota();
    }

    private double calcularISS() {
        return valor * ISS_PERCENTUAL;
    }

    private int gerarNumeroNota() {
        Random random = new Random();
        return 100000000 + random.nextInt(900000000);
    }

    public void emitirNotaFiscal() {
        System.out.println("=====================================");
        System.out.println("NOTA FISCAL");
        System.out.println("=====================================");
        System.out.printf("Número da Nota: %09d\n", numeroNota);
        System.out.println(emissor);
        System.out.println(cliente);
        System.out.println("Descrição: " + descricao);
        System.out.printf("Valor do Serviço: R$ %.2f\n", valor);
        System.out.printf("ISS (5%%): R$ %.2f\n", valorISS);
        System.out.printf("Valor Total da Nota: R$ %.2f\n", valor + valorISS);
        System.out.println("=====================================");
    }

    @Override
    public String toString() {
        return String.format("Número da Nota: %09d | Cliente: %s | Valor Total: R$ %.2f",
                numeroNota, cliente.getNome(), valor + valorISS);
    }
}


public class Sistema {
    private static List<Cliente> clientes = new ArrayList<>();
    private static List<NotaFiscal> notasEmitidas = new ArrayList<>();
    private static Emissor emissor;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        
        emissor = new Emissor("NALDO ELÉTRICA E SERVIÇOS", "49.108.360/0001-09", "Rua Rosalina Clara da Silva, 68 - Jardim Maria Beatriz - Carapicuíba SP",
                "(11)97036-4044", "naldoeletrica@gmail.com");

        do {
            System.out.println("\n===== Sistema de Emissão de Nota Fiscal =====");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Emitir Nota Fiscal");
            System.out.println("3. Visualizar Notas Emitidas");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarCliente(scanner);
                    break;
                case 2:
                    emitirNota(scanner);
                    break;
                case 3:
                    visualizarNotasEmitidas();
                    break;
                case 4:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 4);

        scanner.close();
    }

    public static void cadastrarCliente(Scanner scanner) {
        System.out.println("\n===== Cadastro de Cliente =====");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        if (!validarCPF(cpf)) {
            System.out.println("CPF inválido!");
            return;
        }

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        Cliente cliente = new Cliente(nome, cpf, endereco, telefone, email);
        clientes.add(cliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    public static boolean validarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) return false;
        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) {
            return false;
        }
        int soma = 0, peso = 10;
        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * peso--;
        int resto = 11 - (soma % 11);
        char digito1 = (resto >= 10) ? '0' : (char) (resto + '0');
        soma = 0;
        peso = 11;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * peso--;
        resto = 11 - (soma % 11);
        char digito2 = (resto >= 10) ? '0' : (char) (resto + '0');
        return digito1 == cpf.charAt(9) && digito2 == cpf.charAt(10);
    }

    public static void emitirNota(Scanner scanner) {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado. Por favor, cadastre um cliente primeiro.");
            return;
        }
        System.out.println("\n===== Emissão de Nota Fiscal =====");
        System.out.println("Clientes cadastrados:");
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println((i + 1) + ". " + clientes.get(i).getNome());
        }
        System.out.print("Escolha o cliente (número): ");
        int indiceCliente = scanner.nextInt() - 1;
        scanner.nextLine();
        if (indiceCliente < 0 || indiceCliente >= clientes.size()) {
            System.out.println("Cliente inválido!");
            return;
        }
        Cliente clienteSelecionado = clientes.get(indiceCliente);
        System.out.print("Descrição do produto/serviço: ");
        String descricao = scanner.nextLine();
        System.out.print("Valor: R$ ");
        double valor = scanner.nextDouble();

        NotaFiscal notaFiscal = new NotaFiscal(emissor, clienteSelecionado, valor, descricao);
        notaFiscal.emitirNotaFiscal();
        notasEmitidas.add(notaFiscal); 
        System.out.println("Nota fiscal emitida e salva com sucesso.");
    }

    public static void visualizarNotasEmitidas() {
        if (notasEmitidas.isEmpty()) {
            System.out.println("Nenhuma nota fiscal foi emitida até o momento.");
            return;
        }
        System.out.println("\n===== Notas Fiscais Emitidas =====");
        for (NotaFiscal nota : notasEmitidas) {
            System.out.println(nota);
        }
    }
}