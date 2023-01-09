package fr.delcey.paging.data.paging

import fr.delcey.paging.domain.paging.PagingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class PagingRepositoryImpl @Inject constructor() : PagingRepository {

    private val currentPageMutableStateFlow = MutableStateFlow(0)
    override val currentPageFlow = currentPageMutableStateFlow.asStateFlow()

    override fun increaseCurrentPage() {
        currentPageMutableStateFlow.update { it + 1 }
    }
}