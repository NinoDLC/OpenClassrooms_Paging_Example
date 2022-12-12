package fr.delcey.paging.domain.player

import fr.delcey.paging.data.player.PlayerRepository
import fr.delcey.paging.data.player.model.PlayerStateEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPlayerStateUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) {

    fun invoke(): Flow<PlayerStateEntity?> = playerRepository.getPlayerStateFlow()
}