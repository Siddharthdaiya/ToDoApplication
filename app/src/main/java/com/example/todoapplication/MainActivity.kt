package com.example.todoapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
private lateinit var mAuth: FirebaseAuth
    private lateinit var navControl: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        mAuth = FirebaseAuth.getInstance()
        mAuth = FirebaseAuth.getInstance()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {

        return super.onCreateView(parent, name, context, attrs)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logoutt) {
            //Write the loggin for logout
            mAuth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("KEY_RESTART_INTENT", "nextIntent")
            startActivity(intent)
            Runtime.getRuntime().exit(0)
            return true
        }
        return true
    }
}