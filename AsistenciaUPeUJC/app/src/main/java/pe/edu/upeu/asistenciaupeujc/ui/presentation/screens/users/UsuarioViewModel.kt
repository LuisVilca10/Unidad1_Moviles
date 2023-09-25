package pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.users

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import pe.edu.upeu.asistenciaupeujc.modelo.Usuario

import pe.edu.upeu.asistenciaupeujc.repository.UsuarioRepository
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuRepo: UsuarioRepository,
) : ViewModel(){
    private val _isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val usua: LiveData<List<Usuario>> by lazy {
        usuRepo.reportarUsuarios()
    }
    val isLoading: LiveData<Boolean> get() = _isLoading
    fun addUsuario() {
        if (_isLoading.value == false)
            viewModelScope.launch (Dispatchers.IO) {
                _isLoading.postValue(true)
            }
    }

    fun deleteUsuario(toDelete: Usuario) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("ELIMINAR", toDelete.toString())
            usuRepo.deleteUsuario(toDelete);
        }
    }

}