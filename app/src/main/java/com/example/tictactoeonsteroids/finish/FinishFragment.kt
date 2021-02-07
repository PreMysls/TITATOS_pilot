package com.example.tictactoeonsteroids.finish

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.tictactoeonsteroids.R
import com.example.tictactoeonsteroids.data.GameButton
import com.example.tictactoeonsteroids.database.GameDatabase
import com.example.tictactoeonsteroids.databinding.FragmentFinishBinding
import com.example.tictactoeonsteroids.enums.AttributeEnum
import com.example.tictactoeonsteroids.enums.AttributeValueEnum
import com.example.tictactoeonsteroids.enums.PositionsEnum
import com.example.tictactoeonsteroids.enums.Scale
import com.example.tictactoeonsteroids.firstscreen.FirstScreenFragmentDirections
import com.example.tictactoeonsteroids.game.GameFragmentArgs


class FinishFragment : Fragment() {
    private lateinit var viewModel: FinishViewModel
    private lateinit var binding: FragmentFinishBinding
    private lateinit var viewModelFactory: FinishViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_finish, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = GameDatabase.getInstance(application).gameDatabaseDao
        val arguments = FinishFragmentArgs.fromBundle(requireArguments())

        viewModelFactory = FinishViewModelFactory(
            dataSource,
            application,
            playerWon = arguments.playerWon,
            positions = arguments.position,
            attribute = arguments.attribute,
            attributeValue = arguments.value
        )



        viewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(FinishViewModel::class.java)

        binding.finishViewModel = viewModel
        binding.lifecycleOwner = this



        val backgroundDefault: Drawable? =
            resources.getDrawable(R.drawable.ic_baseline_crop_free_24, null).constantState?.newDrawable()?.mutate()
        val backgroundFirst: Drawable? =
            resources.getDrawable(R.drawable.ic_baseline_ac_unit_24, null).constantState?.newDrawable()?.mutate()
        val backgroundSecond: Drawable? =
            resources.getDrawable(R.drawable.ic_baseline_local_fire_department_24, null).constantState?.newDrawable()?.mutate()

        val colorDefault = resources.getColor(R.color.grey, null)
        val colorFirst = resources.getColor(R.color.orange_400, null)
        val colorSecond = resources.getColor(R.color.green_400, null)

        val colorWon = resources.getColor(R.color.green, null)
        val colorLost = resources.getColor(R.color.red, null)



        val gameButtonList = listOf(
            binding.gamePlaceButton1,
            binding.gamePlaceButton2,
            binding.gamePlaceButton3,
            binding.gamePlaceButton4,
            binding.gamePlaceButton5,
            binding.gamePlaceButton6,
            binding.gamePlaceButton7,
            binding.gamePlaceButton8,
            binding.gamePlaceButton9
        )

        fun getBackground(attributeValueEnum: AttributeValueEnum) = when (attributeValueEnum) {
            AttributeValueEnum.DEFAULT -> backgroundDefault?.constantState?.newDrawable()?.mutate()
            AttributeValueEnum.FIRST -> backgroundFirst?.constantState?.newDrawable()?.mutate()
            AttributeValueEnum.SECOND -> backgroundSecond?.constantState?.newDrawable()?.mutate()
        }

        fun getColor(attributeValueEnum: AttributeValueEnum) = when (attributeValueEnum) {
            AttributeValueEnum.DEFAULT -> colorDefault
            AttributeValueEnum.FIRST -> colorFirst
            AttributeValueEnum.SECOND -> colorSecond
        }

        fun getScaleX(attributeValueEnum: AttributeValueEnum) = when (attributeValueEnum) {
            AttributeValueEnum.DEFAULT -> Scale.MEDIUM.size
            AttributeValueEnum.FIRST -> Scale.LARGE.size
            AttributeValueEnum.SECOND -> Scale.SMALL.size
        }

        fun getScaleY(attributeValueEnum: AttributeValueEnum) = when (attributeValueEnum) {
            AttributeValueEnum.DEFAULT -> Scale.MEDIUM.size
            AttributeValueEnum.FIRST -> Scale.SMALL.size
            AttributeValueEnum.SECOND -> Scale.LARGE.size
        }

        fun refreshGameButton(position: Int, attribute: AttributeEnum = arguments.attribute, value: AttributeValueEnum = arguments.value, visibility: Int = 4) {
            when(attribute) {
                AttributeEnum.BACKGROUND -> gameButtonList[position].background = getBackground(value)
                AttributeEnum.COLOR -> gameButtonList[position].background.setTint(getColor(value))
                AttributeEnum.SCALE -> {gameButtonList[position].scaleX = getScaleX(value)
                                        gameButtonList[position].scaleY = getScaleY(value)}
            }
            gameButtonList[position].visibility = visibility
        }


//        binding.resultButton.text = if(arguments.playerWon) R.string.player_won.toString() else R.string.player_lost.toString()
        binding.resultButton.text = if(arguments.playerWon) "YOU HAVE WON!" else "YOU HAVE LOST"
        binding.playAgainButton.text = "PLAY AGAIN"
        binding.resultButton.background.setTint(if(arguments.playerWon) colorWon else colorLost)
        binding.playAgainButton.background.setTint(if(arguments.playerWon) colorWon else colorLost)

        PositionsEnum.ALL.coordinates.forEach { refreshGameButton(it, AttributeEnum.BACKGROUND, AttributeValueEnum.DEFAULT) }
        PositionsEnum.ALL.coordinates.forEach { refreshGameButton(it, AttributeEnum.COLOR, AttributeValueEnum.DEFAULT) }
        PositionsEnum.ALL.coordinates.forEach { refreshGameButton(it, AttributeEnum.SCALE, AttributeValueEnum.DEFAULT) }

        val position1 = arguments.position.coordinates
        position1.forEach {refreshGameButton(it, visibility = 0)}

        // Add an Observer on the state variable for Navigating when STOP button is pressed.
        viewModel.navigateToGame.observe(viewLifecycleOwner, Observer { gameId ->
            gameId?.let {
                this.findNavController().navigate(FinishFragmentDirections.actionFinishFragmentToGameFragment2(gameId))
                viewModel.doneNavigating()
            }
        })



        return binding.root
    }

}