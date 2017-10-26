# GlideTransformations
This is a glide extend library
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0) [ ![Download](https://api.bintray.com/packages/tsubasap91/maven/transformations/images/download.svg) ](https://bintray.com/tsubasap91/maven/transformations/_latestVersion)

An Android transformation library providing a variety of image transformations for [Glide](https://github.com/bumptech/glide).


![image.png](http://upload-images.jianshu.io/upload_images/1712960-8c7600a50d5e7945.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


# How do I use it?

## Step 1

#### Gradle
```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.tsubasa.glide:transformations:1.0.0'
}
```

## Step 2

Set Glide Transform.

```java
CircleWithLabelTransform circleWithLabelTransform = new CircleWithLabelTransform.Builder()
                .withLabelRes(R.drawable.test)// 标签的图片资源
                .withLabelSize(100)// 标签的大小
                .withLabelStoke(10)// 标签所占的镂空的大小
                .withImageStrokeWidth(10)// 大图的边框大小
                .withImageStrokeColor(Color.RED)// 大图的边框颜色
                .withGravity(Gravity.START | Gravity.TOP)// 标签的位置
                .build();

 Glide.with(this).load(R.drawable.test)
                .apply(RequestOptions.bitmapTransform(circleWithLabelTransform))
                .into(image);
```
