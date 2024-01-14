package com.github.tvbox.osc.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Synthetic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A class with methods to efficiently resize Bitmaps.
 */
// Legacy Public APIs.
@SuppressWarnings("WeakerAccess")
public final class RoundTransformationUtils {

    // See #738.
    private static final Set<String> MODELS_REQUIRING_BITMAP_LOCK =
        new HashSet<>(
            Arrays.asList(
                // Moto X gen 2
                "XT1085", "XT1092", "XT1093", "XT1094", "XT1095",
                "XT1096", "XT1097", "XT1098",
                // Moto G gen 1
                "XT1031", "XT1028", "XT937C", "XT1032", "XT1008",
                "XT1033", "XT1035", "XT1034", "XT939G", "XT1039",
                "XT1040", "XT1042", "XT1045",
                // Moto G gen 2
                "XT1063", "XT1064", "XT1068", "XT1069", "XT1072",
                "XT1077", "XT1078", "XT1079"
            )
        );

    /**
     * https://github.com/bumptech/glide/issues/738 On some devices, bitmap drawing is not thread
     * safe.
     * This lock only locks for these specific devices. For other types of devices the lock is always
     * available and therefore does not impact performance
     */
    private static final Lock BITMAP_DRAWABLE_LOCK =
        MODELS_REQUIRING_BITMAP_LOCK.contains(Build.MODEL)
            ? new ReentrantLock() : new NoLock();


    private RoundTransformationUtils() {
        // Utility class.
    }


    private static Bitmap getAlphaSafeBitmap(
        @NonNull BitmapPool pool, @NonNull Bitmap maybeAlphaSafe) {
        Config safeConfig = getAlphaSafeConfig(maybeAlphaSafe);
        if (safeConfig.equals(maybeAlphaSafe.getConfig())) {
            return maybeAlphaSafe;
        }

        Bitmap argbBitmap =
            pool.get(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(), safeConfig);
        new Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0 /*left*/, 0 /*top*/, null /*paint*/);

        // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
        // when we're finished with it.
        return argbBitmap;
    }

    @NonNull
    private static Config getAlphaSafeConfig(@NonNull Bitmap inBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Config.RGBA_F16.equals(inBitmap.getConfig())) { // NOPMD
                return Config.RGBA_F16;
            }
        }

        return Config.ARGB_8888;
    }

    /**
     * Creates a bitmap from a source bitmap and rounds the corners.
     *
     * <p>This method does <em>NOT</em> resize the given {@link Bitmap}, it only rounds it's corners.
     * To both resize and round the corners of an image, consider
     * {@link com.bumptech.glide.request.RequestOptions#transforms(Transformation[])} and/or
     * {@link com.bumptech.glide.load.MultiTransformation}.
     *
     * @param inBitmap          the source bitmap to use as a basis for the created bitmap.
     * @param leftTopRadius     the corner radius of Left (in device-specific pixels).
     * @param rightTopRadius    the corner radius of Top (in device-specific pixels).
     * @param leftBottomRadius  the corner radius of Right (in device-specific pixels).
     * @param rightBottomRadius the corner radius of Bottom (in device-specific pixels).
     * @return a {@link Bitmap} similar to inBitmap but with rounded corners.
     * @throws IllegalArgumentException if roundingRadius, width or height is 0 or less.
     */
    public static Bitmap roundedCorners(
        @NonNull BitmapPool pool, @NonNull Bitmap inBitmap, int leftTopRadius, int rightTopRadius,
        int leftBottomRadius, int rightBottomRadius) {

        Preconditions.checkArgument(leftTopRadius >= 0,
            "leftTopRadius must be greater than 0.");
        Preconditions.checkArgument(rightTopRadius >= 0,
            "rightTopRadius must be greater than 0.");
        Preconditions.checkArgument(leftBottomRadius >= 0,
            "leftBottomRadius must be greater than 0.");
        Preconditions.checkArgument(rightBottomRadius >= 0,
            "rightBottomRadius must be greater than 0.");

        // Alpha is required for this transformation.
        Config safeConfig = getAlphaSafeConfig(inBitmap);
        Bitmap toTransform = getAlphaSafeBitmap(pool, inBitmap);
        Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), safeConfig);

        result.setHasAlpha(true);

        BitmapShader shader = new BitmapShader(toTransform, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        RectF rect = new RectF(0, 0, result.getWidth(), result.getHeight());
        BITMAP_DRAWABLE_LOCK.lock();
        try {
            Canvas canvas = new Canvas(result);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            Path mPath = new Path();
            float[] mRadii = new float[]{
                leftTopRadius, leftTopRadius,
                rightTopRadius, rightTopRadius,
                rightBottomRadius, rightBottomRadius,
                leftBottomRadius, leftBottomRadius
            };
            mPath.addRoundRect(rect, mRadii, Path.Direction.CW);
            canvas.drawPath(mPath, paint);
            clear(canvas);
        } finally {
            BITMAP_DRAWABLE_LOCK.unlock();
        }

        if (!toTransform.equals(inBitmap)) {
            pool.put(toTransform);
        }

        return result;
    }

    // Avoids warnings in M+.
    private static void clear(Canvas canvas) {
        canvas.setBitmap(null);
    }

    private static final class NoLock implements Lock {

        @Synthetic
        NoLock() {
        }

        @Override
        public void lock() {
            // do nothing
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            // do nothing
        }

        @Override
        public boolean tryLock() {
            return true;
        }

        @Override
        public boolean tryLock(long time, @NonNull TimeUnit unit) throws InterruptedException {
            return true;
        }

        @Override
        public void unlock() {
            // do nothing
        }

        @NonNull
        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Should not be called");
        }
    }
}
