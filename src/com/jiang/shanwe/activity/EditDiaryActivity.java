package com.jiang.shanwe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.jiang.shanwe.Config;
import com.jiang.shanwe.db.DBUtil;
import com.jiang.shanwe.model.Diary;
import com.jiang.shanwe.loveaccount.R;

public class EditDiaryActivity extends Activity implements OnClickListener {

    private EditText etDiary;
    private RatingBar rbStar;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_diary);
        initView();
        String diaryContent = DBUtil.getInstance(this).getDiary(
                Config.getCacheLocationDate(this), Config.getCacheUserId(this)) == null ? ""
                : DBUtil.getInstance(this)
                        .getDiary(Config.getCacheLocationDate(this),
                                Config.getCacheUserId(this)).getContent();
        etDiary.setText(diaryContent);
        etDiary.setSelection(diaryContent.length());
    }

    private void initView() {
        etDiary = (EditText) findViewById(R.id.et_diary);
        btnSave = (Button) findViewById(R.id.btn_diary_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_diary_save:
            if (!TextUtils.isEmpty(etDiary.getText())) {
                saveAndBack();
            }
            finish();
            break;
        default:
            break;
        }
    }

    @Override
    public void onBackPressed() {
        saveAndBack();
        super.onBackPressed();
    }

    private void saveAndBack() {
        Intent intent = new Intent();
        intent.putExtra(Config.EXTRA_KEY_DIARY_CONTENT, etDiary.getText().toString());
        Diary diary = new Diary();
        diary.setContent(etDiary.getText().toString());
        diary.setDiaryDate(Config.getCacheLocationDate(this));
        diary.setOwnerId(Config.getCacheUserId(this));
        DBUtil.getInstance(this).saveOrUpdateDiary(diary);
        setResult(RESULT_OK, intent);
    }

}
