package com.example.linechartapp.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.linechartapp.R
import com.google.android.material.color.MaterialColors

class LineChartView(
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet) {

    private val dataSet = mutableListOf<DataPoint>()
    private var xMin = 0
    private var xMax = 0
    var yMin = 0
        private set
    var yMax = 0
        private set

    private val marginStartX
        get() = CHART_MARGIN
    private val marginStartY
        get() = CHART_MARGIN
    private val marginHeight
        get() = height - CHART_MARGIN
    private val marginWidth
        get() = width - CHART_MARGIN

    private val dataPointPaint = Paint().apply {
        color = MaterialColors.getColor(this@LineChartView, R.attr.colorSecondary)
        strokeWidth = DATA_POINT_LINE_STROKE_WIDTH
        style = Paint.Style.STROKE
    }

    private val dataPointFillPaint = Paint().apply {
        color = MaterialColors.getColor(this@LineChartView, R.attr.colorPrimary)
    }

    private val dataPointLinePaint = Paint().apply {
        color = MaterialColors.getColor(this@LineChartView, R.attr.colorSecondary)
        strokeWidth = DATA_POINT_LINE_STROKE_WIDTH
        isAntiAlias = true
    }

    private val axisLinePaint = Paint().apply {
        color = MaterialColors.getColor(this@LineChartView, R.attr.colorPrimary)
        strokeWidth = AXIS_LINE_STROKE_WIDTH
    }

    fun setData(newDataSet: List<DataPoint>) {
        val set = if (newDataSet.size > MAX_POINTS_ON_CHART) {
            val groupSize = newDataSet.size / MAX_POINTS_ON_CHART
            var chunkIndex = 1
            newDataSet.chunked(groupSize.toInt()) { chunks ->
                DataPoint(
                    chunkIndex++,
                    (chunks.sumOf { it.yVal } / chunks.size.toFloat()).toInt()
                )
            }
        } else {
            newDataSet
        }

        xMin = set.minByOrNull { it.xVal }?.xVal ?: 0
        xMax = set.maxByOrNull { it.xVal }?.xVal ?: 0
        yMin = set.minByOrNull { it.yVal }?.yVal ?: 0
        yMax = set.maxByOrNull { it.yVal }?.yVal ?: 0

        dataSet.clear()
        dataSet.addAll(set)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        dataSet.forEachIndexed { index, currentDataPoint ->
            val realX = currentDataPoint.xVal.toRealX()
            val realY = currentDataPoint.yVal.toRealY()

            if (index < dataSet.size - 1) {
                val nextDataPoint = dataSet[index + 1]
                val startX = currentDataPoint.xVal.toRealX()
                val startY = currentDataPoint.yVal.toRealY()
                val endX = nextDataPoint.xVal.toRealX()
                val endY = nextDataPoint.yVal.toRealY()
                canvas.drawLine(startX, startY, endX, endY, dataPointLinePaint)
            }

            canvas.drawCircle(realX, realY, DATA_POINT_CIRCLE_RADIOUS, dataPointFillPaint)
            canvas.drawCircle(realX, realY, DATA_POINT_CIRCLE_RADIOUS, dataPointPaint)
        }

        canvas.drawLine(marginStartX, marginStartY, marginStartX, marginHeight, axisLinePaint)
        canvas.drawLine(marginStartX, marginStartY, 0f, CHART_MARGIN * 3, axisLinePaint)
        canvas.drawLine(marginStartX, marginStartY, CHART_MARGIN * 2, CHART_MARGIN * 3, axisLinePaint)

        canvas.drawLine(marginStartX, marginHeight, marginWidth, marginHeight, axisLinePaint)
        canvas.drawLine(marginWidth, marginHeight, marginWidth - CHART_MARGIN * 2, height.toFloat(), axisLinePaint)
        canvas.drawLine(marginWidth, marginHeight, marginWidth - CHART_MARGIN * 2, marginHeight - CHART_MARGIN, axisLinePaint)
    }

    private fun Int.toRealX() = CHART_MARGIN + toFloat() / xMax * marginWidth
    private fun Int.toRealY() = marginHeight - ((toFloat() - yMin) / (yMax - yMin)* marginHeight)

    companion object {
        const val DATA_POINT_LINE_STROKE_WIDTH = 3f
        const val DATA_POINT_CIRCLE_RADIOUS = 3f
        const val AXIS_LINE_STROKE_WIDTH = 5f
        const val CHART_MARGIN = 10f
        const val MAX_POINTS_ON_CHART = 100f
    }
}

data class DataPoint(
    val xVal: Int,
    val yVal: Int
)


