package insn.pe.appmovilsige.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import insn.pe.appmovilsige.UsuarioRequest
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository= UsuarioRepository(application)
    val usuarioLogged=repository.leerUsuario.asLiveData()

    fun actualizarValor(usuarioLogged:LoginReponse) = viewModelScope.launch(Dispatchers.IO){
        repository.actualizarValor(usuarioLogged)
    }

    fun existenDatos() : Boolean {
        return repository.existenDatos()
    }

    fun limpiarDatos()=viewModelScope.launch(Dispatchers.IO) {
        repository.eliminarValor()
    }
}