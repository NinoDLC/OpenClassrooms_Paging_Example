package fr.delcey.paging.ui.tracks

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import fr.delcey.paging.R
import fr.delcey.paging.data.player.model.PlayerStateEntity
import fr.delcey.paging.data.track.model.TrackEntity
import fr.delcey.paging.domain.paging.UpdatePagingUseCase
import fr.delcey.paging.domain.player.GetPlayerStateUseCase
import fr.delcey.paging.domain.player.PlayUseCase
import fr.delcey.paging.domain.player.StopUseCase
import fr.delcey.paging.domain.tracks.GetPagedTracksUseCase
import fr.delcey.paging.test_utils.TestCoroutineRule
import fr.delcey.paging.test_utils.observeForTesting
import fr.delcey.paging.ui.utils.EquatableCallback
import fr.delcey.paging.ui.utils.NativeText
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TracksViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val application: Application = mockk()
    private val getPagedTracksUseCase: GetPagedTracksUseCase = mockk()
    private val updatePagingUseCase: UpdatePagingUseCase = mockk()
    private val getPlayerStateUseCase: GetPlayerStateUseCase = mockk()
    private val playUseCase: PlayUseCase = mockk()
    private val stopUseCase: StopUseCase = mockk()
    private val coroutineDispatcherProvider = testCoroutineRule.getCoroutineDispatcherProvider()

    private val tracksViewModel = TracksViewModel(
        application = application,
        getPagedTracksUseCase = getPagedTracksUseCase,
        updatePagingUseCase = updatePagingUseCase,
        getPlayerStateUseCase = getPlayerStateUseCase,
        playUseCase = playUseCase,
        stopUseCase = stopUseCase,
        coroutineDispatcherProvider = coroutineDispatcherProvider
    )

    @Before
    fun setUp() {
        every { getPagedTracksUseCase.invoke() } returns flowOf(getDefaultTrackEntities())
        every { getPlayerStateUseCase.invoke() } returns flowOf(getDefaultPlayerStateEntity())

        verify(exactly = 1) {
            coroutineDispatcherProvider.io
        }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        tracksViewModel.tracksUiModelsLiveData.observeForTesting(this) {
            // Then
            assertThat(it.value).isEqualTo(getDefaultTrackUiModels())
            verify(exactly = 1) {
                coroutineDispatcherProvider.io
                getPagedTracksUseCase.invoke()
                getPlayerStateUseCase.invoke()
            }
            confirmVerified()
        }
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // Given
        every { getPagedTracksUseCase.invoke() } returns flowOf(emptyList())

        // When
        tracksViewModel.tracksUiModelsLiveData.observeForTesting(this) {
            // Then
            assertThat(it.value).isEqualTo(listOf(TrackUiModel.Loading))
            verify(exactly = 1) {
                coroutineDispatcherProvider.io
                getPagedTracksUseCase.invoke()
                getPlayerStateUseCase.invoke()
            }
            confirmVerified()
        }
    }

    @Test
    fun `verify onLoadMore`() = testCoroutineRule.runTest {
        // Given
        justRun { updatePagingUseCase.invoke() }

        // When
        tracksViewModel.onLoadMore()

        // Then
        verify(exactly = 1) {
            updatePagingUseCase.invoke()
        }
        confirmVerified()
    }

    @Test
    fun `verify onStopClicked`() = testCoroutineRule.runTest {
        // Given
        justRun { stopUseCase.invoke() }

        // When
        tracksViewModel.onStopClicked()

        // Then
        verify(exactly = 1) {
            stopUseCase.invoke()
        }
        confirmVerified()
    }

    // region IN
    private fun getDefaultPlayerStateEntity() = PlayerStateEntity(
        playbackState = PlayerStateEntity.PlaybackState.PLAYING,
        trackId = 1
    )

    private fun getDefaultTrackEntities(): List<TrackEntity> = List(3) { index ->
        TrackEntity(
            id = index.toLong(),
        )
    }
    // endregion IN

    // region OUT
    private fun getDefaultTrackUiModels(): List<TrackUiModel> = List(3) { index ->
        TrackUiModel.Content(
            id = index.toLong(),
            title = NativeText.Argument(
                id = if (index == 1) {
                    R.string.playing_track
                } else {
                    R.string.track
                },
                arg = index.toLong()
            ),
            onClicked = EquatableCallback {}
        )
    } + TrackUiModel.Loading
    // endregion OUT
}