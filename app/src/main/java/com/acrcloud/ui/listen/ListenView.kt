package com.acrcloud.ui.listen

import android.content.Context
import android.databinding.ObservableBoolean
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class ListenView : TextView {

    var listening: ObservableBoolean = ObservableBoolean(false)
        private set

    private val listeningColor: Int = Color.RED
    private var inactiveColor: Int = Color.GREEN
    private var currentColor: Int = inactiveColor


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
    }

    constructor(context: Context?) : super(context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val x = (width / 2).toFloat()
        val y = (height / 2).toFloat()

        val radius = y

        val paint = Paint()
        paint.color = currentColor
        canvas!!.drawCircle(x, y, radius, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                listening.set(!listening.get())
                if (listening.get()) {
                    currentColor = listeningColor
                    setText("STOP")
                } else {
                    currentColor = inactiveColor
                    setText("START")
                }

            }
            MotionEvent.ACTION_UP -> {

            }
        }

        return super.onTouchEvent(event)
    }

}