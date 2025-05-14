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

public class TelaCriarReservaPromocional extends JFrame {
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
    private double percentualDesconto;

    private final Color COR_PRIMARIA = new Color(255, 165, 0); // Laranja para diferenciar
    private final Color COR_SECUNDARIA = new Color(255, 255, 255);
    private final Font FONTE_TITULO = new Font("Arial", Font.BOLD, 24);
    private final Font FONTE_SUBTITULO = new Font("Arial", Font.PLAIN, 16);
    private final Font FONTE_TEXTO = new Font("Arial", Font.PLAIN, 14);

    public TelaCriarReservaPromocional(Admin admin, RepositoryUsuario repoUsuario,
                                       RepositoryReserva repoReserva, RepositoryQuarto repoQuarto) {
        this.admin = admin;
        this.repoUsuario = repoUsuario;
        this.repoReserva = repoReserva;
        this.repoQuarto = repoQuarto;

        setTitle("Criar Reserva Promocional");
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

        String[] colunas = {"Número", "Tipo", "Diária", "Descrição"};
        Object[][] dados = new Object[quartosDisponiveis.size()][colunas.length];

        for (int i = 0; i < quartosDisponiveis.size(); i++) {
            Quarto q = quartosDisponiveis.get(i);
            dados[i][0] = q.getNumero();
            dados[i][1] = q.getTipo();
            dados[i][2] = "R$ " + String.format("%.2f", q.getPrecoDiaria());
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

        JPanel corpoPanel = new JPanel(new GridBagLayout());
        corpoPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        corpoPanel.setBackground(COR_SECUNDARIA);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblServicoQuarto = new JLabel("Serviço de Quarto:");
        lblServicoQuarto.setFont(FONTE_SUBTITULO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        corpoPanel.add(lblServicoQuarto, gbc);

        JComboBox<HotelEnums.TipoServico> cbServicoQuarto = new JComboBox<>(HotelEnums.TipoServico.values());
        cbServicoQuarto.setFont(FONTE_TEXTO);
        cbServicoQuarto.setSelectedItem(null);
        gbc.gridx = 1;
        corpoPanel.add(cbServicoQuarto, gbc);

        JLabel lblTour = new JLabel("Tour:");
        lblTour.setFont(FONTE_SUBTITULO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        corpoPanel.add(lblTour, gbc);

        JComboBox<HotelEnums.TipoTour> cbTour = new JComboBox<>(HotelEnums.TipoTour.values());
        cbTour.setFont(FONTE_TEXTO);
        cbTour.setSelectedItem(null);
        gbc.gridx = 1;
        corpoPanel.add(cbTour, gbc);

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
            selecionarDesconto();
        });

        rodapePanel.add(btnCancelar);
        rodapePanel.add(btnAnterior);
        rodapePanel.add(btnProximo);

        panel.add(corpoPanel, BorderLayout.CENTER);
        panel.add(rodapePanel, BorderLayout.SOUTH);

        atualizarTela(panel);
    }

    private void selecionarDesconto() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_SECUNDARIA);

        panel.add(criarPainelCabecalho("Aplicar Desconto Promocional"), BorderLayout.NORTH);

        JPanel corpoPanel = new JPanel(new GridBagLayout());
        corpoPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        corpoPanel.setBackground(COR_SECUNDARIA);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblDesconto = new JLabel("Percentual de Desconto (%):");
        lblDesconto.setFont(FONTE_SUBTITULO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        corpoPanel.add(lblDesconto, gbc);

        JSpinner spinnerDesconto = new JSpinner(new SpinnerNumberModel(10, 0, 100, 5));
        spinnerDesconto.setFont(FONTE_TEXTO);
        gbc.gridx = 1;
        corpoPanel.add(spinnerDesconto, gbc);

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

        JButton btnProximo = criarBotao("Próximo", COR_PRIMARIA);
        btnProximo.addActionListener(e -> {
            percentualDesconto = (Integer) spinnerDesconto.getValue() / 100.0;
            getContentPane().removeAll();
            confirmarReserva();
        });

        rodapePanel.add(btnCancelar);
        rodapePanel.add(btnAnterior);
        rodapePanel.add(btnProximo);

        panel.add(corpoPanel, BorderLayout.CENTER);
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
                        "Check-In: " + dataCheckIn + "\n" +
                        "Check-Out: " + dataCheckOut + "\n" +
                        "Dias: " + dataCheckIn.until(dataCheckOut).getDays() + "\n" +
                        "Serviço de Quarto: " + (servicoSelecionado != null ? servicoSelecionado : "Nenhum") + "\n" +
                        "Tour: " + (tourSelecionado != null ? tourSelecionado : "Nenhum") + "\n" +
                        "Valor Original: R$ " + String.format("%.2f", reservaTemp.getValorTotal() / (1 - percentualDesconto)) + "\n" +
                        "Desconto Promocional: " + (percentualDesconto * 100) + "%\n" +
                        "Valor com Desconto: R$ " + String.format("%.2f", reservaTemp.getValorTotal()) + "\n" +
                        "Pontos disponíveis: " + clienteSelecionado.getPontos() + "\n" +
                        "⚠️ Reserva promocional não acumula pontos ⚠️"
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

        cbMetodo.addActionListener(e -> {
            spinnerParcelas.setEnabled(cbMetodo.getSelectedItem() == HotelEnums.MetodoPagamento.CREDITO);
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
                reservaTemp.aplicarDesconto(percentualDesconto);
                int parcelas = (int) spinnerParcelas.getValue();
                boolean usarPontos = checkUsarPontos.isSelected();

                double valorFinal = reservaTemp.getValorTotal();
                int pontosUsados = 0;

                if (usarPontos) {
                    pontosUsados = Math.min(clienteSelecionado.getPontos(), (int) valorFinal);
                    valorFinal -= pontosUsados;
                }

                if (metodo == HotelEnums.MetodoPagamento.CREDITO) {
                    valorFinal *= (1 + metodo.getJuros());
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

                reservaFinal.aplicarDesconto(percentualDesconto);

                repoQuarto.alterarDisponibilidade(quartoSelecionado.getNumero(), false);

                repoReserva.adicionarReserva(reservaFinal);
                clienteSelecionado.adicionarReserva(reservaFinal);

                gerarRecibo(reservaFinal, metodo, parcelas, valorFinal, pontosUsados);

                JOptionPane.showMessageDialog(this,
                        "Reserva criada com sucesso!\nNúmero da reserva: " + reservaFinal.getId(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
            catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Erro ao criar reserva", JOptionPane.ERROR_MESSAGE);
            }
        });

        rodapePanel.add(btnCancelar);
        rodapePanel.add(btnAnterior);
        rodapePanel.add(btnFinalizar);

        panel.add(corpoPanel, BorderLayout.CENTER);
        panel.add(rodapePanel, BorderLayout.SOUTH);

        atualizarTela(panel);
    }

    private void gerarRecibo(Reserva reserva, HotelEnums.MetodoPagamento metodo, int parcelas, double valorFinal, int pontosUsados) {
        JTextArea recibo = new JTextArea();
        recibo.setEditable(false);
        recibo.setFont(new Font("Consolas", Font.PLAIN, 14));

        double valorOriginal = reserva.getValorTotal() / (1 - percentualDesconto);

        recibo.setText(
                "=== Hotel Vieira Norte ===\n" +
                        "=== Recibo de Reserva Promocional ===\n\n" +
                        "ID: " + reserva.getId() + "\n" +
                        "Cliente: " + reserva.getCliente().getNome() + "\n" +
                        "Quarto: " + reserva.getQuarto().getNumero() + " (" + reserva.getQuarto().getTipo() + ")\n" +
                        "Período: " + reserva.getDataCheckIn() + " a " + reserva.getDataCheckOut() + "\n" +
                        "Dias: " + reserva.getDataCheckIn().until(reserva.getDataCheckOut()).getDays() + "\n\n" +
                        "Serviços:\n" +
                        "• Quarto: " + (reserva.getServicoQuarto() != null ? reserva.getServicoQuarto() : "Nenhum") + "\n" +
                        "• Tour: " + (reserva.getTour() != null ? reserva.getTour() : "Nenhum") + "\n\n" +
                        "Valor Original: R$ " + String.format("%.2f", valorOriginal) + "\n" +
                        "Desconto: " + (percentualDesconto * 100) + "%\n" +
                        "Subtotal: R$ " + String.format("%.2f", reserva.getValorTotal()) + "\n" +
                        (pontosUsados > 0 ? "Pontos Utilizados: -R$ " + pontosUsados + "\n" : "") +
                        (metodo == HotelEnums.MetodoPagamento.CREDITO ?
                                "Juros (" + (metodo.getJuros() * 100) + "%): R$ " +
                                        String.format("%.2f", reserva.getValorTotal() * metodo.getJuros()) + "\n" : "") +

                        "Total Final: R$ " + String.format("%.2f", valorFinal) + "\n\n" +
                        "Pagamento: " + metodo.getDescricao() +
                        (metodo == HotelEnums.MetodoPagamento.CREDITO ? " (" + parcelas + "x)" : "") + "\n" +
                        "Status: " + reserva.getStatus() + "\n\n" +
                        "⚠️ Reserva promocional não acumula pontos ⚠️\n" +
                        "Obrigado pela preferência!"
        );

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