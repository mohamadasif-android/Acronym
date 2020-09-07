package com.safeway.acronyms.search

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.safeway.acronyms.R
import com.safeway.acronyms.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val searchAdapter = SearchAdapter()

    private val viewModel: SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupUi()
    }

    private fun setupUi() {
        binding.searchResult.adapter = searchAdapter
        viewModel.queryResult.observe(this) { handleSearchResult(it) }

        // Start with empty query view
        searchAdapter.submitList(emptyList())
        binding.otherResultText.visibility = View.VISIBLE
        binding.searchResult.visibility = View.GONE
        binding.otherResultText.setText(R.string.data_unavailable)
        binding.searchText.requestFocus()

        searchText.doAfterTextChanged {
            viewModel.setQueryText(it.toString())
        }
    }

    private fun handleSearchResult(it: SearchResult) {
        when (it) {
            is ValidResult -> {
                binding.otherResultText.visibility = View.GONE
                binding.searchResult.visibility = View.VISIBLE
                searchAdapter.submitList(it.result)
            }
            is ErrorResult -> {
                searchAdapter.submitList(emptyList())
                binding.otherResultText.visibility = View.VISIBLE
                binding.searchResult.visibility = View.GONE
                binding.otherResultText.text = getString(it.errorMsgResId)
            }
            is EmptyResult -> {
                searchAdapter.submitList(emptyList())
                binding.otherResultText.visibility = View.VISIBLE
                binding.searchResult.visibility = View.GONE
                binding.otherResultText.setText(R.string.empty_result)
            }
            is EmptyQuery -> {
                searchAdapter.submitList(emptyList())
                binding.otherResultText.visibility = View.VISIBLE
                binding.searchResult.visibility = View.GONE
                binding.otherResultText.setText(R.string.enter_valid_acronym)
            }
        }
    }
}
