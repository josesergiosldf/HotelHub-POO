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

            String cpfRaw = campoCpf.getText().trim();
            String senha = new String(campoSenha.getPassword()).trim();

            if (cpfRaw.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cpf = cpfRaw.replaceAll("[^0-9]", "");
            if (cpf.length() != 11) {
                JOptionPane.showMessageDialog(this,
                        "CPF inválido! Deve conter 11 dígitos numéricos.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!senha.matches("^(?=.*[A-Za-z])(?=.*\\d).{4,}$")) {
                JOptionPane.showMessageDialog(this,
                        "Senha inválida! Requisitos:\n"
                                + "- Mínimo 4 caracteres\n"
                                + "- Pelo menos 1 letra e 1 número",
                        "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Usuario usuario = repoUsuario.buscarUsuarioPorCpf(cpf);

                if (usuario == null || !usuario.getSenha().equals(senha)) {
                    throw new SecurityException("Credenciais inválidas");
                }

                if (usuario instanceof Cliente) {
                    new TelaCliente((Cliente) usuario, repoReserva, repoUsuario, repoQuarto).setVisible(true);
                }
                else if (usuario instanceof Admin) {
                    new TelaAdmin((Admin) usuario, repoReserva, repoQuarto, repoUsuario).setVisible(true);
                }

                dispose();

            }
            catch (SecurityException ex) {
                JOptionPane.showMessageDialog(this,
                        "Usuário ou senha incorretos.\nVerifique suas credenciais.",
                        "Falha na Autenticação",
                        JOptionPane.WARNING_MESSAGE);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro inesperado durante o login:\n" + ex.getMessage(),
                        "Erro do Sistema",
                        JOptionPane.ERROR_MESSAGE);
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
            if (cpf == null || cpf.trim().isEmpty()) {
                return;
            }

            cpf = cpf.trim();

            Usuario usuario = repoUsuario.buscarUsuarioPorCpf(cpf);
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "CPF não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String email = JOptionPane.showInputDialog(this, "Digite o e-mail cadastrado:");
            if (email == null || email.trim().isEmpty()) {
                return;
            }

            email = email.trim();

            if (!usuario.getEmail().equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(this, "E-mail não corresponde ao CPF informado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String novaSenha;
            do {
                novaSenha = JOptionPane.showInputDialog(this, "Digite a nova senha (mínimo 4 caracteres com letras e números):");
                if (novaSenha == null) return;

                novaSenha = novaSenha.trim();
                if (novaSenha.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Senha não pode ser vazia!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                else if (!novaSenha.matches("^(?=.*[A-Za-z])(?=.*\\d).{4,}$")) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Senha inválida.\nRequisitos:\n- Mínimo 4 caracteres\n- Deve conter letras e números",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                else {
                    break;
                }
            }
            while (true);

            repoUsuario.atualizarUsuario(cpf, novaSenha);
            JOptionPane.showMessageDialog(this, "Senha redefinida com sucesso!");
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