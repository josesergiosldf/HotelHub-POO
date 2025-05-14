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

        String[] colunas = {"Número do Quarto", "Tipo", "Disponível"};
        Object[][] dados = new Object[quartos.size()][colunas.length];

        for (int i = 0; i < quartos.size(); i++) {
            Quarto q = quartos.get(i);
            dados[i][0] = q.getNumero();
            dados[i][1] = q.getTipo();
            dados[i][2] = q.isDisponivel() ? "Sim" : "Não";
        }

        exibirTabela(titulo, dados, colunas);
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

        exibirTabela(titulo, dados, colunas);
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
