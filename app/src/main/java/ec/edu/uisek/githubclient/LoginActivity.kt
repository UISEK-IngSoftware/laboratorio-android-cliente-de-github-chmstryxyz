package ec.edu.uisek.githubclient

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import ec.edu.uisek.githubclient.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etToken: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        // Verificar si ya hay sesión iniciada
        if (!sessionManager.getAuthToken().isNullOrEmpty()) {
            goToMainActivity()
            return
        }

        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.et_username)
        etToken = findViewById(R.id.et_token)
        btnLogin = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val token = etToken.text.toString().trim()

            if (username.isNotEmpty() && token.isNotEmpty()) {
                // Guardar sesión
                sessionManager.saveUserName(username)
                sessionManager.saveAuthToken(token)
                goToMainActivity()
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}