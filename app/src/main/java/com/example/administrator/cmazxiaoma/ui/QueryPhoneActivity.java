package com.example.administrator.cmazxiaoma.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/18.
 */

public class QueryPhoneActivity extends BaseActivity implements View.OnClickListener, TextWatcher, View.OnTouchListener {
    private Button btn_query_phone;
    private EditText query_phone;
    private TextView phone_location,phone_city_postcode,phone_code,phone_card;
    private ImageView phone_img;
    private LinearLayout phone_linear;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.life_tools_query_phone);
        initView();
    }

    private void initView() {
        phone_linear= (LinearLayout) findViewById(R.id.query_phone_linear);
        phone_linear.setOnTouchListener(this);
        btn_query_phone= (Button) findViewById(R.id.btn_query_phone);
        query_phone= (EditText) findViewById(R.id.query_phone);
        btn_query_phone.setOnClickListener(this);
        query_phone.addTextChangedListener(this);
        phone_img= (ImageView) findViewById(R.id.query_phone_img);
        phone_location= (TextView) findViewById(R.id.phone_location);
        phone_city_postcode= (TextView) findViewById(R.id.phone_location_postcode);
        phone_code= (TextView) findViewById(R.id.phone_location_city_code);
        phone_card= (TextView) findViewById(R.id.phone_card);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_query_phone:
                //隐藏输入法
                UtilTools.HideSoftInputViewGroup(this,phone_linear);
                btn_query_phone.setEnabled(false);
                btn_query_phone.setText(getResources().getString(R.string.loading_query));
                String phone=query_phone.getText().toString().trim();
                String url="http://apis.haoservice.com/mobile?phone="+phone+"&key="+ StaticClass.QUERY_PHONE_LAND;
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        try {
                            JSONObject object=new JSONObject(t);
                            String reason=object.getString("reason");
                            if(reason.equals(getResources().getString(R.string.query_phone_land_success))){
                                JSONObject object1=object.getJSONObject("result");
                                UtilsLog.i("返回查询手机状态="+object);
                                UtilsLog.i("result="+object1);
                                String province=object1.getString("province");
                                UtilsLog.i("province="+province);
                                String city=object1.getString("city");
                                String location=province+city;
                                phone_location.setText("归属地:"+location);
                                String company=object1.getString("company");
                                switch (company){
                                    case "移动":
                                        phone_img.setImageResource(R.drawable.china_mobile);
                                        break;
                                    case "电信":
                                        phone_img.setImageResource(R.drawable.china_telecom);
                                        break;
                                    case "联通":
                                        phone_img.setImageResource(R.drawable.china_unicom);
                                        break;
                                }
                                String zip=object1.getString("zip");
                                phone_city_postcode.setText("城市邮编:"+zip);
                                String areacode=object1.getString("areacode");
                                phone_code.setText("城市区号:"+areacode);
                                String card=object1.getString("card");
                                phone_card.setText("卡类型:"+card);
                                btn_query_phone.setEnabled(true);
                                btn_query_phone.setText(getResources().getString(R.string.btn_query_phone));
                            }else{
                                UtilTools.setAlertDialog(QueryPhoneActivity.this,reason);
                                btn_query_phone.setEnabled(true);
                                btn_query_phone.setText(getResources().getString(R.string.btn_query_phone));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(query_phone.getText().toString().trim())){
            btn_query_phone.setEnabled(true);
        }else {
            btn_query_phone.setEnabled(false);
        }

        if(TextUtils.isEmpty(query_phone.getText().toString().trim())){
            phone_card.setText("");
            phone_code.setText("");
            phone_city_postcode.setText("");
            phone_location.setText("");
            phone_img.setImageBitmap(null);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        phone_linear.requestFocus();
        UtilTools.HideSoftInputViewGroup(this,phone_linear);
        return false;
    }
}
