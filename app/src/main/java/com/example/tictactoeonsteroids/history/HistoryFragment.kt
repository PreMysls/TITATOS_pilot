package com.example.tictactoeonsteroids.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tictactoeonsteroids.R
import com.example.tictactoeonsteroids.database.GameDatabase
import com.example.tictactoeonsteroids.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHistoryBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_history, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = GameDatabase.getInstance(application).gameDatabaseDao

        val viewModelFactory = HistoryViewModelFactory(dataSource, application)

        val viewModel =
            ViewModelProvider(
                this, viewModelFactory).get(HistoryViewModel::class.java)

        binding.historyViewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }

}