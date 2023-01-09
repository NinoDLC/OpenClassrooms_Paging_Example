package fr.delcey.paging.domain.player

import javax.inject.Inject

class StopUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) {

    fun invoke() {
        playerRepository.stop()
    }
}