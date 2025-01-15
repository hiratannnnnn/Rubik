package com.webserva.wings.android.rubik;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataProvider{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TabLayoutとViewPager2の設定
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // FragmentをViewPagerに追加
        viewPagerAdapter.addFragment(new MainFragment(), getString(R.string.tab_title_main));
        viewPagerAdapter.addFragment(new RecordFragment(), getString(R.string.tab_title_record));
        viewPagerAdapter.addFragment(new TimerFragment(), getString(R.string.tab_title_timer));

        // TabLayoutとViewPager2を連携
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(viewPagerAdapter.getPageTitle(position));
        }).attach();

        //SharedPreferencesの初期化
        SharedPreferences sharedPreferences = getSharedPreferences("oll_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        try{
            InputStream inputStream = getAssets().open("solutions.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            int i = 1;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.replace("\\n","\n");
                editor.putString("oll_" + i + "_solution",line);
                i++;
            }
            editor.apply();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reset) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.confirm))
                    .setMessage(getString(R.string.reset_confirmation))
                    .setNegativeButton(getString(R.string.confirm_no),null)
                    .setPositiveButton(getString(R.string.confirm_yes),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            resetAllRecords();
                        }
                    })
                    .show();
            return true;
        }

        if (id == R.id.notation_explanation) {
            Intent intent = new Intent(this, ExplanationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences("oll_data", Context.MODE_PRIVATE);
    }

    @Override
    public List<MyDataModel> getData() {
        List<MyDataModel> myDataList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("oll_data", Context.MODE_PRIVATE);
        for (int i = 1; i <= 57; i++) {
            int correct = sharedPreferences.getInt("oll_" + i + "_correct", 0);
            int incorrect = sharedPreferences.getInt("oll_" + i + "_incorrect", 0);
            String resourceName = "oll_example_" + (i + 1);
            int imageId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
            String solution = "some_solution";  // 実際には適切な解法を設定
            String meanTime = "" + Math.round(sharedPreferences.getLong("oll_"+i+"_meanTime",0) * 100.0) / 100.0;

            MyDataModel dataModel = new MyDataModel(imageId, correct, incorrect, solution, meanTime);
            myDataList.add(dataModel);
        }
        return myDataList;
    }
    private void resetAllRecords() {
        SharedPreferences sharedPreferences = getSharedPreferences("oll_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 1; i <= 57; i++) {
            editor.putInt("oll_" + i + "_correct", 0);
            editor.putInt("oll_" + i + "_incorrect", 0);
            editor.putLong("oll_" + i + "_meanTime", 0);
        }
        editor.apply();
    }


}
