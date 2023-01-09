package fr.delcey.paging.ui.tracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.delcey.paging.databinding.TracksItemBinding
import fr.delcey.paging.databinding.TracksItemLoadingBinding
import fr.delcey.paging.ui.utils.setText

class TracksAdapter : ListAdapter<TrackUiModel, RecyclerView.ViewHolder>(PokemonDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (TrackUiModel.Type.values()[viewType]) {
        TrackUiModel.Type.CONTENT -> TrackContentViewHolder.newInstance(parent)
        TrackUiModel.Type.LOADING -> TrackLoadingViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is TrackUiModel.Content -> (holder as TrackContentViewHolder).bind(item)
            is TrackUiModel.Loading -> Unit // Just display the loading
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).type.ordinal

    class TrackContentViewHolder(private val binding: TracksItemBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun newInstance(parent: ViewGroup) = TrackContentViewHolder(
                TracksItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        fun bind(uiModel: TrackUiModel.Content) {
            binding.root.setOnClickListener {
                uiModel.onClicked.invoke()
            }

            binding.trackItemTitle.setText(uiModel.title)
        }
    }

    object TrackLoadingViewHolder {
        fun newInstance(parent: ViewGroup) = object : RecyclerView.ViewHolder(
            TracksItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
        ) {}
    }

    object PokemonDiffUtil : DiffUtil.ItemCallback<TrackUiModel>() {
        override fun areItemsTheSame(oldItem: TrackUiModel, newItem: TrackUiModel) =
            oldItem is TrackUiModel.Loading && newItem is TrackUiModel.Loading
                || oldItem is TrackUiModel.Content && newItem is TrackUiModel.Content
                && oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TrackUiModel, newItem: TrackUiModel) =
            oldItem == newItem
    }
}

