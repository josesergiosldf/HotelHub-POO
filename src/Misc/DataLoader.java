package Misc;

import Repository.RepositoryQuarto;
import Repository.RepositoryUsuario;
import Users.Admin;
import Hotel.Quarto;

public class DataLoader {
    public static void carregarUsuariosPadrao(RepositoryUsuario repoUsuario) {
        repoUsuario.adicionarUsuario(new Admin("Funcionario", "func123", "00011122233", "funcionario@admin.com"));
        repoUsuario.adicionarUsuario(new Admin("Gerente", "hotel456", "99988877766", "gerente@admin.com"));
    }

    public static void carregarQuartosPadrao(RepositoryQuarto repoQuarto) {
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
