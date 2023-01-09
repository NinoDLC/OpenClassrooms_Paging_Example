package fr.delcey.paging.ui.utils

import android.content.Context
import android.widget.TextView
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class NativeText {
    data class Simple(val text: String) : NativeText()
    data class Resource(@StringRes val id: Int) : NativeText()
    data class Plural(@PluralsRes val id: Int, val number: Int, val args: List<Any>) : NativeText()
    data class Argument(@StringRes val id: Int, val arg: Any) : NativeText()
    data class Arguments(@StringRes val id: Int, val args: List<Any>) : NativeText()
    data class Multi(val text: List<NativeText>) : NativeText()
}

fun TextView.setText(nativeText: NativeText) {
    text = nativeText.toCharSequence(context)
}

private fun NativeText.toCharSequence(context: Context): CharSequence = when (this) {
    is NativeText.Simple -> text
    is NativeText.Argument -> context.getString(id, arg)
    is NativeText.Arguments -> context.getString(id, *args.toTypedArray())
    is NativeText.Resource -> context.getString(id)
    is NativeText.Plural -> context.resources.getQuantityString(id, number, *args.toTypedArray())
    is NativeText.Multi -> {
        val builder = StringBuilder()
        for (t in text) {
            builder.append(t.toCharSequence(context))
        }
        builder.toString()
    }
}