/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.upeu.examen.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.upeu.examen.models.Usuario;
import pe.edu.upeu.examen.services.userdao;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/asis/usuario")
public class UsuarioController {
    @Autowired
    private userdao userservice;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> listEntidad() {
        List<Usuario> actDto = userservice.findAll();
        return ResponseEntity.ok().body(actDto);
        // return new ResponseEntity<>(actDto, HttpStatus.OK);
    }

    @PostMapping("/crear")
    public ResponseEntity<Usuario> createPeriodo(@RequestBody Usuario usuario) {
        Usuario data = userservice.save(usuario);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = userservice.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteusuario(@PathVariable Long id) {
        Usuario periodo = userservice.getUsuarioById(id);
        return ResponseEntity.ok(userservice.delete(periodo.getId()));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Usuario> updatePeriodo(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario updatedPeriodo = userservice.update(usuario, id);
        return ResponseEntity.ok(updatedPeriodo);
    }
}
