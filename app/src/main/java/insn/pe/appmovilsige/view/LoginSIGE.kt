package insn.pe.appmovilsige.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.internal.Utility
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import insn.pe.appmovilsige.HomeActivity
import insn.pe.appmovilsige.ProviderType
import insn.pe.appmovilsige.R
import insn.pe.appmovilsige.UsuarioRequest
import insn.pe.appmovilsige.common.MainViewModel
import insn.pe.appmovilsige.databinding.ActivityLoginSIGEBinding
import insn.pe.appmovilsige.retrofit.RetrofitCliente
import insn.pe.appmovilsige.retrofit.request.LoginRequest
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class loginSIGE : AppCompatActivity() {
    private val GOOGLE_SIGN_IN=100
    private val callbackManager= CallbackManager.Factory.create()

    private var personaId:Int = 0


    private lateinit var binding: ActivityLoginSIGEBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginSIGEBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session()
        //gmail
        binding.ivgmailsignin.setOnClickListener {
            val googleConf=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient= GoogleSignIn.getClient(this,googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
        }

        //facebook
        binding.ivfacebooksignin.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult>{
                    override fun onSuccess(result: LoginResult?) {
                        result?.let {
                            val token= it.accessToken
                            val credencial = FacebookAuthProvider.getCredential(token.token)
                            FirebaseAuth.getInstance().signInWithCredential(credencial)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        verificarExistencia(it.result?.user?.email?:"")
                                    } else {
                                        mostrarPantallaError()
                                    }
                                }
                        }
                    }
                    override fun onCancel() {
                    }
                    override fun onError(error: FacebookException?) {
                    }
                })
        }

        binding.btnsignup.setOnClickListener{
            startActivity(Intent(applicationContext,RegistroUsersActivity::class.java))
        }

        binding.btningresar.setOnClickListener {
            if(binding.etusuario.text.toString()!=""
                && Patterns.EMAIL_ADDRESS.matcher(binding.etusuario.text.toString()).matches()) {
                    if (binding.etcontra.text.toString()!="") {
                        val loginRequest = LoginRequest(
                            binding.etusuario.text.toString(),
                            binding.etcontra.text.toString()
                        )
                        val call: Call<List<LoginReponse>> =
                            RetrofitCliente.retrofitService.autenticacion(loginRequest)
                        call.enqueue(object : Callback<List<LoginReponse>> {
                            override fun onResponse(
                                call: Call<List<LoginReponse>>,
                                response: Response<List<LoginReponse>>
                            ) {
                                if (response.isSuccessful) {
                                    val respuesta = response.body()!!
                                    var usuarioLogged: LoginReponse = respuesta.get(0)

                                    if (respuesta.size != 0) {
                                        personaId = usuarioLogged.personaId
                                        irActivityHome(usuarioLogged.personaId)

                                    } else {
                                        mostrarPantallaError()
                                    }
                                } else {
                                    mostrarPantallaError()
                                }
                            }

                            override fun onFailure(call: Call<List<LoginReponse>>, t: Throwable) {
                                TODO("Not yet implemented")
                            }

                        })
                    }else{
                        binding.etcontra.setError("Ingrese contraseña!")
                    }
            }else{
                binding.etusuario.setError("Ingrese un mail válido")
            }
        }

    }

    //implementar validacio onStart()
    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val personaId=prefs.getString("personaId",null)
        if (personaId!=null){
            irActivityHome(personaId.toInt())
        }
    }
    private fun irActivityHome(personaId :Int){
        var intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("personaId",personaId)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode,resultCode,data)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==GOOGLE_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val cuenta = task.getResult(ApiException::class.java)
                if (cuenta != null) {
                    val credencial = GoogleAuthProvider.getCredential(cuenta.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credencial)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                verificarExistencia(cuenta.email ?: "")
                                //check this
                                //irActivityHome(cuenta.email ?: "", ProviderType.GOOGLE.name)
                            } else {
                                mostrarPantallaError()
                            }
                        }
                }
            }catch (e: ApiException){
                mostrarPantallaError()
            }
        }
    }



    private fun mostrarPantallaError(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error de autenticación")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }


    private fun verificarExistencia(email: String){

        val emailRequest: String = email
        val call: Call<List<LoginReponse>> =
            RetrofitCliente.retrofitService.buscarxEmail(emailRequest)
        call.enqueue( object : Callback<List<LoginReponse>>{
            override fun onResponse(call: Call<List<LoginReponse>>,response: Response<List<LoginReponse>>) {
                if (response.isSuccessful){
                    val respuesta=response.body()!!
                        if (respuesta.size!=0) {
                            var usuarioLogged: LoginReponse =respuesta.get(0)
                            irActivityHome(usuarioLogged.personaId)
                        } else {
                            irPantallaRegistro(email)
                        }

                }else{
                    irPantallaRegistro(email)
                }
            }

            override fun onFailure(call: Call<List<LoginReponse>>, t: Throwable) {
                mostrarPantallaError()
                t.printStackTrace()
                Toast.makeText(applicationContext,""+t.printStackTrace(),Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun irPantallaRegistro(email: String) {
        val intent=Intent(this,RegistroUsersActivity::class.java)
        intent.putExtra("email",email)
        startActivity(intent)
    }
}