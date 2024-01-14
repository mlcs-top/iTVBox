package com.github.tvbox.osc.util;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * A {@link BitmapTransformation} which rounds the corners of a bitmap.
 */
public final class RoundBitmapTransformation extends BitmapTransformation {
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.RoundedCorners";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private final int leftTopRadius;
    private final int rightTopRadius;
    private final int leftBottomRadius;
    private final int rightBottomRadius;


    /**
     * @param leftTopRadius     the corner radius of Left (in device-specific pixels).
     * @param rightTopRadius    the corner radius of Top (in device-specific pixels).
     * @param leftBottomRadius  the corner radius of Right (in device-specific pixels).
     * @param rightBottomRadius the corner radius of Bottom (in device-specific pixels).
     * @throws IllegalArgumentException if rounding radius is 0 or less.
     */
    public RoundBitmapTransformation(int leftTopRadius, int rightTopRadius,
                                     int leftBottomRadius, int rightBottomRadius) {
        Preconditions.checkArgument(leftTopRadius >= 0, "leftTopRadius must be greater than 0.");
        Preconditions.checkArgument(rightTopRadius >= 0, "rightTopRadius must be greater than 0.");
        Preconditions.checkArgument(leftBottomRadius >= 0, "leftBottomRadius must be greater than 0.");
        Preconditions.checkArgument(rightBottomRadius >= 0, "rightBottomRadius must be greater than 0.");
        this.leftTopRadius = leftTopRadius;
        this.rightTopRadius = rightTopRadius;
        this.leftBottomRadius = leftBottomRadius;
        this.rightBottomRadius = rightBottomRadius;
    }

    @Override
    protected Bitmap transform(
            @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return RoundTransformationUtils.roundedCorners(pool, toTransform,
            leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RoundBitmapTransformation) {
            RoundBitmapTransformation other = (RoundBitmapTransformation) o;
            return (leftTopRadius == other.leftTopRadius) && (rightTopRadius == other.rightTopRadius) &&
                (leftBottomRadius == other.leftBottomRadius) && (rightBottomRadius == other.rightBottomRadius);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return
            Util.hashCode(ID.hashCode(),
                Util.hashCode(leftTopRadius,
                    Util.hashCode(rightTopRadius,
                        Util.hashCode(leftBottomRadius,
                            Util.hashCode(rightBottomRadius)))));
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] leftRadiusData = ByteBuffer.allocate(4).putInt(leftTopRadius).array();
        messageDigest.update(leftRadiusData);
        byte[] topRadiusData = ByteBuffer.allocate(4).putInt(rightTopRadius).array();
        messageDigest.update(topRadiusData);
        byte[] rightRadiusData = ByteBuffer.allocate(4).putInt(leftBottomRadius).array();
        messageDigest.update(rightRadiusData);
        byte[] bottomRadiusData = ByteBuffer.allocate(4).putInt(rightBottomRadius).array();
        messageDigest.update(bottomRadiusData);
    }

}
