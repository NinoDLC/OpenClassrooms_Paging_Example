package fr.delcey.paging.domain.tracks

import fr.delcey.paging.data.paging.PagingRepository
import fr.delcey.paging.data.track.TrackRepository
import fr.delcey.paging.data.track.model.TrackEntity
import fr.delcey.paging.domain.core.HttpResult
import fr.delcey.paging.domain.core.HttpResult.HttpError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class GetPagedTracksUseCase @Inject constructor(
    private val pagingRepository: PagingRepository,
    private val trackRepository: TrackRepository,
) {
    companion object {
        private const val LIMIT = 30

        private const val MAXIMUM_RETRIES = 5
    }

    fun invoke(): Flow<PagedTracksResult> = flow {
        val aggregated = mutableListOf<TrackEntity>()

        pagingRepository.currentPageFlow.collect { page ->
            emit(queryData(page, aggregated, 0))
        }
    }

    private suspend fun queryData(page: Int, aggregated: MutableList<TrackEntity>, repeatCount: Int = 0): PagedTracksResult =
        when (val result = trackRepository.getTracks(limit = LIMIT, offset = page * LIMIT + 1)) {
            is HttpResult.Success -> {
                aggregated.addAll(result.data)
                if (result.data.isNotEmpty()) {
                    PagedTracksResult(aggregated, QueryState.SUCCESS)
                } else {
                    PagedTracksResult(aggregated, QueryState.EMPTY)
                }
            }
            is HttpError.Critical -> PagedTracksResult(aggregated, QueryState.CRITICAL_ERROR)
            is HttpError.IO -> {
                delay((repeatCount * repeatCount).seconds)

                if (repeatCount < MAXIMUM_RETRIES) {
                    queryData(page, aggregated, repeatCount + 1)
                } else {
                    PagedTracksResult(aggregated, QueryState.TOO_MANY_RETRIES)
                }
            }
        }

    data class PagedTracksResult(
        val aggregated: List<TrackEntity>,
        val lastQueryState: QueryState
    )

    enum class QueryState {
        SUCCESS,
        EMPTY,
        TOO_MANY_RETRIES,
        CRITICAL_ERROR;
    }
}