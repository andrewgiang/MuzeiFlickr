package com.andrewgiang.muzei.flickr;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.andrewgiang.muzei.flickr.model.PersonResponse;
import com.andrewgiang.muzei.flickr.model.Photo;
import com.andrewgiang.muzei.flickr.model.PhotoResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import java.util.Random;

/**
 * Created by Andrew on 2/12/14.
 */
public class FlickrArtSource extends RemoteMuzeiArtSource {
    private static final String TAG = "Flickr";
    private static final String SOURCE_NAME = "FlickrArtSource";
    private static final int ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000; // rotate every 3 hours
    private RequestQueue mRequestQueue;

    public FlickrArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    @Override
    protected void onTryUpdate(int i) throws RetryException {
        final String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;

        final Artwork.Builder builder = new Artwork.Builder();
        mRequestQueue.add(new GsonRequest<PhotoResponse>(
                "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key="+Config.API_KEY+"&group_id=40961104@N00&format=json&nojsoncallback=1",
                PhotoResponse.class, null, new Response.Listener<PhotoResponse>() {
            @Override
            public void onResponse(PhotoResponse request) {

                Log.e(TAG, request.toString());
                if (request != null) {
                    if (request.photos.photo.size() == 0) {
                        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
                    }

                    Random random = new Random();

                    Photo photo;
                    String token;
                    while (true) {
                        photo = request.photos.photo.get(random.nextInt(request.photos.photo.size()));
                        token = photo.id;
                        if (request.photos.photo.size() <= 1 || !TextUtils.equals(token, currentToken)) {
                            break;
                        }
                    }
                    String url = getUrl(photo);
                    builder.title(photo.title)
                            .imageUri(Uri.parse(url))
                            .token(token)
                            .viewIntent(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(url)));
                    Log.w(TAG, url);
                    final String[] user = {"Unkown"};
                    String personUrl = "http://api.flickr.com/services/rest/?method=flickr.people.getInfo&api_key="+Config.API_KEY+"&user_id=" + photo.owner + "&format=json&nojsoncallback=1";
                    mRequestQueue.add(new GsonRequest<PersonResponse>(personUrl, PersonResponse.class, null, new Response.Listener<PersonResponse>() {
                        @Override
                        public void onResponse(PersonResponse personResponse) {
                            String name = "Unkown";
                            if (personResponse.person.realname == null)
                                name = personResponse.person.username._content;
                            else
                                name = personResponse.person.realname._content;
                            builder.byline(name);
                            publishArtwork(builder.build());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }
                    ));
                    scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e(TAG, volleyError.getMessage());
            }
        }
        ));


    }

    private String getUrl(Photo photo) {
        return String.format("http://farm%d.staticflickr.com/%s/%s_%s.jpg", photo.farm, photo.server, photo.id, photo.secret);
    }
}
