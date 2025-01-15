package com.webserva.wings.android.rubik;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecordFragment extends Fragment {

    private RecyclerView recyclerView;
    private DataProvider dataProvider;  // DataProviderを追加
    private SharedPreferences sharedPreferences; // クラスレベルで定義
    private List<MyDataModel> myDataList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DataProvider) {
            dataProvider = (DataProvider) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement DataProvider");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateData(); // データを更新
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // SharedPreferencesの初期化
        sharedPreferences = dataProvider.getSharedPreferences();

        Button resetButton = view.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetData();
            }
        });

        Button sortButton = view.findViewById(R.id.sortButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByIncorrect();
            }
        });

        updateData(); // 初回のデータ設定

        return view;
    }

    private void updateData() {
        myDataList = new ArrayList<>();
        for (int i = 1; i <= 57; i++) {
            int correct = sharedPreferences.getInt("oll_" + i + "_correct", 0);
            int incorrect = sharedPreferences.getInt("oll_" + i + "_incorrect", 0);
            String resourceName = "oll_example_" + i;
            int imageId = getResources().getIdentifier(resourceName, "drawable", getActivity().getPackageName());
            String solution = sharedPreferences.getString("oll_" + i + "_solution","");  // 実際には適切な解法を設定
            String meanTime = "" + Math.round(sharedPreferences.getLong("oll_"+i+"_meanTime",0) * 100.0) / 100.0;
            MyDataModel dataModel = new MyDataModel(imageId, correct, incorrect, solution, meanTime);
            myDataList.add(dataModel);
        }
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(getActivity(), myDataList);
        recyclerView.setAdapter(adapter);
    }

    private void resetData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 1; i <= 57; i++) {
            editor.putInt("oll_" + i + "_correct", 0);
            editor.putInt("oll_" + i + "_incorrect", 0);
            editor.putLong("oll_" + i + "_meanTime", 0);
        }
        editor.apply();
        updateData(); // データをリセットした後で更新
    }
    private void sortByIncorrect() {
        Collections.sort(myDataList, new Comparator<MyDataModel>() {
            @Override
            public int compare(MyDataModel o1, MyDataModel o2) {
                return Integer.compare(o2.getIncorrect(), o1.getIncorrect());
            }
        });
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
