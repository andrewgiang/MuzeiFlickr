package com.andrewgiang.muzei.flickr;

import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

/**
 * Created by Andrew on 2/12/14.
 */
public class FlickrArtSource extends RemoteMuzeiArtSource {
    private static final String TAG = "Flickr";
    private static final String SOURCE_NAME = "FlickrArtSource";
    private static final int ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000; // rotate every 3 hours
    public FlickrArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int i) throws RetryException {
        String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;
    }
}
