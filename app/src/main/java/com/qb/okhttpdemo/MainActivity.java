package com.qb.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText etPhone, etPassword;
    private Button btnLogin;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRequset();
            }
        });
    }

    private void onRequset() {
        if (!TextUtils.isEmpty(etPhone.getText()) && !TextUtils.isEmpty(etPassword.getText())) {
            OkHttpClient mOkHttpClient = new OkHttpClient();
            //创建一个Request
            Request request = new Request.Builder()
                    .url("http://youbangserver.cn/YouBang/Login?phone=" + etPhone.getText().toString() + "&password=" + etPassword.getText().toString())
                    .build();
            //new call
            Call call = mOkHttpClient.newCall(request);
            //请求加入调度（异步方式，同步为call.execute）
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result=response.body().string();
                    Gson gson = new Gson();
                    final LoginMsg msg=gson.fromJson(result,LoginMsg.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if("1".equals(msg.getResult())){
                                tvStatus.setText("登录成功");
                            }else{
                                tvStatus.setText("登录失败");
                            }
                        }
                    });
                }
            });
        }else{
            Toast.makeText(MainActivity.this,"两个输入框不能为空",Toast.LENGTH_SHORT).show();
        }
    }
}
