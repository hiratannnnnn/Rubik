package com.webserva.wings.android.rubik;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class MainFragment extends Fragment {

    private int[] imageArray;
    private ImageView imageView;
    private Random random;
    private float currentRotation = 0f; //現在の回転角度
    private SharedPreferences sharedPreferences;
    private int currentImageIndex;
    private boolean isFocusedLearning;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //SharedPreferencesの初期化
        sharedPreferences = getActivity().getSharedPreferences("oll_data", Context.MODE_PRIVATE);

        // 画像リソースIDの配列を初期化
        imageArray = new int[57];
        for (int i = 1; i <= 57; i++) {
            String resourceName = "oll_example_" + i;
            int resID = getResources().getIdentifier(resourceName, "drawable", getActivity().getPackageName());
            imageArray[i - 1] = resID;
        }

        // ImageViewとRandomオブジェクトの初期化
        imageView = view.findViewById(R.id.imageView);
        random = new Random();

        // 初期画像をランダムに設定
        currentImageIndex = random.nextInt(57);
        currentRotation = random.nextInt(4) * 90;
        imageView.setImageResource(imageArray[currentImageIndex]);
        imageView.setRotation(currentRotation);
        String solution = sharedPreferences.getString("oll_" + (currentImageIndex + 1) + "_solution", "");
        TextView answerText = view.findViewById(R.id.answerText);
        // SwitchCompat のインスタンスを取得
        SwitchCompat toggleSwitch = view.findViewById(R.id.toggleAnswer);
        answerText.setVisibility(View.GONE);
        answerText.setText(solution);
        long startTime = System.currentTimeMillis(); // タイムスタンプ

        Button rotateLeftButton = view.findViewById(R.id.rotateLeftButton);
        Button rotateRightButton = view.findViewById(R.id.rotateRightButton);

        rotateLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentRotation -= 90f;
                imageView.setRotation(currentRotation);
            }
        });
        rotateRightButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentRotation += 90f;
                imageView.setRotation(currentRotation);
            }
        }));

        //解法ボタンのリスナ

        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // スイッチがオンの場合、テキストを表示
                    answerText.setVisibility(View.VISIBLE);
                    imageView.setRotation(0f);
                } else {
                    // スイッチがオフの場合、テキストを非表示
                    answerText.setVisibility(View.GONE);
                }
            }
        });

        SwitchCompat focusButton = view.findViewById(R.id.focusButton);
        isFocusedLearning = focusButton.isChecked();

        focusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isFocusedLearning = true;
                } else {
                    isFocusedLearning = false;
                }
            }
        });

        // ボタンのクリックリスナーを設定
        Button correctButton = view.findViewById(R.id.correctButton);
        Button incorrectButton = view.findViewById(R.id.incorrectButton);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime; // 経過時間

                //データを更新
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int correctCount = sharedPreferences.getInt("oll_" + (currentImageIndex + 1) + "_correct",0);
                int incorrectCount = sharedPreferences.getInt("oll_" + (currentImageIndex + 1) + "_incorrect", 0);
                long meanTime = sharedPreferences.getLong("oll_" + (currentImageIndex + 1) + "_meanTime", 0);

                if (v.getId() == R.id.correctButton) {
                    //経過時間に関する処理
                    meanTime = (correctCount * meanTime + elapsedTime)/((correctCount + 1)* 1000L);
                    correctCount++;
                } else {
                    incorrectCount++;
                }

                editor.putInt("oll_" + (currentImageIndex + 1) + "_correct", correctCount);
                editor.putInt("oll_" + (currentImageIndex + 1) + "_incorrect", incorrectCount);
                editor.putLong("oll_" + (currentImageIndex + 1) + "_meanTime", meanTime);
                editor.apply();

                // 新しい画像をランダムに選ぶ

                if (isFocusedLearning){
                    //focusButtonがオンの場合、間違えた回数に基づいて画像を選ぶ
                    int totalWeight = 0;
                    int[] weights = new int[57];
                    for (int i=0; i<57; i++) {
                        int incorrectCount2 = sharedPreferences.getInt("oll_"+(i+1) + "_incorrect",0);
                        weights[i] = (int) Math.pow(2,incorrectCount2);
                        totalWeight += weights[i];
                    }
                    int randNum = random.nextInt(totalWeight);
                    for(int i=0; i<57; i++) {
                        randNum -= weights[i];
                        if(randNum<0) {
                            currentImageIndex = i;
                            //Log.d("MainActivity",weights[i]+"の"+i);
                            break;
                        }
                    }
                } else {
                    currentImageIndex = random.nextInt(57);
                    //Log.d("MainActivity","普通に選びました");
                }


                currentRotation = random.nextInt(4) * 90;
                imageView.setImageResource(imageArray[currentImageIndex]);
                imageView.setRotation(currentRotation);
                String solution = sharedPreferences.getString("oll_" + (currentImageIndex + 1) + "_solution", "");
                TextView answerText = view.findViewById(R.id.answerText);
                answerText.setText(solution);
                toggleSwitch.setChecked(false);
            }
        };

        correctButton.setOnClickListener(buttonClickListener);
        incorrectButton.setOnClickListener(buttonClickListener);

        return view;
    }
}

