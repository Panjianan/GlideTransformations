package com.tsubasa.glide.transformations

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.util.Util
import java.security.MessageDigest


/***
 * <br> Project GlideTransformations
 * <br> Package com.tsubasa.glide.transformations
 * <br> Description 相关转化的父类
 * <br> Version 1.0
 * <br> Author Tsubasa
 */
abstract class BitmapTransformation : Transformation<Bitmap> {

    protected abstract fun getId(): String

    protected abstract fun transform(context: Context, pool: BitmapPool, source: Bitmap, outWidth: Int, outHeight: Int): Bitmap

    override fun transform(context: Context, resource: Resource<Bitmap>, outWidth: Int, outHeight: Int): Resource<Bitmap> {
        if (!Util.isValidDimensions(outWidth, outHeight)) {
            throw IllegalArgumentException(
                    "Cannot apply transformation on width: " + outWidth + " or height: " + outHeight
                            + " less than or equal to zero and not Target.SIZE_ORIGINAL")
        }
        val bitmapPool = Glide.get(context).bitmapPool
        val toTransform = resource.get()
        val targetWidth = outWidth
        val targetHeight = outHeight
        val transformed = transform(context.applicationContext, bitmapPool, toTransform, targetWidth, targetHeight)

        val result: Resource<Bitmap>
        if (toTransform == transformed) {
            result = resource
        } else {
            result = BitmapResource.obtain(transformed, bitmapPool)!!
        }
        return result
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest?) {
        messageDigest?.update(getId().toByteArray())
    }

}