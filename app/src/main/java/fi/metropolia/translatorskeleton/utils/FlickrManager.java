package fi.metropolia.translatorskeleton.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flickr4java.flickr.Flickr;

import org.json.JSONException;
import org.json.JSONObject;

import fi.metropolia.translatorskeleton.R;

public class FlickrManager {
    //private String API_KEY = "64c0f179f8aec0444033c8b2c57a7db0";
    //private String APP_SECRET = "";
    private String apiKey = "8adaafc499295b7db35e1e56732af688";
    private String appSecret = "7d0ae5af3ea1c8b2";
    private String serviceEndpoint = "https://api.flickr.com/services";
    private String querySearch = "/rest/?method=flickr.photos.search";
    private String querySizes = "/rest/?method=flickr.photos.getSizes";
    private String queryPerPage = "&per_page=1";
    private String queryJsonNoCallback = "&nojsoncallback=1";
    private String queryFormat = "&format=json";
    private String queryText = "&text=";
    private String queryApiKey = "&api_key=";
    private String queryExtras = "&extras=url_q";
    private String queryPhotoId = "&photo_id=";
    private Context context;
    private Flickr flickr;

    public FlickrManager(Context context){
        this.context = context;
    }

    public void placeImage(final String id, final ImageView view){
        String queryString = serviceEndpoint + querySizes + queryApiKey + apiKey
                + queryPhotoId + id + queryFormat + queryJsonNoCallback;

        RequestQueue queue = Volley.newRequestQueue(context);

        System.out.println("query string " + queryString);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, queryString, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject photos = response.getJSONObject("sizes");
                    JSONObject photo = (JSONObject) photos.getJSONArray("size").get(1);
                    String imageUrl = photo.getString("source");
                    Glide.with(context).load(imageUrl).into(view);
                } catch (JSONException e){
                    Glide.with(context).load(R.drawable.placeholder).into(view);
                    Log.d("fail","no image");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fail","loading image");
            }
        });
        queue.add(objectRequest);
    }

}
