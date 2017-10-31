package com.example.administrator.cmazxiaoma.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.ui.ChatActivity;
import com.example.administrator.cmazxiaoma.ui.QueryDeliveryActivity;
import com.example.administrator.cmazxiaoma.ui.QueryPhoneActivity;

/**
 * Created by Administrator on 2017/2/15.
 */

public class LifeToolsFragment extends Fragment implements View.OnClickListener {
    private ImageView life_query_delivery,life_query_phone_land,life_chat;
    private TextView query_delivery_text,query_phone_text,life_chat_text;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_life_tools,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        life_query_delivery= (ImageView) view.findViewById(R.id.life_query_delivery);
        life_query_phone_land= (ImageView) view.findViewById(R.id.life_query_phone_land);
        life_query_delivery.setOnClickListener(this);
        life_query_phone_land.setOnClickListener(this);
        query_delivery_text= (TextView) view.findViewById(R.id.query_delivery_text);
        query_phone_text= (TextView) view.findViewById(R.id.query_phone_text);
        query_phone_text.setOnClickListener(this);
        query_delivery_text.setOnClickListener(this);
        life_chat= (ImageView) view.findViewById(R.id.life_chat_img);
        life_chat_text= (TextView) view.findViewById(R.id.life_chat_text);
        life_chat.setOnClickListener(this);
        life_chat_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.query_delivery_text:
            case R.id.life_query_delivery:
                startActivity(new Intent(getActivity(), QueryDeliveryActivity.class));
                break;
            case R.id.query_phone_text:
            case R.id.life_query_phone_land:
                startActivity(new Intent(getActivity(), QueryPhoneActivity.class));
                break;
            case R.id.life_chat_img:
                case R.id.life_chat_text:
                startActivity(new Intent(getActivity(), ChatActivity.class));
                break;
        }


    }
}
