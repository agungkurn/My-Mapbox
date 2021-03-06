package com.example.mymapbox.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mymapbox.MainViewModel
import com.example.mymapbox.core.SearchResultAdapter
import com.example.mymapbox.core.model.DataState
import com.example.mymapbox.core.model.FeaturesItem
import com.example.mymapbox.core.model.SearchingState
import com.example.mymapbox.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
	private val TAG = SearchFragment::class.java.simpleName
	private val viewModel: MainViewModel by activityViewModels()

	private var binding: FragmentSearchBinding? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		binding = FragmentSearchBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		binding?.inputSearch?.editText?.requestFocus()
		setupSearchField()
	}

	private fun setupSearchField() {
		binding?.inputSearch?.editText?.doAfterTextChanged {
			it?.let {
				if (it.toString().isNotEmpty()) {
					viewModel.searchLocation(it.toString())
				} else {
					showSearchResult(emptyList())
				}
			}
		}

		viewModel.searchResult.observe(viewLifecycleOwner) {
			when (it) {
				is DataState.Loading -> {
					binding?.pbSearch?.visibility = View.VISIBLE
					binding?.rvSearch?.visibility = View.GONE
				}
				is DataState.Success -> {
					binding?.pbSearch?.visibility = View.GONE
					binding?.rvSearch?.visibility = View.VISIBLE

					showSearchResult(it.data)
				}
				is DataState.Failed -> {
					binding?.pbSearch?.visibility = View.GONE
					binding?.rvSearch?.visibility = View.VISIBLE

					it.errorMessage?.let {
						Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
					}

					it.exception?.let {
						Log.e(
							TAG,
							"setupSearchField: ${it::class.java.simpleName}: ${it.localizedMessage}"
						)
					}
				}
			}
		}
	}

	private fun showSearchResult(items: List<FeaturesItem>) {
		SearchResultAdapter(items) {
			// Hide soft keyboard
//			(activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
//				.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
			binding?.inputSearch?.editText?.clearFocus()
			(activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
				.hideSoftInputFromWindow(binding?.inputSearch?.editText?.windowToken, 0)

			viewModel.foundPlacePosition.value = SearchingState.FoundPlace(it)
		}.also {
			binding?.rvSearch?.adapter = it
			it.notifyDataSetChanged()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}
}