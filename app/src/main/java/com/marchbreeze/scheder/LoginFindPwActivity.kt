package com.marchbreeze.scheder

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.ActivityLoginFindPwBinding
import java.util.concurrent.TimeUnit

class LoginFindPwActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginFindPwBinding

    // 인증코드 전송 실패 시, 재전송에 사용
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    private var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerificationId: String? = null
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var firbaseAuth: FirebaseAuth

    private lateinit var currentId: String

    // progress dialog 설정
    private lateinit var progressDialogSend: LottieProgressDialog
    private lateinit var progressDialogAuth: LottieProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LOGIN", "View LoginFindPwActivity")
        binding = ActivityLoginFindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.titleAuth.visibility = View.GONE
        binding.edittextAuth.visibility = View.GONE
        binding.btnAuth.visibility = View.GONE
        binding.btnResend.visibility = View.GONE

        binding.arrowDown.visibility = View.GONE
        binding.titlePw.visibility = View.GONE
        binding.btnToLogin.visibility = View.GONE

        // 파이어베이스 인증 설정
        firbaseAuth = FirebaseAuth.getInstance()

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
                progressDialogAuth.dismiss()
                Toast.makeText(this@LoginFindPwActivity, "잘못된 인증 요청이에요", Toast.LENGTH_SHORT).show()
            }

            // 인증 코드 전송 후 호출
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // 인증코드 입력 요구 & 크루덴션 생성해야함 (코드랑 인증ID랑 합쳐서)
                Log.d("LOGIN", "Send Verification Code")
                progressDialogSend.dismiss()
                Toast.makeText(this@LoginFindPwActivity, "인증번호가 발송됐어요!", Toast.LENGTH_SHORT).show()

                //ID와 token 저장
                mVerificationId = verificationId
                forceResendingToken = token
                Log.d("LOGIN", "verificationId : $mVerificationId")
                Log.d("LOGIN", "token : $forceResendingToken")

                Log.d("LOGIN", "Visiblize Auth Textviews")
                binding.titleAuth.visibility = View.VISIBLE
                binding.edittextAuth.visibility = View.VISIBLE
                binding.btnAuth.visibility = View.VISIBLE
                binding.btnResend.visibility = View.VISIBLE
            }
        }

        // 전화번호 입력 버튼
        binding.btnPhonenumber.setOnClickListener {
            // input 전화번호
            val phoneFront = binding.edittextPhonenumberFront.text.toString().trim()
            val phoneBack = binding.edittextPhonenumberBack.text.toString().trim()
            val phone = "+8210$phoneFront$phoneBack"
            currentId = "010$phoneFront$phoneBack"

            // 전화번호 인증 절차
            if (TextUtils.isEmpty(phoneFront) or TextUtils.isEmpty(phoneBack)) {
                Log.d("LOGIN", "Missing Phone Number")
                Toast.makeText(this, "전화번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                // 이미 등록된 번호인지 여부 확인 후 인증 번호 전송
                val docOwner = db.collection("owner")
                    .document("010${phoneFront}${phoneBack}_1")
                val docWorker = db.collection("worker")
                    .document("010${phoneFront}${phoneBack}_1")
                docOwner.get().addOnSuccessListener { document ->
                    if (document != null) {
                        // Owner DB에서 비밀번호 찾기
                        if (document.get("finishSignin") == true) {
                            Log.d("LOGIN", "Found id: Verificated Number")
                            if (binding.warningPhone.text == "등록되지 않은 번호에요") {
                                binding.warningPhone.text = ""
                            }
                            startPhoneNumberVerification(phone)
                        } else {
                            Log.d("LOGIN", "Found id: Uncompleted Signin")
                            Toast.makeText(this, "등록되지 않은 번호입니다!\n회원가입 해주세요", Toast.LENGTH_LONG)
                                .show()
                            binding.warningPhone.text = "등록되지 않은 번호에요"
                            binding.warningPhone.setTextColor(Color.RED)
                        }
                    } else {
                        // Worker DB에서 비밀번호 찾기
                        docWorker.get().addOnSuccessListener { document ->
                            if (document != null) {
                                if (document.get("finishSignin") == true) {
                                    Log.d("LOGIN", "Found id: Verificated Number")
                                    if (binding.warningPhone.text == "등록되지 않은 번호에요") {
                                        binding.warningPhone.text = ""
                                    }
                                    startPhoneNumberVerification(phone)
                                } else {
                                    Log.d("LOGIN", "Found id: Uncompleted Signin")
                                    Toast.makeText(
                                        this,
                                        "등록되지 않은 번호입니다!\n회원가입 해주세요",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    binding.warningPhone.text = "등록되지 않은 번호에요"
                                    binding.warningPhone.setTextColor(Color.RED)
                                }
                            } else {
                                Log.d("LOGIN", "Cannot find id")
                                Toast.makeText(this, "등록되지 않은 번호입니다!\n회원가입 해주세요", Toast.LENGTH_LONG)
                                    .show()
                                binding.warningPhone.text = "등록되지 않은 번호에요"
                                binding.warningPhone.setTextColor(Color.RED)
                            }
                        }
                    }
                }.addOnFailureListener { e ->
                    Log.d("LOGIN", "btnPhonenumber failure ($e)")
                }
            }
        }

        // 인증번호 재전송 버튼
        binding.btnResend.setOnClickListener {
            // input 전화번호
            val phoneFront = binding.edittextPhonenumberFront.text.toString().trim()
            val phoneBack = binding.edittextPhonenumberBack.text.toString().trim()
            val phone = "+8210$phoneFront$phoneBack"
            // 전화번호 인증 절차
            if (TextUtils.isEmpty(phoneFront) or TextUtils.isEmpty(phoneBack)) {
                Log.d("LOGIN", "Missing Phone Number")
                Toast.makeText(this, "전화번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("LOGIN", "Resend Verification Code")
                resendVerificationCode(phone, forceResendingToken)
            }
        }

        // 인증 버튼
        binding.btnAuth.setOnClickListener {
            // input 인증코드
            val code = binding.edittextAuth.text.toString().trim()
            // 인증코드 인증 절차
            if (TextUtils.isEmpty(code)) {
                Log.d("LOGIN", "Missing Verification Number")
                Toast.makeText(this, "인증번호 6자리를 모두 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                verifyPhoneNumberWithCode(mVerificationId, code)
            }
        }

        // 로그인 화면으로 버튼
        binding.btnToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // 인증 진행 함수
    private fun startPhoneNumberVerification(phone: String) {
        // progress dialog 설정
        progressDialogSend = LottieProgressDialog(
            context = this,
            isCancel = true,
            dialogWidth = null,
            dialogHeight = null,
            animationViewWidth = null,
            animationViewHeight = null,
            fileName = LottieProgressDialog.SAMPLE_1,
            title = "인증번호 발송 중 ...",
            titleVisible = 0
        )
        progressDialogSend.show()

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
        // progress dialog 설정
        progressDialogSend = LottieProgressDialog(
            context = this,
            isCancel = true,
            dialogWidth = null,
            dialogHeight = null,
            animationViewWidth = null,
            animationViewHeight = null,
            fileName = LottieProgressDialog.SAMPLE_1,
            title = "인증번호 발송 중 ...",
            titleVisible = 0
        )
        progressDialogSend.show()

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
        // progress dialog 설정
        progressDialogAuth = LottieProgressDialog(
            context = this,
            isCancel = false,
            dialogWidth = null,
            dialogHeight = null,
            animationViewWidth = null,
            animationViewHeight = null,
            fileName = LottieProgressDialog.SAMPLE_1,
            title = "전화번호 인증 중 ...",
            titleVisible = 0
        )
        progressDialogAuth.show()

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firbaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                // 인증번호 인증 성공 시
                Log.d("LOGIN", "SIGNIN Successful")
                progressDialogAuth.dismiss()
                Toast.makeText(this, "전화번호 인증 성공!", Toast.LENGTH_SHORT).show()

                // 인터페이스 표시
                binding.arrowDown.visibility = View.VISIBLE
                binding.titlePw.visibility = View.VISIBLE
                binding.btnToLogin.visibility = View.VISIBLE

                // DB에서 맞는 pw 추출 후 표시
                // id 검색
                // 우선 관리자 유저에서 id 검색
                db.collection("owner")
                    .whereEqualTo("id", currentId)
                    .get().addOnSuccessListener { documentsOwner ->
                        if (!documentsOwner.isEmpty) {
                            for (document in documentsOwner) {
                                val pw = document.get("pw")
                                progressDialogAuth.dismiss()
                                Log.d("LOGIN", "Found PW : $pw")
                                binding.titlePw.text = "비밀번호 : ${pw.toString()}"
                            }
                        } else {
                            // 직원 유저에서 id 검색
                            db.collection("worker")
                                .whereEqualTo("id", currentId)
                                .get().addOnSuccessListener { documentsWorker ->
                                    if (!documentsWorker.isEmpty) {
                                        for (document in documentsOwner) {
                                            val pw = document.get("pw")
                                            progressDialogAuth.dismiss()
                                            Log.d("LOGIN", "Found PW : $pw")
                                            binding.titlePw.text = "비밀번호 : ${pw.toString()}"
                                        }
                                    } else {
                                        progressDialogAuth.dismiss()
                                        Toast.makeText(this, "인증번호를 다시 확인해주세요", Toast.LENGTH_SHORT)
                                            .show()
                                        Log.d("LOGIN", "Failed to find pw")
                                    }
                                }
                        }
                    }
            }
            .addOnFailureListener { e ->
                // 실패 시
                Log.d("LOGIN", "SIGNIN Failed")
                progressDialogAuth.dismiss()
                Toast.makeText(this, "인증번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
