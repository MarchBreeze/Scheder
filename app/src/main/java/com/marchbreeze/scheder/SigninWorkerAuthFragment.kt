package com.marchbreeze.scheder

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.FragmentSigninAuthBinding
import java.util.concurrent.TimeUnit

class SigninWorkerAuthFragment : Fragment() {

    private lateinit var binding: FragmentSigninAuthBinding

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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("SIGNIN", "View SigninAuthFragment")
        binding = FragmentSigninAuthBinding.inflate(inflater, container, false)

        binding.titleAuth.visibility = View.GONE
        binding.edittextAuth.visibility = View.GONE
        binding.btnAuth.visibility = View.GONE
        binding.btnResend.visibility = View.GONE

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
                Toast.makeText(activity, "잘못된 인증 요청이에요", Toast.LENGTH_SHORT).show()
            }

            // 인증 코드 전송 후 호출
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // 인증코드 입력 요구 & 크루덴션 생성해야함 (코드랑 인증ID랑 합쳐서)
                Log.d("SIGNIN", "Send Verification Code")
                progressDialogSend.dismiss()
                Toast.makeText(activity, "인증번호가 발송됐어요!", Toast.LENGTH_SHORT).show()

                //ID와 token 저장
                mVerificationId = verificationId
                forceResendingToken = token
                Log.d("SIGNIN", "verificationId : $mVerificationId")
                Log.d("SIGNIN", "token : $forceResendingToken")

                Log.d("SIGNIN", "Visiblize Auth Textviews")
                binding.titleAuth.visibility = View.VISIBLE
                binding.edittextAuth.visibility = View.VISIBLE
                binding.btnAuth.visibility = View.VISIBLE
                binding.btnResend.visibility = View.VISIBLE
            }
        }

        // 전화번호 입력 버튼
        binding.btnPhonenumber.setOnClickListener {
            Log.d("SIGNIN", "Pushed - send auth number")
            // input 전화번호
            val phoneFront = binding.edittextPhonenumberFront.text.toString().trim()
            val phoneBack = binding.edittextPhonenumberBack.text.toString().trim()
            val phone = "+8210$phoneFront$phoneBack"
            // 전화번호 인증 절차
            if (TextUtils.isEmpty(phoneFront) or TextUtils.isEmpty(phoneBack)) {
                Log.d("SIGNIN", "Missing Phone Number")
                Toast.makeText(activity, "전화번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {

                // 이미 등록된 번호인지 여부 확인 후 인증 번호 전송
                val phone010 = "010${phoneFront}${phoneBack}_1"
                val docOwner = db.collection("owner")
                    .document(phone010)
                val docWorker = db.collection("worker")
                    .document(phone010)

                // 관리자 데이터 확인
                docOwner.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        if (document.get("finishSignin") == true) {
                            Log.d("SIGNIN", "Already Verificated Number as Owner")
                            Toast.makeText(activity, "이미 관리자로 등록된 번호에요", Toast.LENGTH_LONG)
                                .show()
                            binding.warningPhone.text = "이미 관리자로 등록된 번호에요"
                            binding.warningPhone.setTextColor(Color.RED)
                        } else {
                            Log.d("SIGNIN", "Run Phone Verification (Uncompleted User as Owner)")
                            db.collection("owner").document(phone010).delete()
                            if (binding.warningPhone.text == "이미 등록된 번호입니다! 로그인해주세요") {
                                binding.warningPhone.text = ""
                            }
                            startPhoneNumberVerification(phone)
                        }
                    } else {

                        // 직원 데이터 확인
                        docWorker.get().addOnSuccessListener { document ->
                            if (!document.exists()) {
                                Log.d("SIGNIN", "Run Phone Verification (New User)")
                                if (binding.warningPhone.text == "이미 등록된 번호입니다! 로그인해주세요") {
                                    binding.warningPhone.text = ""
                                }
                                startPhoneNumberVerification(phone)

                            } else {
                                if (document.get("finishSignin") == true) {
                                    Log.d("SIGNIN", "Already Verificated Number")
                                    Toast.makeText(
                                        activity,
                                        "이미 등록된 번호입니다 \n 로그인해주세요",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    binding.warningPhone.text = "이미 등록된 번호입니다! 로그인해주세요"
                                    binding.warningPhone.setTextColor(Color.RED)

                                } else {
                                    Log.d("SIGNIN", "Run Phone Verification (Uncompleted User)")
                                    if (binding.warningPhone.text == "이미 등록된 번호입니다! 로그인해주세요") {
                                        binding.warningPhone.text = ""
                                    }
                                    startPhoneNumberVerification(phone)
                                }
                            }
                        }.addOnFailureListener { e ->
                            Log.d("SIGNIN", "btnPhonenumber failure ($e)")
                        }
                    }
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
                Log.d("SIGNIN", "Missing Phone Number")
                Toast.makeText(activity, "전화번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("SIGNIN", "Resend Verification Code")
                resendVerificationCode(phone, forceResendingToken)
            }
        }

        // 인증 버튼
        binding.btnAuth.setOnClickListener {
            // input 인증코드
            val code = binding.edittextAuth.text.toString().trim()
            // 인증코드 인증 절차
            if (TextUtils.isEmpty(code)) {
                Log.d("SIGNIN", "Missing Verification Number")
                Toast.makeText(activity, "인증번호 6자리를 모두 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                verifyPhoneNumberWithCode(mVerificationId, code)
            }
        }
        return binding.root
    }

    // 인증 진행 함수
    private fun startPhoneNumberVerification(phone: String) {
        // progress dialog 설정
        progressDialogSend = LottieProgressDialog(
            context = context as Activity,
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
            .setActivity(activity!!)
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
            context = context as Activity,
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
            .setActivity(activity!!)
            .setCallbacks(mCallBacks!!)
            .setForceResendingToken(token!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // 인증 함수
    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // progress dialog 설정
        progressDialogAuth = LottieProgressDialog(
            context = context as Activity,
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
                Log.d("SIGNIN", "SIGNIN Successful")
                progressDialogAuth.dismiss()
                Toast.makeText(activity, "전화번호 인증 성공!", Toast.LENGTH_SHORT).show()

                // 정보 저장
                val phoneFront = binding.edittextPhonenumberFront.text.toString().trim()
                val phoneBack = binding.edittextPhonenumberBack.text.toString().trim()
                val phone = "010$phoneFront$phoneBack"

                // 새로운 직원 유저 생성
                val user = WorkerObject(id = phone)
                db.collection("worker")
                    .document("${phone}_1")
                    .set(user)
                Log.d("SIGNIN", "Add user (doc : ${phone}_1 )")

                // 다음 회원가입 페이지로 전환
                currentId = "${phone}_1"
                (activity as SigninActivity).replaceFragmentWithId(
                    SigninWorkerPasswordFragment(),
                    currentId
                )
                Log.d("SIGNIN", "Set SigninWorkerPasswordFragment (currentId : $currentId)")

            }
            .addOnFailureListener { e ->
                // 실패 시
                Log.d("SIGNIN", "SIGNIN Failed")
                progressDialogAuth.dismiss()
                Toast.makeText(activity, "인증번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}