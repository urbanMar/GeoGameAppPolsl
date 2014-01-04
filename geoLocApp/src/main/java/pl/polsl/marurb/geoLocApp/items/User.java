package pl.polsl.marurb.geoLocApp.items;

import android.util.JsonReader;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;

/**
 * Created by B3stia on 23.12.13.
 */
public class User {

    private static long id;
    private static String mail;
    private static String pass;
    private static String login;

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        User.login = login;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public static CookieStore cookieStore = new BasicCookieStore();
    public static HttpContext localContext = new BasicHttpContext();

    private static JSONObject Responder;
    public static Boolean isLogged = false;

    public boolean isLogged() {
        return isLogged;
    }

    public static JSONObject login(String mail, String pass) throws IOException {
        String hash = md5(pass);
        return getResponse(GlobalVariables.getUserLogin(mail, pass));
    }

    public static JSONObject forgot(String mail) throws IOException {
        return getResponse(GlobalVariables.getUserForgot(mail));
    }

    public static JSONObject register(String login, String mail, String pass) throws IOException {
        return getResponse(GlobalVariables.getUserRegister(login, mail, pass));
    }

    public static JSONObject info() throws IOException {
        return getResponse(GlobalVariables.getUserInfo());
    }

    public static JSONObject update(String login, String mail, String pass1, String pass2) {

        List<NameValuePair> post = new ArrayList<NameValuePair>(4);
        post.add(new BasicNameValuePair("login", login));
        post.add(new BasicNameValuePair("name", mail));
        post.add(new BasicNameValuePair("pass1", pass1));
        post.add(new BasicNameValuePair("pass2", pass2));


        return getPostResponse(GlobalVariables.getUserEdit(), post);
    }

    public void setUserData() throws IOException {

        // if(!isLogged()) {
        info();

        if (getLastStatus() == 0) {
            isLogged = false;
        }

        else {
            JSONObject data = getLastData();

            isLogged = true;

            try {
                mail = data.getString("mail");
                id = Integer.parseInt(data.getString("user_id"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // }

        // isLogged = true;
    }

    private static JSONObject getResponse(String url) throws IOException {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            HttpResponse response = client.execute(httpGet, localContext);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(JsonReader.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = builder.toString();

        JSONObject out = new JSONObject();

        try {
            out = new JSONObject(response);
            Log.d("User-model", out.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Responder = out;

        return out;
    }

    private static JSONObject getPostResponse(String url,
                                              List<NameValuePair> post) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);


        try {
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            httpPost.setEntity(new UrlEncodedFormEntity(post, HTTP.UTF_8));

            HttpResponse response = client.execute(httpPost, localContext);
            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(JsonReader.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = builder.toString();

        JSONObject out = new JSONObject();

        try {
            out = new JSONObject(response);
            Log.d("User-model", out.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Responder = out;

        return out;
    }

    public String getLastErrors() {
        String out = "";
        Iterator key;

        try {
            key = Responder.getJSONObject("errors").keys();
            out = Responder.getJSONObject("errors").getString(
                    (String) key.next());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    public JSONObject getLastData() {
        JSONObject out = new JSONObject();

        try {
            out = Responder.getJSONObject("data");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    public int getLastStatus() {
        int out = 0;

        try {
            out = Integer.parseInt((String) Responder.getString("status")
                    .toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    public static String md5(String s) {
        String hashtext = "";

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(s.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashtext;
    }




}
