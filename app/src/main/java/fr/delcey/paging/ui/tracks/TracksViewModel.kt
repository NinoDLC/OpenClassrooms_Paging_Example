package fr.delcey.paging.ui.tracks

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.paging.data.player.model.PlayerStateEntity
import fr.delcey.paging.data.player.model.PlayerStateEntity.PlaybackState.PLAYING
import fr.delcey.paging.data.track.model.TrackEntity
import fr.delcey.paging.domain.core.CoroutineDispatcherProvider
import fr.delcey.paging.domain.paging.UpdatePagingUseCase
import fr.delcey.paging.domain.player.GetPlayerStateUseCase
import fr.delcey.paging.domain.player.PlayUseCase
import fr.delcey.paging.domain.player.StopUseCase
import fr.delcey.paging.domain.tracks.GetPagedTracksUseCase
import fr.delcey.paging.domain.tracks.GetPagedTracksUseCase.QueryState
import fr.delcey.paging.ui.utils.EquatableCallback
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TracksViewModel @Inject constructor(
    private val application: Application,
    private val getPagedTracksUseCase: GetPagedTracksUseCase,
    private val updatePagingUseCase: UpdatePagingUseCase,
    private val getPlayerStateUseCase: GetPlayerStateUseCase,
    private val playUseCase: PlayUseCase,
    private val stopUseCase: StopUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val tracksUiModelsLiveData: LiveData<List<TrackUiModel>> = liveData(coroutineDispatcherProvider.io) {
        combine(
            getPagedTracksUseCase.invoke().onEach { tracksResult ->
                if (tracksResult.lastQueryState == QueryState.TOO_MANY_RETRIES || tracksResult.lastQueryState == QueryState.CRITICAL_ERROR) {
                    displayMessage("Query failed! Error: ${tracksResult.lastQueryState}")
                }
            },
            getPlayerStateUseCase.invoke()
        ) { tracks, playerStateEntity ->
            val mapped = map(tracks.aggregated, playerStateEntity)
            emit(
                if (tracks.lastQueryState != QueryState.SUCCESS) {
                    mapped
                } else {
                    mapped + TrackUiModel.Loading
                }
            )
        }.collect()
    }

    fun onLoadMore() {
        updatePagingUseCase.invoke()
    }

    fun onStopClicked() {
        stopUseCase.invoke()
    }

    private fun map(
        tracks: List<TrackEntity>,
        playerStateEntity: PlayerStateEntity?
    ): List<TrackUiModel.Content> = tracks.map { track ->
        TrackUiModel.Content(
            id = track.id,
            title = if (playerStateEntity?.playbackState == PLAYING && playerStateEntity.trackId == track.id) {
                "Track #${track.id} is currently playing..."
            } else {
                "Track #${track.id}"
            },
            onClicked = EquatableCallback {
                viewModelScope.launch {
                    displayMessage(
                        message = "Track #${track.id} clicked, in list[${tracks.first().id},${tracks.last().id}]",
                        duration = Toast.LENGTH_LONG
                    )

                    playUseCase.invoke(track.id)
                }
            }
        )
    }

    private suspend fun displayMessage(message: String, @Duration duration: Int = Toast.LENGTH_SHORT) {
        withContext(coroutineDispatcherProvider.main) {
            Toast.makeText(
                application,
                message,
                duration
            ).show()
        }
    }
}