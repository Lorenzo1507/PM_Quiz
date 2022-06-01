package com.example.pm_quiz

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.pm_quiz.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth

class CadastroActivity : AppCompatActivity() {
    //viewBinding
    private lateinit var binding: ActivityCadastroBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email =""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure ActionBar, enable back button
        actionBar = supportActionBar!!
        actionBar.title = "Cadastre-se"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Aguarde")
        progressDialog.setMessage("Fazendo o cadastro...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click, begin signup
        binding.cadastrarBtn.setOnClickListener {
            //validate data
            validateData()
        }
    }

    private fun validateData() {
        //get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailEt.error = "E-mail inválido"
        }
        else if (TextUtils.isEmpty(password)){
            //password is not entered
            binding.passwordEt.error = "Por favor, digite a senha"
        }
        else if (password.length < 6){
            //password length is less than 6
            binding.passwordEt.error = "A senha deve ter pelo menos 6 caractéres"
        }
        else{
            //data is valid. continue sign up
            firebaseSignUp()
        }

    }

    private fun firebaseSignUp() {
        //show progress
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //signup success
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Conta criada com o $email", Toast.LENGTH_SHORT).show()

                //open profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                //signup failed
                progressDialog.dismiss()
                Toast.makeText(this, "Conta não criada por causa de ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        //go back to previous activity, when back button of actionBar is clicked
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}