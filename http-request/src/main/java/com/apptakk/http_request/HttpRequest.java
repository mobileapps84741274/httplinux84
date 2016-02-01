package com.apptakk.http_request;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpRequest {

    public interface Handler {
        void response(HttpResponse response);
    }

    public final static String OPTIONS = "OPTIONS";
    public final static String GET = "GET";
    public final static String HEAD = "HEAD";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";
    public final static String TRACE = "TRACE";

    private final String url;
    private final String method;
    private final String json;
    private final String authorization;

    public HttpRequest(String url, String method) {
        this(url, method, null, null);
    }

    public HttpRequest(String url, String method, String json) {
        this(url, method, json, null);
    }

    public HttpRequest(String url, String method, String json, String authorization) {
        this.url = url;
        this.method = method;
        this.json = json;
        this.authorization = authorization;
    }

    public HttpResponse request()  {

        HttpResponse response = new HttpResponse();

        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return response;
        }

        try {

            if(method != null){
                con.setRequestMethod(method);
            }

            if(authorization != null) {
                con.setRequestProperty("Authorization", this.authorization);
            }

            if(json != null){
                con.setDoOutput(true);
                con.setChunkedStreamingMode(0);

                final byte[] bytes = json.getBytes("UTF-8");
                con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Content-Length", "" + bytes.length);

                IO.write(con.getOutputStream(), bytes);
            }

            response.code = con.getResponseCode();
            response.body = IO.read(con.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }
        return response;
    }
}
