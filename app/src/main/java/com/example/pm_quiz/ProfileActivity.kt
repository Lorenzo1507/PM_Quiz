package com.example.pm_quiz

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.pm_quiz.databinding.ActivityLoginBinding
import com.example.pm_quiz.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityProfileBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //FireBaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionBar
        actionBar = supportActionBar!!
        actionBar.title = "Perfil"

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //hnadle click, logout
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null, user is logged in, get user info
            val email = firebaseUser.email
            //set to text view
            binding.emailTv.text = email
        }
        else{
            //user is null, user is not logged in, go to login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}