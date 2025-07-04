package com.example.spoti5.presentations.feature.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.spoti5.R
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentSignUpBinding


/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater, container, false)
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEmail.setOnClickListener {
            // Handle sign up logic here
            // For example, you can collect input data and send it to your backend
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()

        }

        binding.txtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }
    companion object {

    }

}