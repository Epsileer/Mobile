package com.example.zues.healthok;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.example.zues.healthok.model.Location;
import com.example.zues.healthok.model.User;
import com.example.zues.healthok.service.Applocation;
import com.example.zues.healthok.util.ServiceHandler;
import com.example.zues.healthok.util.ServiceURL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Emergency_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Emergency_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Emergency_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
  private Spinner emgchoose;
  private EditText emgdsc,emglocation;
  private CheckBox ch;
  private Button submit;
    private OnFragmentInteractionListener mListener;
    Applocation mylocation;
    private ProgressDialog pDialog;
    JSONObject result = null;
    String status = "-5";
    SessionManager sessionManager;
    User user;
    private String jsonStr;
    double latitude;
    double longitude;

    public Emergency_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Emergency_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Emergency_frag newInstance(String param1, String param2) {
        Emergency_frag fragment = new Emergency_frag();
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
        View inflate =  inflater.inflate(R.layout.fragment_emergency_frag, container, false);
        emgchoose = inflate.findViewById(R.id.emgtype);
        emgdsc = inflate.findViewById(R.id.emgdcp);
        emglocation = inflate.findViewById(R.id.emglocation);
        ch = inflate.findViewById(R.id.emgsetgps);
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUser();
        submit = inflate.findViewById(R.id.emgsub);

        mylocation = new Applocation(getContext());


        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                android.location.Location gpsLocation = mylocation
                        .getLocation(LocationManager.GPS_PROVIDER);

                if (gpsLocation != null) {
                    latitude = gpsLocation.getLatitude();
                    longitude = gpsLocation.getLongitude();
                    Toast.makeText(
                            getContext(),
                            "Mobile Location (GPS): \nLatitude: " + latitude
                                    + "\nLongitude: " + longitude,
                            Toast.LENGTH_LONG).show();
                    emglocation.setText(String.valueOf(latitude)+"||"+String.valueOf(longitude));
                } else {
                    showSettingsAlert("GPS");
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new EmergencyPlace().execute();


            }
        });


        return inflate;

    }
    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getContext());

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getContext().startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
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

private class EmergencyPlace extends AsyncTask< Void , Void ,Void>
{

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Placing order... ");
        // pDialog.setMax(16);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        pDialog.setCancelable(false);
        pDialog.show();


    }

    @Override
    protected Void doInBackground(Void... voids) {

        ServiceHandler sh = new ServiceHandler();
        // Making a request to url and getting response

        int imageId = 0;

        List<NameValuePair> params = new ArrayList<>(5);
        params.add(new BasicNameValuePair("emergencyType", emgchoose.getSelectedItem().toString()));
        params.add(new BasicNameValuePair("description", emgdsc.getText().toString()));
        params.add(new BasicNameValuePair("location", emglocation.getText().toString()));
        params.add(new BasicNameValuePair("memberId", "" + user.getUserId()));

        jsonStr = sh.makeServiceCall(ServiceURL.emg, ServiceHandler.POST, params);
        if (jsonStr != null) {
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
        if (jsonStr == null) {
            Toast.makeText(getActivity(), "Unable to connect!!", Toast.LENGTH_SHORT).show();}
        else
            {
                Toast.makeText(getContext(),"Emergency palced ",Toast.LENGTH_LONG).show();
            }
    }
}




}



