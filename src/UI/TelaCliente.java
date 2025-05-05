package UI;

import Repository.*;
import Hotel.Reserva;
import Users.Cliente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaCliente extends JFrame {
    private final Cliente cliente;
    private final RepositoryReserva repoReserva;
    private final RepositoryUsuario repoUsuario;
    private final RepositoryQuarto repoQuarto;

    public TelaCliente(Cliente cliente, RepositoryReserva repoReserva, RepositoryUsuario repoUsuario, RepositoryQuarto repoQuarto) {
        this.cliente = cliente;
        this.repoReserva = repoReserva;
        this.repoUsuario = repoUsuario;
        this.repoQuarto = repoQuarto;

        setTitle("Painel do Cliente - Hotel Vieira Norte");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel titulo = new JLabel("Bem-vindo, " + cliente.getNome(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 102, 204));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        infoPanel.setBackground(new Color(240, 248, 255));

        JLabel lblPontos = new JLabel("Seus pontos: " + cliente.getPontos(), SwingConstants.CENTER);
        lblPontos.setFont(new Font("Arial", Font.PLAIN, 18));

        Reserva reservaAtiva = cliente.getReservaAtiva();
        JLabel lblReserva = new JLabel(
                reservaAtiva != null ? "Reserva ativa: Quarto " + reservaAtiva.getQuarto().getNumero() :
                        "Você não tem reservas ativas.", SwingConstants.CENTER
        );
        lblReserva.setFont(new Font("Arial", Font.PLAIN, 18));

        infoPanel.add(lblPontos);
        infoPanel.add(lblReserva);

        add(infoPanel, BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));
        botoesPanel.setBackground(new Color(240, 248, 255));

        JButton btnHistorico = new JButton("Ver Histórico");
        JButton btnSair = new JButton("Sair");

        JButton[] botoes = {btnHistorico, btnSair};

        for (JButton btn : botoes) {
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(100, 149, 237));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180)),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)));
            botoesPanel.add(btn);
        }

        add(botoesPanel, BorderLayout.SOUTH);

        btnHistorico.addActionListener((ActionEvent e) -> {
            List<Reserva> reservas = repoReserva.listarReservasPorCliente(cliente.getCpf());
            exibirHistoricoReservas(reservas);
        });

        btnSair.addActionListener((ActionEvent e) -> {
            dispose();
            new TelaLogin(repoUsuario, repoReserva, repoQuarto).setVisible(true);
        });

        setVisible(true);
    }

    private void exibirHistoricoReservas(List<Reserva> reservas) {
        if (reservas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Você ainda não tem reservas realizadas.");
            return;
        }

        String[] colunas = {"ID", "Quarto", "Check-In", "Check-Out", "Status"};
        Object[][] dados = new Object[reservas.size()][colunas.length];

        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);
            dados[i][0] = r.getId();
            dados[i][1] = r.getQuarto().getNumero();
            dados[i][2] = r.getDataCheckIn();
            dados[i][3] = r.getDataCheckOut();
            dados[i][4] = r.getStatus();
        }

        JTable tabela = new JTable(dados, colunas);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tabela.getTableHeader().setBackground(Color.CYAN);
        tabela.setGridColor(Color.GRAY);
        tabela.setSelectionBackground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(tabela);
        JOptionPane.showMessageDialog(this, scrollPane, "Histórico de Reservas", JOptionPane.INFORMATION_MESSAGE);
    }
}
