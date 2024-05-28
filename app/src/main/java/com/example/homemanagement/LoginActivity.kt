package com.example.homemanagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putInt
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.homemanagement.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DBHelper
    private var id: Int ?=null
   // private val viewModel: UserViewModel by viewModels()

//    fun setUserId(userId: Int) {
//        viewModel.userId = userId
//    }
//
//    fun getUserId(): Int? {
//        return viewModel.userId
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DBHelper(this)
        //id = intent.getIntExtra("id", -1)
        binding.loginButton.setOnClickListener{
            val loginUsername = binding.loginUsername.text.toString()
            val loginPassword = binding.loginPassword.text.toString()
            loginDatabase(loginUsername, loginPassword)
        }

        binding.signupRedirect.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        val animatedGifImageView: ImageView = findViewById(R.id.animatedGifImageView)

        // Load the GIF dynamically using Glide
        Glide.with(this)
            .load(R.drawable.pingu)
            .into(animatedGifImageView)

        // Set up click listener on the ImageView (optional)
        animatedGifImageView.setOnClickListener {
            // Add any additional actions you want to perform when the ImageView is clicked
        }
    }

    private fun loginDatabase(username:String, password:String){
        val userExists = databaseHelper.readUser(username, password)
        if(userExists){
            id= databaseHelper.getUserId(username)
            //viewModel.userId=id
            Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
            val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("user_email", username)
                apply()
            }
            val userId= databaseHelper.getUserId(username) // Get the selected room
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("userId", userId)
                Log.d("UserId", "Retrieved userId: $userId")
            }
            startActivity(intent)
            finish()
        }
        else{
            Toast.makeText(this, "Login failed",Toast.LENGTH_SHORT).show()
        }
    }

}