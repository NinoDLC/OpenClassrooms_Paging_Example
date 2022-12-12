package fr.delcey.paging.ui.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfiniteScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val visibleThreshold: Int = DEFAULT_VISIBLE_THRESHOLD,
    private val onLoadMore: () -> Unit,
) : RecyclerView.OnScrollListener() {

    companion object {
        // The minimum number of items remaining before we should loading more.
        private const val DEFAULT_VISIBLE_THRESHOLD = 5
    }

    private var yTotalScrolled = 0
    private var yMax = 0

    private var maxDispatchedEvenItemCount = -1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        yTotalScrolled += dy

        if (dy < 0) {
            return
        }

        val totalItemCount = layoutManager.itemCount
        if (totalItemCount > maxDispatchedEvenItemCount && yTotalScrolled >= yMax) {
            yMax = yTotalScrolled
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            if (lastVisibleItemPosition == RecyclerView.NO_POSITION
                || lastVisibleItemPosition >= totalItemCount - visibleThreshold
            ) {
                maxDispatchedEvenItemCount = totalItemCount
                onLoadMore()
            }
        }
    }
}