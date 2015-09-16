package me.parkerwilliams.periscope;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.ViewGroup;

import com.mattprecious.telescope.Lens;
import com.mattprecious.telescope.TelescopeLayout;

/**
 * This is a simple class that injects the {@link TelescopeLayout} into an activities root view. So it does not need
 * to be added to the layout.
 * <p/>
 * {@link #Periscope(Activity, Lens)} needs to be called after {@link Activity#setContentView(int)} is called. So the
 * activity has a view.
 * {@link #onDestroy()} needs to be called in the {@link Activity#onDestroy()}
 * <p/>
 * For {@link TelescopeLayout} the {@code android.permission.WRITE_EXTERNAL_STORAGE} is required. However the {@code
 * android.permission.VIBRATE} is opitional and will only be on if it is in the manifest.
 * <p/>
 * Created by parker on 9/16/15.
 */
public class Periscope {

    private TelescopeLayout mTelescope;
    private Lens mLens;
    private Context mContext;

    /**
     * Call after the {@link Activity#setContentView(int)} is called.
     *
     * @param activity
     */
    public Periscope(Activity activity, Lens lens) {
        this.mLens = lens;
        mContext = activity.getApplicationContext();
        ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        mTelescope = new TelescopeLayout(mContext);
        mTelescope.setVibrate(hasVibratePermission(mContext));
        mTelescope.setLens(mLens);
        viewGroup.addView(mTelescope, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * Call in {@link Activity#onDestroy()} calls the {@link TelescopeLayout#cleanUp(Context)} method.
     */
    public void onDestroy() {
        TelescopeLayout.cleanUp(mContext);
        mContext = null;
    }

    /**
     * {@link TelescopeLayout} can vibrate and by default does. So check if the app has the permission since we do
     * not want to add it.
     *
     * @param context
     * @return
     */
    private boolean hasVibratePermission(Context context) {
        String permission = "android.permission.VIBRATE";
        return context.checkCallingPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
