package com.sevenlearn.a7learnstudents;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private Gson gson = new Gson();
    private Type type;
    private Response.Listener<T> listener;
    private JSONObject body;

    public GsonRequest(int method, Type type, String url, @Nullable JSONObject body, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.type = type;
        this.listener = listener;
        this.body = body;
    }

    public GsonRequest(int method, Type type, String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, type, url, null, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String responseInString = new String(networkResponse.data);
            T response = gson.fromJson(responseInString, type);
            return Response.success(response, HttpHeaderParser.parseCacheHeaders(networkResponse));
        }catch (Exception e){
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (body == null)
            return super.getBody();
        else
            return body.toString().getBytes();
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> headers=new HashMap<>();
        headers.put("Accept","application/json");
//        headers.put("Authorization","Bearer asdVZVZXC");
        return headers;
    }
}
