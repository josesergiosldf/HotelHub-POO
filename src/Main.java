import Hotel.Quarto;
import Misc.HotelEnums;
import Repository.*;
import UI.TelaLogin;
import Users.Admin;

public class Main {
    public static void main(String[] args) {
        RepositoryUsuario repoUsuario = new RepositoryUsuario();
        RepositoryReserva repoReserva = new RepositoryReserva();
        RepositoryQuarto repoQuarto = new RepositoryQuarto();

        repoUsuario.adicionarUsuario(new Admin("Funcionario", "func123", "00011122233", "funcionario@admin.com"));
        repoUsuario.adicionarUsuario(new Admin("Gerente", "hotel456", "99988877766", "gerente@admin.com"));

        adicionarQuartosNoRepository(repoQuarto);

        new TelaLogin(repoUsuario, repoReserva, repoQuarto).setVisible(true);
    }

    private static void adicionarQuartosNoRepository(RepositoryQuarto repoQuarto) {
        for (int i = 100; i <= 110; i++) {
            Quarto quarto = new Quarto(i, HotelEnums.TipoQuarto.REGULAR);
            quarto.setDisponivel(true);
            repoQuarto.adicionarQuarto(quarto);
        }

        for (int i = 200; i <= 210; i++) {
            Quarto quarto = new Quarto(i, HotelEnums.TipoQuarto.PREMIUM);
            quarto.setDisponivel(true);
            repoQuarto.adicionarQuarto(quarto);
        }

        for (int i = 300; i <= 310; i++) {
            Quarto quarto = new Quarto(i, HotelEnums.TipoQuarto.LUXO);
            quarto.setDisponivel(true);
            repoQuarto.adicionarQuarto(quarto);
        }
    }
}