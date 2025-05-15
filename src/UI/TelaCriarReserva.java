package UI;

import Repository.*;
import Hotel.*;
import Users.*;
import Misc.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class TelaCriarReserva extends JFrame {
    private final RepositoryUsuario repoUsuario;
    private final RepositoryReserva repoReserva;
    private final RepositoryQuarto repoQuarto;
    private final Admin admin;
    private Cliente clienteSelecionado;
    private Quarto quartoSelecionado;
    private HotelEnums.TipoServico servicoSelecionado;
    private HotelEnums.TipoTour tourSelecionado;
    private LocalDate dataCheckIn;
    private LocalDate dataCheckOut;

    private final Color COR_PRIMARIA = new Color(100, 149, 237);
    private final Color COR_SECUNDARIA = new Color(255, 255, 255);
    private final Font FONTE_TITULO = new Font("Arial", Font.BOLD, 24);
    private final Font FONTE_SUBTITULO = new Font("Arial", Font.PLAIN, 16);
    private final Font FONTE_TEXTO = new Font("Arial", Font.PLAIN, 14);

    public TelaCriarReserva(Admin admin, RepositoryUsuario repoUsuario, RepositoryReserva repoReserva, RepositoryQuarto repoQuarto) {
        this.admin = admin;
        this.repoUsuario = repoUsuario;
        this.repoReserva = repoReserva;
        this.repoQuarto = repoQuarto;

        setTitle("Criar Nova Reserva");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(COR_SECUNDARIA);

        iniciarFluxoCriacaoReserva();
    }

    private void iniciarFluxoCriacaoReserva() {
        selecionarCliente();
    }

    private void selecionarCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_SECUNDARIA);

        panel.add(criarPainelCabecalho("Selecione o Cliente"), BorderLayout.NORTH);

        List<Usuario> clientes = repoUsuario.listarUsuarios().stream()
                .filter(u -> u instanceof Cliente)
                .filter(u -> !repoReserva.clienteTemReservaAtiva(u.getCpf()))
                .toList();

        JPanel corpoPanel = new JPanel(new BorderLayout());
        corpoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        corpoPanel.setBackground(COR_SECUNDARIA);

        String[] colunas = {"Nome", "CPF", "Email"};
        Object[][] dados = new Object[clientes.size()][colunas.length];

        for (int i = 0; i < clientes.size(); i++) {
            Usuario u = clientes.get(i);
            dados[i][0] = u.getNome();
            dados[i][1] = u.getCpf();
            dados[i][2] = u.getEmail();
        }

        JTable tabela = new JTable(dados, colunas);
        tabela.setFont(FONTE_TEXTO);
        tabela.setRowHeight(25);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setFont(FONTE_SUBTITULO);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        corpoPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel rodapePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        rodapePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rodapePanel.setBackground(COR_SECUNDARIA);

        JButton btnCancelar = criarBotao("Cancelar", new Color(204, 0, 0));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnProximo = criarBotao("Próximo", COR_PRIMARIA);
        btnProximo.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para continuar!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            clienteSelecionado = (Cliente) clientes.get(linhaSelecionada);
            getContentPane().removeAll();
            selecionarQuarto();
        });

        rodapePanel.add(btnCancelar);
        rodapePanel.add(btnProximo);

        panel.add(corpoPanel, BorderLayout.CENTER);
        panel.add(rodapePanel, BorderLayout.SOUTH);

        atualizarTela(panel);
    }

    private void selecionarQuarto() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_SECUNDARIA);

        panel.add(criarPainelCabecalho("Selecione o Quarto"), BorderLayout.NORTH);

        JPanel corpoPanel = new JPanel(new BorderLayout());
        corpoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        corpoPanel.setBackground(COR_SECUNDARIA);

        List<Quarto> quartosDisponiveis = repoQuarto.listarQuartosDisponiveis();
        quartosDisponiveis.sort(Comparator.comparingInt(Quarto::getNumero));

        String[] colunas = {"Número", "Tipo", "Diária", "Capacidade"};
        Object[][] dados = new Object[quartosDisponiveis.size()][colunas.length];

        for (int i = 0; i < quartosDisponiveis.size(); i++) {
            Quarto q = quartosDisponiveis.get(i);
            dados[i][0] = q.getNumero();
            dados[i][1] = q.getTipo();
            dados[i][2] = "R$ " + String.format("%.2f", q.getPrecoDiaria());
            dados[i][3] = q.getTipo().getCapacidade() + " pessoas";
        }

        JTable tabela = new JTable(dados, colunas);
        tabela.setFont(FONTE_TEXTO);
        tabela.setRowHeight(25);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setFont(FONTE_SUBTITULO);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        corpoPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel rodapePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        rodapePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rodapePanel.setBackground(COR_SECUNDARIA);

        JButton btnCancelar = criarBotao("Cancelar", new Color(204, 0, 0));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnAnterior = criarBotao("Anterior", new Color(153, 153, 153));
        btnAnterior.addActionListener(e -> {
            getContentPane().removeAll();
            selecionarCliente();
        });

        JButton btnProximo = criarBotao("Próximo", COR_PRIMARIA);

        btnProximo.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um quarto para continuar!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            quartoSelecionado = quartosDisponiveis.get(linhaSelecionada);

            if (!quartoSelecionado.isDisponivel()) {
                JOptionPane.showMessageDialog(this, "Este quarto não está mais disponível. Por favor, selecione outro.", "Quarto Indisponível", JOptionPane.WARNING_MESSAGE);
                return;
            }

            getContentPane().removeAll();
            selecionarDatas();
        });

        rodapePanel.add(btnCancelar);
        rodapePanel.add(btnAnterior);
        rodapePanel.add(btnProximo);

        panel.add(corpoPanel, BorderLayout.CENTER);
        panel.add(rodapePanel, BorderLayout.SOUTH);

        atualizarTela(panel);
    }

    private void selecionarDatas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_SECUNDARIA);

        panel.add(criarPainelCabecalho("Selecione as Datas"), BorderLayout.NORTH);

        JPanel corpoPanel = new JPanel(new GridBagLayout());
        corpoPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        corpoPanel.setBackground(COR_SECUNDARIA);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCheckIn = new JLabel("Data de Check-In:");
        lblCheckIn.setFont(FONTE_SUBTITULO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        corpoPanel.add(lblCheckIn, gbc);

        JTextField txtCheckIn = new JTextField(LocalDate.now().toString());
        txtCheckIn.setFont(FONTE_TEXTO);
        gbc.gridx = 1;
        corpoPanel.add(txtCheckIn, gbc);

        JLabel lblCheckOut = new JLabel("Data de Check-Out:");
        lblCheckOut.setFont(FONTE_SUBTITULO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        corpoPanel.add(lblCheckOut, gbc);

        JTextField txtCheckOut = new JTextField(LocalDate.now().plusDays(1).toString());
        txtCheckOut.setFont(FONTE_TEXTO);
        gbc.gridx = 1;
        corpoPanel.add(txtCheckOut, gbc);

        JPanel rodapePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        rodapePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rodapePanel.setBackground(COR_SECUNDARIA);

        JButton btnCancelar = criarBotao("Cancelar", new Color(204, 0, 0));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnAnterior = criarBotao("Anterior", new Color(153, 153, 153));
        btnAnterior.addActionListener(e -> {
            getContentPane().removeAll();
            selecionarQuarto();
        });

        JButton btnProximo = criarBotao("Próximo", COR_PRIMARIA);
        btnProximo.addActionListener(e -> {
            try {
                dataCheckIn = LocalDate.parse(txtCheckIn.getText());
                dataCheckOut = LocalDate.parse(txtCheckOut.getText());

                if (dataCheckIn.isAfter(dataCheckOut) || dataCheckIn.isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("Datas inválidas");
                }

                getContentPane().removeAll();
                selecionarServicos();
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Datas inválidas!\nUse o formato AAAA-MM-DD\nCertifique-se que a data de check-in é hoje ou depois.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        rodapePanel.add(btnCancelar);
        rodapePanel.add(btnAnterior);
        rodapePanel.add(btnProximo);

        panel.add(corpoPanel, BorderLayout.CENTER);
        panel.add(rodapePanel, BorderLayout.SOUTH);

        atualizarTela(panel);
    }

    private void selecionarServicos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_SECUNDARIA);

        panel.add(criarPainelCabecalho("Serviços Adicionais"), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(COR_SECUNDARIA);

        JPanel selecaoPanel = new JPanel(new GridBagLayout());
        selecaoPanel.setBackground(COR_SECUNDARIA);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblServicoQuarto = new JLabel("Serviço de Quarto:");
        lblServicoQuarto.setFont(FONTE_SUBTITULO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        selecaoPanel.add(lblServicoQuarto, gbc);

        JComboBox<HotelEnums.TipoServico> cbServicoQuarto = new JComboBox<>(HotelEnums.TipoServico.values());
        cbServicoQuarto.setFont(FONTE_TEXTO);
        cbServicoQuarto.setSelectedItem(null);
        gbc.gridx = 1;
        selecaoPanel.add(cbServicoQuarto, gbc);

        JLabel lblTour = new JLabel("Tour:");
        lblTour.setFont(FONTE_SUBTITULO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        selecaoPanel.add(lblTour, gbc);

        JComboBox<HotelEnums.TipoTour> cbTour = new JComboBox<>(HotelEnums.TipoTour.values());
        cbTour.setFont(FONTE_TEXTO);
        cbTour.setSelectedItem(null);
        gbc.gridx = 1;
        selecaoPanel.add(cbTour, gbc);

        JPanel descricaoPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        descricaoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel servicoDescPanel = new JPanel(new BorderLayout());
        servicoDescPanel.setBorder(BorderFactory.createTitledBorder("Descrição do Serviço de Quarto"));
        JTextArea txtServicoDesc = new JTextArea();
        txtServicoDesc.setEditable(false);
        txtServicoDesc.setFont(FONTE_TEXTO);
        txtServicoDesc.setLineWrap(true);
        txtServicoDesc.setWrapStyleWord(true);
        txtServicoDesc.setBackground(COR_SECUNDARIA);
        JScrollPane scrollServico = new JScrollPane(txtServicoDesc);
        scrollServico.setBorder(BorderFactory.createEmptyBorder());
        servicoDescPanel.add(scrollServico);

        JPanel tourDescPanel = new JPanel(new BorderLayout());
        tourDescPanel.setBorder(BorderFactory.createTitledBorder("Descrição do Tour"));
        JTextArea txtTourDesc = new JTextArea();
        txtTourDesc.setEditable(false);
        txtTourDesc.setFont(FONTE_TEXTO);
        txtTourDesc.setLineWrap(true);
        txtTourDesc.setWrapStyleWord(true);
        txtTourDesc.setBackground(COR_SECUNDARIA);
        JScrollPane scrollTour = new JScrollPane(txtTourDesc);
        scrollTour.setBorder(BorderFactory.createEmptyBorder());
        tourDescPanel.add(scrollTour);

        descricaoPanel.add(servicoDescPanel);
        descricaoPanel.add(tourDescPanel);

        cbServicoQuarto.addActionListener(e -> {
            HotelEnums.TipoServico servico = (HotelEnums.TipoServico) cbServicoQuarto.getSelectedItem();
            txtServicoDesc.setText(servico != null ? servico.getDescricao() : "");
        });

        cbTour.addActionListener(e -> {
            HotelEnums.TipoTour tour = (HotelEnums.TipoTour) cbTour.getSelectedItem();
            txtTourDesc.setText(tour != null ? tour.getDescricao() : "");
        });

        mainPanel.add(selecaoPanel);
        mainPanel.add(descricaoPanel);

        JPanel rodapePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        rodapePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rodapePanel.setBackground(COR_SECUNDARIA);

        JButton btnCancelar = criarBotao("Cancelar", new Color(204, 0, 0));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnAnterior = criarBotao("Anterior", new Color(153, 153, 153));
        btnAnterior.addActionListener(e -> {
            getContentPane().removeAll();
            selecionarDatas();
        });

        JButton btnProximo = criarBotao("Próximo", COR_PRIMARIA);
        btnProximo.addActionListener(e -> {
            servicoSelecionado = (HotelEnums.TipoServico) cbServicoQuarto.getSelectedItem();
            tourSelecionado = (HotelEnums.TipoTour) cbTour.getSelectedItem();

            getContentPane().removeAll();
            confirmarReserva();
        });

        rodapePanel.add(btnCancelar);
        rodapePanel.add(btnAnterior);
        rodapePanel.add(btnProximo);

        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(rodapePanel, BorderLayout.SOUTH);
        atualizarTela(panel);
    }

    private void confirmarReserva() {
        if (repoReserva.clienteTemReservaAtiva(clienteSelecionado.getCpf())) {
            JOptionPane.showMessageDialog(this,
                    "Este cliente já possui uma reserva ativa.\n" +
                            "Um cliente só pode ter uma reserva ativa por vez.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        Reserva reservaTemp = new Reserva(
                repoReserva.listarReservas().size() + 1,
                clienteSelecionado,
                quartoSelecionado,
                dataCheckIn,
                dataCheckOut
        );

        if (servicoSelecionado != null) {
            reservaTemp.adicionarServicoQuarto(servicoSelecionado);
        }

        if (tourSelecionado != null) {
            reservaTemp.adicionarTour(tourSelecionado);
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_SECUNDARIA);

        panel.add(criarPainelCabecalho("Confirmar Reserva"), BorderLayout.NORTH);

        JPanel corpoPanel = new JPanel(new BorderLayout());
        corpoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        corpoPanel.setBackground(COR_SECUNDARIA);

        JTextArea detalhesReserva = new JTextArea();
        detalhesReserva.setEditable(false);
        detalhesReserva.setFont(new Font("Consolas", Font.PLAIN, 14));
        detalhesReserva.setBackground(COR_SECUNDARIA);
        detalhesReserva.setText(
                "Cliente: " + clienteSelecionado.getNome() + "\n" +
                        "CPF: " + clienteSelecionado.getCpf() + "\n" +
                        "Quarto: " + quartoSelecionado.getNumero() + " (" + quartoSelecionado.getTipo() + ")\n" +
                        "Capacidade: " + quartoSelecionado.getTipo().getCapacidade() + " pessoas\n" +
                        "Check-In: " + dataCheckIn + "\n" +
                        "Check-Out: " + dataCheckOut + "\n" +
                        "Dias: " + dataCheckIn.until(dataCheckOut).getDays() + "\n" +
                        "Serviço de Quarto: " + (servicoSelecionado != null ? servicoSelecionado : "Nenhum") + "\n" +
                        "Tour: " + (tourSelecionado != null ? tourSelecionado : "Nenhum") + "\n" +
                        "Valor Total: R$ " + String.format("%.2f", reservaTemp.getValorTotal()) + "\n\n" +
                        "Pontos disponíveis: " + clienteSelecionado.getPontos()
        );

        JScrollPane scrollPane = new JScrollPane(detalhesReserva);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Detalhes da Reserva"));
        corpoPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel rodapePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        rodapePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rodapePanel.setBackground(COR_SECUNDARIA);

        JButton btnCancelar = criarBotao("Cancelar", new Color(204, 0, 0));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnAnterior = criarBotao("Anterior", new Color(153, 153, 153));
        btnAnterior.addActionListener(e -> {
            getContentPane().removeAll();
            selecionarServicos();
        });

        JButton btnConfirmar = criarBotao("Confirmar e Pagar", COR_PRIMARIA);
        btnConfirmar.addActionListener(e -> {
            getContentPane().removeAll();
            processarPagamento(reservaTemp);
        });

        rodapePanel.add(btnCancelar);
        rodapePanel.add(btnAnterior);
        rodapePanel.add(btnConfirmar);

        panel.add(corpoPanel, BorderLayout.CENTER);
        panel.add(rodapePanel, BorderLayout.SOUTH);

        atualizarTela(panel);
    }

    private void processarPagamento(Reserva reservaTemp) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_SECUNDARIA);

        panel.add(criarPainelCabecalho("Pagamento"), BorderLayout.NORTH);

        JPanel corpoPanel = new JPanel(new GridBagLayout());
        corpoPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        corpoPanel.setBackground(COR_SECUNDARIA);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMetodo = new JLabel("Método de Pagamento:");
        lblMetodo.setFont(FONTE_SUBTITULO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        corpoPanel.add(lblMetodo, gbc);

        JComboBox<HotelEnums.MetodoPagamento> cbMetodo = new JComboBox<>(HotelEnums.MetodoPagamento.values());
        cbMetodo.setFont(FONTE_TEXTO);
        gbc.gridx = 1;
        corpoPanel.add(cbMetodo, gbc);

        JLabel lblParcelas = new JLabel("Parcelas (apenas crédito):");
        lblParcelas.setFont(FONTE_SUBTITULO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        corpoPanel.add(lblParcelas, gbc);

        JSpinner spinnerParcelas = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        spinnerParcelas.setFont(FONTE_TEXTO);
        spinnerParcelas.setEnabled(false);
        gbc.gridx = 1;
        corpoPanel.add(spinnerParcelas, gbc);

        JLabel lblUsarPontos = new JLabel("Usar pontos para desconto:");
        lblUsarPontos.setFont(FONTE_SUBTITULO);
        gbc.gridx = 0;
        gbc.gridy = 2;
        corpoPanel.add(lblUsarPontos, gbc);

        JCheckBox checkUsarPontos = new JCheckBox();
        checkUsarPontos.setEnabled(clienteSelecionado.getPontos() > 0);
        gbc.gridx = 1;
        corpoPanel.add(checkUsarPontos, gbc);

        JLabel lblValorPago = new JLabel("Valor Pago (Dinheiro):");
        lblValorPago.setFont(FONTE_SUBTITULO);
        lblValorPago.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        corpoPanel.add(lblValorPago, gbc);

        JTextField txtValorPago = new JTextField();
        txtValorPago.setFont(FONTE_TEXTO);
        txtValorPago.setVisible(false);
        gbc.gridx = 1;
        corpoPanel.add(txtValorPago, gbc);

        cbMetodo.addActionListener(e -> {
            HotelEnums.MetodoPagamento metodo = (HotelEnums.MetodoPagamento) cbMetodo.getSelectedItem();
            spinnerParcelas.setEnabled(metodo == HotelEnums.MetodoPagamento.CREDITO);

            boolean isDinheiro = metodo == HotelEnums.MetodoPagamento.DINHEIRO;
            lblValorPago.setVisible(isDinheiro);
            txtValorPago.setVisible(isDinheiro);
        });

        JPanel rodapePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        rodapePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rodapePanel.setBackground(COR_SECUNDARIA);

        JButton btnCancelar = criarBotao("Cancelar", new Color(204, 0, 0));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnAnterior = criarBotao("Anterior", new Color(153, 153, 153));
        btnAnterior.addActionListener(e -> {
            getContentPane().removeAll();
            confirmarReserva();
        });

        JButton btnFinalizar = criarBotao("Finalizar Reserva", COR_PRIMARIA);

        btnFinalizar.addActionListener(e -> {
            try {
                HotelEnums.MetodoPagamento metodo = (HotelEnums.MetodoPagamento) cbMetodo.getSelectedItem();
                int parcelas = (int) spinnerParcelas.getValue();
                boolean usarPontos = checkUsarPontos.isSelected();

                double valorFinal = reservaTemp.getValorTotal();
                int pontosUsados = 0;
                double valorPago = 0;
                double troco = 0;

                if (usarPontos) {
                    pontosUsados = Math.min(clienteSelecionado.getPontos(), (int) valorFinal);
                    valorFinal -= pontosUsados;
                }

                if (metodo == HotelEnums.MetodoPagamento.CREDITO) {
                    valorFinal *= (1 + metodo.getJuros());
                }

                if (metodo == HotelEnums.MetodoPagamento.DINHEIRO) {
                    if (txtValorPago.getText().isEmpty()) {
                        throw new IllegalArgumentException("Informe o valor pago em dinheiro");
                    }

                    valorPago = Double.parseDouble(txtValorPago.getText());
                    if (valorPago < valorFinal) {
                        JOptionPane.showMessageDialog(this,
                                "Valor insuficiente!\nFaltam R$ " + String.format("%.2f", valorFinal - valorPago),
                                "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    troco = valorPago - valorFinal;
                }

                Reserva reservaFinal = new Reserva(
                        repoReserva.listarReservas().size() + 1,
                        clienteSelecionado,
                        quartoSelecionado,
                        dataCheckIn,
                        dataCheckOut
                );

                if (servicoSelecionado != null) {
                    reservaFinal.adicionarServicoQuarto(servicoSelecionado);
                }

                if (tourSelecionado != null) {
                    reservaFinal.adicionarTour(tourSelecionado);
                }

                repoQuarto.alterarDisponibilidade(quartoSelecionado.getNumero(), false);
                repoReserva.adicionarReserva(reservaFinal);
                clienteSelecionado.adicionarReserva(reservaFinal);

                int pontosGanhos = (int) (valorFinal / 10);
                if (usarPontos) {
                    pontosGanhos = pontosGanhos / 2;
                }
                clienteSelecionado.adicionarPontos(pontosGanhos);

                gerarRecibo(reservaFinal, metodo, parcelas, valorFinal, pontosUsados, valorPago, troco);

                JOptionPane.showMessageDialog(this,
                        "Reserva criada com sucesso!\nNúmero da reserva: " + reservaFinal.getId(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Valor inválido no campo de pagamento!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
            catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao processar pagamento: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        rodapePanel.add(btnCancelar);
        rodapePanel.add(btnAnterior);
        rodapePanel.add(btnFinalizar);

        panel.add(corpoPanel, BorderLayout.CENTER);
        panel.add(rodapePanel, BorderLayout.SOUTH);
        atualizarTela(panel);
    }

    private void gerarRecibo(Reserva reserva, HotelEnums.MetodoPagamento metodo, int parcelas, double valorFinal, int pontosUsados, double valorPago, double troco) {
        JTextArea recibo = new JTextArea();
        recibo.setEditable(false);
        recibo.setFont(new Font("Consolas", Font.PLAIN, 14));

        int pontosGanhos = (int) (valorFinal / 10);
        if (pontosUsados > 0) {
            pontosGanhos = pontosGanhos / 2;
        }

        String reciboTexto =
                "=== Hotel Vieira Norte ===\n" +
                        "=== Recibo de Reserva ===\n\n" +
                        "ID: " + reserva.getId() + "\n" +
                        "Cliente: " + reserva.getCliente().getNome() + "\n" +
                        "Quarto: " + reserva.getQuarto().getNumero() + " (" + reserva.getQuarto().getTipo() + ")\n" +
                        "Capacidade: " + quartoSelecionado.getTipo().getCapacidade() + " pessoas\n" +
                        "Período: " + reserva.getDataCheckIn() + " a " + reserva.getDataCheckOut() + "\n" +
                        "Dias: " + reserva.getDataCheckIn().until(reserva.getDataCheckOut()).getDays() + "\n\n" +
                        "Serviços:\n" +
                        "• Quarto: " + (reserva.getServicoQuarto() != null ? reserva.getServicoQuarto() : "Nenhum") + "\n" +
                        "• Tour: " + (reserva.getTour() != null ? reserva.getTour() : "Nenhum") + "\n\n" +
                        "Subtotal: R$ " + String.format("%.2f", reserva.getValorTotal()) + "\n" +
                        (pontosUsados > 0 ? "Pontos Utilizados: -R$ " + pontosUsados + "\n" : "") +
                        (metodo == HotelEnums.MetodoPagamento.CREDITO ?
                                "Juros (" + (metodo.getJuros() * 100) + "%): R$ " +
                                        String.format("%.2f", reserva.getValorTotal() * metodo.getJuros()) + "\n" : "") +
                        "Valor Final: R$ " + String.format("%.2f", valorFinal) + "\n";

        if (metodo == HotelEnums.MetodoPagamento.DINHEIRO) {
            reciboTexto +=
                    "\nValor Recebido: R$ " + String.format("%.2f", valorPago) + "\n" +
                            "Troco: R$ " + String.format("%.2f", troco) + "\n";
        }

        reciboTexto +=
                "\nMétodo de Pagamento: " + metodo.getDescricao() +
                        (metodo == HotelEnums.MetodoPagamento.CREDITO ? " (" + parcelas + "x)" : "") + "\n" +
                        "Status: " + reserva.getStatus() + "\n\n" +
                        "Pontos ganhos: " + pontosGanhos + "\n" +
                        "Pontos totais: " + reserva.getCliente().getPontos() + "\n\n" +
                        "Obrigado pela preferência!";

        recibo.setText(reciboTexto);

        JOptionPane.showMessageDialog(this, new JScrollPane(recibo), "Recibo da Reserva", JOptionPane.INFORMATION_MESSAGE);
    }

    private void atualizarTela(JPanel novoPainel) {
        getContentPane().removeAll();
        getContentPane().add(novoPainel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel criarPainelCabecalho(String titulo) {
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(COR_PRIMARIA);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(FONTE_TITULO);
        labelTitulo.setForeground(Color.WHITE);
        cabecalho.add(labelTitulo);

        return cabecalho;
    }

    private JButton criarBotao(String texto, Color corFundo) {
        JButton botao = new JButton(texto);
        botao.setFont(FONTE_TEXTO);
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return botao;
    }
}