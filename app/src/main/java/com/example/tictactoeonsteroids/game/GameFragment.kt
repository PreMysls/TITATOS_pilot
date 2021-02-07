package com.example.tictactoeonsteroids.game

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tictactoeonsteroids.enums.AttributeValueEnum
import com.example.tictactoeonsteroids.data.GameButton
import com.example.tictactoeonsteroids.R
import com.example.tictactoeonsteroids.database.GameDatabase
import com.example.tictactoeonsteroids.databinding.FragmentGameBinding

import com.example.tictactoeonsteroids.enums.Alpha
import com.example.tictactoeonsteroids.enums.Scale
import com.example.tictactoeonsteroids.firstscreen.FirstScreenFragmentDirections


class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameBinding
    private lateinit var viewModelFactory: GameViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = GameDatabase.getInstance(application).gameDatabaseDao
        val arguments = GameFragmentArgs.fromBundle(requireArguments())

        viewModelFactory = GameViewModelFactory(dataSource, application, arguments.gameId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel
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

        binding.setColorButton.background = backgroundDefault?.constantState?.newDrawable()?.mutate()
        binding.setScaleButton.background = backgroundDefault?.constantState?.newDrawable()?.mutate()

        binding.colorButtonEngine.background = backgroundDefault?.constantState?.newDrawable()?.mutate()
        binding.scaleButtonEngine.background = backgroundDefault?.constantState?.newDrawable()?.mutate()

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

        fun setButtonListPlayable(buttonList: List<ImageButton>, playable: Boolean) {
            buttonList.forEach { it.isClickable = playable }
            buttonList.forEach {
                it.alpha = if (playable) Alpha.ALPHA_100.value else Alpha.ALPHA_20.value
            }
        }

        fun setButtonPlayable(imageButton: ImageButton, playable: Boolean) {
            imageButton.isClickable = playable
            imageButton.alpha = if (playable) Alpha.ALPHA_100.value else Alpha.ALPHA_20.value
        }

        fun refreshGameButton(gameButton: GameButton, position: Int) {
            gameButtonList[position].background = getBackground(gameButton.background)
            gameButtonList[position].background.setTint(getColor(gameButton.color))
            gameButtonList[position].scaleX = getScaleX(gameButton.scale)
            gameButtonList[position].scaleY = getScaleY(gameButton.scale)
        }

        viewModel.gameButtonOne.observe(viewLifecycleOwner, Observer { gameButton ->
            refreshGameButton(gameButton, 0)
        })
        viewModel.gameButtonTwo.observe(viewLifecycleOwner, Observer { gameButton ->
            refreshGameButton(gameButton, 1)
        })
        viewModel.gameButtonThree.observe(viewLifecycleOwner, Observer { gameButton ->
            refreshGameButton(gameButton, 2)
        })
        viewModel.gameButtonFour.observe(viewLifecycleOwner, Observer { gameButton ->
            refreshGameButton(gameButton, 3)
        })
        viewModel.gameButtonFive.observe(viewLifecycleOwner, Observer { gameButton ->
            refreshGameButton(gameButton, 4)
        })
        viewModel.gameButtonSix.observe(viewLifecycleOwner, Observer { gameButton ->
            refreshGameButton(gameButton, 5)
        })
        viewModel.gameButtonSeven.observe(viewLifecycleOwner, Observer { gameButton ->
            refreshGameButton(gameButton, 6)
        })
        viewModel.gameButtonEight.observe(viewLifecycleOwner, Observer { gameButton ->
            refreshGameButton(gameButton, 7)
        })
        viewModel.gameButtonNine.observe(viewLifecycleOwner, Observer { gameButton ->
            refreshGameButton(gameButton, 8)
        })

        // ===== ATTRIBUTE BUTTONS VISIBILITY =====
        viewModel.backgroundButtonPlayable.observe(viewLifecycleOwner, Observer { playable ->
            setButtonPlayable(binding.setBackgroundButton, playable)
        })
        viewModel.colorButtonPlayable.observe(viewLifecycleOwner, Observer { playable ->
            setButtonPlayable(binding.setColorButton, playable)
        })
        viewModel.scaleButtonPlayable.observe(viewLifecycleOwner, Observer { playable ->
            setButtonPlayable(binding.setScaleButton, playable)
        })
        // ===== ENGINE BUTTONS VISIBILITY =====
        viewModel.engineBackgroundButtonPlayable.observe(viewLifecycleOwner, Observer { playable ->
            setButtonPlayable(binding.backgroundButtonEngine, playable)
        })
        viewModel.engineColorButtonPlayable.observe(viewLifecycleOwner, Observer { playable ->
            setButtonPlayable(binding.colorButtonEngine, playable)
        })
        viewModel.engineScaleButtonPlayable.observe(viewLifecycleOwner, Observer { playable ->
            setButtonPlayable(binding.scaleButtonEngine, playable)
        })
        // ===== GAME BUTTONS VISIBILITY =====
        viewModel.gameButtonsPlayable.observe(viewLifecycleOwner, Observer { playable ->
            setButtonListPlayable(gameButtonList, playable)
        })

        // ===== CURRENT VALUES =====
        viewModel.backgroundActual.observe(viewLifecycleOwner, Observer { attributeValue ->
            val background = getBackground(attributeValue)
            binding.setBackgroundButton.background = background
            binding.backgroundButtonEngine.background = background
        })
        viewModel.colorActual.observe(viewLifecycleOwner, Observer { attributeValue ->
            val color = getColor(attributeValue)
            binding.setColorButton.background.setTint(color)
            binding.colorButtonEngine.background.setTint(color)
        })
        viewModel.scaleActual.observe(viewLifecycleOwner, Observer { attributeValue ->
            val scaleX = getScaleX(attributeValue)
            val scaleY = getScaleY(attributeValue)
            binding.setScaleButton.scaleX = scaleX
            binding.setScaleButton.scaleY = scaleY
            binding.scaleButtonEngine.scaleX = scaleX
            binding.scaleButtonEngine.scaleY = scaleY
        })

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { isFinished ->
                isFinished?.let {
                    this.findNavController().navigate(GameFragmentDirections.actionGameFragment2ToFinishFragment(
                        viewModel.playerWon, viewModel.winningPosition, viewModel.winningAttribute, viewModel.winningAttributeValue))

                }
//                viewModel.onGameFinishComplete()
        })

        return binding.root
    }
}

//fun Fragment.mayNavigate(): Boolean {
//
//    val navController = findNavController()
//    val destinationIdInNavController = navController.currentDestination?.id
//
//    // add tag_navigation_destination_id to your ids.xml so that it's unique:
//    val destinationIdOfThisFragment = view?.getTag(R.id.finishFragment) ?: destinationIdInNavController
//
//    // check that the navigation graph is still in 'this' fragment, if not then the app already navigated:
//    if (destinationIdInNavController == destinationIdOfThisFragment) {
//        view?.setTag(R.id.finishFragment, destinationIdOfThisFragment)
//        return true
//    } else {
//        Log.d("FragmentExtensions", "May not navigate: current destination is not the current fragment.")
//        return false
//    }
//}