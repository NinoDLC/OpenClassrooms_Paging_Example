package fr.delcey.paging.data.player

import fr.delcey.paging.data.player.model.PlayerStateEntity
import fr.delcey.paging.data.player.model.PlayerStateEntity.PlaybackState
import fr.delcey.paging.data.player.model.PlayerStateEntity.PlaybackState.PLAYING
import fr.delcey.paging.data.player.model.PlayerStateEntity.PlaybackState.STOPPED
import fr.delcey.paging.domain.player.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(): PlayerRepository {

    private val trackIdMutableStateFlow = MutableStateFlow<Long?>(null)
    private val playbackStateMutableStateFlow = MutableStateFlow(STOPPED)

    override fun getPlayerStateFlow(): Flow<PlayerStateEntity?> = combine(
        trackIdMutableStateFlow,
        playbackStateMutableStateFlow
    ) { trackId: Long?, playbackState: PlaybackState ->
        trackId?.let {
            PlayerStateEntity(
                playbackState = playbackState,
                trackId = trackId,
            )
        }
    }

    override fun play(trackId: Long) {
        trackIdMutableStateFlow.value = trackId
        playbackStateMutableStateFlow.value = PLAYING
    }

    override fun stop() {
        playbackStateMutableStateFlow.value = STOPPED
    }
}