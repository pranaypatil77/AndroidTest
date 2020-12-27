package com.example.android.androidtest.ui.homeScreen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.androidtest.R
import com.example.android.androidtest.adapter.UsersAdapter
import com.example.android.androidtest.api.TestService
import com.example.android.androidtest.model.usersdata.Data
import com.example.android.androidtest.model.usersdata.usersData
import com.example.android.androidtest.ui.locationScreen.MapsActivity
import com.example.android.androidtest.ui.loginScreen.LoginActivity
import com.example.android.androidtest.utils.showSnackBar
import com.example.android.androidtest.utils.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeActivity : AppCompatActivity(), Callback<usersData> {

    private lateinit var saveToken: SharedPreferences // save token for session management
    private var backPressTime: Long = 0 // for back button press action
    private var backToast: Toast? = null
    val udata = ArrayList<Data>() // for user data
    val sdata = ArrayList<Data>() // for search user data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = toolbarHome
        setSupportActionBar(toolbar) //set action bar

        saveToken =
            getSharedPreferences("token", Context.MODE_PRIVATE) // get token from login activity
        saveToken.getString("save_token", " ")

        // call users api
        val users = TestService.create()
        users.getUsers().enqueue(this)

        setupRecycler() // call setupRecycler function
    }

    // setup RecyclerView
    private fun setupRecycler() {
        recyclerUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HomeActivity) //set layout
            adapter = UsersAdapter(sdata) //set data class
        }
    }

    // Retrofit Failure response
    override fun onFailure(call: Call<usersData>, t: Throwable) {
        showSnackBar(getString(R.string.error_internet), this)
        proBarHome.visibility = INVISIBLE
        imgError.visibility = VISIBLE
        tvLoad.text = getString(R.string.error)

    }

    // Retrofit Success response
    override fun onResponse(call: Call<usersData>, response: Response<usersData>?) {
        val responseData = response?.body()
        if (responseData != null) {
            recyclerUsers.visibility = VISIBLE
            tvLoad.visibility = INVISIBLE
            proBarHome.visibility = INVISIBLE
            udata.clear()
            udata.addAll(responseData.data!!) // add response data in userdata
            sdata.addAll(udata) // add userdata to search userdata
            recyclerUsers.adapter?.notifyDataSetChanged() // for update RecyclerView
        }
    }

    // for manage action bar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu!!.findItem(R.id.app_bar_search)

        if (menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            searchView.queryHint = getString(R.string.search_hint)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                //for matching user query with users data
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        sdata.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        udata.forEach {
                            if (it.first_name!!.toLowerCase(Locale.getDefault()).contains(search)) {
                                sdata.add(it)
                            }
                        }
                        recyclerUsers.adapter!!.notifyDataSetChanged()
                    } else {
                        sdata.clear()
                        sdata.addAll(udata)
                        recyclerUsers.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //open map Activity
            R.id.map -> {
                if (!isLocationEnabled(this)) {
                    //open location setting
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                } else {
                    val i = Intent(this, MapsActivity::class.java)
                    startActivity(i)
                }
            }
            // logout Button
            R.id.logout -> {
               showCustomAlert()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCustomAlert() {
        val dialogView = layoutInflater.inflate(R.layout.alert_layout, null)
        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()
        val btDismiss = dialogView.findViewById<Button>(R.id.btnCancel)
        btDismiss.setOnClickListener {
            customDialog.dismiss()
        }
        val btYes = dialogView.findViewById<Button>(R.id.btnYes)
        btYes.setOnClickListener {
            customDialog.dismiss()
            val editor = saveToken.edit()
            editor.putString("save_token", " ")
            editor.apply()
            editor.commit()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            toast(getString(R.string.logout_s))
            finish()
        }

    }

    //check GPS ON or Not
    private fun isLocationEnabled(mContext: Context): Boolean {
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // back button exit or not function
    override fun onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            backToast?.cancel()
            super.onBackPressed()
            return
        } else {
            backToast = Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT)
            backToast?.show()
        }
        backPressTime = System.currentTimeMillis()
    }
}