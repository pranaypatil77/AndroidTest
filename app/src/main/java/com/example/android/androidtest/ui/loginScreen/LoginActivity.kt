package com.example.android.androidtest.ui.loginScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.example.android.androidtest.R
import com.example.android.androidtest.api.TestService
import com.example.android.androidtest.model.logindata.loginData
import com.example.android.androidtest.ui.homeScreen.HomeActivity
import com.example.android.androidtest.utils.showSnackBar
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), Callback<loginData> {
    //    global variable declration
    private lateinit var unHolder: String  // username holder
    private lateinit var psHolder: String  // password holder
    private lateinit var saveToken: SharedPreferences // save token for remember user
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        saveToken = getSharedPreferences("token", Context.MODE_PRIVATE)
        checkUser() // check user.. login or not

        btnLogin.setOnClickListener {
            checkInputs() // check user inputs
        }
    }

    private fun checkInputs() {
        unHolder = etUsername.text.toString()
        psHolder = etPassword.text.toString()
        if (unHolder.isEmpty() || psHolder.isEmpty()) {
            showSnackBar(getString(R.string.inputcheck), this)
        } else {
            proBarLogin.visibility = VISIBLE
            tvPleasewait.visibility = VISIBLE
            //API call
            val login = TestService.create()
            login.getLogin(unHolder, psHolder)
                .enqueue(this)
        }
    }

    // check already login or not
    private fun checkUser() {
        if (saveToken.getString("save_token", " ") != " ") {
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    // response failure
    override fun onFailure(call: Call<loginData>, t: Throwable?) {
        proBarLogin.visibility = INVISIBLE
        tvPleasewait.visibility = INVISIBLE
        showSnackBar(getString(R.string.error_internet),this)
    }

    // response success
    override fun onResponse(call: Call<loginData>, response: Response<loginData>?) {
        token = response?.body().toString()
        proBarLogin.visibility = INVISIBLE
        tvPleasewait.visibility = INVISIBLE
        // save token and then login user
        if (response!!.isSuccessful) {
            val editor = saveToken.edit()
            editor.putString("save_token", token)
            editor.apply()
            editor.commit()
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
        } else {
            showSnackBar(getString(R.string.inputvalid),this)
        }

    }
}