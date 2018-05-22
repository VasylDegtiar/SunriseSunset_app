package com.example.vasyl.sunrisesunset.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.vasyl.sunrisesunset.Constants;
import com.example.vasyl.sunrisesunset.model.PlaceInfo;
import com.example.vasyl.sunrisesunset.model.SunInfoResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class RequestService extends IntentService {

    public static final String TAG = "RequestService";

    public static final String EXTRA_PLACE_INFO = ".RequestService.EXTRA_PLACE_INFO";
    public static final String EXTRA_SUN_INFO_RESULT = ".RequestService.EXTRA_SUN_INFO_RESULT";

    public static final String ACTION_GET_CURRENT_PLACE_SUN_INFO
            = ".RequestService.ACTION_GET_CURRENT_PLACE_SUN_INFO";

    public static final String ACTION_GET_PLACE_SUN_INFO
            = ".RequestService.ACTION_GET_PLACE_SUN_INFO";

    private LocalBroadcastManager mLocalBroadcastManager;
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RequestService(String name) {
        super(TAG);
    }

    public RequestService() {
        super(TAG);
    }

    public static void getCurrentPlaceSunInfo(@NonNull final Context context,
                                              @NonNull final PlaceInfo placeInfo) {
        final Intent intent = createServiceActionIntent(context,
                ACTION_GET_CURRENT_PLACE_SUN_INFO);
        intent.putExtra(EXTRA_PLACE_INFO, placeInfo);
        context.startService(intent);
    }

    public static void getPlaceSunInfo(@NonNull final Context context,
                                       @NonNull final PlaceInfo placeInfo) {
        final Intent intent = createServiceActionIntent(context,
                ACTION_GET_PLACE_SUN_INFO);
        intent.putExtra(EXTRA_PLACE_INFO, placeInfo);
        context.startService(intent);
    }

    private static Intent createServiceActionIntent(@NonNull final Context context,
                                                    @NonNull final String action) {
        final Intent intent = new Intent(context, RequestService.class);
        intent.setAction(action);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null){
                switch (action){
                    case ACTION_GET_CURRENT_PLACE_SUN_INFO:
                    case ACTION_GET_PLACE_SUN_INFO:
                        handleInfo(intent);
                        break;
                    default:
                        throw new RuntimeException("Uknown action send to service");
                }
            }
        }
    }

    private void handleInfo(Intent intent) {
        PlaceInfo info = intent.getParcelableExtra(EXTRA_PLACE_INFO);
        final Gson gson = new Gson();

        if (info != null) {
            HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
            urlBuilder.scheme("https");
            urlBuilder.host("api.sunrise-sunset.org");
            urlBuilder.addPathSegment("json");
            for (Map.Entry<String, String> entry : info.mapRepresentation().entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());

            }


            HttpUrl url = urlBuilder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            final Intent broadcast = new Intent(intent.getAction());
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, IOException e) {
                    broadcast.putExtra(Constants.EXTRA_EXCEPTION, e);
                    mLocalBroadcastManager.sendBroadcast(broadcast);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response)
                        throws IOException {
                    SunInfoResult result
                            = gson.fromJson(response.body().charStream(), SunInfoResult.class);
                    broadcast.putExtra(EXTRA_SUN_INFO_RESULT, result);
                    mLocalBroadcastManager.sendBroadcast(broadcast);
                }
            });
        }
    }
}
