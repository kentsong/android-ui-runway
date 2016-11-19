package com.android.ui.kent.demo.network.rxjava;

import com.android.ui.kent.demo.network.retrofit.vo.User;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Kent on 2016/11/19.
 */

public interface GitHubUser {
    @GET( "users/{user}" )
    Observable<User> getUser (@Path("user") String  user );
}
