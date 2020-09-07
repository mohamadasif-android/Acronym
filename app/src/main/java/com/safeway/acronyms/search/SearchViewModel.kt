package com.safeway.acronyms.search

import androidx.annotation.StringRes
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeway.acronyms.data.remote.Result
import com.safeway.acronyms.repository.SearchRepository
import com.safeway.utils.Constants.Companion.SEARCH_DELAY_MS
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private var queryText = MutableStateFlow("")

    private var _queryResult = MutableLiveData<SearchResult>(EmptyResult)
    val queryResult: LiveData<SearchResult>
        get() = _queryResult

    init {

        viewModelScope.launch {
            queryText
                .debounce(SEARCH_DELAY_MS)
                .filter { query ->
                    if (query.isEmpty()) {
                        _queryResult.value = EmptyQuery
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    searchRepository.dataFromNetwork(query)
                }
                .collect { result ->
                    when (result.status) {
                        Result.Status.SUCCESS -> {
                            if (!result.data.isNullOrEmpty()) {
                                val resultList = result.data[0].lfs.map { it.lf }
                                _queryResult.value = ValidResult(resultList)
                            } else {
                                _queryResult.value = EmptyResult
                            }
                        }
                        Result.Status.ERROR -> {
                            _queryResult.value = ErrorResult(result.messageResId!!)
                        }
                    }
                }
        }
    }

    fun setQueryText(query: String) {
        queryText.value = query
    }
}

sealed class SearchResult
class ValidResult(val result: List<String>) : SearchResult()
object EmptyResult : SearchResult()
object EmptyQuery : SearchResult()
class ErrorResult(@StringRes val errorMsgResId: Int) : SearchResult()
