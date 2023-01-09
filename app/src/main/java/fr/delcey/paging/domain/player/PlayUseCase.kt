package fr.delcey.paging.domain.player

import javax.inject.Inject

class PlayUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) {

    fun invoke(trackId: Long) {
        playerRepository.play(trackId)
    }
}