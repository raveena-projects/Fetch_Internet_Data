package com.example.fetch_internet_data;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvResponse;
    private Button btnGetResponse;
    private ProgressBar progressBar;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResponse = findViewById(R.id.tv_response);
        btnGetResponse = findViewById(R.id.btn_get_response);
        progressBar = findViewById(R.id.idLoadingPB);

        btnGetResponse.setOnClickListener(v -> fetchPosts());
    }

    private void fetchPosts() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Post>> call = apiService.getPosts();

        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    posts = response.body();
                    if (posts != null && !posts.isEmpty()) {
                        displayRandomPost();
                    } else {
                        tvResponse.setText("No posts found.");
                    }
                } else {
                    tvResponse.setText("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvResponse.setText("Failed to connect: " + t.getMessage());
            }
        });
    }

    private void displayRandomPost() {
        if (posts != null && !posts.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(posts.size());
            Post randomPost = posts.get(randomIndex);
            tvResponse.setText(randomPost.getBody());
        }
    }
}
