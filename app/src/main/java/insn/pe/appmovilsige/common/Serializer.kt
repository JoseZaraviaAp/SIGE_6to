package insn.pe.appmovilsige.common

import androidx.datastore.CorruptionException
import androidx.datastore.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import insn.pe.appmovilsige.UsuarioRequest
import java.io.InputStream
import java.io.OutputStream

class Serializer:Serializer<UsuarioRequest> {
    override fun readFrom(input: InputStream): UsuarioRequest {
        try {
            return UsuarioRequest.parseFrom(input)
        }catch (exception: InvalidProtocolBufferException){
            throw CorruptionException("NO se pudo leer Usuario o no se almacenó",exception)
        }
    }
    override fun writeTo(t: UsuarioRequest, output: OutputStream) {
        t.writeTo(output)
    }
}