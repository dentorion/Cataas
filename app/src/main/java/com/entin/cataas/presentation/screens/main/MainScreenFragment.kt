package com.entin.cataas.presentation.screens.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.entin.cataas.R
import com.entin.cataas.databinding.FragmentMainScreenBinding
import com.entin.cataas.presentation.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainScreenViewModel>()
    private var snackbar: Snackbar? = null

    private lateinit var adapterTags: ArrayAdapter<String>
    private lateinit var adapterFilter: ArrayAdapter<String>
    private lateinit var adapterColors: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.stateSplashScreen.collect { splashScreenShowing ->
                    if (!splashScreenShowing && viewModel.isSuccessfulStart) {
                        showInterface()
                    } else {
                        showInterface()
                        failResult()
                    }
                }
            }
        }
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showInterface() {
        getRandomCat()
        observeViewState()
        setUserInterface()
    }

    private fun getRandomCat() =
        viewModel.getRandomCat()

    private fun observeViewState() {
        observe(viewModel.stateMainScreen) { viewState ->
            when (viewState) {
                is Fail -> failResult()
                is Pending -> pendingResult()
                is Success -> successResult(viewState)
            }
        }
    }

    private fun setUserInterface() {
        setTagsMenu()
        setFiltersMenu()
        setCheckBox()
        setTextLabel()
        setSlider()
        setColorMenu()
        setSearchButton()
    }

    private fun setTagsMenu() {
        adapterTags = ArrayAdapter(requireContext(), R.layout.list_item, viewModel.allTags)
        binding.mainScreenMenuTagsTextField.apply {
            setAdapter(adapterTags)
            setText(adapterTags.getItem(viewModel.chosenTag), false)
            setOnItemClickListener { _, _, i, _ ->
                viewModel.chosenTag = i
            }
        }
    }

    private fun setFiltersMenu() {
        adapterFilter = ArrayAdapter(requireContext(), R.layout.list_item, viewModel.filters)
        binding.mainScreenMenuFiltersTextField.apply {
            setAdapter(adapterFilter)
            setText(adapterFilter.getItem(viewModel.chosenFilter), false)
            setOnItemClickListener { _, _, i, _ ->
                viewModel.chosenFilter = i
            }
        }
    }

    private fun setCheckBox() {
        binding.apply {
            mainScreenMenuCheckBox.apply {
                isChecked = viewModel.isFullSearch
                setOnCheckedChangeListener { _, visibility ->
                    mainScreenMenuTextForImage.isVisible = visibility
                    mainScreenMenuSlider.isVisible = visibility
                    mainScreenMenuColors.isVisible = visibility
                    mainScreenMenuSliderLabelZero.isVisible = visibility
                    mainScreenMenuSliderLabelSixty.isVisible = visibility
                    viewModel.isFullSearch = visibility
                }
            }
        }
    }

    private fun setTextLabel() {
        binding.apply {
            mainScreenMenuTextForImage.isVisible = viewModel.isFullSearch
            mainScreenMenuTextForImageTextfield.apply {
                setText(viewModel.textLabelValue)
                addTextChangedListener { editable ->
                    viewModel.textLabelValue = editable.toString()
                }
            }
        }
    }

    private fun setSlider() {
        binding.apply {
            mainScreenMenuSliderLabelZero.isVisible = viewModel.isFullSearch
            mainScreenMenuSliderLabelSixty.isVisible = viewModel.isFullSearch
            mainScreenMenuSlider.apply {
                isVisible = viewModel.isFullSearch
                value = viewModel.sliderValue.toFloat()
                addOnChangeListener { _, value, _ ->
                    viewModel.sliderValue = value.toInt()
                }
            }
        }
    }

    private fun setColorMenu() {
        binding.mainScreenMenuColors.isVisible = viewModel.isFullSearch
        adapterColors = ArrayAdapter(requireContext(), R.layout.list_item, viewModel.colors)
        binding.mainScreenMenuColorsTextField.apply {
            setAdapter(adapterColors)
            setText(adapterColors.getItem(viewModel.chosenColor), false)
            setOnItemClickListener { _, _, i, _ ->
                viewModel.chosenColor = i
            }
        }
    }

    private fun setSearchButton() {
        binding.mainScreenGetButton.setOnClickListener {
            viewModel.search(isFull = binding.mainScreenMenuCheckBox.isChecked)
        }
    }

    private fun successResult(viewResult: Success<MainScreenViewState>) {
        setCatImage(viewResult.data.catImgUrl)
    }

    private fun pendingResult() {
        Log.i("Catd", "pendingResult")
        // Make button disable with running spinner
    }

    private fun failResult() {
        Glide.with(binding.mainScreenCatImage)
            .load(R.drawable.cat_black)
            .transform(FitCenter(), RoundedCorners(24))
            .into(binding.mainScreenCatImage)
        snackbar?.dismiss()
        snackbar = binding.root.showSnackbar(R.string.error).also {
            it.show()
        }
    }

    private fun setCatImage(catImgUrl: String) {
        Glide.with(binding.mainScreenCatImage)
            .load(catImgUrl)
            .transform(FitCenter())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_cat)
            .error(R.drawable.placeholder_gray_rectangle_rounded)
            .into(binding.mainScreenCatImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}