package com.jpw.agocal.home.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.utl.fragment.TitleFragment;
import com.frame.base.utl.fragment.listenter.OnFragmentInteractionListener;
import com.jpw.agocal.R;
import com.jpw.agocal.home.delegate.FootPrintListFragmentDelegate;

/**
 * 足迹
 */
public class FootPrintListFragment extends TitleFragment<FootPrintListFragmentDelegate> {

    private OnFragmentInteractionListener mListener;

    public FootPrintListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FootPrintListFragment newInstance() {
        FootPrintListFragment fragment = new FootPrintListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foot_print_list, container, false);
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

    @Override
    protected Class<FootPrintListFragmentDelegate> getDelegateClass() {
        return FootPrintListFragmentDelegate.class;
    }
}
