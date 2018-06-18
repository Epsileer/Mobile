package com.example.zues.healthok;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zues.healthok.model.MemberDetail;
import com.example.zues.healthok.model.User;
import com.example.zues.healthok.model.UserFull;
import com.example.zues.healthok.util.ServiceHandler;
import com.example.zues.healthok.util.ServiceURL;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserHealthChart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserHealthChart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHealthChart extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private String jsonStr;
    private User user;
    JSONObject result = null;
    private UserFull userFull;
    PieChart pieChart;
    LineChart mChart;
    HomeActivity homeActivity;
    private SessionManager sessionManager;
    TextView textView;
    private ProgressDialog pDialog;
    ServiceURL serviceURL = new ServiceURL();
    String url = serviceURL.Base;
    String query1 = serviceURL.UserDetails;
    final ArrayList<String> members = new ArrayList<String>();
    final ArrayList<String> names = new ArrayList<String>();
    ArrayList<ArrayList<Integer>> memberData = new ArrayList<ArrayList<Integer>>(12);
    ArrayList<String> measureType = new ArrayList<String>(12);
    String g;

    private OnFragmentInteractionListener mListener;

    public UserHealthChart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHealthChart.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHealthChart newInstance(String param1, String param2) {
        UserHealthChart fragment = new UserHealthChart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =inflater.inflate(R.layout.fragment_user_health_chart, container, false);
        textView = v.findViewById(R.id.welcomeTextView);
        textView.setText("Welcome " + homeActivity.fullName + " !\n Now you can manage all your \n healthcare needs with one click.");
        //query1 = query1 + String.valueOf(userFull.getUserId());
        //names.add(homeActivity.fullName);
        pieChart = (PieChart) v.findViewById(R.id.pichart);
        mChart = (LineChart) v.findViewById(R.id.linechart);

        return v;
    }

    public void findMember(){
        /** Amit Tiwari **/
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(20f);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        for(int i=0;i<names.size();i++)
            yValues.add(new PieEntry(i+1,names.get(i)));

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutSine);

        PieDataSet dataSet = new PieDataSet(yValues,"Names");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        /** Amit Tiwari **/
    }

    public void setData(ArrayList<String> measureType, ArrayList<ArrayList<Integer>> values){

        /**for test only started **/
        /*
        int count=10;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for(int i=0;i<count;i++){
            yVals1.add(new Entry(i,i*i));
        }
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        for(int i=0;i<count;i++){
            yVals2.add(new Entry(i,i*i+50));
        }
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        for(int i=0;i<count;i++){
            yVals3.add(new Entry(i,i*i+150));
        }

        LineDataSet set1,set2,set3;

        set1 = new LineDataSet(yVals1,"Height");
        set2 = new LineDataSet(yVals2,"Bp");
        set3 = new LineDataSet(yVals3,"Weight");

        set1.setColor(Color.RED);
        set2.setColor(Color.GREEN);
        set3.setColor(Color.BLUE);

        set1.setLineWidth(2f);
        set2.setLineWidth(2f);
        set3.setLineWidth(2f);
        */
      /**for test only ended **/

        ArrayList<ArrayList<Entry>> val = new ArrayList<ArrayList<Entry>>();
        for(int i=0;i<12;i++){
            if(measureType.get(i) != ""){
                ArrayList<Entry> entries = new ArrayList<Entry>();
                for(int j=1;j<values.get(i).size();j++){
                    int x = values.get(i).get(j);
                    entries.add(new Entry(j,x));
                }
                val.add(entries);
            }
        }
        int size = val.size();
        int a[] = {Color.RED,Color.YELLOW,Color.BLACK,Color.BLUE,Color.GREEN,Color.GRAY,Color.CYAN,Color.MAGENTA,Color.BLACK,Color.argb(1,59,243,78),Color.argb(1,123,123,123),Color.argb(1,90,123,200)};
        LineDataSet dataSet[] = new LineDataSet[size];
        for(int i=0;i<size;i++){
            dataSet[i] = new LineDataSet(val.get(i),measureType.get(i));
            dataSet[i].setColor(a[i]);
            dataSet[i].setLineWidth(2f);
        }

        LineData data = new LineData(dataSet);

        mChart.setData(data);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sessionManager = new SessionManager(context);
        homeActivity = (HomeActivity) getActivity();
        user = sessionManager.getUser();
        new GetUserDetails().execute();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private class GetUserDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(homeActivity);
            pDialog.setMessage("Retrieving family member Details... ");
            // pDialog.setMax(16);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            pDialog.setCancelable(false);
            pDialog.show();


        }


        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String url = ServiceURL.UserDetails + user.getUserId();
            // Making a request to url and getting response

            jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    result = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void resultt) {
            super.onPostExecute(resultt);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (jsonStr == null) {
                Toast.makeText(homeActivity, "Unable to connect!!", Toast.LENGTH_SHORT).show();
            } else {
                userFull = UserFull.fromJSON(jsonStr);
                showFamilyMembers();
            }

        }

    }

    private void showFamilyMembers() {
        ArrayList<MemberDetail> memberDetail = userFull.getMemberDetail();
        for (MemberDetail member : memberDetail
                ) {
            names.add(member.getFirstName() + " " + member.getLastName());
            members.add(Integer.toString(member.getMemberId()));
        }
        if(names.size()==0 || members.size()==0){
            Toast.makeText(getContext(),"UNABLE TO CONNECT",Toast.LENGTH_SHORT).show();
        }
        else {
            new getMemberData().execute();
            findMember();
        }
    }


    /***** chart data for each member  ***/

    public class getMemberData extends AsyncTask<Void, Void, Void> {
        String query = "measure/member/";

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(homeActivity);
            pDialog.setMessage("Retrieving family member medical data... ");
            // pDialog.setMax(16);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            pDialog.setCancelable(false);
            pDialog.show();

            for(int i=0;i<12;i++){
                ArrayList<Integer> integers = new ArrayList<Integer>();
                String strings="";
                integers.add(i);
                memberData.add(integers);
                measureType.add(strings);
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
/*
            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(getActivity());
            g="hellll";
*/
            ServiceHandler sk = new ServiceHandler();

             g = sk.makeServiceCall(query+members.get(0),ServiceHandler.GET);

            JSONArray response;
            if(g!=null)
            {
                try {
                    response = new JSONArray(g);
                    for(int i=0;i<response.length();i++){
                        JSONObject job = null;
                        try {
                            job = response.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {

                            if(job.has("value")) {
                                memberData.get(Integer.parseInt(job.getString("measureTypeId"))-1).add(Integer.parseInt(job.getString("value").split("/")[0]));

                            }
                            else {
                                memberData.get(Integer.parseInt(job.getString("measureTypeId")) - 1).add(Integer.parseInt(job.getString("memberMeasureId")));
                            }measureType.set(Integer.parseInt(job.getString("measureTypeId"))-1,job.getString("measureTypeCode"));
                            g=job.getString("measureTypeId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            g="heellllo";
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            /*

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+query+members.get(0),null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response)  {
                            for(int i=0;i<response.length();i++){
                                JSONObject job = null;
                                try {
                                    job = response.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    memberData.get(Integer.parseInt(job.getString("measureTypeId"))-1).add(Integer.parseInt(job.getString("memberMeasureId")));
                                    measureType.set(Integer.parseInt(job.getString("measureTypeId"))-1,job.getString("measureTypeCode"));
                                    g=job.getString("measureTypeId");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    g="heellllo";
                                }
                            }

                        g = "hllooo";}

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            requestQueue.add(jsonArrayRequest);
*/
            return null;
        }

        @Override
        protected void onPostExecute(Void resultt) {
            super.onPostExecute(resultt);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(g == null)
            {
                Toast.makeText(getContext(),"UNABLE TO CONNECT",Toast.LENGTH_SHORT).show();
            }else
            {
            for(int i=0;i<12;i++){
                Log.d("measureType",measureType.get(i));
                if (measureType.get(i)!="")
                Log.d("memberData",Integer.toString(memberData.get(i).get(1)));
            }
            Log.d("new1234",g);

            setData(measureType,memberData);
            mChart.animateX(1000);

        }}

    }
}
