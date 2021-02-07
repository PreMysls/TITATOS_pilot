package com.example.tictactoeonsteroids.firstscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.tictactoeonsteroids.R
import com.example.tictactoeonsteroids.database.GameDatabase
import com.example.tictactoeonsteroids.databinding.FragmentFirstScreenBinding


class FirstScreenFragment : Fragment() {
    private lateinit var viewModel: FirstScreenViewModel
    private lateinit var binding: FragmentFirstScreenBinding
    private lateinit var viewModelFactory: FirstScreenViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_first_screen, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = GameDatabase.getInstance(application).gameDatabaseDao

        viewModelFactory =
            FirstScreenViewModelFactory(
                dataSource, application
            )
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(FirstScreenViewModel::class.java)

        binding.firstScreenViewModel = viewModel
        binding.lifecycleOwner = this

        binding.historyButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_firstScreenFragment2_to_historyFragment)
        )

        // Add an Observer on the state variable for Navigating when STOP button is pressed.
        viewModel.navigateToGame.observe(viewLifecycleOwner, Observer { gameId ->
            gameId?.let {
                this.findNavController().navigate(FirstScreenFragmentDirections.actionFirstScreenFragment2ToGameFragment2(gameId))
                viewModel.doneNavigating()
            }
        })

        return binding.root

        //return inflater.inflate(R.layout.fragment_first_screen, container, false)
    }

}