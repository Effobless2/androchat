package com.example.androchat.conversations;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.androchat.R;
import com.example.localDB.repositories.conversationRepositories.ConversationDataAccessRepository;
import com.example.model.Conversation;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConversationsList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConversationsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversationsList extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ConversationsList() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConversationsList.
     */
    // TODO: Rename and change types and number of parameters
    public static ConversationsList newInstance(String param1, String param2) {
        ConversationsList fragment = new ConversationsList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_conversations_list, container, false);
        ConversationDataAccessRepository conversationDataAccessRepository = new ConversationDataAccessRepository(getContext());
        LiveData<List<Conversation>> list = conversationDataAccessRepository.getAll();
        list.observe(this, new Observer<List<Conversation>>() {
            @Override
            public void onChanged(List<Conversation> conversations) {
                ArrayAdapter listViewAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    conversations
                );
                ListView listView = view.findViewById(R.id.conversation_list);
                listView.setAdapter(listViewAdapter);
            }
        });

        view.findViewById(R.id.newConvBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewConversationActivity.class);
                startActivity(intent);
            }
        });

        return view;
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
}
