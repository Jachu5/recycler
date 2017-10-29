package com.unchained.itemtouchexample

import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View

class SwipeBackgroundHelper {

    companion object {

        private const val THRESHOLD = 2.5

        @JvmStatic
        fun paintDrawCommandToStart(canvas: Canvas, viewItem: View, @DrawableRes iconResId: Int, dX: Float) {
            val drawCommand = createDrawCommand(viewItem, dX, iconResId)
            paintDrawCommand(drawCommand, canvas, dX, viewItem)
        }

        private fun createDrawCommand(viewItem: View, dX: Float, iconResId: Int): DrawCommand {
            val context = viewItem.context
            var icon = ContextCompat.getDrawable(context, iconResId)
            icon = DrawableCompat.wrap(icon).mutate()
            icon.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.white),
                    PorterDuff.Mode.SRC_IN)
            val backgroundColor = getBackgroundColor(R.color.red, R.color.grey, dX, viewItem)
            return DrawCommand(icon, backgroundColor)
        }

        private fun getBackgroundColor(firstColor: Int, secondColor: Int, dX: Float, viewItem: View): Int {
            when (willActionBeTriggered(dX, viewItem.width)) {
                true -> return ContextCompat.getColor(viewItem.context, firstColor)
                false -> return ContextCompat.getColor(viewItem.context, secondColor)
            }
        }

        private fun paintDrawCommand(drawCommand: DrawCommand, canvas: Canvas, dX: Float, viewItem: View) {
            drawBackground(canvas, viewItem, dX, drawCommand.backgroundColor)
            drawIcon(drawCommand, canvas, viewItem, dX)
        }

        private fun drawIcon(drawCommand: DrawCommand, canvas: Canvas, viewItem: View, dX: Float) {
            val topMargin = calculateTopMargin(drawCommand.icon, viewItem)
            drawCommand.icon.bounds = getStartContainerRectangle(viewItem, drawCommand.icon.intrinsicWidth,
                    topMargin, 20, dX)
            drawCommand.icon.draw(canvas)
        }

        private fun getStartContainerRectangle(viewItem: View, iconWidth: Int, topMargin: Int, sideOffset: Int,
                                               displacement: Float): Rect {
            val leftBound = viewItem.right + displacement.toInt() + sideOffset
            val rightBound = viewItem.right + displacement.toInt() + iconWidth + sideOffset
            val topBound = viewItem.top + topMargin
            val bottomBound = viewItem.bottom - topMargin

            return Rect(leftBound, topBound, rightBound, bottomBound)
        }

        private fun calculateTopMargin(icon: Drawable, viewItem: View): Int {
            return (viewItem.height - icon.intrinsicHeight) / 2
        }

        private fun drawBackground(canvas: Canvas, viewItem: View, dX: Float, color: Int) {
            val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            backgroundPaint.color = color
            val backgroundRectangle = getBackGroundRectangle(viewItem, dX)
            canvas.drawRect(backgroundRectangle, backgroundPaint)
        }

        private fun getBackGroundRectangle(viewItem: View, dX: Float): RectF {
            return RectF(viewItem.right.toFloat() + dX, viewItem.top.toFloat(), viewItem.right.toFloat(),
                    viewItem.bottom.toFloat())
        }

        private fun willActionBeTriggered(dX: Float, viewWidth: Int): Boolean {
            return Math.abs(dX) >= viewWidth / THRESHOLD
        }
    }

    private class DrawCommand internal constructor(internal val icon: Drawable, internal val backgroundColor: Int)

}
