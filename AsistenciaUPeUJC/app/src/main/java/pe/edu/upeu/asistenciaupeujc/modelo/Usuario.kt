package pe.edu.upeu.asistenciaupeujc.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var nombres: String,
    var apellidos: String,
    var correo: String,
    var password: String,
    @ColumnInfo(name = "token")
    var token: String? = null,
    var dni: String,
    var perfilPrin: String,
    var estado: String,
    var offlinex: String,
)

data class UsuarioDto(
    var correo: String,
    var password: String,
)

data class UsuarioResp(
    val id: Long,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val token: String,
    val dni: String,
    val perfilPrin: String,
    val estado: String,
    val offlinex: String,
)
