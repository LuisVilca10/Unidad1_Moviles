package pe.edu.upeu.examen.services;

import java.util.List;
import java.util.Map;

import pe.edu.upeu.examen.models.Usuario;

public interface userdao {
    Usuario save(Usuario usuario);

    List<Usuario> findAll();

    Map<String, Boolean> delete(Long id);

    Usuario getUsuarioById(Long id);

    Usuario update(Usuario usuario, Long id);
}
