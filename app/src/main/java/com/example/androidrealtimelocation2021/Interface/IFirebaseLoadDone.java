package com.example.androidrealtimelocation2021.Interface;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFireLoadUserNameDone(List<String> lstEmail);
    void onFirebaseLoadFailed(String message);
}
