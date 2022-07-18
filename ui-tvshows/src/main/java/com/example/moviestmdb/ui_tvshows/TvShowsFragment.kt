package com.example.moviestmdb.ui_tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moviestmdb.ui_tvshows.databinding.TvshowsFragmentBinding

class TvShowsFragment :Fragment() {

    lateinit var binding : TvshowsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TvshowsFragmentBinding.inflate(inflater)
        return binding.root
    }
}