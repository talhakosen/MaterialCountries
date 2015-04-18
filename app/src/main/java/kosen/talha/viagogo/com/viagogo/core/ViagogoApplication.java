package kosen.talha.viagogo.com.viagogo.core;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import kosen.talha.viagogo.com.viagogo.pojos.CountryModal;
import kosen.talha.viagogo.com.viagogo.utils.BitmapLruCache;


/**
 * Author
 * User: tkosen
 * Date: 06/03/2015
 * talhakosen@gmail.com
 */
public final class ViagogoApplication extends Application {
    private RequestQueue requestQueue;
    private static ViagogoApplication instance;
    private HashMap<String,CountryModal> countryMap;

    //Volley image loader
    ImageLoader imageLoader ;
    BitmapLruCache cache = new BitmapLruCache();

    public static ViagogoApplication getInstance() {
        if(instance==null)
            instance = new ViagogoApplication();

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //  Network Stack
        requestQueue = Volley.newRequestQueue(this);
        VolleyLog.DEBUG = true;
        VolleyLog.TAG = "Truecaller";

    }

    public ImageLoader getImageLoader(){

        if(imageLoader==null)
            imageLoader = new ImageLoader(Volley.newRequestQueue(this), cache);
        return  imageLoader;
    }

    public HashMap<String,CountryModal> getCountryMap() {
        return countryMap;
    }

    public void setCountryMap(HashMap<String, CountryModal> countryList) {
        this.countryMap = countryList;
    }
}
