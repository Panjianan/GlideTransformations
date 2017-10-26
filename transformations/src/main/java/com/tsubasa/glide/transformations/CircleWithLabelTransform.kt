package com.tsubasa.glide.transformations

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.Gravity
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.tsubasa.glide.transformations.util.getVectorBitmap
import com.tsubasa.glide.transformations.util.toRound


/***
 * <br> Project kfx
 * <br> Package com.caetp.kfx.core.util.glide
 * <br> Description 圆形头像加上角标
 * <br> Version 1.0
 * <br> Author Tsubasa
 */
class CircleWithLabelTransform
private constructor(builder: Builder) : BitmapTransformation() {

    var labelSize: Int? = null
        private set
    var labelStoke: Int? = null
        private set
    var strokeWidth: Int = 0
        private set
    var strokeColor: Int = Color.TRANSPARENT
        private set
    var labelImgResource: Int? = null
        private set
    var gravity: Int = Gravity.BOTTOM.or(Gravity.END)
        private set

    init {
        labelSize = builder.labelSize
        labelStoke = builder.labelStoke
        strokeWidth = builder.strokeWidth
        strokeColor = builder.strokeColor
        labelImgResource = builder.labelImgResource
        gravity = builder.gravity
    }

    override fun getId(): String = toString()

    override fun transform(context: Context, pool: BitmapPool, source: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        // 原图的尺寸
        val size = Math.min(source.width, source.height)
        // 输出图的尺寸
        val outSize = Math.min(outWidth, outHeight)
        // 原图和输出尺寸的比例
        val sizeScale = size.toDouble().div(outSize)
        // 按比例计算过后的头像边框
        val strokeWidthScale = strokeWidth.times(sizeScale).toInt()
        // 去除头像边框后的头像
        val imageSize = size.minus(strokeWidthScale.times(2))
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        // 整个头像的半径
        val avatarRadius = size.toFloat().div(2)
        // 按比例计算过后的标签尺寸
        val labelSizeScale = labelSize?.times(sizeScale)?.toInt() ?: size.toDouble().div(4).toInt()
        // 按比例计算过后的标签边距
        val labelStokeScale = labelStoke?.times(sizeScale)?.toInt() ?: size.toDouble().div(24).toInt()

        // TODO this could be acquired from the pool too
        // 图片的bitmap
        val squared = Bitmap.createBitmap(source, x, y, imageSize, imageSize)
        // 结果的bitmap
        val result: Bitmap = pool.get(size, size, Bitmap.Config.ARGB_8888)
        // 画布
        val canvas = Canvas(result)
        // 画笔
        val paint = Paint()
        // 用来弄成圆形的遮盖
        val bitmapShader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = bitmapShader
        paint.isAntiAlias = true
        val centerXY = size.toFloat().div(2)
        canvas.drawCircle(centerXY, centerXY, imageSize.div(2).toFloat(), paint)

        if (strokeWidthScale > 0) {
            paint.reset()
            paint.isAntiAlias = true
            paint.color = strokeColor
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
            canvas.drawCircle(centerXY, centerXY, avatarRadius, paint)
        }

        if (labelSizeScale > 0 && labelImgResource != null) {
            val coverLabelSize = (labelSizeScale + labelStokeScale.times(2)).toFloat()
            val coverRadius = coverLabelSize.div(2)
            val labelRadius = labelSizeScale.div(2)
            val labelY = when (gravity.and(Gravity.VERTICAL_GRAVITY_MASK)) {
                Gravity.TOP -> coverRadius
                Gravity.BOTTOM -> size.minus(coverRadius)
                else -> size.div(2).toFloat()
            }
            @SuppressLint("RtlHardcoded")
            val labelX = when (gravity.and(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK)) {
                Gravity.START, Gravity.LEFT -> coverRadius
                Gravity.END, Gravity.RIGHT -> size.minus(coverRadius)
                else -> size.div(2).toFloat()
            }

            paint.reset()
            paint.color = Color.TRANSPARENT
            paint.isAntiAlias = true
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            canvas.drawCircle(labelX, labelY, coverRadius, paint)

            paint.reset()
            paint.isAntiAlias = true
            val sexIcon = context.getVectorBitmap(labelImgResource!!, labelSizeScale, labelSizeScale)?.toRound()
            canvas.drawBitmap(sexIcon, labelX.minus(labelRadius), labelY.minus(labelRadius), paint)
        }
        return result
    }

    override fun toString(): String {
        return "CircleWithLabelTransform(labelSize=$labelSize, labelStoke=$labelStoke, strokeWidth=$strokeWidth, strokeColor=$strokeColor, labelImgResource=$labelImgResource, gravity=$gravity)"
    }

    class Builder {
        internal var labelSize: Int? = null
            private set
        internal var labelStoke: Int? = null
            private set
        internal var strokeWidth: Int = 0
            private set
        internal var strokeColor: Int = Color.TRANSPARENT
            private set
        internal var labelImgResource: Int? = null
            private set
        internal var gravity: Int = Gravity.BOTTOM.or(Gravity.END)
            private set

        fun withGravity(@DrawableRes gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        fun withLabelRes(@DrawableRes labelImgResource: Int): Builder {
            this.labelImgResource = labelImgResource
            return this
        }

        fun withLabelSize(labelSize: Int): Builder {
            this.labelSize = labelSize
            return this
        }

        fun withLabelStoke(labelStoke: Int): Builder {
            this.labelStoke = labelStoke
            return this
        }

        fun withImageStrokeWidth(strokeWidth: Int): Builder {
            this.strokeWidth = strokeWidth
            return this
        }

        fun withImageStrokeColor(@ColorInt strokeColor: Int): Builder {
            this.strokeColor = strokeColor
            return this
        }

        fun build(): CircleWithLabelTransform {
            return CircleWithLabelTransform(this)
        }
    }
}
