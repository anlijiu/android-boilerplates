package com.anlijiu.example.ui.bindingadapter.image;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;


import timber.log.Timber;


public final class ViewBindingAdapter {

//    @BindingAdapter({"uri"})
//    public static void setImageUri(SimpleDraweeView simpleDraweeView, String uri) {
//        if (!TextUtils.isEmpty(uri)) {
//            simpleDraweeView.setImageURI(Uri.parse(uri));
//        }
//    }
//

    @BindingAdapter(value = {"imageUrl"})
    public static void frescoImageLoader(SimpleDraweeView draweeView,
                                         String imageUrl) {
        if(TextUtils.isEmpty(imageUrl)) return;
        Timber.e(" fullImageUrl is %s", imageUrl);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl));
        ImageRequest request = builder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .build();

        draweeView.setController(controller);
    }
}

