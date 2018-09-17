package veoride.com.veoridedemo;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.locationlayer.Utils;

public class VeorideApplication extends Application {


    // Set access token
    private static final String DEFAULT_MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoiemFjaC15YW5nIiwiYSI6ImNqbHpmajN3MDAxNHUzbG15bjB1YnhrNWMifQ.D43SoFV09mab2iXB9NddvA";
    private static final String LOG_TAG = VeorideApplication.class.getSimpleName();




    @Override
    public void onCreate() {
        super.onCreate();

        String mapboxAccessToken = getMapboxAccessToken(getApplicationContext());
        if (TextUtils.isEmpty(mapboxAccessToken) || mapboxAccessToken.equals(DEFAULT_MAPBOX_ACCESS_TOKEN)) {
            Log.w(LOG_TAG, "Warning: access token isn't set.");
        }

        Mapbox.getInstance(getApplicationContext(), mapboxAccessToken);

    }

    public static String getMapboxAccessToken(@NonNull Context context) {
        try {
            // Read out AndroidManifest
            String token = Mapbox.getAccessToken();
            if (token == null || token.isEmpty()) {
                throw new IllegalArgumentException();
            }
            return token;
        } catch (Exception exception) {
            // Use fallback on string resource, used for development
            int tokenResId = context.getResources()
                    .getIdentifier("mapbox_access_token", "string", context.getPackageName());
            return tokenResId != 0 ? context.getString(tokenResId) : null;
        }
    }

}
