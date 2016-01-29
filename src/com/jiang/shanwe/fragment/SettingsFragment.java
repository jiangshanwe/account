package com.jiang.shanwe.fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiang.shanwe.activity.SetPasswordActivity;
import com.jiang.shanwe.loveaccount.R;

@EFragment
public class SettingsFragment extends Fragment {
    private View view;

    @ViewById
    TextView tvReset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Click(R.id.tvReset)
    public void resetPassword() {
        Intent intent = new Intent(getActivity(), SetPasswordActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
