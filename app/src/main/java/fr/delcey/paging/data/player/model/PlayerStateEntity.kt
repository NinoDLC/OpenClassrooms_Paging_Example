package fr.delcey.paging.data.player.model

data class PlayerStateEntity(
    val playbackState: PlaybackState,
    val trackId: Long,
) {
    enum class PlaybackState {
        PLAYING,
        STOPPED
    }
}
