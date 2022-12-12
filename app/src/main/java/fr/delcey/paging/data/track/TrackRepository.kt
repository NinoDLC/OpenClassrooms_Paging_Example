package fr.delcey.paging.data.track

import android.util.Log
import fr.delcey.paging.data.track.model.TrackEntity
import fr.delcey.paging.domain.core.HttpResult
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class TrackRepository @Inject constructor() {

    suspend fun getTracks(limit: Int, offset: Int): HttpResult<List<TrackEntity>> {
        delay(Random.nextInt(1_000).milliseconds)
        return when (Random.nextInt(5)) {
            0 -> HttpResult.HttpError.IO // 20 % chance
         //   1 -> HttpResult.HttpError.Critical
            else -> HttpResult.Success( // 80 % chance
                if (offset >= 400) {
                    emptyList()
                } else {
                    List(limit) {
                        val index = it.toLong() + offset
                        TrackEntity(index)
                    }
                }
            )
        }.also {
            Log.d("Nino", "TrackRepository.getTracks() called with: limit = $limit, offset = $offset, result is = ${it.javaClass.simpleName}")
        }
    }
}