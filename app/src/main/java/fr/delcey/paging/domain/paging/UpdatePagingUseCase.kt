package fr.delcey.paging.domain.paging

import javax.inject.Inject

class UpdatePagingUseCase @Inject constructor(
    private val pagingRepository: PagingRepository
) {

    fun invoke() {
        pagingRepository.increaseCurrentPage()
    }
}
