package fr.delcey.paging.data.track

import fr.delcey.paging.data.track.model.TrackEntity
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class TrackRepository @Inject constructor() {

    suspend fun getTracks(limit: Int, offset: Int): List<TrackEntity> {
        delay(Random.nextInt(1_000).milliseconds)

        return List(limit) {
            val index = it.toLong() + offset
            TrackEntity(index)
        }
    }
}