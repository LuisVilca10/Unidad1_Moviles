package pe.edu.upeu.asistenciaupeujc.repository

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.edu.upeu.asistenciaupeujc.data.local.dao.UsuarioDao
import pe.edu.upeu.asistenciaupeujc.data.remote.RestUsuario
import pe.edu.upeu.asistenciaupeujc.modelo.Usuario
import pe.edu.upeu.asistenciaupeujc.modelo.UsuarioDto
import pe.edu.upeu.asistenciaupeujc.modelo.UsuarioResp
import pe.edu.upeu.asistenciaupeujc.utils.TokenUtils
import retrofit2.Response
import javax.inject.Inject

interface UsuarioRepository {
    suspend fun loginUsuario(user:UsuarioDto): Response<UsuarioResp>
    suspend fun  deleteUsuario(usuario: Usuario)
    fun reportarUsuarios(): LiveData<List<Usuario>>

    fun buscarUsuarioId(id:Long): LiveData<Usuario>

    suspend fun insertarUsuario(usuario: Usuario):Boolean

    suspend fun modificarRemoteUsuario(usuario: Usuario):Boolean
}

class UsuarioRepositoryImp @Inject constructor(
    private val restUsuario: RestUsuario,
    private val usuarioDao: UsuarioDao):UsuarioRepository{
    override suspend fun loginUsuario(user:UsuarioDto): Response<UsuarioResp>{
        return restUsuario.login(user)
    }

    override suspend fun deleteUsuario(usuario: Usuario) {
        CoroutineScope(Dispatchers.IO).launch {
            restUsuario.deleteUsuario(TokenUtils.TOKEN_CONTENT,usuario.id)
        }
        usuarioDao.eliminarUsuario(usuario)
    }

    override fun reportarUsuarios(): LiveData<List<Usuario>> {
        try {
            CoroutineScope(Dispatchers.IO).launch{
                delay(3000)
                val data=restUsuario.reportarUsuarios(TokenUtils.TOKEN_CONTENT).body()!!
                usuarioDao.insertarUsuarios(data)
            }
        }catch (e:Exception){
            Log.i("ERROR", "Error: ${e.message}")
        }
        return usuarioDao.reportatUsuario()
    }

    override fun buscarUsuarioId(id: Long): LiveData<Usuario> {
        return  usuarioDao.buscarUsuario(id)
    }

    override suspend fun insertarUsuario(usuario: Usuario): Boolean {
        return restUsuario.insertarUsuario(TokenUtils.TOKEN_CONTENT, usuario).body()!=null
    }

    override suspend fun modificarRemoteUsuario(usuario: Usuario): Boolean {
        var dd:Boolean=false
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("VER", TokenUtils.TOKEN_CONTENT)
        }
        return restUsuario.actualizarUsuario(TokenUtils.TOKEN_CONTENT, usuario.id, usuario).body()!=null
    }

}