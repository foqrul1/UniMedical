package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "AAAA7n3TgCc:APA91bEXpeLYGM19kVXsscRjXrmSsGLleJgS3qvbdpI0oCZbTDE9tVh13eFbfiGM-wf6oqAp8UjCtGKHYUC-WiZyGBKIVMHcQUxMl9m7ZOGqOhhkrRg4zxYJNln1KkVJ6Z0YGoP4bW5e"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
