package insn.pe.appmovilsige.common

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import insn.pe.appmovilsige.UsuarioRequest
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

class UsuarioRepository(context: Context) {

    private val dataStore: DataStore<UsuarioRequest> = context.createDataStore(
        "Usuario",
        serializer = Serializer()
    )

    val leerUsuario: Flow<UsuarioRequest> = dataStore.data
        .catch {
            exception ->
            if (exception is IOException){
                Log.d("Error" , exception.message.toString())
                emit(UsuarioRequest.getDefaultInstance())
            }else{
                throw exception
            }
        }

    suspend fun actualizarValor(usuarioRequest: LoginReponse){
        dataStore.updateData { preference->
            preference.toBuilder()
                .setPersonaId(usuarioRequest.personaId)
                .setDni(usuarioRequest.dni)
                .setNombre(usuarioRequest.nombre)
                .setApellido(usuarioRequest.apellido)
                .setFechanac(usuarioRequest.fechanac)
                .setDireccion(usuarioRequest.direccion)
                .setThumbnail(usuarioRequest.thumbnail)
                .setEstado(usuarioRequest.estado)
                .build()
        }
    }

    suspend fun eliminarValor()
    {
        dataStore.updateData { preference->preference.toBuilder().clear().build() }
    }

    fun existenDatos():Boolean{
        if (dataStore!=null){
            return true
        }
        return false
    }
}