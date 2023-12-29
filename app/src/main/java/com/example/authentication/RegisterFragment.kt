package com.example.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.authentication.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

            private val auth = FirebaseAuth.getInstance()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val binding = FragmentRegisterBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val binding = FragmentRegisterBinding.bind(view)

            binding.btnCreateAccount.setOnClickListener {
                val userName = binding.edtTxtCreateUserName.text.toString()
                val userPassword = binding.edtTxtCreatePassword.text.toString()
                createAccount(email = userName, password = userPassword)
                binding.edtTxtCreateUserName.text.clear()
                binding.edtTxtCreatePassword.text.clear()
            }

            binding.btnBackToLogin.setOnClickListener {
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(action)

                //logine dön
            }
        }

        private fun createAccount(email: String, password: String) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(requireContext(), "Kayıt Başarılı", Toast.LENGTH_LONG).show()
                    } else {
                        // If sign-in fails, display a message to the user.
                        Toast.makeText(
                            requireContext(),
                            "Kayıt Yapılamadı, Lütfen e-mail adresini doğru girdiğinizden emin olun.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }