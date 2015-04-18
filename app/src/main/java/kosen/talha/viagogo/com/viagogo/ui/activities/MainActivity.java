/*
 * Copyright (C) ${YEAR} Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kosen.talha.viagogo.com.viagogo.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import kosen.talha.viagogo.com.viagogo.R;
import kosen.talha.viagogo.com.viagogo.adapters.RecyclerViewAdapter;
import kosen.talha.viagogo.com.viagogo.core.Constants;
import kosen.talha.viagogo.com.viagogo.core.MyVolley;
import kosen.talha.viagogo.com.viagogo.core.ViagogoApplication;
import kosen.talha.viagogo.com.viagogo.pojos.CountryModal;
import kosen.talha.viagogo.com.viagogo.utils.ScrollManager;


public class MainActivity extends ActionBarActivity implements RecyclerViewAdapter.OnItemClickListener {
    RecyclerView recyclerView;
    private final int MSG_SUCCESS = 100;
    private final int MSG_ERROR = 200;
    private Handler workerThreadOneHandler;
    private CircleProgressBar circleProgressBar;
    private TextView txt_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_error = (TextView)findViewById(R.id.txt_error);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_green_dark);

        final Button fab = (Button) findViewById(R.id.fab);
        fab.setText(getResources().getText(R.string.lang));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        // Toolbar height needs to be known before establishing the initial offset
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                ScrollManager manager = new ScrollManager();
                manager.attach(recyclerView);
                manager.addView(toolbar, ScrollManager.Direction.UP);
                manager.addView(fab, ScrollManager.Direction.DOWN);
                manager.setInitialOffset(toolbar.getHeight());
            }
        });

        addCountryListRequestToQueue();
        handleConcurency();
    }

    private void handleConcurency() {
        Thread workerThreadOne = new Thread() {
            @Override
            public void run() {
                Looper.prepare();

                workerThreadOneHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.obj == null)
                            return;

                        JSONArray response = (JSONArray) msg.obj;
                        switch (msg.what) {
                            case MSG_SUCCESS:
                                HashMap<String, CountryModal> countryMap  = loadJsonArrayToMap(response);
                                List<CountryModal> countriesByLocalizedName = sortMapByLocalizedName(countryMap);
                                loadDataToRecyclerView(countriesByLocalizedName);
                                ViagogoApplication.getInstance().setCountryMap(countryMap);
                                break;
                            case MSG_ERROR:
                                // TODO
                                break;
                        }
                    }

                    private HashMap<String, CountryModal> loadJsonArrayToMap(JSONArray response) {
                        HashMap<String, CountryModal> countryMap = new HashMap<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = new JSONObject(response.get(i).toString());
                                countryMap.put(obj.getString("alpha3Code"), new CountryModal(MainActivity.this, obj));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        return countryMap;
                    }

                    private List<CountryModal> sortMapByLocalizedName(HashMap<String, CountryModal> countryMap) {
                        List<CountryModal> countriesByLocalizedName = new ArrayList<>(countryMap.values());

                        Collections.sort(countriesByLocalizedName, new Comparator<CountryModal>() {
                            public int compare(CountryModal o1, CountryModal o2) {
                                return o1.getLocalizedName().compareTo(o2.getLocalizedName());
                            }
                        });

                        for (CountryModal p : countriesByLocalizedName) {
                            System.out.println(p.getLocalizedName());
                        }

                        return  countriesByLocalizedName;
                    }

                };

                Looper.loop();
            }
        };
        workerThreadOne.start();
    }

    @Override
    public void onItemClick(View view, CountryModal viewModel) {
        DetailActivity.navigate(this, view.findViewById(R.id.image), viewModel);
    }

    public void addCountryListRequestToQueue() {
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Constants.API_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                workerThreadOneHandler.obtainMessage(MSG_SUCCESS, response).sendToTarget();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                circleProgressBar.setVisibility(View.GONE);
                txt_error.setVisibility(View.VISIBLE);
            }
        });

        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private void loadDataToRecyclerView(final List<CountryModal> countryList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this, countryList);
                adapter.setOnItemClickListener(MainActivity.this);
                recyclerView.setAdapter(adapter);
                circleProgressBar.setVisibility(View.GONE);
            }
        });
    }

}
