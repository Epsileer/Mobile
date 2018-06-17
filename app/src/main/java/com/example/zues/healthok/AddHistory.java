package com.example.zues.healthok;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zues.healthok.model.User;
import com.example.zues.healthok.util.ServiceHandler;
import com.example.zues.healthok.util.ServiceURL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddHistory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    HomeActivity homeActivity;
    TextView textView;
    TextView familyname,date11;
    String id;
    Spinner addtest,addtype;
    private Button submit;
    private EditText value;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    private  User user;
   private List<String> type = new ArrayList<String>();
    ArrayAdapter adaptertype;
    private JSONArray jtype;
    private String jsonStr;
    private int index;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener  date;
    String sendjtr;
    private String status;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHistory newInstance(String param1, String param2) {
        AddHistory fragment = new AddHistory();
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
        View v= inflater.inflate(R.layout.fragment_add_history, container, false);
        homeActivity = (HomeActivity) getActivity();
        textView = v.findViewById(R.id.welcomeTextView);
        textView.setText("Welcome " + homeActivity.fullName + " !\n Now you can manage all your \n healthcare needs with one click.");
        familyname = v.findViewById(R.id.tv1);
        familyname.setText(homeActivity.fullName);
        date11 = v.findViewById(R.id.et);
        value = v.findViewById(R.id.et1);
        submit = v.findViewById(R.id.save);

        addtest = v.findViewById(R.id.addtest);
        addtype = v.findViewById(R.id.addtype);
        index = addtest.getSelectedItemPosition();
        new Gettype().execute();

        addtest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(index!=i)
                {
                    new Gettype().execute();
                    index = i;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });












          type.add("Select Measure");
        adaptertype = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,type);
        adaptertype.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        addtype.setAdapter(adaptertype);


        //
        myCalendar = Calendar.getInstance();
         date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Test();
        }
    });







        return v;
    }


    private void updateLabel() {

        String myFormat = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        date11.setText(sdf.format(myCalendar.getTime())+"Z");
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
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUser();


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
        void onFragmentInteraction(Uri uri);}



        public void Test()
        {
           new Sendtype().execute();


        }




        private class Gettype extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                //super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Retrieving  Details... ");
                // pDialog.setMax(16);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                pDialog.setCancelable(false);
                pDialog.show();


            }

            @Override
            protected Void doInBackground(Void... voids) {

                ServiceHandler sh1 = new ServiceHandler();
                String url1 = ServiceURL.UserDetails + user.getUserId();
                // Making a request to url and getting response

                String jsonStr1 = sh1.makeServiceCall(url1, ServiceHandler.GET);
                JSONObject result;

                try {

                    if (jsonStr1 != null) {

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
                String url = "measure/measureCategoryType/" + addtest.getSelectedItem().toString();
                // Making a request to url and getting response

                jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
               // Log.d("REsponse ", jsonStr);
                if (jsonStr != null) {
                    try {
                        jtype = new JSONArray(jsonStr);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    type.clear();


                    for (int i = 0; i < jtype.length(); i++) {
                        final JSONObject j;
                        try {
                            j = jtype.getJSONObject(i);
                            Log.d("Response",j.getString("measureTypeText"));
                            type.add(j.getString("measureTypeText"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            type.add("NOTHING");
                        }



                    }

                   adaptertype = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,type);
                    adaptertype.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                    adaptertype.notifyDataSetChanged();



                }


            }


        }

        private class Sendtype extends AsyncTask<Void,Void,Void>
        {

            @Override
            protected void onPreExecute() {
                //super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Send  Details... ");
                // pDialog.setMax(16);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                pDialog.setCancelable(false);
                pDialog.show();


            }
            @Override
            protected Void doInBackground(Void... voids) {

                ServiceHandler sm = new ServiceHandler();
                String url = "measure";

                String md = "1";
                if(jtype!=null){
                try {
                   md = jtype.getJSONObject(addtype.getSelectedItemPosition()).getString("measureTypeId");
                } catch (JSONException e) {
                    e.printStackTrace();
                    md= "1";

                }}

                List<NameValuePair> params = new ArrayList<>(5);
                params.add(new BasicNameValuePair("measureDate", date11.getText().toString()));
                params.add(new BasicNameValuePair("measureTypeId", md));

                params.add(new BasicNameValuePair("memberId", id));
                params.add(new BasicNameValuePair("type","memberMeasure"));
                params.add(new BasicNameValuePair("value",value.getText().toString()));
                sendjtr = sm.makeServiceCall(url,ServiceHandler.POST, params);


                 JSONObject result;
                if (sendjtr != null) {
                    try {
                        result = new JSONObject(jsonStr);
                        status = result.getString("status");
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
                if (sendjtr == null) {
                    Toast.makeText(getActivity(), "Unable to connect!!", Toast.LENGTH_SHORT).show();
                } else {
                        Toast.makeText(getActivity(),status+"Send Complete ",Toast.LENGTH_LONG).show();

                }
            }
        }
    }


