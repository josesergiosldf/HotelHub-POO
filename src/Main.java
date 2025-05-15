import Misc.DataLoader;
import Repository.*;
import UI.TelaLogin;

public class Main {
    public static void main(String[] args) {
        RepositoryUsuario repoUsuario = new RepositoryUsuario();
        RepositoryReserva repoReserva = new RepositoryReserva();
        RepositoryQuarto repoQuarto = new RepositoryQuarto();

        DataLoader.carregarUsuariosPadrao(repoUsuario);
        DataLoader.carregarQuartosPadrao(repoQuarto);

        new TelaLogin(repoUsuario, repoReserva, repoQuarto).setVisible(true);
    }
}