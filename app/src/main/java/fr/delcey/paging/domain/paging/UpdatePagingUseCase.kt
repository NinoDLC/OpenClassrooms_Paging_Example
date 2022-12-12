package fr.delcey.paging.domain.paging

import fr.delcey.paging.data.paging.PagingRepository
import javax.inject.Inject

class UpdatePagingUseCase @Inject constructor(
    private val pagingRepository: PagingRepository
) {

    fun invoke() {
        pagingRepository.increaseCurrentPage()
    }
}
