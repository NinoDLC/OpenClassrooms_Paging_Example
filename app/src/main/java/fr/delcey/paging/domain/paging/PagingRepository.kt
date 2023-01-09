package fr.delcey.paging.domain.paging

import kotlinx.coroutines.flow.StateFlow

interface PagingRepository {
    val currentPageFlow: StateFlow<Int>
    fun increaseCurrentPage()
}