package com.example.androidrealtimelocation2021.Utils;

import com.example.androidrealtimelocation2021.Model.User;
import com.example.androidrealtimelocation2021.Remote.IFCMService;
import com.example.androidrealtimelocation2021.Remote.RetrofitClient;

public class Common {
    public static final String USER_INFORMATION ="UserInformation" ;
    public static final String USER_UID_SAVE_KEY ="SaveUid" ;
    public static final String TOKENS = "Tokens";
    public static final String FROM_NAME ="FromName" ;
    public static final String ACCEPT_LIST ="acceptList" ;
    public static final String FROM_UID = "FromUid";
    public static final String TO_UID ="ToUid" ;
    public static final String TO_NAME ="ToName" ;
    public static final String FRIEND_REQUEST = "FriendRequest";
    public static User loggedUser;

    public static IFCMService getFCMService(){
        return RetrofitClient.getClient("https://fcm.googleapis.com/")
                .create(IFCMService.class);
    }
}
