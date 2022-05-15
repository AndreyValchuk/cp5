package com.example.finance.ui;

import static java.time.temporal.TemporalAdjusters.previousOrSame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.finance.R;
import com.example.finance.databinding.FragmentStatisticsBinding;
import com.example.finance.db.CategoryType;
import com.example.finance.db.Operation;
import com.example.finance.viewmodel.OperationViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class StatisticsFragment extends TabFragment {

    public StatisticsFragment(int index) {
        super(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);

        OperationViewModel operationViewModel = new ViewModelProvider(this).get(OperationViewModel.class);

        operationViewModel.getOperationsFromStartOfYear().observe(getViewLifecycleOwner(), operations -> {
            int cashInflowFromStartOfWeek = 0;
            int cashOutflowFromStartOfWeek = 0;
            int cashInflowFromStartOfMonth = 0;
            int cashOutflowFromStartOfMonth = 0;
            int cashInflowFromStartOfYear = 0;
            int cashOutflowFromStartOfYear = 0;
            HashMap<String, Integer> operationsFlow = new HashMap<>();

            for (Operation operation : operations) {
                CategoryType type = operation.getMonetaryAmount() >= 0 ? CategoryType.CASH_INFLOW : CategoryType.CASH_OUTFLOW;
                LocalDate localDate = LocalDate.parse(operation.getOperationDate().toString());
                LocalDate startOfWeek = LocalDate.now().with(previousOrSame(DayOfWeek.MONDAY));
                LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);

                int monetaryAmount = operationsFlow.getOrDefault(localDate.getMonth().name() + type, 0);
                operationsFlow.put(localDate.getMonth().name() + type, monetaryAmount + operation.getMonetaryAmount());

                cashInflowFromStartOfYear += type == CategoryType.CASH_INFLOW ? operation.getMonetaryAmount() : 0;
                cashOutflowFromStartOfYear += type == CategoryType.CASH_OUTFLOW ? operation.getMonetaryAmount() : 0;
                cashInflowFromStartOfWeek += type == CategoryType.CASH_INFLOW && (localDate.isEqual(startOfWeek) || localDate.isAfter(startOfWeek))
                        ? operation.getMonetaryAmount() : 0;
                cashOutflowFromStartOfWeek += type == CategoryType.CASH_OUTFLOW && (localDate.isEqual(startOfWeek) || localDate.isAfter(startOfWeek))
                        ? operation.getMonetaryAmount() : 0;
                cashInflowFromStartOfMonth += type == CategoryType.CASH_INFLOW && (localDate.isEqual(startOfMonth) || localDate.isAfter(startOfMonth))
                        ? operation.getMonetaryAmount() : 0;
                cashOutflowFromStartOfMonth += type == CategoryType.CASH_OUTFLOW && (localDate.isEqual(startOfMonth) || localDate.isAfter(startOfMonth))
                        ? operation.getMonetaryAmount() : 0;
            }

            ((TextView)binding.getRoot().findViewById(R.id.inflow_week_statistics)).setText(String.valueOf(cashInflowFromStartOfWeek));
            ((TextView)binding.getRoot().findViewById(R.id.outflow_week_statistics)).setText(String.valueOf(Math.abs(cashOutflowFromStartOfWeek)));
            ((TextView)binding.getRoot().findViewById(R.id.inflow_month_statistics)).setText(String.valueOf(cashInflowFromStartOfMonth));
            ((TextView)binding.getRoot().findViewById(R.id.outflow_month_statistics)).setText(String.valueOf(Math.abs(cashOutflowFromStartOfMonth)));
            ((TextView)binding.getRoot().findViewById(R.id.inflow_year_statistics)).setText(String.valueOf(cashInflowFromStartOfYear));
            ((TextView)binding.getRoot().findViewById(R.id.outflow_year_statistics)).setText(String.valueOf(Math.abs(cashOutflowFromStartOfYear)));

            ArrayList<BarEntry> entries = new ArrayList<>();

            for (int i = 0; i < Month.values().length; i++) {
                entries.add(new BarEntry(i, new float[] { operationsFlow.getOrDefault(Month.values()[i].name() + CategoryType.CASH_INFLOW, 0),
                        Math.abs(operationsFlow.getOrDefault(Month.values()[i].name() + CategoryType.CASH_OUTFLOW, 0)) }));
            }

            updateBarChart(entries);
        });

        return binding.getRoot();
    }

    private void updateBarChart(ArrayList<BarEntry> entries) {
        BarChart barChart = binding.getRoot().findViewById(R.id.chart);

        if (barChart.getData() != null) {
            ((BarDataSet) barChart.getData().getDataSetByIndex(0)).setValues(entries);

            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        }
        else {
            barChart.getDescription().setEnabled(false);
            barChart.getXAxis().setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return getResources().getStringArray(R.array.months)[Math.round(value)];
                }
            });
            barChart.getXAxis().setGranularity(1);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.getAxisLeft().setDrawGridLines(false);
            barChart.getAxisRight().setDrawGridLines(false);
            barChart.getXAxis().setTextSize(15);
            barChart.getAxisLeft().setTextSize(15);
            barChart.getAxisRight().setDrawLabels(false);
            barChart.setExtraTopOffset(10);

            BarDataSet set = new BarDataSet(entries, "");
            set.setColors(getResources().getColor(R.color.teal, binding.getRoot().getContext().getTheme()),
                    getResources().getColor(R.color.blue, binding.getRoot().getContext().getTheme()));
            set.setStackLabels(new String[] { getString(R.string.inflow_categories_text), getString(R.string.outflow_categories_text) });
            set.setValueTextSize(15);
            set.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return value > 0 ? super.getFormattedValue(value) : "";
                }
            });

            barChart.setData(new BarData(Collections.singletonList(set)));
            barChart.setVisibleXRangeMaximum(6);
        }

        barChart.invalidate();
    }
}
