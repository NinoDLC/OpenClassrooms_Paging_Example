package fr.delcey.paging.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.paging.databinding.MainActivityBinding
import fr.delcey.paging.ui.tracks.TracksFragment
import fr.delcey.paging.ui.utils.viewBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding { MainActivityBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(binding.mainFrameLayoutFragmentContainer.id, TracksFragment())
            }
        }
    }
}