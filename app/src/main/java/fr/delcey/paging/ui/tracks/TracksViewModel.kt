package fr.delcey.paging.ui.tracks

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.paging.data.player.model.PlayerStateEntity
import fr.delcey.paging.data.player.model.PlayerStateEntity.PlaybackState.PLAYING
import fr.delcey.paging.data.track.model.TrackEntity
import fr.delcey.paging.domain.CoroutineDispatcherProvider
import fr.delcey.paging.domain.paging.UpdatePagingUseCase
import fr.delcey.paging.domain.player.GetPlayerStateUseCase
import fr.delcey.paging.domain.player.PlayUseCase
import fr.delcey.paging.domain.player.StopUseCase
import fr.delcey.paging.domain.tracks.GetPagedTracksUseCase
import fr.delcey.paging.ui.tracks.TrackUiModel
import fr.delcey.paging.ui.utils.EquatableCallback
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class TracksViewModel @Inject constructor(
    private val application: Application,
    private val getPagedTracksUseCase: GetPagedTracksUseCase,
    private val updatePagingUseCase: UpdatePagingUseCase,
    private val getPlayerStateUseCase: GetPlayerStateUseCase,
    private val playUseCase: PlayUseCase,
    private val stopUseCase: StopUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val tracksUiModelsLiveData: LiveData<List<TrackUiModel>> = liveData(coroutineDispatcherProvider.io) {
        combine(
            getPagedTracksUseCase.invoke(),
            getPlayerStateUseCase.invoke()
        ) { tracks, playerStateEntity ->
            emit(
                map(tracks, playerStateEntity) + TrackUiModel.Loading
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
                Toast.makeText(
                    application,
                    "Track #${track.id} clicked, in list[${tracks.first().id},${tracks.last().id}]",
                    Toast.LENGTH_LONG
                ).show()

                playUseCase.invoke(track.id)
            }
        )
    }
}