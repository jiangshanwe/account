package com.jiang.shanwe.fragment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiang.shanwe.Config;
import com.jiang.shanwe.activity.SetPasswordActivity;
import com.jiang.shanwe.db.DBUtil;
import com.jiang.shanwe.loveaccount.R;
import com.jiang.shanwe.model.Record;
import com.jiang.shanwe.model.RecordTagAss;
import com.jiang.shanwe.model.Tag;

@EFragment
public class SettingsFragment extends Fragment {
    private View view;
    private static Context mContext;

    private static Handler uploadHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                Toast.makeText(mContext, "成功备份到云端", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "备份失败，请检查网络稍后重试", Toast.LENGTH_SHORT).show();
            }
        };
    };

    @ViewById
    TextView tvReset;

    @ViewById
    TextView tvBackupData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
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

    @Click(R.id.tvBackupData)
    public void backupData() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                JSONObject dataJsonObject = new JSONObject();

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Config.BACKUP_DATA_URL);
                HttpResponse httpResponse;

                httpPost.addHeader("Content-Type", "application/json");
                httpPost.addHeader("charset", HTTP.UTF_8);

                DBUtil dbUtil = DBUtil.getInstance(getActivity());
                List<Record> records = dbUtil.getAllRecords();
                List<Tag> tags = dbUtil.getAllTags();
                List<RecordTagAss> recordTagAsses = dbUtil.getAllRecordTagAsses();

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                        .create();
                try {
                    dataJsonObject.put("records", new JSONArray(gson.toJson(records)
                            .toString()));
                    dataJsonObject.put("tags",
                            new JSONArray(gson.toJson(tags).toString()));
                    dataJsonObject.put("recordTagAsses",
                            new JSONArray(gson.toJson(recordTagAsses).toString()));

                    httpPost.setEntity(new StringEntity(dataJsonObject.toString(),
                            HTTP.UTF_8));

                    httpResponse = httpclient.execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        JSONObject resultJsonObject = new JSONObject(EntityUtils
                                .toString(httpResponse.getEntity()));
                        int status = resultJsonObject.getInt("status");
                        uploadHandler.sendEmptyMessage(status);
                    }
                } catch (UnsupportedEncodingException e) {
                    uploadHandler.sendEmptyMessage(-1);
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    uploadHandler.sendEmptyMessage(-1);
                    e.printStackTrace();
                } catch (IOException e) {
                    uploadHandler.sendEmptyMessage(-1);
                    e.printStackTrace();
                } catch (JSONException e) {
                    uploadHandler.sendEmptyMessage(-1);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
