package fr.delcey.paging.domain.player

import fr.delcey.paging.data.player.PlayerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) {

    fun invoke(trackId: Long) {
        playerRepository.play(trackId)
    }
}