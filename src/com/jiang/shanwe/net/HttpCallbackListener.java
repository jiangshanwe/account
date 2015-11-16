package com.jiang.shanwe.net;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError();
}
