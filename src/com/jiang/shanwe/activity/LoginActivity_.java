package com.jiang.shanwe.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jiang.shanwe.Config;
import com.jiang.shanwe.net.HttpCallbackListener;
import com.jiang.shanwe.net.HttpMethod;
import com.jiang.shanwe.net.HttpUtil;
import com.jiang.shanwe.loveaccount.R;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
@SuppressLint("NewApi")
public class LoginActivity_ extends Activity implements OnClickListener {
    private static final String[] DUMMY_CREDENTIALS = new String[] {
            "foo@example.com:hello", "bar@example.com:world" };

    private EditText mPhone;
    private EditText mPassword;
    private Button mLoginButton;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mPhone = (EditText) findViewById(R.id.phone);
        mPassword = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.sign_in_button);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mPhone.setText(pref.getString("phoneNum", ""));

        if (!mPhone.getText().toString().isEmpty()) {
            mPassword.requestFocus();
        }

        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.sign_in_button:
            if (mPassword.getText().toString().isEmpty()
                    || mPhone.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity_.this, "请先输入账号和密码", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            HttpUtil.sendHttpRequest(Config.GET_USER_BY_PHONE_NUM + "getUser?phoneNum="
                    + mPhone.getText().toString(), HttpMethod.GET,
                    new HttpCallbackListener() {

                        @Override
                        public void onFinish(String response) {
                            try {
                                JSONObject loginInfoJsonObject = new JSONObject(response);
                                int loginStatus = loginInfoJsonObject.getInt("status");
                                switch (loginStatus) {
                                case -1:
                                    Toast.makeText(LoginActivity_.this, "请输入手机号",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case 0:
                                    Toast.makeText(LoginActivity_.this, "手机号不存在,请先注册",
                                            Toast.LENGTH_SHORT).show();
                                case 1:
                                    JSONObject userInfoJsonObject = loginInfoJsonObject
                                            .getJSONObject("info");
                                    if (mPassword
                                            .getText()
                                            .toString()
                                            .equals(userInfoJsonObject
                                                    .getString("numberPwd"))) {
                                        Intent intent = new Intent(LoginActivity_.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                        editor = pref.edit();
                                        editor.putString("phoneNum", mPhone.getText()
                                                .toString());
                                        editor.commit();
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity_.this, "密码错误,请重新输入",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                default:
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError() {
                            Toast.makeText(LoginActivity_.this, "登录失败,请检查网络",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }, "");
            break;

        default:
            break;
        }
    }

}
