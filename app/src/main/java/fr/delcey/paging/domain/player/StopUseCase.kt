package fr.delcey.paging.domain.player

import fr.delcey.paging.data.player.PlayerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) {

    fun invoke() {
        playerRepository.stop()
    }
}