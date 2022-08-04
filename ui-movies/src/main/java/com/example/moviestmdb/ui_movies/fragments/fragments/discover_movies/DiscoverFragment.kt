package com.example.moviestmdb.ui_movies.fragments.fragments.discover_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.moviestmdb.ui_movies.R
import com.example.moviestmdb.ui_movies.databinding.FragmentDiscoverMoviesBinding
import com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies.FilterBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DiscoverFragment : Fragment() {
    lateinit var binding: FragmentDiscoverMoviesBinding
    private val viewModel: DiscoverViewModel by viewModels()

    var bottomTag: String = "FilterBottomSheet"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVars()
    }

    private fun initVars() {
        setupWithNavController(binding.toolbar, findNavController())
        binding.toolbar.title = "Discover Movies"
        binding.toolbar.inflateMenu(R.menu.filter_menu)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filter_movies -> showFilterBottomSheet()
            }
            true
        }
    }

    private fun showFilterBottomSheet() {
        val callback: (response: HashMap<String, String>) -> Unit = { filterData ->
            Timber.i("$filterData")
        }

        val filterBottomSheet = FilterBottomSheet()
        filterBottomSheet.listener = callback
        filterBottomSheet.show(childFragmentManager, bottomTag)
    }

}