package fr.delcey.paging.domain.player

import fr.delcey.paging.data.player.model.PlayerStateEntity
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun getPlayerStateFlow(): Flow<PlayerStateEntity?>
    fun play(trackId: Long)
    fun stop()
}