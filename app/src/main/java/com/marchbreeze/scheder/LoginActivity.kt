package com.marchbreeze.scheder

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        Log.d("LOGIN", "Set LoginActivity")
        setContentView(binding.root)

        // 비밀번호 찾기
        binding.btnFindPw.setOnClickListener {
            startActivity(Intent(this@LoginActivity, LoginFindPwActivity::class.java))
            finish()
        }
        
        binding.btnLogin.setOnClickListener {
            // 입력받은 값 변수로 저장
            val phoneFront = binding.edittextPhonenumberFront.text.toString().trim()
            val phoneBack = binding.edittextPhonenumberBack.text.toString().trim()
            val id = "010$phoneFront$phoneBack"
            val pw = binding.edittextPw.text.toString()

            binding.warningPw.text = ""
            binding.warningId.text = ""

            // 프로그래스 다이얼로그 시작 (대체 - 로티)
            val progressDialog = LottieProgressDialog(
                context = this,
                isCancel = false,
                dialogWidth = null,
                dialogHeight = null,
                animationViewWidth = null,
                animationViewHeight = null,
                fileName = LottieProgressDialog.SAMPLE_1,
                title = "스케더 로그인 중 ...",
                titleVisible = 0
            )
            progressDialog.show()

            // id 검색
            // 우선 관리자 유저에서 id 검색
            db.collection("owner")
                .whereEqualTo("id", id)
                .get().addOnSuccessListener { documentsOwner ->
                    if (!documentsOwner.isEmpty) {
                        for (document in documentsOwner) {
                            if (document.get("pw").toString() == pw) {
                                progressDialog.dismiss()
                                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                                Log.d("LOGIN", "Login as owner : $id")
                                startActivity(Intent(this, OwnerMainActivity::class.java))
                                finish()

                            } else {
                                progressDialog.dismiss()
                                Toast.makeText(this, "비밀번호가 일치하지 않아요", Toast.LENGTH_SHORT).show()
                                Log.d("LOGIN", "Login Failed : $id")
                                binding.warningPw.text = "비밀번호가 일치하지 않아요"
                                binding.warningPw.setTextColor(Color.RED)
                            }
                        }
                    } else {

                        // 직원 유저에서 id 검색
                        db.collection("worker")
                            .whereEqualTo("id", id)
                            .get().addOnSuccessListener { documentsWorker ->
                                if (!documentsWorker.isEmpty) {
                                    for (document in documentsWorker) {
                                        if (document.get("pw").toString() == pw) {
                                            progressDialog.dismiss()
                                            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT)
                                                .show()
                                            Log.d("LOGIN", "Login as worker : $id")
                                            // TODO 직원 액티비티 만들고 링크 수정하기 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                            // TODO 액티비티 연결 전 승인 여부 확인하기 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                            startActivity(
                                                Intent(
                                                    this,
                                                    OwnerMainActivity::class.java
                                                )
                                            )
                                            finish()
                                        } else {
                                            progressDialog.dismiss()
                                            Toast.makeText(
                                                this,
                                                "비밀번호가 일치하지 않아요",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Log.d("LOGIN", "Login Failed : $id")
                                            binding.warningPw.text = "비밀번호가 일치하지 않아요"
                                            binding.warningPw.setTextColor(Color.RED)
                                        }
                                    }
                                } else {
                                    progressDialog.dismiss()
                                    Toast.makeText(this, "등록되지 않은 전화번호에요", Toast.LENGTH_SHORT)
                                        .show()
                                    Log.d("LOGIN", "Login Failed (unverificated) : $id")
                                    binding.warningId.text = "등록되지 않은 전화번호에요"
                                    binding.warningId.setTextColor(Color.RED)
                                }
                            }
                    }
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, FirstActivity::class.java))
        finish()
    }
}
