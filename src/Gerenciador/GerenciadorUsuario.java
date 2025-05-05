package Gerenciador;

import Users.Usuario;
import java.util.List;

public interface GerenciadorUsuario {
    void adicionarUsuario(Usuario usuario);
    Usuario buscarUsuarioPorCpf(String cpf);
    boolean removerUsuario(String cpf);
    void atualizarUsuario(String cpf, String novaSenha);
    List<Usuario> listarUsuarios();
}
