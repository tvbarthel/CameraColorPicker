package fr.tvbarthel.apps.cameracolorpicker.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Static methods for dealing with application version.
 */
public final class Versions {

    /**
     * The default version name.
     */
    public static final String DEFAULT_VERSION_NAME = "unknown name";

    /**
     * Get the version name of the application.
     *
     * @param context a {@link android.content.Context} used to get the package manager and the package name.
     * @return the version name of the application or {@link fr.tvbarthel.apps.cameracolorpicker.utils.Versions#DEFAULT_VERSION_NAME}.
     */
    public static String getVersionName(Context context) {
        String versionName;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = DEFAULT_VERSION_NAME;
        }
        return versionName;
    }

    // Non-instantiability
    private Versions() {
    }
}
