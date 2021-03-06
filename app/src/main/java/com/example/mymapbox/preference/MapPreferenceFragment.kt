package com.example.mymapbox.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mymapbox.MainViewModel
import com.example.mymapbox.databinding.FragmentMapPreferenceBinding

class MapPreferenceFragment : Fragment() {
	private var binding: FragmentMapPreferenceBinding? = null

	private val viewModel by activityViewModels<MainViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		binding = FragmentMapPreferenceBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		showInitialSettings()
		writeSettingsUpdate()
	}

	private fun showInitialSettings() {
		binding?.switchDarkMode?.isChecked = viewModel.isDarkModeEnabled
		binding?.switchTraffic?.isChecked = viewModel.isTrafficFlowsEnabled
	}

	private fun writeSettingsUpdate() {
		binding?.switchDarkMode?.setOnCheckedChangeListener { _, isChecked ->
			if (isChecked != viewModel.isDarkModeEnabled) {
				viewModel.setDarkModeEnabled(isChecked)
				Toast.makeText(
					context, "New settings will be loaded after app restarts", Toast.LENGTH_SHORT
				).show()
			}
		}
		binding?.switchTraffic?.setOnCheckedChangeListener { _, isChecked ->
			if (isChecked != viewModel.isTrafficFlowsEnabled) {
				viewModel.setTrafficFlowsEnabled(isChecked)
				Toast.makeText(
					context, "New settings will be loaded after app restarts", Toast.LENGTH_SHORT
				).show()
			}
		}
	}
}