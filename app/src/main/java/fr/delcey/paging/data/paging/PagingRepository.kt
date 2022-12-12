package fr.delcey.paging.data.paging

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ViewModelScoped
class PagingRepository @Inject constructor() {

    private val currentPageMutableStateFlow = MutableStateFlow(0)
    val currentPageFlow = currentPageMutableStateFlow.asStateFlow()

    fun increaseCurrentPage() {
        currentPageMutableStateFlow.update { it + 1 }
    }
}