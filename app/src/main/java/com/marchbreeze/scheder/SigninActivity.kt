package com.marchbreeze.scheder

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.marchbreeze.scheder.databinding.ActivitySigninBinding
import java.util.concurrent.TimeUnit

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding

    // 인증코드 전송 실패 시, 재전송에 사용
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    private var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerificationId: String? = null
    private lateinit var firbaseAuth: FirebaseAuth

    private val TAG = "MAIN_TAG"

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.titleAuth.visibility = View.GONE
        binding.edittextAuth.visibility = View.GONE
        binding.btnAuth.visibility = View.GONE
        binding.btnResend.visibility = View.GONE

        // 파이어베이스 인증 설정
        firbaseAuth = FirebaseAuth.getInstance()

        // progress dialog 설정
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        // 인증 상태에 따른 인증 콜백 정의
        mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // 2가지 상황에서 호출됨
                // 1. 즉시 인증 (인증 코드 없이 즉시 인증)
                // 2. 자동 검색 (GooglePlay가 문자 자동 감지해서 사용자 개입 없이 인증 수행 - SMS Retriever API)
                signInWithPhoneAuthCredential(credential)
            }

            // 잘못된 인증 요청 (잘못된 번호 혹은 인증 코드) 시 호출
            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                Toast.makeText(this@SigninActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }

            // 인증 코드 전송 후 호출
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // 인증코드 입력 요구 & 크루덴션 생성해야함 (코드랑 인증ID랑 합쳐서)
                Log.d(TAG, "onCodeSent: $verificationId")
                progressDialog.dismiss()
                Toast.makeText(this@SigninActivity, "Verification code sent ... ", Toast.LENGTH_SHORT).show()

                //ID와 token 저장
                mVerificationId = verificationId
                forceResendingToken = token

                binding.titleAuth.visibility = View.VISIBLE
                binding.edittextAuth.visibility = View.VISIBLE
                binding.btnAuth.visibility = View.VISIBLE
                binding.btnResend.visibility = View.VISIBLE

            }
        }

        // 전화번호 입력 버튼
        binding.btnPhonenumber.setOnClickListener {
            // input 전화번호
            val phone = binding.edittextPhonenumber.text.toString().trim()
            // 전화번호 인증 절차
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this@SigninActivity, "Please enter phone number", Toast.LENGTH_SHORT).show()
            } else {
                startPhoneNumberVerification(phone)
            }
        }

        // 인증번호 재전송 버튼
        binding.btnResend.setOnClickListener {
            // input 전화번호
            val phone = binding.edittextPhonenumber.text.toString().trim()
            // 전화번호 인증 절차
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this@SigninActivity, "Please enter phone number", Toast.LENGTH_SHORT).show()
            } else {
                resendVerificationCode(phone, forceResendingToken)
            }
        }

        // 인증 버튼
        binding.btnAuth.setOnClickListener {
            // input 인증코드
            val code = binding.edittextAuth.text.toString().trim()
            // 인증코드 인증 절차
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(this@SigninActivity, "Please enter verification code", Toast.LENGTH_SHORT).show()
            } else {
                verifyPhoneNumberWithCode(mVerificationId, code)
            }

        }
    }

    // 인증 진행 함수
    private fun startPhoneNumberVerification(phone: String) {
        progressDialog.setMessage("Verifying Phone Number ... ")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firbaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBacks!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // 인증번호 재전송 함수
    private fun resendVerificationCode(
        phone: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        progressDialog.setMessage("Resending Code ... ")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firbaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBacks!!)
            .setForceResendingToken(token!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // 인증 함수
    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        progressDialog.setMessage("Verifying Code ... ")
        progressDialog.show()

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        progressDialog.setMessage("Logging In ... ")

        firbaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                // 로그인 성공 시
                progressDialog.dismiss()
                val phone = firbaseAuth.currentUser?.phoneNumber
                Toast.makeText(this, "Logged In as $phone", Toast.LENGTH_SHORT).show()

                // 프로필 액티비티 시작
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                // 로그인 실패 시
                progressDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
