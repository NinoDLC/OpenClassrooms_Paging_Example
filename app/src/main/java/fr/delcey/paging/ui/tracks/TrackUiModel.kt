package fr.delcey.paging.ui.tracks

import fr.delcey.paging.ui.utils.EquatableCallback
import fr.delcey.paging.ui.utils.NativeText

sealed class TrackUiModel(val type: Type) {

    data class Content(
        val id: Long,
        val title: NativeText,
        val onClicked: EquatableCallback,
    ) : TrackUiModel(Type.CONTENT)

    object Loading : TrackUiModel(Type.LOADING)

    enum class Type {
        LOADING,
        CONTENT,
    }
}
