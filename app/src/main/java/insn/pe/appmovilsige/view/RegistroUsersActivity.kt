package insn.pe.appmovilsige.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import insn.pe.appmovilsige.HomeActivity
import insn.pe.appmovilsige.databinding.ActivityRegistroUsersBinding
import insn.pe.appmovilsige.retrofit.RetrofitCliente
import insn.pe.appmovilsige.retrofit.request.LoginRequest
import insn.pe.appmovilsige.retrofit.request.RegistroRequest
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import insn.pe.appmovilsige.retrofit.response.RegistroPaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class RegistroUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroUsersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegistroUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val emailExtra=intent.getStringExtra("email")
        if (emailExtra!=null){
            binding.etemail.setText(emailExtra)
        }

        binding.etfechanac.addTextChangedListener (object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (start==3)
                {
                    val texto=binding.etfechanac.text.toString()
                    binding.etfechanac.setText("$texto-")
                    binding.etfechanac.requestFocus()
                    binding.etfechanac.setSelection(binding.etfechanac.text!!.length)
                }
                if (start==6)
                {
                    val texto=binding.etfechanac.text.toString()
                    binding.etfechanac.setText("$texto-")
                    binding.etfechanac.requestFocus()
                    binding.etfechanac.setSelection(binding.etfechanac.text!!.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.btnregresar.setOnClickListener {
            startActivity(Intent(applicationContext,loginSIGE::class.java))
        }
        binding.btnregistraruser.setOnClickListener {

            val patronEmail= Patterns.EMAIL_ADDRESS;

            if (binding.etdni.text.toString()!=""&&
            binding.etnombre.text.toString()!=""&&
            binding.etapellido.text.toString()!=""&&
            binding.etfechanac.text.toString()!=""&&
            binding.etdireccion.text.toString()!=""&&
            binding.ettiposangre.text.toString()!=""&&
            binding.etpeso.text.toString()!=""&&
            binding.ettalla.text.toString()!=""&&
            binding.etnumero.text.toString()!=""&&
            binding.etalergias.text.toString()!=""&&
            binding.etemail.text.toString()!=""&&
            binding.etemail.text.toString()!=""&&
            binding.etpassword.text.toString()!="") {

                if (patronEmail.matcher(binding.etemail.text.toString()).matches()) {

                    val RegistroRequest = RegistroRequest(
                        binding.etdni.text.toString(),
                        binding.etnombre.text.toString(),
                        binding.etapellido.text.toString(),
                        binding.etfechanac.text.toString(),
                        binding.etdireccion.text.toString(),
                        "no image", "1",
                        binding.ettiposangre.text.toString(),
                        binding.etpeso.text.toString(),
                        binding.ettalla.text.toString(),
                        binding.etnumero.text.toString(),
                        binding.etalergias.text.toString(),
                        binding.etemail.text.toString(),
                        binding.etemail.text.toString(),
                        binding.etpassword.text.toString()
                    )

                    val call: Call<List<RegistroPaResponse>> =
                        RetrofitCliente.retrofitService.registroPaciente(RegistroRequest)
                    call.enqueue(object : Callback<List<RegistroPaResponse>> {
                        override fun onResponse(
                            call: Call<List<RegistroPaResponse>>,
                            response: Response<List<RegistroPaResponse>>
                        ) {
                            if (response.isSuccessful) {
                                val respuesta = response.body()!!
                                if (respuesta.size != 0) {
                                    irActivityHome(RegistroRequest.email)
                                } else {
                                    mostrarPantallaError("Ha ocurrido un error en el registro. Es posible que ya esté registrado")
                                }
                            } else {
                                mostrarPantallaError("Ha ocurrido un error en el registro.")
                            }
                        }

                        override fun onFailure(call: Call<List<RegistroPaResponse>>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
                else{
                    binding.etemail.setError("email no valido!")
                }
            }else{
                mostrarPantallaError("Complete todos los campos!")
            }
        }

    }
    private fun irActivityHome(email :String){
        var intent = Intent(this, loginSIGE::class.java)
        intent.putExtra("email",email)
        startActivity(intent)

    }
    private fun mostrarPantallaError(error:String){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(error)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
}