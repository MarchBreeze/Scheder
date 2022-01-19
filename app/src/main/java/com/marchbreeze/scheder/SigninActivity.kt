package com.marchbreeze.scheder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.marchbreeze.scheder.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)

        Log.d("SIGNIN", "Set SigninActivity")
        setContentView(binding.root)
        setAuthFragment()
    }

    private fun setAuthFragment() {
        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_signin, SigninChoiceFragment())
        transaction.commit()
        Log.d("SIGNIN", "Set SigninChoiceFragment")
    }

    fun replaceFragment(fragment: Fragment) {
        Log.d("SIGNIN", "Replace Fragment")
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_signin, fragment)
        transaction.disallowAddToBackStack().commit()
    }

    fun replaceFragmentWithId(fragment: Fragment, documentId: String) {
        val bundle = Bundle()
        bundle.putString("documentId", documentId)
        fragment.arguments = bundle
        Log.d("SIGNIN", "Replace Fragment")
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_signin, fragment)
        transaction.disallowAddToBackStack().commit()
    }

    fun replaceFragmentWithIdList(fragment: Fragment, documentId: String, timeList: MutableList<String>) {
        val bundle = Bundle()
        bundle.putString("documentId", documentId)
        bundle.putStringArray("timeList", timeList.toTypedArray())
        fragment.arguments = bundle
        Log.d("SIGNIN", "Replace Fragment")
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_signin, fragment)
        transaction.disallowAddToBackStack().commit()
    }
}
