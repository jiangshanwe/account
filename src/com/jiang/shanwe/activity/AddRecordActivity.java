package com.jiang.shanwe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.jiang.shanwe.Config;
import com.jiang.shanwe.db.DBUtil;
import com.jiang.shanwe.model.Record;
import com.jiang.shanwe.uidesign.R;
import com.jiang.shanwe.util.NumberUtil;

public class AddRecordActivity extends Activity implements OnClickListener {

    private EditText etCount;
    private EditText etComments;

    private Button btnAddAgain;
    private Button btnSaveRecord;

    private TableLayout tblAllTagCheckbox;

    private int recordId = Config.DEFAULT_EXIST_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_record);
        recordId = getIntent().getIntExtra(Config.EXTRA_KEY_RECORD_ID, -1);
        initViewAndEvent();
    }

    private void initViewAndEvent() {
        etCount = (EditText) findViewById(R.id.et_count);
        etComments = (EditText) findViewById(R.id.et_comments);
        tblAllTagCheckbox = (TableLayout) findViewById(R.id.tbl_all_tag_checkbox);
        btnSaveRecord = (Button) findViewById(R.id.btn_save_record);
        btnSaveRecord.setOnClickListener(this);

        if (recordId != Config.DEFAULT_EXIST_ID) {
            Record existRecord = DBUtil.getInstance(this).getRecord(recordId);
            etCount.setText(NumberUtil.getSimpleDouble(existRecord.getCount()) + "");
            etComments.setText(existRecord.getComments());
            etCount.setSelection((NumberUtil.getSimpleDouble(existRecord.getCount()) + "").length());
            etComments.setSelection(existRecord.getComments().length());

            List<Integer> tagIds = existRecord.getTagIds();

            for (int i = 0; i < tblAllTagCheckbox.getChildCount(); i++) {
                if (tblAllTagCheckbox.getChildAt(i) instanceof TableRow) {
                    TableRow trv = (TableRow) tblAllTagCheckbox.getChildAt(i);
                    for (int j = 0; j < trv.getChildCount(); j++) {
                        if (trv.getChildAt(j) instanceof CheckBox) {
                            CheckBox tagChx = (CheckBox) trv.getChildAt(j);
                            if (tagIds.contains(Integer.parseInt((String) tagChx.getTag()))) {
                                tagChx.setChecked(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_record, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_save_record:
            if (!TextUtils.isEmpty(etCount.getText())) {
                saveAndBack();
            }
            finish();
            break;
        default:
            break;
        }
    }

    private void saveAndBack() {
        Record record = new Record();
        List<Integer> tagIds = new ArrayList<Integer>();
        record.setCount(Double.parseDouble(etCount.getText().toString()));
        for (int i = 0; i < tblAllTagCheckbox.getChildCount(); i++) {
            if (tblAllTagCheckbox.getChildAt(i) instanceof TableRow) {
                TableRow trv = (TableRow) tblAllTagCheckbox.getChildAt(i);
                for (int j = 0; j < trv.getChildCount(); j++) {
                    if (trv.getChildAt(j) instanceof CheckBox) {
                        CheckBox tagChx = (CheckBox) trv.getChildAt(j);
                        if (tagChx.isChecked()) {
                            tagIds.add(Integer.parseInt((String) tagChx.getTag()));
                        }
                    }
                }
            }
        }
        record.setTagIds(tagIds);
        record.setOwnerId(Config.getCacheUserId(this));
        record.setConsumeDate(Config.getCacheLocationDate(this));
        record.setComments(etComments.getText().toString());
        record.setId(recordId);
        DBUtil.getInstance(this).saveOrUpdateRecord(record);
        setResult(RESULT_OK, new Intent());
    }
}
