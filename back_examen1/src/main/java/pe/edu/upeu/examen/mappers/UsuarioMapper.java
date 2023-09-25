package pe.edu.upeu.examen.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.examen.dtos.UsuarioCrearDto;
import pe.edu.upeu.examen.dtos.UsuarioDto;
import pe.edu.upeu.examen.models.Usuario;



@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDto toUserDto(Usuario user);

    @Mapping(target = "password", ignore = true)
    Usuario usuarioCrearDtoToUser(UsuarioCrearDto usuarioCrearDto);

}
