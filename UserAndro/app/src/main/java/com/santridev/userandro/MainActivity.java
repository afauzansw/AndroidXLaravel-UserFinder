package com.santridev.userandro;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santridev.userandro.apiservice.BaseApiService;
import com.santridev.userandro.apiservice.UtilsApi;
import com.santridev.userandro.model.Repo;
import com.santridev.userandro.model.ResponseRepos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
//        komponen activity_main
        @BindView(R.id.pbLoading)
        ProgressBar pbLoading;
        @BindView(R.id.rvRepos)
        RecyclerView rvRepos;
        @BindView(R.id.etUsername)
        EditText etUsername;

//
        BaseApiService mApiService;
        ReposAdapter mRepoAdapter;

        List<Repo> repoList = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);

            mApiService = UtilsApi.getAPIService();

//        menginisialisasi adapter dan recyclerview
            mRepoAdapter = new ReposAdapter(this, repoList, null);
            rvRepos.setLayoutManager(new LinearLayoutManager(this));
            rvRepos.setItemAnimator(new DefaultItemAnimator());
            rvRepos.setHasFixedSize(true);
            rvRepos.setAdapter(mRepoAdapter);

            etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*
                EditorInfo.IME_ACTION_SEARCH ini berfungsi untuk men-set keyboard kamu
                agar enter di keyboard menjadi search.
                 */
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        String username = etUsername.getText().toString();
                        requestRepos(username);
                        return true;
                    }
                    return false;
                }
            });
        }

        /*
        Fungsi untuk berkomunikasi dengan API Server menggunakan library Retrofit dan RxJava.
         */
        private void requestRepos(String username) {
            pbLoading.setVisibility(View.VISIBLE);

            mApiService.requestRepos(username)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ResponseRepos>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<ResponseRepos> responseRepos) {

                            for (int i = 0; i < responseRepos.size(); i++) {
                                String name = responseRepos.get(i).getName();
                                String address = responseRepos.get(i).getAddress();

                                repoList.add(new Repo(name, address));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            pbLoading.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Berhasil mengambil data", Toast.LENGTH_SHORT).show();

                            mRepoAdapter = new ReposAdapter(MainActivity.this, repoList, null);
                            rvRepos.setAdapter(mRepoAdapter);
                            mRepoAdapter.notifyDataSetChanged();
                        }
                    });
        }
}
