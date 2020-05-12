package com.example.covid19;


import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {
    public ArrayList<States> stateinfo = new ArrayList<States>();
    public ArrayList<States> stateinfo2 = new ArrayList<States>();

    MutableLiveData<List<States>> mStates;
//Repository repository;
//    public void init()
//    {
//        Log.i("ajeet", "init: method called");
//   repository = new Repository();
//mStates = repository.getStatesValue();


    public LiveData<List<States>> getStates() {
        Log.i("ajeet", "getStates: method is called");
        if (mStates == null) {
            mStates = new MutableLiveData<List<States>>();
//             aynsta();
            StatesAsyncTask task = new StatesAsyncTask();
            task.execute("https://api.rootnet.in/covid19-in/stats/latest");
            Log.i("ajeet", "getStates: function is called");
        }

//         mStates.postValue(stateinfo); // yaha pr galti kr rahe the response aane k baad data add krna tha ( onpostexecute wale method m kiye h)
        return mStates;
    }

    class StatesAsyncTask extends AsyncTask<String, Void, ArrayList<States>> {

        @Override
        protected ArrayList<States> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            ArrayList<States> result = fetchStateData(urls[0]);
            return result;

        }

        protected void onPostExecute(ArrayList<States> data) {
            // Clear the adapter of previous earthquake data
            stateinfo.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
//                stateinfo.addAll(data);
                mStates.setValue(data);

            }
        }
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("testing", "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("testing", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("testing", "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<States> extractFeatureFromJson(String response) {
        ArrayList<States> list = new ArrayList<States>();
        try {
            JSONObject jsonresponse = new JSONObject(response);
            JSONObject jsonObjectdata = jsonresponse.getJSONObject("data");
            JSONArray jsonArray = jsonObjectdata.getJSONArray("regional");
            final int max = jsonArray.length();

            for (int i = 0; i < max; i++) {
                JSONObject currentStatesResponse = jsonArray.getJSONObject(i);
                String loca = currentStatesResponse.getString("loc");
                int totalCase = currentStatesResponse.getInt("totalConfirmed");
                States states = new States(loca, totalCase);
                list.add(states);


            }

//
        } catch (JSONException e) {
            Log.e("testing", "extractFeatureFromJson: ", e);
        }

        return list;
    }

    public static ArrayList<States> fetchStateData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("testing", "Problem making the HTTP request.", e);
        }
        ArrayList<States> statess = extractFeatureFromJson(jsonResponse);
        return statess;
////        }
    }

}
