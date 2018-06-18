package com.example.zues.healthok;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.zues.healthok.model.User;
import com.example.zues.healthok.util.ServiceHandler;
import com.example.zues.healthok.util.ServiceURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 *
 * subodhrai
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Health_chart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Health_chart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Health_chart extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HomeActivity homeActivity;
    User user;
    SessionManager sessionManager;
    TextView textView;
    Spinner spinner1,spinner2,spinner3;
    ServiceURL serviceURL = new ServiceURL();
    String url = serviceURL.Base;
    String query = serviceURL.healthhistory;
    final ArrayList<String> category = new ArrayList<String>();
    final ArrayList<String> familyMember = new ArrayList<String>();
    String id;
    JSONObject result;
    JSONArray history;
    ProgressDialog pDialog;
    String jsonStr;
    View infalt;
    private Button show,addhistory;

    private OnFragmentInteractionListener mListener;

    public Health_chart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Health_chart.
     */
    // TODO: Rename and change types and number of parameters
    public static Health_chart newInstance(String param1, String param2) {
        Health_chart fragment = new Health_chart();
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
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
         infalt = inflater.inflate(R.layout.fragment_health_chart, container, false);
        textView = infalt.findViewById(R.id.welcomeTextView);
        //textView.setText("Welcome " + homeActivity.fullName + " !\n Now you can manage all your \n healthcare needs with one click.");
        category.add("ALL");
        spinner1=infalt.findViewById(R.id.addtest);
        spinner2=infalt.findViewById(R.id.addtype);
        familyMember.add(homeActivity.fullName);
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUser();
        query = "measure/measureCategoryType";
        show = infalt.findViewById(R.id.healthhistoryshow);
        addhistory = infalt.findViewById(R.id.addhistory);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showHistory(spinner2.getSelectedItem().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        addhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new AddHistory() );

                transaction.commit();

            }
        });



        // Creating service handler class instance



        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+query,null,
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
                                category.add(job.getString("measureCategoryType"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonArrayRequest);

        ArrayAdapter adapter1 = new ArrayAdapter(getActivity(),
                android.R.layout.select_dialog_singlechoice,familyMember);
        spinner1.setAdapter(adapter1);

        ArrayAdapter adapter2 = new ArrayAdapter(getActivity(),
                android.R.layout.select_dialog_singlechoice,category);
        spinner2.setAdapter(adapter2);

        new GetOrderDetails().execute();

        return infalt;

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
        homeActivity = (HomeActivity) getActivity();
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
    @SuppressLint("ResourceType")
    public void showHistory(String g) throws JSONException {
        TableLayout tableLayout = (TableLayout) infalt.findViewById(R.id.healthhistorytable);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        //JSONObject lm = history.getJSONObject(0);
        tableLayout.removeAllViews();

        //textView.setText(lm.getString("measureCategoryType")+" "+g);

        if(g.equals("ALL"))
        {

        for (int i = 0; i < history.length(); i++) {
            final JSONObject j = history.getJSONObject(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.healhistorytab, null);
            if (i % 2 != 0)
                view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.border));
            ((TextView) view.findViewById(R.id.historyvital)).setText(j.getString("measureTypeText"));
            if(j.has("value")) {
                ((TextView) view.findViewById(R.id.historyvalue)).setText(j.getString("value"));
            }
            else
            {
                ((TextView) view.findViewById(R.id.historyvalue)).setText(" ");

            }
            ((TextView)view.findViewById(R.id.historydate)).setText(j.getString("measureDate").substring(0,10));
            tableLayout.addView(view, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


        }}

        else
        {
            for (int i = 0; i < history.length(); i++) {
                final JSONObject j = history.getJSONObject(i);
                if(j.getString("measureCategoryType").equals(g)) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.healhistorytab, null);
                    if (i % 2 != 0)
                        view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.border));
                    ((TextView) view.findViewById(R.id.historyvital)).setText(j.getString("measureTypeText"));
                    if(j.has("value")) {
                        ((TextView) view.findViewById(R.id.historyvalue)).setText(j.getString("value"));
                    }
                    else
                    {
                        ((TextView) view.findViewById(R.id.historyvalue)).setText(" ");

                    }                    ((TextView) view.findViewById(R.id.historydate)).setText(j.getString("measureDate").substring(0, 10));
                    tableLayout.addView(view, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }

            }
        }
    }

    private class GetOrderDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(homeActivity);
            pDialog.setMessage("Retrieving  Details... ");
            // pDialog.setMax(16);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            pDialog.setCancelable(false);
            pDialog.show();


        }


        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh1 = new ServiceHandler();
            String url1 = ServiceURL.UserDetails + user.getUserId();
            // Making a request to url and getting response

            String jsonStr1 = sh1.makeServiceCall(url1, ServiceHandler.GET);


            try{

                if(jsonStr1 !=null) {

                    result = new JSONObject(jsonStr1);
                    //s = result.getString("emailId");
                    JSONArray j = result.getJSONArray("memberDetail");
                    JSONObject jb = j.getJSONObject(0);
                    id = jb.getString("memberId");
                }

//                    status = result.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }





            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String url = ServiceURL.healthhistory + id;
            // Making a request to url and getting response

            jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    history = new JSONArray(jsonStr);
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
                try {
                    showHistory("ALL");
                   // Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }

    }
}
