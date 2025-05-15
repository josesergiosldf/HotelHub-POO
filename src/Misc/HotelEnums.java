package Misc;

public class HotelEnums {
    public enum TipoServico {
        REGULAR(30.0,
                "Serviço de quarto essencial, perfeito para quem busca conforto sem luxo. " +
                        "Inclui: limpeza diária, reposição de amenities, toalhas frescas e cama arrumada. " +
                        "Ideal para estadias rápidas e práticas."),

        PREMIUM(60.0,
                "Experiência premium com mimos extras para tornar sua estadia ainda mais especial. " +
                        "Inclui: serviço de quarto 24h, chocolates finos na chegada, roupão de algodão egípcio " +
                        "e seleção de travesseiros para melhor descanso."),

        LUXO(100.0,
                "Tratamento VIP com atenção aos mínimos detalhes. " +
                        "Inclui: mordomo pessoal, decoração personalizada no quarto, espumante de boas-vindas, " +
                        "frutas frescas diárias e menu de travesseiros de alta linha.");

        private final double valor;
        private final String descricao;

        TipoServico(double valor, String descricao) {
            this.valor = valor;
            this.descricao = descricao;
        }

        public double getValor() {
            return valor;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public enum TipoTour {
        CIDADE(120.0,
                "Descubra os segredos da cidade com nosso tour exclusivo! " +
                        "Visite pontos icônicos, mercados locais e tenha degustações gastronômicas. " +
                        "Guia especializado e transporte privativo incluso."),

        VINICOLA(250.0,
                "Uma jornada pelos melhores vinhedos da região, com direito a degustação de rótulos premiados " +
                        "e um almoço gourmet harmonizado. Inclui transporte de luxo e fotos profissionais."),

        AVENTURA(180.0,
                "Para os aventureiros de plantão: trilhas emocionantes, passeios de buggy ou rafting em águas cristalinas. " +
                        "Equipamento de segurança incluso e guias experientes para garantir diversão sem riscos.");

        private final double valor;
        private final String descricao;

        TipoTour(double valor, String descricao) {
            this.valor = valor;
            this.descricao = descricao;
        }

        public double getValor() {
            return valor;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public enum TipoQuarto {
        REGULAR(100.0, 2, "/Imagens/quarto_regular.png"),
        PREMIUM(200.0, 3, "/Imagens/quarto_premium.png"),
        LUXO(300.0, 4, "/Imagens/quarto_luxo.png");

        private final double valorDiaria;
        private final int capacidade;
        private final String imagePath;

        TipoQuarto(double valorDiaria, int capacidade, String imagePath) {
            this.valorDiaria = valorDiaria;
            this.capacidade = capacidade;
            this.imagePath = imagePath;
        }

        public double getValorDiaria() {
            return valorDiaria;
        }

        public int getCapacidade() {
            return capacidade;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    public enum MetodoPagamento {
        PIX("PIX", 0.0),
        CREDITO("Cartão de Crédito", 0.05),
        DEBITO("Cartão de Débito", 0.0),
        DINHEIRO("Dinheiro", 0.0);

        private final String descricao;
        private final double juros;

        MetodoPagamento(String descricao, double juros) {
            this.descricao = descricao;
            this.juros = juros;
        }

        public String getDescricao() {
            return descricao;
        }

        public double getJuros() {
            return juros;
        }
    }
}
