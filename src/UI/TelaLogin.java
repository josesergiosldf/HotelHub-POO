package UI;

import Repository.*;
import Users.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaLogin extends JFrame {
    private final RepositoryUsuario repoUsuario;
    private final RepositoryReserva repoReserva;
    private final RepositoryQuarto repoQuarto;

    public TelaLogin(RepositoryUsuario repoUsuario, RepositoryReserva repoReserva, RepositoryQuarto repoQuarto) {
        this.repoUsuario = repoUsuario;
        this.repoReserva = repoReserva;
        this.repoQuarto = repoQuarto;
        configurarTela();
    }

    private void configurarTela() {
        setTitle("Login - Hotel Vieira Norte");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Hotel Vieira Norte", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(new Color(0, 102, 102));
        titulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));
        formPanel.setBackground(Color.WHITE);

        JTextField campoCpf = criarCampoTexto();
        JPasswordField campoSenha = criarCampoSenha();

        adicionarCampo(formPanel, "CPF:", campoCpf);
        adicionarCampo(formPanel, "Senha:", campoSenha);

        add(formPanel, BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        botoesPanel.setBackground(Color.WHITE);

        JButton btnLogin = criarBotao("Login", new Color(0, 128, 0));
        JButton btnMostrarSenha = criarBotao("Mostrar Senha", new Color(105, 105, 105));
        JButton btnEsqueceuSenha = criarBotao("Esqueceu Senha?", new Color(0, 60, 255));

        botoesPanel.add(btnLogin);
        botoesPanel.add(btnMostrarSenha);
        botoesPanel.add(btnEsqueceuSenha);

        add(botoesPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener((ActionEvent e) -> {
            String cpf = campoCpf.getText().trim();
            String senha = new String(campoSenha.getPassword()).trim();

            Usuario usuario = repoUsuario.buscarUsuarioPorCpf(cpf);

            if (cpf.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuario != null && usuario.getSenha().equals(senha)) {
                if (usuario instanceof Cliente) {
                    new TelaCliente((Cliente) usuario, repoReserva, repoUsuario, repoQuarto).setVisible(true);
                }
                else if (usuario instanceof Admin) {
                    new TelaAdmin((Admin) usuario, repoReserva, repoQuarto, repoUsuario).setVisible(true);
                }
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnMostrarSenha.addActionListener((ActionEvent e) -> {
            if (campoSenha.getEchoChar() == (char) 0) {
                campoSenha.setEchoChar('*');
                btnMostrarSenha.setText("Mostrar Senha");
            }
            else {
                campoSenha.setEchoChar((char) 0);
                btnMostrarSenha.setText("Ocultar Senha");
            }
        });

        btnEsqueceuSenha.addActionListener((ActionEvent e) -> {
            String cpf = JOptionPane.showInputDialog(this, "Digite seu CPF cadastrado:");
            if (cpf == null || cpf.trim().isEmpty()) return;

            String email = JOptionPane.showInputDialog(this, "Digite o e-mail cadastrado:");
            if (email == null || email.trim().isEmpty()) return;

            Usuario usuario = repoUsuario.buscarUsuarioPorCpf(cpf);

            if (usuario != null && usuario.getEmail().equalsIgnoreCase(email.trim())) {
                String novaSenha = JOptionPane.showInputDialog(this, "Digite a nova senha:");
                if (novaSenha != null && !novaSenha.trim().isEmpty()) {
                    repoUsuario.atualizarUsuario(cpf, novaSenha.trim());
                    JOptionPane.showMessageDialog(this, "Senha redefinida com sucesso!");
                }
            }
            else {
                JOptionPane.showMessageDialog(this,
                        "CPF ou e-mail não correspondem aos registros.",
                        "Erro de Verificação",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void adicionarCampo(JPanel panel, String labelText, JComponent campo) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(0, 51, 102));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        campo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(campo);
        panel.add(Box.createVerticalStrut(15));
    }

    private JTextField criarCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Arial", Font.PLAIN, 16));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return campo;
    }

    private JPasswordField criarCampoSenha() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("Arial", Font.PLAIN, 16));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campo.setEchoChar('*');
        return campo;
    }

    private JButton criarBotao(String texto, Color corFundo) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return botao;
    }
}