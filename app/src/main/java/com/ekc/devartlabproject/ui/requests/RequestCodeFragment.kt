package com.ekc.devartlabproject.ui.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ekc.devartlabproject.databinding.RequestCodeFragmentLayoutBinding

class RequestCodeFragment : Fragment() {
    private lateinit var binding: RequestCodeFragmentLayoutBinding
    private val viewModel: RequestCodeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RequestCodeFragmentLayoutBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {

        binding.run {
            pinLayout.setPinViewEventListener { pinview, _ ->
                if (viewModel.validate(pinview.value))
                    findNavController().popBackStack()
                else
                    Toast.makeText(
                        requireContext(),
                        "wrong code please ensure the customer code ...",
                        Toast.LENGTH_LONG
                    ).show()

            }

        }

    }


}
