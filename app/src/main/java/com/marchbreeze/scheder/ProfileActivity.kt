package com.marchbreeze.scheder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.marchbreeze.scheder.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // 로그아웃 버튼
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()

        }
    }

    private fun checkUser() {
        // 현재 사용중인 유저
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            // 현재 사용자 없으면 로그아웃
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        } else {
            // 화면에 현재 사용자 번호 띄우기
            val phone = firebaseUser.phoneNumber
            binding.textPhone.text = phone
        }
    }
}