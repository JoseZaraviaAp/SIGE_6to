package insn.pe.appmovilsige

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import insn.pe.appmovilsige.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TIEMPO_SPLASH:Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            startActivity(Intent(this,loginSIGE::class.java))
            finish()
        },TIEMPO_SPLASH)
        }
    }

