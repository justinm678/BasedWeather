package com.example.basedweather;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basedweather.models.Todo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity_backup extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Todo> call = apiService.getTodos();
        call.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                Log.d("yay",String.format("%s was received!",response.body()));
                List<String> hourly_time_list = response.body().hourly.time;
                List<Float> hourly_temperature_list = response.body().hourly.temperature_2m;
                List<Float> hourly_precipitaiton_probability_list = response.body().hourly.precipitation;
                List<String> full_data_list = new ArrayList<>(hourly_time_list.size());
                for (int i = 0; i < hourly_time_list.size(); i ++) {
                    // i is the index
                    // yourArrayList.get(i) is the element

                    full_data_list.add(hourly_time_list.get(i)+"---------"+Float.toString(hourly_temperature_list.get(i))+response.body().hourly_units.temperature_2m+"---------"+Float.toString(hourly_precipitaiton_probability_list.get(i))+response.body().hourly_units.precipitation);
                }
                arrayAdapter.addAll(full_data_list);
                listView.setAdapter(arrayAdapter);
                Log.d("afdsafad",String.format("%s ",listView.getChildAt(10)));
//                listView.getChildAt(1).setBackgroundColor(Color.RED);
                    Toast.makeText(MainActivity_backup.this, "Weather data fetched successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Log.d("oof","Nothin");
                Toast.makeText(MainActivity_backup.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}