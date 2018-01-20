package com.example.heenali.loademore_recyclerview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Student> studentList;
    String json_save;
    UserFunctions UF;
    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UF = new UserFunctions(MainActivity.this);
       // toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        studentList = new ArrayList<Student>();
        handler = new Handler();

        ///////default calling
        new GetJson_save().execute();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DataAdapter(studentList, mRecyclerView);


        //calling lodemore
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore()
            {

                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());

                        new GetJson_save_lodemore().execute();

                    }
                }, 2000);

            }
        });



    }



    private void loadData()
    {
        //deftault calling data
        mRecyclerView.setAdapter(mAdapter);

        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

    }

    private class GetJson_save extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();


            try {

                prmsLogin.put("fromdate", "");
                prmsLogin.put("inquiry_destination_city", "");
                prmsLogin.put("inquiry_source_city", "");
                prmsLogin.put("owner_id", "S1617000242");
                prmsLogin.put("status", "");
                prmsLogin.put("todate","");
                jsonArray.put(prmsLogin);
                prms.put("shipper_order", jsonArray);


            } catch (JSONException e)
            {

                e.printStackTrace();
            }

            json_save = UF.RegisterUser("shipper/GetShipperOrdersDetails", prms);


            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (json_save.equals("lost"))
            {
                UF.msg("Connection Problem.");

            } else
            {
                if (json_save.equalsIgnoreCase("0"))
                {
                    UF.msg("Invalid");
                } else
                {
                    try
                    {

                        JSONObject jobj = new JSONObject(json_save);
                        Log.e("Home Get--", json_save.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();

                        Log.e("Myorder Home status >",status);
                        Log.e("--------------------", "----------------------------------");
                        if (status.equalsIgnoreCase("1"))
                        {

                            JSONArray array = new JSONArray();
                            array = jobj.getJSONArray("message");

                            for(int i=0;i<array.length();i++)
                            {

                                String s= array.getJSONObject(i).getString("load_inquiry_no");

                                studentList.add(new Student("IDNo "+s, i+" Recorder No"));

                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run()
                                {

                                    loadData();
                                }
                            }, 1000);




                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }


                }
            }


        }
    }
    private class GetJson_save_lodemore extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();


            try {

                prmsLogin.put("fromdate", "");
                prmsLogin.put("inquiry_destination_city", "");
                prmsLogin.put("inquiry_source_city", "");
                prmsLogin.put("owner_id", "S1617000242");
                prmsLogin.put("status", "");
                prmsLogin.put("todate","");
                jsonArray.put(prmsLogin);
                prms.put("shipper_order", jsonArray);


            } catch (JSONException e)
            {

                e.printStackTrace();
            }

            json_save = UF.RegisterUser("shipper/GetShipperOrdersDetails", prms);
            Log.e("--------------------", "----------------------------------");
            Log.e("Home Post--", prms.toString());
            Log.e("Home url---", "shipper/GetShipperOrdersDetails");

            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (json_save.equals("lost"))
            {
                UF.msg("Connection Problem.");

            }
            else
            {
                if (json_save.equalsIgnoreCase("0"))
                {
                    UF.msg("Invalid");
                } else
                {
                    try
                    {

                        JSONObject jobj = new JSONObject(json_save);
                        Log.e("Home Get--", json_save.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();

                        Log.e("Myorder Home status >",status);
                        Log.e("--------------------", "----------------------------------");
                        if (status.equalsIgnoreCase("1"))
                        {

                            JSONArray array = new JSONArray();
                            array = jobj.getJSONArray("message");

                            int start=studentList.size();

                            for(int i=0;i<array.length();i++)
                            {

                                String s= array.getJSONObject(i).getString("load_inquiry_no");

                                studentList.add(new Student("IDNo "+s, i+start+" Recorder No"));
                                mAdapter.notifyItemInserted(studentList.size());
                            }
                            mAdapter.setLoaded();

                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }


                }
            }


        }
    }

}
