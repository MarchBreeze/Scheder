package com.marchbreeze.scheder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.marchbreeze.scheder.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnSignin.setOnClickListener {
            Log.d("SIGNIN", "Pushed Signin Btn")
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }
    }
}