package kosen.talha.viagogo.com.viagogo.exception;

import org.json.JSONException;

/**
 * Created by tkosen on 07/03/2015.
 */
public class LocationNotFoundException extends Exception {

    public LocationNotFoundException(String s) {
        super(s);
    }

    public  String getMessage(){
        return  super.getMessage();
    }
}
