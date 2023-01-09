package fr.delcey.paging.domain.tracks

import fr.delcey.paging.data.track.model.TrackEntity

interface TrackRepository {
    suspend fun getTracks(limit: Int, offset: Int) : List<TrackEntity>
}
