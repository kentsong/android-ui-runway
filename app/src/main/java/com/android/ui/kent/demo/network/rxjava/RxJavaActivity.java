package com.android.ui.kent.demo.network.rxjava;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.ui.kent.BuildConfig;
import com.android.ui.kent.R;
import com.android.ui.kent.demo.BaseActivity;
import com.android.ui.kent.demo.network.retrofit.vo.User;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kent on 2016/11/19.
 */

public class RxJavaActivity extends BaseActivity {

    private static String TAG = RxJavaActivity.class.getSimpleName();
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.button5)
    Button button5;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);

        ButterKnife.bind(this);
        initToolbar();
        mContext = RxJavaActivity.this;
    }

    private void initToolbar() {
        this.setupToolbar();
        this.setToolbarTitle("RXJava");
        this.enableBackButton();
    }

    private static Retrofit createRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return new Retrofit.Builder().baseUrl("https://api.github.com/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }


    void singleRequest() {
        Retrofit retrofit = createRetrofit();
        GitHubUser api = retrofit.create(GitHubUser.class);
        api.getUser("john")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "call onSubscribe");
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "call onNext, user = " + new Gson().toJson(user));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "call onError");
                        Toast.makeText(mContext, "查詢失敗", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "call onComplete");
                        Toast.makeText(mContext, "查詢成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void singleRequestQueue() {
        Retrofit retrofit = createRetrofit();
        GitHubUser api = retrofit.create(GitHubUser.class);
        api.getUser("john")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        //先根據结果去做一些操作
                        Toast.makeText(mContext, "get user.name:"+user.name, Toast.LENGTH_SHORT).show();
                    }
                })
                .observeOn(Schedulers.io())
                .concatMap(new Function<User, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(User user) throws Exception {
                        return api.getUser("jack");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        Toast.makeText(mContext, "get user.name:"+user.name, Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "查詢失敗", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    void multipleRequestByZip() {
        Retrofit retrofit = createRetrofit();
        GitHubUser gitHubUser = retrofit.create(GitHubUser.class);
        Observable<User> observable1 = gitHubUser.getUser("john")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observable<User> observable2 = gitHubUser.getUser("jack")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable.zip(observable1, observable2, new BiFunction<User, User, String>() {
            @Override
            public String apply(User user, User user2) throws Exception {
                String result = " query result user1.name =  " + user.name + ", user2.name = " + user2.name;
                return result;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext");
                Log.d(TAG, "result = " + value);
                Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });

    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                singleRequest();
                break;
            case R.id.button2:
                singleRequestQueue();
                break;
            case R.id.button3:
                multipleRequestByZip();
                break;
            case R.id.button4:
                break;
            case R.id.button5:
                break;
        }
    }


    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, RxJavaActivity.class);
        activity.startActivity(intent);
    }


}
