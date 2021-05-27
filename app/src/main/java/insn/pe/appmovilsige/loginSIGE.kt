package insn.pe.appmovilsige

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import insn.pe.appmovilsige.databinding.ActivityLoginSIGEBinding
import java.security.Provider
import javax.security.auth.callback.Callback

class loginSIGE : AppCompatActivity() {
    private val GOOGLE_SIGN_IN=100
    private val callbackManager= CallbackManager.Factory.create()

    private lateinit var binding: ActivityLoginSIGEBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginSIGEBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                                        irActivityHome(it.result?.user?.email?:"", ProviderType.FACEBOOK.name)
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


    }


    //implementar validacio onStart()
    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email=prefs.getString("email",null)
        val provider=prefs.getString("provider",null)

        if (email!=null && provider !=null){
            irActivityHome(email,provider)
        }

    }
    private fun irActivityHome(email :String, provider: String){
        var intent = Intent(this,HomeActivity::class.java)
        intent.putExtra("email",email)
        intent.putExtra("provider",provider)
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
                                irActivityHome(cuenta.email ?: "", ProviderType.GOOGLE.name)
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
}