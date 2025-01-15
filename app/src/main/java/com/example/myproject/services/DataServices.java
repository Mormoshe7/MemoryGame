package com.example.myproject.services;

import android.os.AsyncTask;

import com.example.myproject.models.Country;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DataServices {

    public interface DataCallback {
        void onDataLoaded(ArrayList<Country> countries);
        void onError(String errorMessage);
    }

    public void getAllCountry(DataCallback callback) {
        new FetchDataTask(callback).execute();
    }

    private static class FetchDataTask extends AsyncTask<Void, Void, ArrayList<Country>> {
        private DataCallback callback;
        private String errorMessage;

        public FetchDataTask(DataCallback callback) {
            this.callback = callback;
        }

        @Override
        protected ArrayList<Country> doInBackground(Void... voids) {
            ArrayList<Country> countries = new ArrayList<>();
            String sURL = "https://restcountries.com/v2/all?fields=name,nativeName,flag";

            try {
                URL url = new URL(sURL);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();

                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                JsonArray rootobj = root.getAsJsonArray();

                for (JsonElement je : rootobj) {
                    JsonObject obj = je.getAsJsonObject();
                    String name = obj.get("name").getAsString();
                    String nativeName = obj.get("nativeName").getAsString();
                    String flag = obj.get("flag").getAsString();

                    countries.add(new Country(name, nativeName, flag));
                }

            } catch (Exception e) {
                errorMessage = e.getMessage();
                e.printStackTrace();
            }

            return countries;
        }

        @Override
        protected void onPostExecute(ArrayList<Country> countries) {
            if (callback != null) {
                if (errorMessage != null) {
                    callback.onError(errorMessage);
                } else {
                    callback.onDataLoaded(countries);
                }
            }
        }
    }
}