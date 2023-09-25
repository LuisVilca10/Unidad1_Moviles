package pe.edu.upeu.asistenciaupeujc.data.remote

import pe.edu.upeu.asistenciaupeujc.modelo.Actividad
import pe.edu.upeu.asistenciaupeujc.modelo.MsgGeneric
import pe.edu.upeu.asistenciaupeujc.modelo.Usuario
import pe.edu.upeu.asistenciaupeujc.modelo.UsuarioDto
import pe.edu.upeu.asistenciaupeujc.modelo.UsuarioResp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestUsuario {
    @POST("/asis/login")
    suspend fun login(@Body user:UsuarioDto):Response<UsuarioResp>

    @GET("/asis/usuario/list")
    suspend fun reportarUsuarios(@Header("Authorization") token:String):Response<List<Usuario>>

    @GET("/asis/usuario/buscar/{id}")
    suspend fun getUsuarioId(@Header("Authorization") token:String, @Query("id") id:Long):Response<Usuario>

    @DELETE("/asis/usuario/eliminar/{id}")
    suspend fun deleteUsuario(@Header("Authorization") token:String, @Path("id") id:Long):Response<Usuario>

    @PUT("/asis/usuario/editar/{id}")
    suspend fun actualizarUsuario(@Header("Authorization") token:String, @Path("id") id:Long, @Body usuario: Usuario): Response<Usuario>

    @POST("/asis/usuario/crear")
    suspend fun insertarUsuario(@Header("Authorization") token:String, @Body usuario: Usuario): Response<Usuario>
}