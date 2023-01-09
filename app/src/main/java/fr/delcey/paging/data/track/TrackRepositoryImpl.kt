package fr.delcey.paging.data.track

import fr.delcey.paging.data.track.model.TrackEntity
import fr.delcey.paging.domain.tracks.TrackRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class TrackRepositoryImpl @Inject constructor() : TrackRepository {

    override suspend fun getTracks(limit: Int, offset: Int): List<TrackEntity> {
        delay(Random.nextInt(1_000).milliseconds)

        return List(limit) {
            val index = it.toLong() + offset
            TrackEntity(index)
        }
    }
}