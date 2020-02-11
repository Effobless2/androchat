package com.example.androchat.friendRequest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androchat.R;
import com.example.androchat.adapters.UserAdapter;
import com.example.firelib.managers.UserManagement;
import com.example.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchUserFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public SearchUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchUserFragment newInstance(String param1, String param2) {
        SearchUserFragment fragment = new SearchUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);
        Button button = view.findViewById(R.id.btn_search_user);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText mEdit = getView().findViewById(R.id.edit_text_id);
                String googleId = mEdit.getText().toString();
                if(null == googleId || googleId.length() == 0)
                    getAllUser();
                else
                    UserManagement.getUserByEmail(googleId)
                            .continueWith(new Continuation<List<User>, List<User>>() {
                                @Override
                                public List<User> then(@NonNull Task<List<User>> task) throws Exception{
                                    List<User> result = task.getResult();
                                    if(result.size() > 0) {
                                        ListView listView = getView().findViewById(R.id.list_user);
                                        UserAdapter listViewAdapter = new UserAdapter(
                                                getActivity(),
                                                result
                                        );
                                        listView.setAdapter(listViewAdapter);
                                    }else {
                                        Toast.makeText(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
                                    }
                                    return result;
                                }
                            });
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllUser();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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

    public void getAllUser(){
        UserManagement.getAllUser()
                .continueWith(new Continuation<List<User>, List<User>>() {
                    @Override
                    public List<User> then(@NonNull Task<List<User>> task) throws Exception{
                        List<User> result = task.getResult();
                        ListView listView = getView().findViewById(R.id.list_user);
                        UserAdapter listViewAdapter = new UserAdapter(
                                getActivity(),
                                result
                        );
                        listView.setAdapter(listViewAdapter);
                        return result;
                    }
                });
    }
}
