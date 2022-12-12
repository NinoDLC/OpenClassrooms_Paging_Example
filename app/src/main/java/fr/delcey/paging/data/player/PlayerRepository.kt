package fr.delcey.paging.data.player

import fr.delcey.paging.data.player.model.PlayerStateEntity
import fr.delcey.paging.data.player.model.PlayerStateEntity.PlaybackState
import fr.delcey.paging.data.player.model.PlayerStateEntity.PlaybackState.PLAYING
import fr.delcey.paging.data.player.model.PlayerStateEntity.PlaybackState.STOPPED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor() {

    private val trackIdMutableStateFlow = MutableStateFlow<Long?>(null)
    private val playbackStateMutableStateFlow = MutableStateFlow(STOPPED)

    fun getPlayerStateFlow(): Flow<PlayerStateEntity?> = combine(
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

    fun play(trackId: Long) {
        trackIdMutableStateFlow.value = trackId
        playbackStateMutableStateFlow.value = PLAYING
    }

    fun stop() {
        playbackStateMutableStateFlow.value = STOPPED
    }
}