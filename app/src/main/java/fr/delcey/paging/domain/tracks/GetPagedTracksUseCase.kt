package fr.delcey.paging.domain.tracks

import fr.delcey.paging.data.track.model.TrackEntity
import fr.delcey.paging.domain.paging.PagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPagedTracksUseCase @Inject constructor(
    private val pagingRepository: PagingRepository,
    private val trackRepository: TrackRepository,
) {
    companion object {
        private const val LIMIT = 30
    }

    fun invoke(): Flow<List<TrackEntity>> = flow {
        val aggregated = mutableListOf<TrackEntity>()

        pagingRepository.currentPageFlow.map { page ->
            trackRepository.getTracks(LIMIT, page * LIMIT + 1)
        }.collect {
            aggregated.addAll(it)
            emit(aggregated)
        }
    }
}