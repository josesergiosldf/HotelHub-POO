package UI;

import Repository.*;
import Users.Admin;
import Hotel.Reserva;
import Hotel.Quarto;
import Users.Cliente;
import Users.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TelaAdmin extends JFrame {
    private final Admin admin;
    private final RepositoryReserva repoReserva;
    private final RepositoryQuarto repoQuarto;
    private final RepositoryUsuario repoUsuario;

    public TelaAdmin(Admin admin, RepositoryReserva repoReserva, RepositoryQuarto repoQuarto, RepositoryUsuario repoUsuario) {
        this.admin = admin;
        this.repoReserva = repoReserva;
        this.repoQuarto = repoQuarto;
        this.repoUsuario = repoUsuario;

        setTitle("Painel do Administrador - Hotel Vieira Norte");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel titulo = new JLabel("Painel de Administração do Hotel", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(0, 102, 204));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel botoesPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        botoesPanel.setBackground(new Color(240, 248, 255));

        JButton btnReservasAtivas = new JButton("Ver Reservas Ativas");
        JButton btnHistorico = new JButton("Histórico de Reservas");
        JButton btnQuartosDisponiveis = new JButton("Quartos Disponíveis");
        JButton btnTodosQuartos = new JButton("Todos os Quartos");
        JButton btnLucro = new JButton("Faturamento Total");
        JButton btnCriarReserva = new JButton("Criar Nova Reserva");
        JButton btnRegistrarCliente = new JButton("Registrar Cliente");
        JButton btnVerClientes = new JButton("Ver Clientes Registrados");
        JButton btnVerFuncionarios = new JButton("Ver Funcionários Registrados");
        JButton btnSair = new JButton("Sair");

        JButton[] botoes = {btnReservasAtivas, btnHistorico, btnQuartosDisponiveis, btnTodosQuartos,
                btnLucro, btnCriarReserva, btnRegistrarCliente, btnVerClientes, btnVerFuncionarios, btnSair};

        for (JButton btn : botoes) {
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(100, 149, 237));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180)),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)));
            botoesPanel.add(btn);
        }

        add(botoesPanel, BorderLayout.CENTER);

        btnReservasAtivas.addActionListener((ActionEvent e) -> {
            List<Reserva> reservas = admin.getReservasAtivas(repoReserva);
            exibirReservas(reservas, "Reservas Ativas");
        });

        btnHistorico.addActionListener((ActionEvent e) -> {
            List<Reserva> reservas = admin.getHistoricoReservas(repoReserva);
            exibirReservas(reservas, "Histórico de Reservas");
        });

        btnQuartosDisponiveis.addActionListener((ActionEvent e) -> {
            List<Quarto> quartos = admin.getQuartosDisponiveis(repoQuarto);
            exibirQuartos(quartos, "Quartos Disponíveis");
        });

        btnTodosQuartos.addActionListener((ActionEvent e) -> {
            List<Quarto> quartos = admin.getTodosQuartos(repoQuarto);
            exibirQuartos(quartos, "Todos os Quartos");
        });

        btnCriarReserva.addActionListener((ActionEvent e) -> {
            List<Usuario> clientesDisponiveis = repoUsuario.listarUsuarios().stream()
                    .filter(u -> u instanceof Cliente)
                    .filter(u -> !repoReserva.clienteTemReservaAtiva(((Cliente) u).getCpf()))
                    .toList();

            if (clientesDisponiveis.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Todos os clientes já possuem reservas ativas.\n" +
                                "Um cliente só pode ter uma reserva ativa por vez.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
            }
            else {
                String[] opcoes = {"Reserva Normal", "Reserva Promocional"};
                int escolha = JOptionPane.showOptionDialog(
                        this,
                        "Selecione o tipo de reserva:",
                        "Tipo de Reserva",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opcoes,
                        opcoes[0]);

                if (escolha == 0) {
                    new TelaCriarReserva(admin, repoUsuario, repoReserva, repoQuarto).setVisible(true);
                }
                else if (escolha == 1) {
                    new TelaCriarReservaPromocional(admin, repoUsuario, repoReserva, repoQuarto).setVisible(true);
                }
            }
        });

        btnLucro.addActionListener((ActionEvent e) -> {
            double faturamento = admin.calcularFaturamentoTotal(repoReserva, repoUsuario);
            long numFuncionarios = repoUsuario.listarUsuarios().stream().filter(u -> u instanceof Admin).count();

            JOptionPane.showMessageDialog(this,
                    "Faturamento total: R$ " + String.format("%.2f", faturamento) + "\n" +
                            "(Número de reservas: " + repoReserva.listarReservas().size() + ")\n" +
                            "Custo com " + numFuncionarios + " funcionários: R$ " + (numFuncionarios * 50));
        });

        btnRegistrarCliente.addActionListener((ActionEvent e) -> {
            new TelaRegistro(repoUsuario).setVisible(true);
        });

        btnVerClientes.addActionListener((ActionEvent e) -> {
            List<Usuario> clientes = repoUsuario.listarUsuarios()
                    .stream()
                    .filter(u -> !(u instanceof Admin))
                    .collect(Collectors.toList());
            exibirUsuarios(clientes, "Clientes Registrados");
        });

        btnVerFuncionarios.addActionListener((ActionEvent e) -> {
            List<Usuario> admins = repoUsuario.listarUsuarios()
                    .stream()
                    .filter(u -> u instanceof Admin)
                    .collect(Collectors.toList());
            exibirUsuarios(admins, "Funcionários Registrados");
        });

        btnSair.addActionListener((ActionEvent e) -> {
            dispose();
            new TelaLogin(repoUsuario, repoReserva, repoQuarto).setVisible(true);
        });

        setVisible(true);
    }

    private void exibirReservas(List<Reserva> reservas, String titulo) {
        String[] colunas = {"ID", "Cliente", "Quarto", "Check-In", "Check-Out", "Status"};
        Object[][] dados = new Object[reservas.size()][colunas.length];

        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);
            dados[i][0] = r.getId();
            dados[i][1] = r.getCliente().getNome();
            dados[i][2] = r.getQuarto().getNumero();
            dados[i][3] = r.getDataCheckIn();
            dados[i][4] = r.getDataCheckOut();
            dados[i][5] = r.getStatus();
        }

        JTable tabela = new JTable(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tabela.getTableHeader().setBackground(new Color(100, 149, 237));
        tabela.setGridColor(Color.GRAY);
        tabela.setSelectionBackground(Color.LIGHT_GRAY);

        tabela.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                int selectedRow = tabela.getSelectedRow();
                Reserva reservaSelecionada = reservas.get(selectedRow);
                mostrarOpcoesReserva(reservaSelecionada);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, panel, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarOpcoesReserva(Reserva reserva) {
        List<String> opcoes = new ArrayList<>();

        if (reserva.getStatus() == Reserva.Status.PASSIVA) {
            opcoes.add("Realizar Check-In");
            opcoes.add("Cancelar Reserva");
        }
        else if (reserva.getStatus() == Reserva.Status.ATIVA) {
            opcoes.add("Realizar Check-Out");
            opcoes.add("Cancelar Reserva");
        }

        opcoes.add("Ver Detalhes");

        String escolha = (String) JOptionPane.showInputDialog(
                this,
                "Selecione uma ação para a reserva #" + reserva.getId() + ":",
                "Ações da Reserva",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes.toArray(),
                opcoes.getFirst());

        if (escolha != null) {
            switch (escolha) {
                case "Realizar Check-In":
                    reserva.realizarCheckIn();
                    repoReserva.atualizarReserva(reserva);
                    JOptionPane.showMessageDialog(this, "Check-in realizado com sucesso!");
                    break;

                case "Realizar Check-Out":
                    reserva.realizarCheckOut();
                    repoReserva.atualizarReserva(reserva);
                    JOptionPane.showMessageDialog(this, "Check-out realizado com sucesso!");
                    break;

                case "Cancelar Reserva":
                    int confirmacao = JOptionPane.showConfirmDialog(
                            this,
                            "Tem certeza que deseja cancelar esta reserva?",
                            "Confirmar Cancelamento",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmacao == JOptionPane.YES_OPTION) {
                        reserva.setStatus(Reserva.Status.CANCELADA);
                        if (reserva.isCheckInRealizado()) {
                            reserva.getQuarto().setDisponivel(true);
                        }
                        repoReserva.atualizarReserva(reserva);
                        JOptionPane.showMessageDialog(this, "Reserva cancelada com sucesso!");
                    }
                    break;

                case "Ver Detalhes":
                    mostrarDetalhesReserva(reserva);
                    break;
            }
        }
    }

    private void mostrarDetalhesReserva(Reserva reserva) {
        String detalhes = "Detalhes da Reserva #" + reserva.getId() + "\n\n" +
                "Cliente: " + reserva.getCliente().getNome() + "\n" +
                "Quarto: " + reserva.getQuarto().getNumero() + " (" + reserva.getQuarto().getTipo() + ")\n" +
                "Capacidade: " + reserva.getQuarto().getTipo().getCapacidade() + " pessoas\n" +
                "Check-In: " + reserva.getDataCheckIn() + "\n" +
                "Check-Out: " + reserva.getDataCheckOut() + "\n" +
                "Status: " + reserva.getStatus() + "\n" +
                "Valor Total: R$ " + String.format("%.2f", reserva.getValorTotal()) + "\n" +
                "Serviço de Quarto: " + (reserva.getServicoQuarto() != null ? reserva.getServicoQuarto() : "Nenhum") + "\n" +
                "Tour: " + (reserva.getTour() != null ? reserva.getTour() : "Nenhum");

        JOptionPane.showMessageDialog(this, detalhes, "Detalhes da Reserva", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exibirQuartos(List<Quarto> quartos, String titulo) {
        quartos.sort(Comparator.comparingInt(Quarto::getNumero));

        String[] colunas = {"Número", "Tipo", "Diária", "Capacidade", "Disponível"};
        Object[][] dados = new Object[quartos.size()][colunas.length];

        for (int i = 0; i < quartos.size(); i++) {
            Quarto q = quartos.get(i);
            dados[i][0] = q.getNumero();
            dados[i][1] = q.getTipo();
            dados[i][2] = "R$ " + String.format("%.2f", q.getPrecoDiaria());
            dados[i][3] = q.getTipo().getCapacidade() + " pessoas";
            dados[i][4] = q.isDisponivel() ? "Sim" : "Não";
        }

        JTable tabela = new JTable(dados, colunas);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tabela.getTableHeader().setBackground(new Color(100, 149, 237));
        tabela.setGridColor(Color.GRAY);
        tabela.setSelectionBackground(Color.LIGHT_GRAY);

        tabela.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                int selectedRow = tabela.getSelectedRow();
                Quarto quartoSelecionado = quartos.get(selectedRow);
                mostrarDetalhesQuarto(quartoSelecionado);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);
        JOptionPane.showMessageDialog(this, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarDetalhesQuarto(Quarto quarto) {
        JPanel panel = new JPanel(new BorderLayout());

        ImageIcon icon = new ImageIcon(getClass().getResource(quarto.getTipo().getImagePath()));
        Image image = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        JLabel lblImagem = new JLabel(new ImageIcon(image));
        panel.add(lblImagem, BorderLayout.NORTH);

        String numeroQuarto = String.valueOf(quarto.getNumero());
        String andar = numeroQuarto.substring(0, 1);

        String detalhes = "Andar: " + andar + "\n" +
                "Número: " + quarto.getNumero() + "\n" +
                "Tipo: " + quarto.getTipo() + "\n" +
                "Diária: R$ " + String.format("%.2f", quarto.getPrecoDiaria()) + "\n" +
                "Capacidade: " + quarto.getTipo().getCapacidade() + " pessoas";

        JTextArea txtDetalhes = new JTextArea(detalhes);
        txtDetalhes.setEditable(false);
        txtDetalhes.setOpaque(false);
        txtDetalhes.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDetalhes.setBorder(null);

        panel.add(txtDetalhes, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, panel, "Detalhes do Quarto", JOptionPane.PLAIN_MESSAGE);
    }

    private void exibirUsuarios(List<Usuario> usuarios, String titulo) {
        String[] colunas = {"Nome", "CPF", "Email"};
        Object[][] dados = new Object[usuarios.size()][colunas.length];

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            dados[i][0] = u.getNome();
            dados[i][1] = u.getCpf();
            dados[i][2] = u.getEmail();
        }

        JTable tabela = new JTable(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tabela.getTableHeader().setBackground(new Color(100, 149, 237));
        tabela.setGridColor(Color.GRAY);
        tabela.setSelectionBackground(Color.LIGHT_GRAY);

        tabela.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                int selectedRow = tabela.getSelectedRow();
                Usuario usuarioSelecionado = usuarios.get(selectedRow);
                mostrarOpcoesCliente((Cliente) usuarioSelecionado);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);
        JOptionPane.showMessageDialog(this, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarOpcoesCliente(Cliente cliente) {
        String[] opcoes = {"Remover Cliente", "Ver Detalhes"};

        String escolha = (String) JOptionPane.showInputDialog(
                this,
                "Selecione uma ação para o cliente " + cliente.getNome() + ":",
                "Ações do Cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha != null) {
            switch (escolha) {
                case "Remover Cliente":
                    removerCliente(cliente);
                    break;

                case "Ver Detalhes":
                    mostrarDetalhesCliente(cliente);
                    break;
            }
        }
    }

    private void removerCliente(Cliente cliente) {
        List<Reserva> reservasCliente = repoReserva.listarReservas().stream()
                .filter(r -> r.getCliente().getCpf().equals(cliente.getCpf()))
                .filter(r -> r.getStatus() == Reserva.Status.ATIVA || r.getStatus() == Reserva.Status.PASSIVA)
                .toList();

        if (!reservasCliente.isEmpty()) {
            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Este cliente possui " + reservasCliente.size() + " reserva(s) ativa(s).\n" +
                            "Deseja cancelar todas as reservas e remover o cliente?",
                    "Confirmação de Remoção",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            reservasCliente.forEach(reserva -> {
                reserva.setStatus(Reserva.Status.CANCELADA);
                repoReserva.atualizarReserva(reserva);
            });
        }

        boolean removido = repoUsuario.removerUsuario(cliente.getCpf());

        if (removido) {
            JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!");
        }
        else {
            JOptionPane.showMessageDialog(this, "Erro ao remover cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetalhesCliente(Cliente cliente) {
        String detalhes = "Detalhes do Cliente:\n\n" +
                "Nome: " + cliente.getNome() + "\n" +
                "CPF: " + cliente.getCpf() + "\n" +
                "Email: " + cliente.getEmail() + "\n";

        JOptionPane.showMessageDialog(this, detalhes, "Detalhes do Cliente", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exibirTabela(String titulo, Object[][] dados, String[] colunas) {
        JTable tabela = new JTable(dados, colunas);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tabela.getTableHeader().setBackground(new Color(100, 149, 237));
        tabela.setGridColor(Color.GRAY);
        tabela.setSelectionBackground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(tabela);
        JOptionPane.showMessageDialog(this, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}
