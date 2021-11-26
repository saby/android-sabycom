package ru.tensor.sabycomdemo.demo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycomdemo.databinding.DemoFragmentBinding

class DemoFragment : Fragment() {

    companion object {
        fun newInstance() = DemoFragment()
    }

    private val viewModel: DemoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DemoFragmentBinding.inflate(inflater)

        binding.showSabycom.setOnClickListener {
            Sabycom.show(requireActivity() as AppCompatActivity)
        }

        viewModel.messageCounter.observe(viewLifecycleOwner) {
            binding.counter.text = it.toString()
        }
        return binding.root
    }
}