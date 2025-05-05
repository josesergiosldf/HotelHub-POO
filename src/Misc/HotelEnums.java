package Misc;

public class HotelEnums {
    public enum TipoServico {
        REGULAR(30.0),
        PREMIUM(60.0),
        LUXO(100.0);

        private final double valor;

        TipoServico(double valor) {
            this.valor = valor;
        }

        public double getValor() {
            return valor;
        }
    }

    public enum TipoTour {
        CIDADE(120.0),
        VINICOLA(250.0),
        AVENTURA(180.0);

        private final double valor;

        TipoTour(double valor) {
            this.valor = valor;
        }

        public double getValor() {
            return valor;
        }
    }

    public enum TipoQuarto {
        REGULAR(100.0, 2),
        PREMIUM(200.0, 3),
        LUXO(300.0, 4);

        private final double valorDiaria;
        private final int capacidade;

        TipoQuarto(double valorDiaria, int capacidade) {
            this.valorDiaria = valorDiaria;
            this.capacidade = capacidade;
        }

        public double getValorDiaria() {
            return valorDiaria;
        }

        public int getCapacidade() {
            return capacidade;
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
