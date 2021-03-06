package com.developer.valyutaapp.ui.chart

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.developer.valyutaapp.R
import com.developer.valyutaapp.core.common.PATH_EXP
import com.developer.valyutaapp.core.common.Result
import com.developer.valyutaapp.databinding.FragmentChartBinding
import com.developer.valyutaapp.domain.entities.History
import com.developer.valyutaapp.domain.entities.ValCurs
import com.developer.valyutaapp.ui.MainViewModel
import com.developer.valyutaapp.utils.ImageResource
import com.developer.valyutaapp.utils.Utils
import com.developer.valyutaapp.utils.Utils.getDate
import com.developer.valyutaapp.utils.Utils.getMonthAge
import com.developer.valyutaapp.utils.Utils.getWeekAge
import com.developer.valyutaapp.utils.Utils.getYearAge
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChartFragment : Fragment(R.layout.fragment_chart) {

    private val viewBinding by viewBinding(FragmentChartBinding::bind)
    private val viewModel by viewModel<MainViewModel>()

    private val args: ChartFragmentArgs by navArgs()
    private var dateItems: MutableList<String> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupViewModel()
        viewBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            when (radio) {
                viewBinding.week ->
                    viewModel.getRemoteHistories(getWeekAge(), getDate(), args.valId, args.charCode, PATH_EXP)
                viewBinding.month ->
                    viewModel.getRemoteHistories(getMonthAge(), getDate(), args.valId, args.charCode, PATH_EXP)
                viewBinding.year ->
                    viewModel.getRemoteHistories(getYearAge(), getDate(), args.valId, args.charCode, PATH_EXP)
            }
        }
    }

    private fun setupToolbar() = with(viewBinding) {
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun setupViewModel() = with(viewBinding) {
        viewModel.getLocalValuteById(args.valId)
        viewModel.getRemoteValutes.observe(viewLifecycleOwner) { subscribeHistoryState(it) }
        lifecycleScope.launchWhenCreated {
            viewModel.getLocalHistories(args.valId).collect { getAllValuteSuccess(it) }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.getLocalValuteById.observe(viewLifecycleOwner) {
                val bt = ImageResource.getImageRes(requireContext(), it.charCode)
                iconValute.setImageBitmap(bt)
                name.text = it.charCode
                somon.text = it.value
            }
        }
    }

    private fun subscribeHistoryState(it: Result<ValCurs>) {
        when (it) {
            is Result.Loading -> {}
            is Result.Success -> {}
            is Result.Error -> {
                Log.d("Error ", it.code.toString() + " == " + it.errorMessage)
            }
        }
    }

    private fun getAllValuteSuccess(valutes: List<History>) {
        dateItems.addAll(valutes.map { it.dates })
        showBarChart(valutes)
    }

    private fun showBarChart(valutes: List<History>) = with(viewBinding) {
        val entries = ArrayList<Entry>()
        val title = "color"
        chart.setBackgroundColor(Color.WHITE)
        // no description text
        chart.description.isEnabled = false
        // enable touch gestures
        chart.setTouchEnabled(true)
        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        chart.maxHighlightDistance = 300F

        val y = chart.axisLeft
        y.setLabelCount(6, false)
        y.textColor = Color.BLACK
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.axisLineColor = Color.WHITE
        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = true

        val xAxis: XAxis = viewBinding.chart.xAxis
        //xAxis.setDrawGridLines(true)
//        xAxis.granularity = 1f
//        xAxis.isGranularityEnabled = true
//        xAxis.granularity = 1f
//        xAxis.enableGridDashedLine(10f, 10f, 0f)
//        xAxis.setDrawLimitLinesBehindData(true)

        if (valutes.size > 2) {
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Utils.dateFormatChart(valutes[value.toInt()].dates)
                }
            }
        }
        //fit the data into a bar
        for (i in valutes.indices) {
            val barEntry = Entry(i.toFloat(), valutes[i].value.toFloat())
            entries.add(barEntry)
        }
        val barDataSet = LineDataSet(entries, title)
        val data = LineData(barDataSet)
        barDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        barDataSet.cubicIntensity = 0.2f
        barDataSet.setDrawFilled(true)
        barDataSet.setDrawCircles(false)
        barDataSet.lineWidth = 1.8f
        barDataSet.circleRadius = 4f
        barDataSet.setCircleColor(Color.rgb(104, 248, 175))
        barDataSet.highLightColor = Color.rgb(104, 248, 175)
        barDataSet.color = Color.rgb(104, 248, 175)
        barDataSet.fillColor = Color.rgb(104, 248, 175)
        barDataSet.fillAlpha = 100
        barDataSet.setDrawHorizontalHighlightIndicator(false)
        barDataSet.fillFormatter = IFillFormatter { _, _ -> chart.axisLeft.axisMinimum }

        barDataSet.valueTextSize = 12f
        data.setDrawValues(false)
        viewBinding.chart.data = data
        viewBinding.chart.invalidate()
    }
}