package UI;

import Repository.RepositoryUsuario;
import Users.Cliente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaRegistro extends JFrame {
    private final RepositoryUsuario repoUsuario;

    public TelaRegistro(RepositoryUsuario repoUsuario) {
        this.repoUsuario = repoUsuario;
        configurarTela();
    }

    private void configurarTela() {
        setTitle("Registrar Novo Cliente");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Registro de Cliente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(0, 102, 102));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));
        formPanel.setBackground(Color.WHITE);

        JTextField campoNome = criarCampoTexto();
        JTextField campoCpf = criarCampoTexto();
        JTextField campoEmail = criarCampoTexto();
        JPasswordField campoSenha = criarCampoSenha();

        adicionarCampo(formPanel, "Nome:", campoNome);
        adicionarCampo(formPanel, "CPF:", campoCpf);
        adicionarCampo(formPanel, "Email:", campoEmail);
        adicionarCampo(formPanel, "Senha:", campoSenha);

        add(formPanel, BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        botoesPanel.setBackground(Color.WHITE);

        JButton btnRegistrar = criarBotao("Registrar", new Color(34, 139, 34));
        JButton btnVoltar = criarBotao("Voltar", new Color(178, 34, 34));
        JButton btnMostrarSenha = criarBotao("Mostrar Senha", new Color(105, 105, 105));

        botoesPanel.add(btnRegistrar);
        botoesPanel.add(btnVoltar);
        botoesPanel.add(btnMostrarSenha);

        add(botoesPanel, BorderLayout.SOUTH);

        btnRegistrar.addActionListener((ActionEvent e) -> {
            String nome = campoNome.getText().trim();
            String cpfRaw = campoCpf.getText().trim();
            String senha = new String(campoSenha.getPassword()).trim();
            String email = campoEmail.getText().trim();

            if (nome.isEmpty() || cpfRaw.isEmpty() || senha.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cpf = cpfRaw.replaceAll("[^0-9]", "");

            if (cpf.length() != 11) {
                JOptionPane.showMessageDialog(this, "CPF deve conter 11 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                JOptionPane.showMessageDialog(this, "Formato de email inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!senha.matches("^(?=.*[A-Za-z])(?=.*\\d).{4,}$")) {
                JOptionPane.showMessageDialog(this, "Senha deve ter:\n- Mínimo 4 caracteres\n- Letras e números", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            repoUsuario.adicionarUsuario(new Cliente(nome, senha, cpf, email));
            JOptionPane.showMessageDialog(this, "Cliente registrado com sucesso!");
            limparCampos(campoNome, campoCpf, campoEmail, campoSenha);
        });

        btnVoltar.addActionListener((ActionEvent e) -> dispose());

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

    private void limparCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }
}