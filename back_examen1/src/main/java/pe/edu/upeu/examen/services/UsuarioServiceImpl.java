package pe.edu.upeu.examen.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pe.edu.upeu.examen.exceptions.AppException;
import pe.edu.upeu.examen.exceptions.ResourceNotFoundException;
import pe.edu.upeu.examen.models.Usuario;

import pe.edu.upeu.examen.repositories.UsuarioRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class UsuarioServiceImpl implements userdao {
    @Autowired
    private UsuarioRepository actividadRepo;

    @Override
    public Usuario save(Usuario usuario) {

        try {
            return actividadRepo.save(usuario);
        } catch (Exception e) {
            throw new AppException("Error-" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<Usuario> findAll() {
        try {
            return actividadRepo.findAll();
        } catch (Exception e) {
            throw new AppException("Error-" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Map<String, Boolean> delete(Long id) {
        Usuario actividadx = actividadRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not exist with id :" + id));

        actividadRepo.delete(actividadx);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);

        return response;
    }

    @Override
    public Usuario getUsuarioById(Long id) {
        Usuario findActividad = actividadRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));
        return findActividad;
    }

    @Override
    public Usuario update(Usuario usuario, Long id) {
        Usuario actividadx = actividadRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not exist with id :" + id));
        actividadx.setEstado(usuario.getEstado());
        return actividadRepo.save(actividadx);
    }
}
