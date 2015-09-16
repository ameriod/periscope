package me.parkerwilliams.periscope;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import android.view.View;
import android.view.ViewGroup;

import com.mattprecious.telescope.Lens;
import com.mattprecious.telescope.TelescopeLayout;

import static android.Manifest.permission.VIBRATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * This is a simple class that injects the {@link TelescopeLayout} into an Activity's root view. So it does not need
 * to be added to the layout.
 * <p/>
 * To create the {@link Periscope} call the builder {@link me.parkerwilliams.periscope.Periscope.Builder} which
 * provides all of the settings that the {@link TelescopeLayout} does
 * <p/>
 * {@link #onDestroy()} needs to be called in the {@link Activity#onDestroy()}
 * <p/>
 * For {@link TelescopeLayout} the {@code android.permission.WRITE_EXTERNAL_STORAGE} is required on 4.4+. However the
 * {@codeandroid.permission.VIBRATE} is optional and will only be on if it is in the manifest.
 * <p/>
 * Created by parker on 9/16/15.
 */
public class Periscope {

    private Context mContext;
    private TelescopeLayout mTelescope;
    private Activity mActivity;
    private boolean vibrate;
    private Lens mLen;
    private boolean mScreenshot = true;
    private boolean mScreenshotChildrenOnly;
    private int mPointerCount;
    private int mProgressColor;
    private boolean mIsPointerCountSet;
    private boolean mIsProgressColorSet;

    private Periscope(Builder builder, Activity activity) {
        this.mActivity = activity;
        this.vibrate = builder.vibrate;
        this.mLen = builder.lens;
        this.mScreenshot = builder.screenshot;
        this.mScreenshotChildrenOnly = builder.screenshotChildrenOnly;
        this.mPointerCount = builder.pointerCount;
        this.mProgressColor = builder.progressColor;
        this.mIsPointerCountSet = builder.isPointerCountSet;
        this.mIsProgressColorSet = builder.isProgressColorSet;
        init();
    }

    private void init() {
        mContext = mActivity.getApplicationContext();
        ViewGroup viewGroup = (ViewGroup) mActivity.getWindow().getDecorView().getRootView();
        mTelescope = new TelescopeLayout(mContext);
        // Setup the telescope view
        mTelescope.setVibrate(vibrate && hasVibratePermission(mContext));
        mTelescope.setLens(mLen);
        mTelescope.setScreenshot(mScreenshot);
        mTelescope.setScreenshotChildrenOnly(mScreenshotChildrenOnly);
        if (mIsPointerCountSet) {
            mTelescope.setPointerCount(mPointerCount);
        }
        if (mIsProgressColorSet) {
            mTelescope.setProgressColor(mProgressColor);
        }

        viewGroup.addView(mTelescope, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void setScreenshotTarget(View view) {
        mTelescope.setScreenshotTarget(view);
    }

    public static Builder newPeriscope() {
        return new Builder();
    }

    /**
     * Call in {@link Activity#onDestroy()} calls the {@link TelescopeLayout#cleanUp(Context)} method.
     */
    public void onDestroy() {
        TelescopeLayout.cleanUp(mContext);
        // Make sure there are no leaks
        mContext = null;
        mActivity = null;
        mLen = null;
    }

    /**
     * {@link TelescopeLayout} can vibrate and by default does. So check if the app has the permission since we do
     * not want to add it.
     *
     * @param context
     * @return
     */
    private boolean hasVibratePermission(Context context) {
        return context.checkPermission(VIBRATE, android.os.Process.myPid(), Process.myUid()) == PERMISSION_GRANTED;
    }


    /**
     * Lets the {@link TelescopeLayout} settings to be set. And creates the {@link Periscope}
     */
    public static final class Builder {
        // Defaults from the TelescopeLayout Attributes
        private boolean vibrate = true;
        private boolean screenshot = true;
        private boolean screenshotChildrenOnly = false;
        private Lens lens;
        private int pointerCount;
        private int progressColor;
        private boolean isPointerCountSet;
        private boolean isProgressColorSet;

        private Builder() {
        }

        /**
         * By default this build method will create a {@link TelescopeLayout} for the whole Activity.
         *
         * @param activity
         * @return
         */
        public Periscope build(Activity activity) {
            return new Periscope(this, activity);
        }

        /**
         * Use this build method to set the {@link TelescopeLayout#setScreenshotTarget(View)}
         *
         * @param activity
         * @param screenshotTarget
         * @return
         */
        public Periscope build(Activity activity, View screenshotTarget) {
            Periscope periscope = new Periscope(this, activity);
            periscope.setScreenshotTarget(screenshotTarget);
            return periscope;
        }

        public Builder vibrate(boolean vibrate) {
            this.vibrate = vibrate;
            return this;
        }

        public Builder lens(Lens lens) {
            this.lens = lens;
            return this;
        }

        public Builder screenshot(boolean screenshot) {
            this.screenshot = screenshot;
            return this;
        }

        public Builder screenshotChildrenOnly(boolean screenshotChildrenOnly) {
            this.screenshotChildrenOnly = screenshotChildrenOnly;
            return this;
        }


        public Builder pointerCount(int pointerCount) {
            this.pointerCount = pointerCount;
            this.isPointerCountSet = true;
            return this;
        }

        /**
         * {@link TelescopeLayout#setProgressColor(int)}
         *
         * @param progressColor
         * @return
         */
        public Builder progressColor(int progressColor) {
            this.progressColor = progressColor;
            this.isProgressColorSet = true;
            return this;
        }
    }
}
