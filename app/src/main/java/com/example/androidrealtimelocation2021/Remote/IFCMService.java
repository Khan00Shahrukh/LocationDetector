package com.example.androidrealtimelocation2021.Remote;

import com.example.androidrealtimelocation2021.Model.MyResponse;
import com.example.androidrealtimelocation2021.Model.Request;




import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAATfNzbn8:APA91bFr_YVN4IhDwZi9OfpIOkmK_oVVbPR5zIlNrDhXdaASVPHYUoCOiFbs-nvG7Icivd5z0EvjeQNuV8-htRcj3no8bHfMU3NwmqV-oYVg2xrCksITFphyqBBdebfPylmbMrb3j-Eq"
            //Cloud Messaging key

    })
    @POST("fcm/send")
    Observable<MyResponse> sendFriendRequestToUser(@Body Request body);
}
