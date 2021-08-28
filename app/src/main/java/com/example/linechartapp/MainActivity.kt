package com.example.linechartapp

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.linechartapp.view.DataPoint
import com.example.linechartapp.view.LineChartView
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var lineChartView: LineChartView
    private lateinit var changeSetButton: Button
    private lateinit var dataSizeSeekBar: SeekBar

    private var dataSize: Int = MIN_DATA_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lineChartView = findViewById(R.id.view_line_chart)
        changeSetButton = findViewById(R.id.btn_change_set)
        dataSizeSeekBar = findViewById(R.id.seek_bar_data_size)

        lineChartView.setData(generateRandomDataPoints(dataSize))

        changeSetButton.setOnClickListener {
            lineChartView.setData(generateRandomDataPoints(dataSize))
            showInfoToast()
        }

        dataSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dataSize = (MIN_DATA_SET + (progress / 100f) * MAX_DATA_SET).toInt()
                lineChartView.setData(generateRandomDataPoints(dataSize))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                showInfoToast()
            }
        })
    }

    private fun showInfoToast() = Toast
        .makeText(
            this@MainActivity,
            "Min: ${lineChartView.yMin} | Max: ${lineChartView.yMax}",
            Toast.LENGTH_SHORT
        )
        .show()

    private fun generateRandomDataPoints(size: Int): List<DataPoint> = with(Random()) {
        (0..size).map {
            DataPoint(it, nextInt(MAX_VALUE) + 1)
        }
    }

    companion object {
        const val MAX_DATA_SET = 100_000
        const val MIN_DATA_SET = 20
        const val MAX_VALUE = 1000
    }
}