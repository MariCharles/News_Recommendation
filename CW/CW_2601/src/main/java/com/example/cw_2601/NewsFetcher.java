package com.example.cw_2601;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class NewsFetcher {
    private static final String API_KEY = "928c3ac602b0437a9eb68182030598eb";
    private static final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;

    // Fetch news articles from the API
    public static JSONArray fetchNews() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getJSONArray("articles");
    }
}
