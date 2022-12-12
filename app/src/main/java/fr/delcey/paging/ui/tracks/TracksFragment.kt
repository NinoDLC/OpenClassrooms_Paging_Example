package fr.delcey.paging.ui.tracks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.paging.R
import fr.delcey.paging.databinding.TracksFragmentBinding
import fr.delcey.paging.ui.utils.InfiniteScrollListener
import fr.delcey.paging.ui.utils.viewBinding

@AndroidEntryPoint
class TracksFragment : Fragment(R.layout.tracks_fragment) {

    private val binding by viewBinding { TracksFragmentBinding.bind(it) }
    private val viewModel by viewModels<TracksViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TracksAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.tracksRecyclerView.layoutManager = layoutManager
        binding.tracksRecyclerView.adapter = adapter
        binding.tracksRecyclerView.itemAnimator = null
        binding.tracksRecyclerView.addOnScrollListener(
            InfiniteScrollListener(layoutManager) {
                viewModel.onLoadMore()
            }
        )

        binding.tracksButtonStop.setOnClickListener {
            viewModel.onStopClicked()
        }

        viewModel.tracksUiModelsLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}