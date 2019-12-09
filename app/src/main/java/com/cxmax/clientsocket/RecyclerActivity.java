package com.cxmax.clientsocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2019-12-01.
 */
public class RecyclerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main);
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(new RecyclerAdapter(data,this));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        data = new ArrayList<>();
//        for (int i = 'A'; i < 'z'; i++)
//        {
            data.add("这是" + 1 + "项");
            data.add("这是" + 2 + "项");
            data.add("这是" + 3 + "项");
            data.add("这是" + 4 + "项");
//        }
    }
}
