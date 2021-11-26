package ru.tensor.sabycomdemo.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import ru.tensor.sabycomdemo.R
import ru.tensor.sabycomdemo.databinding.SettingsFragmentBinding
import ru.tensor.sabycomdemo.demo.DemoFragment

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: SettingsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater)

        binding.startButton.setOnClickListener {
            viewModel.startWithData()
        }
        binding.anonymousButton.setOnClickListener {
            viewModel.startAnonymous()
        }

        binding.name.doOnTextChanged { text, _, _, _ ->
            viewModel.name = text.toString()
        }
        binding.surname.doOnTextChanged { text, _, _, _ ->
            viewModel.surname = text.toString()
        }
        binding.phone.doOnTextChanged { text, _, _, _ ->
            viewModel.phone = text.toString()
        }
        binding.email.doOnTextChanged { text, _, _, _ ->
            viewModel.email = text.toString()
        }
        binding.appId.doOnTextChanged { text, _, _, _ ->
            viewModel.appId = text.toString()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "it", Toast.LENGTH_SHORT).show()
        }
        viewModel.showDemo.observe(viewLifecycleOwner){
            startDemo()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillFields()
    }

    private fun fillFields() {
        binding.name.setText(viewModel.name)
        binding.surname.setText(viewModel.surname)
        binding.phone.setText(viewModel.phone)
        binding.email.setText(viewModel.email)
        binding.appId.setText(viewModel.appId)
        binding.stand.setSelection(viewModel.stand)
    }

    private fun startDemo() {
        parentFragmentManager.commit {
            replace(R.id.fragment_container_view, DemoFragment.newInstance())
        }
    }
}