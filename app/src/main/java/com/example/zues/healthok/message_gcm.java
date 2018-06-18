package com.example.zues.healthok;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zues.healthok.model.Db;
import com.example.zues.healthok.model.GCMRequestData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link message_gcm.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link message_gcm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class message_gcm extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<String> info = new ArrayList<String>();
    ListView l;
    Db d;
    ArrayAdapter a;

    private OnFragmentInteractionListener mListener;

    public message_gcm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment message_gcm.
     */
    // TODO: Rename and change types and number of parameters
    public static message_gcm newInstance(String param1, String param2) {
        message_gcm fragment = new message_gcm();
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

        View inflate   =  inflater.inflate(R.layout.fragment_message_gcm, container, false);

         d = new Db(getContext());
        info = d.getAllContacts();


        GCMRequestData g = GCMRequestData.getInstance();
        info.add(g.getMessage());


         a = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,info);
        l = inflate.findViewById(R.id.list1);
        l.setAdapter(a);
        registerForContextMenu(l);




    return inflate;
    }


    public void onCreateContextMenu(ContextMenu  menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.list1) {
            AdapterView.AdapterContextMenuInfo info1 = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Option");
            String[] menuItems = {"Copy","Delete"};
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info1 = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = {"Copy","Delete"};
        String menuItemName = menuItems[menuItemIndex];
        if(menuItemName == "Copy")
        {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Clip", info.get(info1.position));
            clipboard.setPrimaryClip(clip);
        }
        if(menuItemName == "Delete")
        {

            d.deleteNote(info.get(info1.position));
            a.remove(a.getItem(info1.position));
            a.notifyDataSetChanged();
        }

        return true;
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
}
