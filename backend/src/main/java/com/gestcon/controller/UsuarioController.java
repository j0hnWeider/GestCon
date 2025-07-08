package com.gestcon.controller;

import com.gestcon.model.Usuario;
import com.gestcon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para gerenciamento de usuários.
 * Herda operações CRUD básicas da classe BaseController.
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController extends BaseController<Usuario, Long> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected JpaRepository<Usuario, Long> getRepository() {
        return usuarioRepository;
    }

    @Override
    protected void updateEntity(Usuario existingUsuario, Usuario usuarioDetails) {
        existingUsuario.setNome(usuarioDetails.getNome());
        existingUsuario.setEmail(usuarioDetails.getEmail());
        existingUsuario.setSenha(usuarioDetails.getSenha());
        existingUsuario.setPapel(usuarioDetails.getPapel());
    }
}
