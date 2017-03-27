package com.example.heinhtet.picasso;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_MESSAGE = MainActivity.class.getSimpleName();
    TextView emptyTextView;
    ProgressDialog progressDialog;

    List<Products> arrayList;
    ListView listView;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_view);
        listView.setTextFilterEnabled(true);


        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            ProductAsyncTask asyncTask = new ProductAsyncTask();
            asyncTask.execute("http://quocnguyen.16mb.com/products.json");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.i(LOG_MESSAGE,"Call Runnable()");
//                    new ProductAsyncTask().execute("http://quocnguyen.16mb.com/products.json");
//                }
//            });

        } else {
            emptyTextView = (TextView) findViewById(R.id.emptytextView);
            emptyTextView.setText("No Internet Connection ");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(false);

        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    listView.clearTextFilter();
                }
                else {
                    listView.setFilterText(newText);
                }
                return true;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    class ProductAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return ReadJSON(strings[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject baseObject = new JSONObject(s);
                JSONArray array = baseObject.getJSONArray("products");
                for (int i = 0;i<array.length();i++){
                    JSONObject current = array.getJSONObject(i);
                    arrayList.add(new Products(
                            current.getString("name"),
                            current.getString("price"),
                            current.getString("image")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new CustomAdapter(getApplicationContext(),R.layout.list_items,arrayList);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (arrayList != null && !arrayList.isEmpty()) {
                listView.setAdapter(adapter);
                //adapter.addAll(arrayList);
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getApplicationContext(),"Selected"+adapter.getItem(i).getmName(),Toast.LENGTH_SHORT).show();
                }
            });

        }

         @Override
         protected void onPreExecute() {
             progressDialog = new ProgressDialog(MainActivity.this);
             progressDialog.setMessage("Loading....");
             progressDialog.setCancelable(false);
             progressDialog.show();
             View view = findViewById(R.id.loading_spinner);
             view.setVisibility(View.GONE);
             super.onPreExecute();
         }
     }
    private String ReadJSON(String s){
        StringBuilder builder = new StringBuilder();

        try {
            URL url = new URL(s);
            URLConnection connection = url.openConnection();
            //connection.setReadTimeout(1);
            connection.setConnectTimeout(10000);
            connection.connect();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line ;

            while ((line = buffer.readLine())!=null){
                builder.append(line +"\n");

            }
            buffer.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();

    }
}
