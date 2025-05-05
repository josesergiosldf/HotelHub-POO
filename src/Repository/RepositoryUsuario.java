package Repository;

import Users.Usuario;
import Gerenciador.GerenciadorUsuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryUsuario implements GerenciadorUsuario {
    private final Map<String, Usuario> usuariosPorCpf = new HashMap<>();

    @Override
    public void adicionarUsuario(Usuario usuario) {
        usuariosPorCpf.put(usuario.getCpf(), usuario);
    }

    @Override
    public Usuario buscarUsuarioPorCpf(String cpf) {
        return usuariosPorCpf.get(cpf);
    }

    @Override
    public boolean removerUsuario(String cpf) {
        return usuariosPorCpf.remove(cpf) != null;
    }

    @Override
    public void atualizarUsuario(String cpf, String novaSenha) {
        Usuario usuario = usuariosPorCpf.get(cpf);

        if (usuario != null) {
            usuario.setSenha(novaSenha);
        }
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuariosPorCpf.values());
    }
}
