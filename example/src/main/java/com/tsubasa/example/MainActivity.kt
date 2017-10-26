package com.tsubasa.example

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tsubasa.glide.transformations.CircleWithLabelTransform
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        find<View>(R.id.button1).onClick { showImage(Gravity.START.or(Gravity.TOP)) }
        find<View>(R.id.button2).onClick { showImage(Gravity.CENTER.or(Gravity.TOP)) }
        find<View>(R.id.button3).onClick { showImage(Gravity.END.or(Gravity.TOP)) }
        find<View>(R.id.button4).onClick { showImage(Gravity.START.or(Gravity.CENTER)) }
        find<View>(R.id.button5).onClick { showImage(Gravity.CENTER.or(Gravity.CENTER)) }
        find<View>(R.id.button6).onClick { showImage(Gravity.END.or(Gravity.CENTER)) }
        find<View>(R.id.button7).onClick { showImage(Gravity.START.or(Gravity.BOTTOM)) }
        find<View>(R.id.button8).onClick { showImage(Gravity.CENTER.or(Gravity.BOTTOM)) }
        find<View>(R.id.button9).onClick { showImage(Gravity.END.or(Gravity.BOTTOM)) }
    }

    fun showImage(gravity: Int) {
        val image = find<ImageView>(R.id.iv_test)
        val circleWithLabelTransform = CircleWithLabelTransform.Builder()
                .withLabelRes(R.drawable.test)
                .withImageStrokeColor(Color.RED)
                .withGravity(gravity)
                .withImageStrokeWidth(10)
                .build()

        Glide.with(this)
                .load(R.drawable.test)
                .apply(RequestOptions.bitmapTransform(circleWithLabelTransform))
                .into(image)
    }
}
