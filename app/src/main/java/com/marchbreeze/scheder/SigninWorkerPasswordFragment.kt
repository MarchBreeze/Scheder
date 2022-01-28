package com.marchbreeze.scheder

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.marchbreeze.scheder.databinding.FragmentSigninPasswordBinding
import java.util.regex.Pattern

class SigninWorkerPasswordFragment : Fragment() {

    lateinit var binding: FragmentSigninPasswordBinding
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var currentId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninPasswordBinding.inflate(inflater, container, false)
        Log.d("SIGNIN", "View SigninPasswordFragment")

        currentId = arguments?.getString("documentId").toString()
        Log.d("SIGNIN", "currentId : $currentId")

        // 비밀번호 규칙 확인 기능
        binding.edittextPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) =
                if (Pattern.matches(
                        "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$",
                        binding.edittextPassword.text.toString()
                    )
                ) {
                    binding.titlePasswordPattern.text = "O 비밀번호 형식 만족"
                    binding.titlePasswordPattern.setTextColor(Color.GREEN)
                } else {
                    binding.titlePasswordPattern.text = "X 비밀번호 형식 불만족"
                    binding.titlePasswordPattern.setTextColor(Color.RED)
                }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (Pattern.matches(
                        "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$",
                        binding.edittextPassword.text.toString()
                    )
                ) {
                    binding.titlePasswordPattern.text = "O 비밀번호 형식 만족"
                    binding.titlePasswordPattern.setTextColor(Color.GREEN)
                } else {
                    binding.titlePasswordPattern.text = "X 비밀번호 형식 불만족"
                    binding.titlePasswordPattern.setTextColor(Color.RED)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        // 비밀번호 확인 기능
        binding.edittextPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (binding.edittextPassword.text.toString() == binding.edittextPasswordCheck.text.toString()
                ) {
                    binding.titlePasswordConfirm.text = "O 비밀번호 일치"
                    binding.titlePasswordConfirm.setTextColor(Color.GREEN)

                } else {
                    binding.titlePasswordConfirm.text = "X 비밀번호 불일치"
                    binding.titlePasswordConfirm.setTextColor(Color.RED)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.edittextPassword.text.toString() == binding.edittextPasswordCheck.text.toString()
                ) {
                    binding.titlePasswordConfirm.text = "O 비밀번호 일치"
                    binding.titlePasswordConfirm.setTextColor(Color.GREEN)
                } else {
                    binding.titlePasswordConfirm.text = "X 비밀번호 불일치"
                    binding.titlePasswordConfirm.setTextColor(Color.RED)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        // 비밀번호 등록
        binding.btnPassword.setOnClickListener {
            if ((binding.titlePasswordConfirm.text == "O 비밀번호 일치") and (binding.titlePasswordPattern.text == "O 비밀번호 형식 만족")) {
                val password = binding.edittextPassword.text.toString()

                // DB에 비밀번호 추가
                db.collection("worker")
                    .document(currentId)
                    .update("pw", password)
                Log.d("SIGNIN", "password: $password")

                // 다음 회원가입 페이지로 전환
                (activity as SigninActivity).replaceFragmentWithId(
                    SigninWorkerDetailFragment(),
                    currentId
                )
                Log.d("SIGNIN", "Set SigninWorkerDetailFragment (currentId : $currentId)")
            } else {
                if (binding.titlePasswordConfirm.text == "O 비밀번호 일치") {
                    Toast.makeText(activity, "비밀번호 형식을 맞춰주세요", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "비밀번호가 일치하지 않아요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }
}