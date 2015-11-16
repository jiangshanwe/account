package com.jiang.shanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jiang.shanwe.model.Record;
import com.jiang.shanwe.model.Tag;
import com.jiang.shanwe.uidesign.R;
import com.jiang.shanwe.util.NumberUtil;

public class RecordAdapter extends ArrayAdapter<Record> {

    private int resourceId;
    private List<Record> records = new ArrayList<Record>();

    public RecordAdapter(Context context, int textViewResourceId, List<Record> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
        this.records = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Record record = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView tvRecordDesc = (TextView) view.findViewById(R.id.tv_record_desc);
        String recordDesc = NumberUtil.getSimpleDouble(record.getCount());
        if (!TextUtils.isEmpty(record.getComments())) {
            recordDesc += " | " + record.getComments();
        }
        for (int i = 0; i < record.getTags().size(); i++) {
            Tag tag = record.getTags().get(i);
            if (i == 0 && record.getTags() != null && record.getTags().size() > 0) {
                recordDesc += " | " + tag.getName();
            } else {
                recordDesc += " " + tag.getName();
            }
        }
        tvRecordDesc.setText(recordDesc);
        return view;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Record getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return records.get(position).getId();
    }

}
