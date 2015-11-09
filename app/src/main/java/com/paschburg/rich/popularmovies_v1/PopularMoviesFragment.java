package com.paschburg.rich.popularmovies_v1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PopularMoviesFragment extends Fragment {


    private GridViewAdapter mPopularMoviesAdapter;
    private GridView gridVies;
    public UserPrefs userPrefs;

    public PopularMoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FetchMoviesTask fetch=new FetchMoviesTask();
        fetch.execute("orderbypopularity");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.popularmoviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_about) {
            startActivity(new Intent(getActivity(), AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView1);

        String[] resultStrs = new String[20];
        String[] json = new String[20];

        ImageItem[] imageitems2 = new ImageItem[20];

        Uri.Builder builder;

        for (int i=0; i < 20; i++){

            builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w185")
                    .appendPath("")
                    .appendPath(json[i]);

            imageitems2[i] = new ImageItem();
            imageitems2[i].imagep = builder.build().toString();
        }

        mPopularMoviesAdapter = new GridViewAdapter (
                getActivity(),
                R.layout.grid_item_layout,
                imageitems2);

        gridView.setAdapter(mPopularMoviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                    ImageItem movie = mPopularMoviesAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), DetailActivity.class).
                            putExtra(Intent.EXTRA_TEXT,String.valueOf(position));
                    startActivity(intent);
                }
            }
        );

        userPrefs = new UserPrefs(getActivity());
        String order = userPrefs.getSortOrder();
        FetchMoviesTask fetch=new FetchMoviesTask();
        // Log.e("Sort Order xxxx",order);
        fetch.execute(order);

        return rootView;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        public FetchMoviesTask() {
            super();
        }

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String popularMoviesJsonStr = null;

            try
            {
                ApiKey apiKey = new ApiKey();
                String apikeystring = apiKey.get();
                Uri.Builder builder = new Uri.Builder();

                String voteOrPopularity = params[0];

                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("discover")
                        .appendPath("movie")
                        .appendQueryParameter("sort_by", voteOrPopularity)
                        .appendQueryParameter("api_key", apikeystring);

                String builderString = String.format("url = %1$s", builder);

                //  Log.e(LOG_TAG, builderString);

                String myUrl = builder.build().toString();
                URL url = new URL(myUrl);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    // Log.e(LOG_TAG, "line = " + line);
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    // Nothing to do.
                    return null;
                }
                popularMoviesJsonStr = buffer.toString();
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Error = " + e.getMessage() , e);
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null ) {
                    try {
                        reader.close();
                    }
                    catch (final IOException e){
                        Log.e(LOG_TAG, "Error closing stream.");
                    }
                }
            }

            try {

                return getPopularMoviesDataFromJson(popularMoviesJsonStr, 20);

            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();

            }

            // will normally not hit this code
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null ) {

                mPopularMoviesAdapter.clear();
                int l = result.length;
                for (int i=0; i<l; i++) {
                    ImageItem imageItem = new ImageItem();
                    imageItem.imagep = result[i];
                    mPopularMoviesAdapter.addImageItem(i,imageItem);
                }
                mPopularMoviesAdapter.notifyDataSetChanged();
            }
        }

        private String[] getPopularMoviesDataFromJson(String popularMoviesJsonStr, int numMovies)
                throws JSONException {

            String[] resultStrs = new String[numMovies];
            String[] json = new String[numMovies];

            // These are the names of the JSON objects that need to be extracted.

            final String TMDB_MOVIES = "results";
            final String TMDB_IMAGE = "poster_path";

            JSONObject movieObject = new JSONObject(popularMoviesJsonStr);
            JSONArray movieArray = movieObject.getJSONArray(TMDB_MOVIES);

            for(int i=0; i<movieArray.length(); i++){
                String image;

                JSONObject movieInfo = movieArray.getJSONObject(i);
                UserPrefs userPrefs = new UserPrefs(getActivity());
                userPrefs.setString(String.valueOf(i),movieInfo.toString());

                image = movieInfo.getString(TMDB_IMAGE);
                json[i] = image.substring(1); // do not include the leading slash

            }

            Uri.Builder builder;

            for (int i=0; i < numMovies; i++){

                builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w154")
                        .appendPath("")
                        .appendPath(json[i]);

                resultStrs[i] = builder.build().toString();

            }
            return resultStrs;
        }
    }
}

