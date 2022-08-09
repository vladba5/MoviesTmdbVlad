package com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.contains
import androidx.core.view.forEach
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.moviestmdb.Genre
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.ui_movies.R
import com.example.moviestmdb.ui_movies.databinding.BottomSheetFilterBinding
import com.example.moviestmdb.ui_movies.databinding.ChipBinding
import com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies.FilterKey.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.threeten.bp.LocalDate
import java.util.*
import kotlin.concurrent.schedule


@AndroidEntryPoint
class FilterBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetFilterBinding
    private val viewModel: FilterBottomSheetViewModel by viewModels()

    private val _currentSelections = MutableStateFlow<Map<String, String>>(emptyMap())
    val currentSelections = _currentSelections.asStateFlow()

    //var listener: (response: String) -> Unit = {}
    var listener: (response: FilterParams) -> Unit = {}

    private val selectedFilter = FilterParams()
    val currentlySelectedChips: MutableList<Int> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVars()

        Handler(Looper.getMainLooper()).postDelayed({
            loadValues()
        }, 10) //millis


    }

    private fun loadValues() {
        binding.userScoreRangeSlider.setValues(viewModel.filerParams.user_min_score.toFloat(), viewModel.filerParams.user_max_score.toFloat())
        binding.runtimeSlider.setValues(viewModel.filerParams.duration_min.toFloat(), viewModel.filerParams.duration_max.toFloat())
        binding.userVotesRangeSlider.value = viewModel.filerParams.user_min_votes.toFloat()

        val adapter = ArrayAdapter(requireContext(), R.layout.language_list_item, viewModel.getLanguageList(requireContext()))
        (binding.languageMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.languageAutoComplete.setText(viewModel.getLanguageList(requireContext())[2], false)

        //currentlySelectedChips.addAll(viewModel.filerParams.genres)

        binding.bottomSheetChipGroup.forEach { chip ->
            if(viewModel.filerParams.genres.contains(chip.id)){
                (chip as Chip).isChecked = true
            }
        }

//        viewModel.geners.

//        binding.bottomSheetChipGroup.setOnCheckedChangeListener { group, checkedId ->
//            if(currentlySelectedChips.contains(checkedId)){
//                viewModel.removeFilter(CHOSEN_GENRES, checkedId)
////               currentlySelectedChips.remove(checkedId)
//            }else{
//                viewModel.addFilter(CHOSEN_GENRES, checkedId)
////                currentlySelectedChips.add(checkedId)
//            }
//        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.SAVE_ALL
        return dialog
    }

    private fun getLatestValues() {
        val ids = currentlySelectedChips.joinToString(separator = ",")
        if (ids.isNotEmpty()) {
            //selectedFilter["with_genres"] = ids
        }

//        val ids = binding.bottomSheetChipGroup.checkedChipIds.joinToString(separator = ",")
//        if (ids.isNotEmpty()) {
//            selectedFilter["with_genres"] = ids
//        }

//        selectedFilter["vote_count.gte"] = "${binding.userVotesRangeSlider.value}"
//        selectedFilter["vote_average.gte"] = "${binding.userScoreRangeSlider.values[0]}"
//        selectedFilter["vote_average.lte"] = "${binding.userScoreRangeSlider.values[1]}"
//        selectedFilter["with_runtime.gte"] = "${binding.runtimeSlider.values[0]}"
//        selectedFilter["with_runtime.lte"] = "${binding.runtimeSlider.values[1]}"

//        val language = binding.languageMenu.editText?.text.toString()
//        if (language.isNotEmpty()) {
//            selectedFilter["language"] = language
//        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    private fun addChips(chipGroup: ChipGroup, chips: List<Genre>) {
        chips.forEach { genre ->
            val chip = ChipBinding.inflate(LayoutInflater.from(chipGroup.context)).root
            chip.id = genre.id
            chip.text = genre.name
            chip.setOnCheckedChangeListener { compountBtn, ischecked ->
                updateChipStatus(compountBtn, ischecked)
            }
            chipGroup.addView(chip)
        }
    }

    private fun updateChipStatus(compountBtn: CompoundButton, isChecked: Boolean) {
        if (!isChecked && currentlySelectedChips.contains(compountBtn.id)) {
            viewModel.removeFilter(CHOSEN_GENRES, compountBtn.id)
            //currentlySelectedChips.remove(compountBtn.id)
        } else {
            viewModel.addFilter(CHOSEN_GENRES, compountBtn.id)
            //currentlySelectedChips.add(compountBtn.id)

        }
    }

    private fun initVars() {
//        binding.userScoreRangeSlider.setValues(0.0f, 10.0f)
//        binding.userScoreRangeSlider.stepSize = 1.0f
//
//        binding.userVotesRangeSlider.stepSize = 10.0f
//
//        binding.runtimeSlider.setValues(0.0f, 400.0f)
//        binding.runtimeSlider.stepSize = 10.0f

        (binding.languageMenu.editText as? AutoCompleteTextView)?.doOnTextChanged { text, start, before, count ->
//            selectedFilter["language"] = text.toString()
            viewModel.addFilter(LANGUAGE, text.toString())
        }

        binding.userScoreRangeSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.addFilter(USER_MIN_SCORE, "${slider.values[0]}")
            viewModel.addFilter(USER_MAX_SCORE, "${slider.values[1]}")
//            selectedFilter["vote_average.gte"] = "${slider.values[0]}"
//            selectedFilter["vote_average.lte"] = "${slider.values[1]}"
        }

        binding.userVotesRangeSlider.addOnChangeListener { slider, value, fromUser ->
            //selectedFilter["vote_count.gte"] = "$value"
            viewModel.addFilter(USER_VOTES, "$value")
        }

        binding.runtimeSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.addFilter(USER_MIN_RUNTIME, "${slider.values[0]}")
            viewModel.addFilter(USER_MAX_RUNTIME, "${slider.values[1]}")

//            selectedFilter["with_runtime.gte"] = "${slider.values[0]}"
//            selectedFilter["with_runtime.lte"] = "${slider.values[1]}"
        }

//        binding.bottomSheetChipGroup.setOnCheckedChangeListener { group, checkedId ->
//            if(currentlySelectedChips.contains(checkedId)){
//                viewModel.removeFilter(CHOSEN_GENRES, checkedId)
////               currentlySelectedChips.remove(checkedId)
//            }else{
//                viewModel.addFilter(CHOSEN_GENRES, checkedId)
////                currentlySelectedChips.add(checkedId)
//            }
//        }

        launchAndRepeatWithViewLifecycle {
            binding.bottomSheetChipGroup.removeAllViews()
            viewModel.geners.collect { genres ->
                addChips(binding.bottomSheetChipGroup, genres)
            }
        }
//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)
        binding.datePickerFrom.setOnClickListener {
            val calendarFrom = getDate(true)
            datePicker(calendarFrom, binding.datePickerFrom, true)
        }

        binding.datePickerTo.setOnClickListener {
            val calendarTo = getDate(false)
            datePicker(calendarTo, binding.datePickerTo, false)
        }

//        val items = listOf(
//            getString(R.string.english),
//            getString(R.string.french),
//            getString(R.string.german),
//            getString(R.string.italian),
//            getString(R.string.chinese)
//        )


        val adapter = ArrayAdapter(requireContext(), R.layout.language_list_item, viewModel.getLanguageList(requireContext()))
        (binding.languageMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.languageAutoComplete.setText(viewModel.getLanguageList(requireContext())[2], false)



        binding.filterBtn.setOnClickListener {
            //getLatestValues()
            listener(viewModel.filerParams)
            dismiss()
        }
    }

//    val date = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(requireContext())
//        .format(LocalDate.of(year, month,day))

    private fun datePicker(date: LocalDate, textView: TextView, from: Boolean) {
//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { view, year, month, day ->
            val chosenDate: LocalDate = LocalDate.of(year, month, day)
            when (from) {
                true -> {
//                    selectedFilter["release_date.gte"] = "$chosenDate"
                    viewModel.addFilter(FROM_DATE, "$chosenDate")

                }
                false -> {
//                    selectedFilter["release_date.lte"] = "$chosenDate"
                    viewModel.addFilter(TO_DATE, "$chosenDate")

                }
            }
            textView.text =
                "${chosenDate.year} ${chosenDate.month.value.plus(1)} ${chosenDate.dayOfMonth}"
        }, date.year, date.month.value, date.year)

        //datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()

    }

    private fun getDate(from: Boolean): LocalDate {
        val strDate: String? = null

//        if (from && selectedFilter.contains("release_date.gte")) {
//            strDate = selectedFilter["release_date.gte"]
//        } else if (!from && selectedFilter.contains("release_date.lte")) {
//            strDate = selectedFilter["release_date.lte"]
//        }

        if (from && viewModel.filerParams.dateFrom.isNotEmpty()) {
            viewModel.addFilter(FROM_DATE, viewModel.filerParams.dateFrom)

           // strDate = selectedFilter["release_date.gte"]
        } else if (!from && viewModel.filerParams.dateTo.isNotEmpty()) {
            viewModel.addFilter(TO_DATE, viewModel.filerParams.dateTo)

            // strDate = selectedFilter["release_date.lte"]
        }

        return if (strDate != null) {
            LocalDate.parse(strDate)
        } else {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            LocalDate.of(year, month, day)
        }
    }

}
