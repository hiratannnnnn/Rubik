package com.webserva.wings.android.rubik;

import android.content.SharedPreferences;

import java.util.List;

public interface DataProvider {
    List<MyDataModel> getData();
    SharedPreferences getSharedPreferences();
}
