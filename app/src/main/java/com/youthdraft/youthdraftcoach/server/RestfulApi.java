package com.youthdraft.youthdraftcoach.server;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.youthdraft.youthdraftcoach.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jjupin on 11/21/16.
 */

public class RestfulApi {

    private static final String TAG = "RESTful API";

    private static RestfulApi mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private HashMap<String,String> headersMap = null;

    private static int TIMEOUT_MAX = 60000;
    private static final int MAX_LOGIN_TRIES = 5;

    private static String sessionId;

    private RestfulApi(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RestfulApi getInstance(Context context) {
        return RestfulApi.getInstance(context, false);
    }

    public static synchronized RestfulApi getInstance(Context context, boolean isPatch) {
        if (mInstance == null) {
            mInstance = new RestfulApi(context);
            mInstance.headersMap = new HashMap<String, String>();
            mInstance.headersMap.put("Content-Type", "application/json");
            mInstance.headersMap.put("charset", "utf-8");
            //mInstance.headersMap.put("app-version", BuildConfig.VERSION_NAME);

            // for testing incoming call
            mInstance.headersMap.put("User-Agent", "android");
        }

        if (sessionId != null && !mInstance.headersMap.containsKey("Cookie")) {
            mInstance.headersMap.put("Cookie", "spokeo_sessions_rails4=" + sessionId);
        }

        if (isPatch) {
            mInstance.headersMap.put("X-HTTP-Method-Override", "PATCH");
        } else {
            mInstance.headersMap.remove("X-HTTP-Method-Override");
        }

        // NOTE only use this when connecting to someone local server
//        HttpsTrustManager.allowAllSSL();

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void setSessionId(String newSessionId) {
        this.sessionId = newSessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    //
    //
    //  Specific RESTful calls...
    //

    //
    // If in the future you need more specific info on the device itself (model, manufacturer),
    // go look at DeviceUtils - those methods have been written
    //

    public static void doLogin(final String useremail, final String password, final String deviceID, final Context context,
                               Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = ServerConstants.getRestfulActionURL(ServerConstants.RestfulActions.login);

        Map<String, Object> postParam= new HashMap<String, Object>();
        postParam.put("email", useremail);
        postParam.put("password", password);
        if (!TextUtils.isEmpty(deviceID)) {
            postParam.put("device_id", deviceID);
        }

        RestfulApi.performPOST(url, postParam, context, "doLogin: ", listener, errorListener);
    }

    public static void doSignup(final String useremail, final String password, final Context context,
                                Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = ServerConstants.getRestfulActionURL(ServerConstants.RestfulActions.signup);

        Map<String, Object> postParam= new HashMap<String, Object>();
        postParam.put("email", useremail);
        postParam.put("password", password);

        RestfulApi.performPOST(url, postParam, context, "doSignup: ", listener, errorListener);

    }

    public static void getPlayersJSON(final Context context,
                                        Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = ServerConstants.getRestfulActionURL(ServerConstants.RestfulActions.get_players);
        RestfulApi.performGET(url, context, listener, errorListener);
    }

    //
    // Generic GET and POST methods to use for retrieving data from the Restful Spokeo connections...
    //

    private static void performGET(String url, final Context context, Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        log("get URL: " + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new JSONObject(), listener, errorListener) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RestfulApi.getInstance(context).headersMap;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MAX,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RestfulApi.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    //
    //  Call this GET when the return value is only a JSONArray - the types have to match...
    //

    private static void performJSONArrayGet(String url, final Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        log("get array URL: " + url);

        JsonArrayRequest jsonArrObjReq = new JsonArrayRequest(Request.Method.GET, url, new JSONArray(), listener, errorListener) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RestfulApi.getInstance(context).headersMap;
            }
        };

        jsonArrObjReq.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MAX,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RestfulApi.getInstance(context).addToRequestQueue(jsonArrObjReq);
    }

    private static void performPOST(String url, Map<String, Object> postParam, final Context context, final String logMessage, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        log("post URL: " + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam), listener, errorListener) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                log(logMessage + RestfulApi.getInstance(context).headersMap);
                return RestfulApi.getInstance(context).headersMap;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MAX,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RestfulApi.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    private static void performPUT(String url, Map<String, Object> postParam, final Context context, final String logMessage, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        log("put URL: " + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                url, new JSONObject(postParam), listener, errorListener) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                log(logMessage + RestfulApi.getInstance(context).headersMap);
                return RestfulApi.getInstance(context).headersMap;
            }
        };

        RestfulApi.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    //
    // TO-Do:  NOT WORKING !!!!
    //
    // Volley documentation says that their version of PATCH does not work correctly.  I've tried a couple of
    // suggested solutions and so far none have worked:
    //
    //
    // http://stackoverflow.com/questions/19797842/patch-request-android-volley
    // http://stackoverflow.com/questions/22411475/android-volley-http-patch-request
    //

    private static void performPATCH(String url, Map<String, Object> putParam, final Context context, final String logMessage, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(putParam), listener, errorListener) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                log(logMessage + RestfulApi.getInstance(context, true).headersMap);
                return RestfulApi.getInstance(context, true).headersMap;
            }
        };

        RestfulApi.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    private static void performDELETE(String url, final Context context, Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {
        log("delete URL: " + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                url, new JSONObject(), listener, errorListener) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RestfulApi.getInstance(context).headersMap;
            }
        };

        RestfulApi.getInstance(context).addToRequestQueue(jsonObjReq);
    }


    //
    // Utility methods...
    //

    public static String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    private static void log(String message) {
        if (Constants.debug) {
            Log.d("RestfulAPI", message);
        }
    }
}
