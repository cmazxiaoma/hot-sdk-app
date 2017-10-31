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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.adapter.DeliveryAdapter;
import com.example.administrator.cmazxiaoma.entity.DeliveryData;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/2/17.
 */

public class QueryDeliveryActivity extends  BaseActivity implements View.OnClickListener, TextWatcher, View.OnTouchListener {
    EditText query_delivery_company,query_delivery_code;
    Button btn_query;
    private List<DeliveryData> mlist=new ArrayList<>();
    private ListView mlistView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.life_tools_query_delivery);
        initView();

    }
    private void initView() {
        mlistView= (ListView) findViewById(R.id.query_delivery_mlistView);
        query_delivery_company= (EditText) findViewById(R.id.query_delivery_company);
        query_delivery_code= (EditText) findViewById(R.id.query_delivery_code);
        mlistView.setOnTouchListener(this);
        btn_query= (Button) findViewById(R.id.btn_query_delivery);
        btn_query.setOnClickListener(this);
        query_delivery_code.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_query_delivery:
                //第一步隐藏输入法
                UtilTools.HideSoftInputViewGroup(this,mlistView);
                btn_query.setEnabled(false);
                btn_query.setText(getResources().getString(R.string.loading_query));
                String company=query_delivery_company.getText().toString().trim();
                final String code=query_delivery_code.getText().toString().trim();
                //要判断物流单号的位数至少要大于10
                if(code.length()>=10)
                {
                    //第一步:先去请求，然后返回一个快递公司的comdoe
                    String comUrl="https://www.kuaidi100.com/autonumber/autoComNum?text="+code;
                    RxVolley.get(comUrl, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            super.onSuccess(t);
                            try {
                                UtilsLog.i("t=" + t);
                                JSONObject object = new JSONObject(t);
                                UtilsLog.i("object=" + object);
                                JSONArray auto = object.getJSONArray("auto");
                                UtilsLog.i("auto=" + auto);
                                //判断auto时候为auto=[]，如果等于这个，那么就说明快递单号是瞎填的
                                if (auto.length()!=0) {
                                    UtilsLog.i("进行到下一步,auto.lengt()="+auto.length());
                                    //默认只取第一个快递的comcode，因为排名第一个都是主流快递，后面的都是垃圾快递。大家用的都是主流快递
                                    JSONObject object1 = (JSONObject) auto.get(0);//如果auto为空，那么程序就不动了，静止了。
                                    UtilsLog.i("object1=" + object1);
                                    String comCode = object1.getString("comCode");
                                    UtilsLog.i("comCode=" + comCode);
                                    if (comCode != null) {
                                        UtilsLog.i("comCode=" + comCode);
                                        query_delivery_company.setText(comCode + "快递");
                                        //第二步：拼接我们的uri，去查询该快递公司对应的快递
                                        String url = "https://www.kuaidi100.com/query?type=" + comCode + "&postid=" + code + "&id=1&valicode=&temp=0.581272836541757";
                                        //拿到我们数据 去请求数据（Json)
                                        RxVolley.get(url, new HttpCallback() {
                                            @Override
                                            public void onSuccess(String t) {
                                                super.onSuccess(t);
                                                UtilsLog.i("快递信息Json=" + t);
                                                parsingJson(t);
                                            }
                                        });
                                    } else {
                                        UtilTools.setAlertDialog(QueryDeliveryActivity.this,"单号不存在或者已经过期");
                                        query_delivery_company.setText(getResources().getString(R.string.query_delivery_company));
                                        btn_query.setText(getResources().getString(R.string.query_delivery));
                                        btn_query.setEnabled(true);
                                        UtilsLog.i("comCode=null");
                                    }

                                }else{
                                    UtilsLog.i("否则失败");
                                    UtilTools.setAlertDialog(QueryDeliveryActivity.this,"单号不存在或者已经过期");
                                    query_delivery_company.setText(getResources().getString(R.string.query_delivery_company));
                                    btn_query.setText(getResources().getString(R.string.query_delivery));
                                    btn_query.setEnabled(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    UtilTools.Toast(QueryDeliveryActivity.this,"物流单号至少是10位");
                    btn_query.setEnabled(true);
                    btn_query.setText(getResources().getString(R.string.query_delivery));
                    query_delivery_company.setText(getResources().getString(R.string.query_delivery_company));
                }
                break;
        }

    }
    //解析物流查询数据
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            int status=jsonObject.getInt("status");
            //判断返回status是否等于200,等于200,就是有物流信息
            if(status==200){
                JSONArray data=jsonObject.getJSONArray("data");
                //必须先清空里面的集合，考虑到用户重复点击多次查询快递，里面的数据会重复
                mlist.clear();
                for(int i=0;i<data.length();i++){
                    JSONObject object= (JSONObject) data.get(i);
                    DeliveryData deliveryData=new DeliveryData();
                    deliveryData.setTime(object.getString("time"));
                    deliveryData.setContext(object.getString("context"));
                    mlist.add(deliveryData);
                }
                DeliveryAdapter adapter=new DeliveryAdapter(QueryDeliveryActivity.this,mlist);
                mlistView.setAdapter(adapter);
                btn_query.setEnabled(true);
                btn_query.setText(getResources().getString(R.string.query_delivery));
            }else{
                UtilTools.setAlertDialog(QueryDeliveryActivity.this,"单号不存在或者已经过期");
                btn_query.setEnabled(true);
                btn_query.setText(getResources().getString(R.string.query_delivery));
                query_delivery_company.setText(getResources().getString(R.string.query_delivery_company));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(query_delivery_code.getText().toString().trim()))
        {
            btn_query.setEnabled(true);
        }else {
            btn_query.setEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mlistView.requestFocus();
        UtilsLog.i("进行触摸");
        UtilTools.HideSoftInputViewGroup(this,mlistView);
        return false;
    }
}
