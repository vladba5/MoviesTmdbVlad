package com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.moviestmdb.Genre
import com.example.moviestmdb.core.extensions.*
import com.example.moviestmdb.ui_movies.R
import com.example.moviestmdb.ui_movies.databinding.BottomSheetFilterBinding
import com.example.moviestmdb.ui_movies.databinding.ChipBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDate
import java.util.*


@AndroidEntryPoint
class FilterBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetFilterBinding
    private val viewModel: FilterBottomSheetViewModel by viewModels()

    //var listener: (response: String) -> Unit = {}
    var listener: (response: HashMap<String, String>) -> Unit = {}
    private val selectedFilter = hashMapOf<String, String>()
    val currentlySelectedChips : MutableList<Int> = mutableListOf()


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
    }

    private fun getLatestValues() {
        val ids = currentlySelectedChips.joinToString(separator = ",")
        if (ids.isNotEmpty()) {
            selectedFilter["with_genres"] = ids
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

    fun addChips(chipGroup: ChipGroup, chips: List<Genre>) {
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
            if(!isChecked && currentlySelectedChips.contains(compountBtn.id)){
                currentlySelectedChips.remove(compountBtn.id)
            }else{
                currentlySelectedChips.add(compountBtn.id)
            }
}

    private fun initVars() {
        binding.userScoreRangeSlider.setValues(0.0f, 10.0f)
        binding.userScoreRangeSlider.stepSize = 1.0f
        binding.userVotesRangeSlider.stepSize = 10.0f
        binding.runtimeSlider.setValues(0.0f, 400.0f)
        binding.runtimeSlider.stepSize = 10.0f

        (binding.languageMenu.editText as? AutoCompleteTextView)?.doOnTextChanged { text, start, before, count ->
            selectedFilter["language"] = text.toString()
        }

        binding.userScoreRangeSlider.addOnChangeListener { slider, value, fromUser ->
            selectedFilter["vote_average.gte"] = "${slider.values[0]}"
            selectedFilter["vote_average.lte"] = "${slider.values[1]}"
        }

        binding.userVotesRangeSlider.addOnChangeListener { slider, value, fromUser ->
            selectedFilter["vote_count.gte"] = "$value"
        }

        binding.runtimeSlider.addOnChangeListener { slider, value, fromUser ->
            selectedFilter["with_runtime.gte"] = "${slider.values[0]}"
            selectedFilter["with_runtime.lte"] = "${slider.values[1]}"
        }

//        binding.bottomSheetChipGroup.setOnCheckedChangeListener { group, checkedId ->
//            if(currentlySelectedChips.contains(checkedId)){
//                currentlySelectedChips.remove(checkedId)
//            }else{
//                currentlySelectedChips.add(checkedId)
//            }
//        }

        launchAndRepeatWithViewLifecycle {
            binding.bottomSheetChipGroup.removeAllViews()
            viewModel.geners.collect { genres ->
                addChips(binding.bottomSheetChipGroup, genres)
            }
        }

        binding.datePickerFrom.setOnClickListener {
            datePicker(binding.fromTxt, 0)
        }

        binding.datePickerTo.setOnClickListener {
            datePicker(binding.toTxt, 1)
        }

        val items = listOf(
            getString(R.string.english),
            getString(R.string.french),
            getString(R.string.german),
            getString(R.string.italian),
            getString(R.string.chinese)
        )
        val adapter = ArrayAdapter(requireContext(), R.layout.language_list_item, items)
        (binding.languageMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.filterBtn.setOnClickListener {
            getLatestValues()
            listener(selectedFilter)
        }
    }

//    val date = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(requireContext())
//        .format(LocalDate.of(year, month,day))

    fun datePicker(textView: TextView, fromTo: Int) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { view, year, month, day ->
            val chosenDate: LocalDate = LocalDate.of(year, month, day)
            when (fromTo) {
                0 -> {
                    selectedFilter["release_date.gte"] = "$chosenDate"
                }
                1 -> {
                    selectedFilter["release_date.lte"] = "$chosenDate"
                }
            }
            textView.text =
                "${chosenDate.year} ${chosenDate.month.value.plus(1)} ${chosenDate.dayOfWeek.value}"
        }, year, month, day)

        datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()

    }

}
