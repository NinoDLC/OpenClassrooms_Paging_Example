package fr.delcey.paging.test_utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent

/**
 * Observes a [LiveData] until the `block` is done executing.
 */
fun <T> LiveData<T>.observeForTesting(
    testScope: TestScope,
    pause: Boolean = false,
    block: (LiveData<T>) -> Unit,
) {
    val observer = Observer<T> { }
    try {
        observeForever(observer)
        if (!pause) {
            testScope.runCurrent()
        }
        block(this)
    } finally {
        removeObserver(observer)
    }
}