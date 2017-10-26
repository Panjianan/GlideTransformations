package com.tsubasa.glide.transformations.util

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Build
import android.support.annotation.ColorInt
import android.support.v7.widget.AppCompatDrawableManager


/***
 * <br> Project GlideTransformations
 * <br> Package com.tsubasa.glide.transformations.util
 * <br> Description 工具类
 * <br> Version 1.0
 * <br> Author Tsubasa
 */
fun Context.getVectorBitmap(vectorDrawableId: Int, imgWidth: Int, imgHeight: Int): Bitmap? {
    var outWidth = imgWidth
    var outHeight = imgHeight
    val bitmap: Bitmap
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val vectorDrawable = AppCompatDrawableManager.get().getDrawable(this, vectorDrawableId) ?: return null
        if (imgWidth == 0) {
            outWidth = vectorDrawable.intrinsicWidth
        }
        if (imgHeight == 0) {
            outHeight = vectorDrawable.intrinsicHeight
        }
        bitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
    } else {
        bitmap = BitmapFactory.decodeResource(resources, vectorDrawableId)
    }
    return bitmap
}

/**
 * 转为圆形图片
 * 
 * @param recycle     是否回收
 * *
 * @param borderSize  边框尺寸
 * *
 * @param borderColor 边框颜色
 * *
 * @return 圆形图片
 */
fun Bitmap.toRound(borderSize: Int = 0, @ColorInt borderColor: Int = 0, recycle: Boolean = false): Bitmap? {
    if (isEmptyBitmap(this)) return null
    val width = width
    val height = height
    val size = Math.min(width, height)
    val paint = Paint(ANTI_ALIAS_FLAG)
    val ret = Bitmap.createBitmap(width, height, config)
    val center = size / 2f
    val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
    rectF.inset((width - size) / 2f, (height - size) / 2f)
    val matrix = Matrix()
    matrix.setTranslate(rectF.left, rectF.top)
    matrix.preScale(size.toFloat() / width, size.toFloat() / height)
    val shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    shader.setLocalMatrix(matrix)
    paint.shader = shader
    val canvas = Canvas(ret)
    canvas.drawRoundRect(rectF, center, center, paint)
    if (borderSize > 0) {
        paint.shader = null
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSize.toFloat()
        val radius = center - borderSize / 2f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }
    if (recycle && !isRecycled) recycle()
    return ret
}

/**
 * 判断bitmap对象是否为空

 * @param src 源图片
 * *
 * @return `true`: 是<br></br>`false`: 否
 */
private fun isEmptyBitmap(src: Bitmap?): Boolean {
    return src == null || src.width == 0 || src.height == 0
}