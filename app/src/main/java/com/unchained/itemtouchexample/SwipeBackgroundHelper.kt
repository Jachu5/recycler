package com.unchained.itemtouchexample

import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View

class SwipeBackgroundHelper {

    companion object {

        @JvmStatic
        fun paintDrawCommandToStart(canvas: Canvas, viewItem: View, @DrawableRes iconResId: Int, dX: Float) {
            val drawCommand = createDrawCommand(viewItem, dX, iconResId)
            paintDrawCommand(drawCommand, canvas, dX, viewItem)
        }

        private fun createDrawCommand(viewItem: View, dX: Float, iconResId: Int): DrawCommand {
            val context = viewItem.context
            var startIcon = ContextCompat.getDrawable(context, iconResId)
            startIcon = DrawableCompat.wrap(startIcon).mutate()
            startIcon.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.white),
                    PorterDuff.Mode.SRC_IN)
            val backgroundColor = getBackgroundColor(R.color.red, R.color.grey, dX, viewItem)
            return DrawCommand(startIcon, backgroundColor)
        }

        private fun getBackgroundColor(firstColor: Int, secondColor: Int, dX: Float, viewItem: View): Int {
            if (willActionBeTriggered(dX, viewItem.width)) {
                return ContextCompat.getColor(viewItem.context, firstColor)
            } else {
                return ContextCompat.getColor(viewItem.context, secondColor)
            }
        }

        private fun paintDrawCommand(drawCommand: DrawCommand, canvas: Canvas, dX: Float, viewItem: View) {
            drawBackground(canvas, viewItem, dX, drawCommand.backgroundColor)
            drawIconSwipe(drawCommand, canvas, viewItem, dX)
        }

        private fun drawIconSwipe(drawCommand: DrawCommand, canvas: Canvas, viewItem: View, dX: Float) {
            val topMargin = calculateTopMargin(drawCommand.icon, viewItem)
            drawCommand.icon.bounds = getStartContainerRectangle(viewItem, drawCommand.icon.intrinsicWidth,
                    20, topMargin, dX)
            drawCommand.icon.draw(canvas)
        }

        private fun getStartContainerRectangle(viewItem: View, iconWidth: Int, topMargin: Int, sideOffset: Int,
                                               displacement: Float): Rect {
            val leftBound = viewItem.left - iconWidth - sideOffset + displacement.toInt()
            val topBound = viewItem.top + topMargin
            val rightBound = viewItem.left - sideOffset + displacement.toInt()
            val bottomBound = viewItem.top + iconWidth + topMargin

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
            if (dX > 0) {
                return RectF(viewItem.left.toFloat(), viewItem.top.toFloat(), dX, viewItem.bottom.toFloat())
            } else {
                return RectF(viewItem.right.toFloat() + dX, viewItem.top.toFloat(), viewItem.right.toFloat(),
                        viewItem.bottom.toFloat())
            }
        }

        @JvmStatic
        private fun willActionBeTriggered(dX: Float, viewWidth: Int): Boolean {
            return Math.abs(dX) >= viewWidth / 2.5
        }
    }

    private class DrawCommand internal constructor(internal val icon: Drawable, internal val backgroundColor: Int)

}