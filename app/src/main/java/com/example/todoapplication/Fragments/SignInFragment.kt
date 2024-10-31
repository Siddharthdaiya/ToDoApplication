package com.example.todoapplication.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoapplication.R
import com.example.todoapplication.databinding.FragmentSignInBinding
import com.example.todoapplication.databinding.ActivityMainBinding
import com.example.todoapplication.databinding.FragmentSignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth: FirebaseAuth
private lateinit var navControl: NavController
private lateinit var binding:FragmentSignInBinding

class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View?
    {
        binding=FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }
    private fun init(view:View){
        navControl = Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun registerEvents(){

        binding.authTextView.setOnClickListener {
                navControl.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.Nextbtn.setOnClickListener{
            val email=binding.emailEt.text.toString().trim()
            val pass=binding.passEt.text.toString().trim()


            if (email.isNotEmpty() && pass.isNotEmpty()){
                binding.progressBar.visibility=View.VISIBLE
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                                navControl.navigate(R.id.action_signInFragment_to_homeFragment)
                            }else
                            {
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                            binding.progressBar.visibility=View.GONE

                        })
                }else{
                Toast.makeText(context, "Empty Fields Not Allowed", Toast.LENGTH_SHORT).show()

            }
            }
        }
    }