package fr.delcey.paging.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.delcey.paging.data.player.PlayerRepositoryImpl
import fr.delcey.paging.data.track.TrackRepositoryImpl
import fr.delcey.paging.domain.player.PlayerRepository
import fr.delcey.paging.domain.tracks.TrackRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindingModule {
    @Binds
    @Singleton
    abstract fun bindPlayerRepository(impl: PlayerRepositoryImpl): PlayerRepository

    @Binds
    @Singleton
    abstract fun bindTrackRepository(impl: TrackRepositoryImpl): TrackRepository
}