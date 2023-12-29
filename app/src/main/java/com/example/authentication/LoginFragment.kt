package com.example.authentication

import android.content.Context
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.authentication.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {


        private val PREF_EMAIL = "email"
        private val PREF_PASSWORD = "password"

        private val auth = FirebaseAuth.getInstance()

        lateinit var binding: FragmentLoginBinding
        private lateinit var navController: NavController

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentLoginBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val savedEmail = sharedPref.getString(PREF_EMAIL, "")
            val savedPassword = sharedPref.getString(PREF_PASSWORD, "")

            binding.edtTxtUserName.setText(savedEmail)
            binding.edtTxtPassword.setText(savedPassword)

            binding.btnLogin.setOnClickListener {
                val email = binding.edtTxtUserName.text.toString()
                val password = binding.edtTxtPassword.text.toString()
                login(email, password)
            }

            val passwordTransformationMethod = PasswordTransformationMethod.getInstance()

            binding.edtTxtPassword.transformationMethod = passwordTransformationMethod

            binding.btnShowPassword.setOnClickListener {
                if (binding.edtTxtPassword.transformationMethod == passwordTransformationMethod) {
                    binding.edtTxtPassword.transformationMethod = null
                } else {
                    binding.edtTxtPassword.transformationMethod = passwordTransformationMethod
                }
            }

            binding.btnRegister.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)

            }

            binding.txtForgetPassword.setOnClickListener {
                val email = binding.edtTxtUserName.text.toString()
                if (email.isNotEmpty()) {
                    sendPasswordResetEmail(email)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Lütfen e-posta adresinizi girin.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        private fun login(email: String, password: String) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        saveCredentials(email, password)

                        Toast.makeText(
                            requireContext(),
                            "HOŞGELDİNİZ",
                            Toast.LENGTH_SHORT
                        ).show()

                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Giriş Yapılamadı, Lütfen e-mail adresini doğru girdiğinizden emin olun.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        private fun saveCredentials(email: String, password: String) {
            val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(PREF_EMAIL, email)
            editor.putString(PREF_PASSWORD, password)
            editor.apply()
        }

        private fun sendPasswordResetEmail(email: String) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Şifre sıfırlama bağlantısı e-posta adresinize gönderildi.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Şifre sıfırlama bağlantısı gönderilemedi. Lütfen geçerli bir e-posta adresi girin.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }