package com.example.librapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.regex.Pattern

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_layout)

    }

    fun Registrate(v: View) {

        val name = findViewById<EditText>(R.id.nameField).text.toString()
        val email = findViewById<EditText>(R.id.emailField).text.toString()
        val pass = findViewById<EditText>(R.id.passwordText).text.toString()

        if (name.isEmpty()) {
            findViewById<EditText>(R.id.nameField).error = getString(R.string.invalid_username)
            return
        }

        if (pass.isEmpty()) {
            findViewById<EditText>(R.id.passwordText).error = getString(R.string.invalid_password)
            return
        }

        if (!isNotValidPassword(pass)) {
            findViewById<EditText>(R.id.passwordText).error = "Password must have at least 8 characters!"
            return
        }

        if (email.isEmpty()) {
            findViewById<EditText>(R.id.emailField).error = getString(R.string.invalid_email)
            return
        }

        if (!isNotValidEmail(email)) {
            findViewById<EditText>(R.id.emailField).error = "Choose a valid email!"
            return
        }

        val Users = FirebaseDatabase.getInstance().getReference("Users").child("nome").get()
        if (Users.equals(name)) {
            findViewById<EditText>(R.id.nameField).error = "Username already used!"
            return
        }


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(name, email, pass)
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).setValue(user)
                    Toast.makeText(applicationContext, "Registration completed", Toast.LENGTH_LONG).show()
                    onBackPressed()
                } else {
                    Toast.makeText(applicationContext, "Error in registration", Toast.LENGTH_LONG).show()
                }

            }
    }

    private fun isNotValidPassword(pass: String): Boolean {
        return if (pass.length >= 8) {
            true
        } else {
            false
        }
    }

    private fun isNotValidEmail(email: String): Boolean {
        val EMAIL_PATTERN =
            ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }


    private fun checkName(name: String): Boolean {
        var checked = false
        val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnap: DataSnapshot in snapshot.getChildren()) {
                    val iterUser = userSnap.getValue(User::class.java)
                    val username = iterUser!!.nome
                    if (name == username){
                      checked = true
                    } else {
                        checked =  false
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })

        return checked
    }

}
