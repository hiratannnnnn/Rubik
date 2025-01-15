package com.webserva.wings.android.rubik;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExplanationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);  // このレイアウトはあなたが作成するものです
        setTitle(getString(R.string.explanation_name));

        List<ExplanationItem> explanationItems = new ArrayList<>();
        explanationItems.add(new ExplanationItem("U",getString(R.string.explanation_u)));
        explanationItems.add(new ExplanationItem("R",getString(R.string.explanation_r)));
        explanationItems.add(new ExplanationItem("F",getString(R.string.explanation_f)));
        explanationItems.add(new ExplanationItem("M",getString(R.string.explanation_m)));
        explanationItems.add(new ExplanationItem("L",getString(R.string.explanation_l)));
        explanationItems.add(new ExplanationItem("D",getString(R.string.explanation_d)));
        explanationItems.add(new ExplanationItem("S",getString(R.string.explanation_s)));
        explanationItems.add(new ExplanationItem("F R U R' U' F'",getString(R.string.explanation_fruruf)));


        ExplanationAdapter adapter = new ExplanationAdapter(explanationItems);
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ここで各種初期化や設定を行います。
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}