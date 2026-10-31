package ru.tensor.sabycomdemo.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import ru.tensor.sabycomdemo.R
import ru.tensor.sabycomdemo.databinding.SettingsFragmentBinding
import ru.tensor.sabycomdemo.demo.DemoFragment
import kotlin.system.exitProcess

/**
 * Фрагмент настроек, показывается на главном экране
 *
 * @author ma.kolpakov
 */
internal class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.showDemo.observe(viewLifecycleOwner) {
            it.handleEvent { startDemo() }
        }

        viewModel.restart.observe(viewLifecycleOwner) {
            it.handleEvent { restartApp(requireContext()) }
        }

        return binding.root
    }

    private fun startDemo() {
        parentFragmentManager.commit {
            replace(R.id.fragment_container_view, DemoFragment())
            setReorderingAllowed(true)
            addToBackStack("Demo")
        }
    }

    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        exitProcess(0)
    }
}