package com.example.basedweather;

import com.example.basedweather.models.Todo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    String grandview_lat_long="latitude=39.99040&longitude=-83.04915";
    String lacanada_lat_long="latitude=34.2154&longitude=-118.2188";
    String gv_get = "forecast?"+grandview_lat_long+"&hourly=temperature_2m,dew_point_2m,precipitation_probability,cloud_cover,uv_index&temperature_unit=fahrenheit&timezone=America%2FNew_York&forecast_days=3";

    String lc_get = "forecast?"+lacanada_lat_long+"&hourly=temperature_2m,dew_point_2m,precipitation_probability,cloud_cover,uv_index&temperature_unit=fahrenheit&timezone=America%2FLos_Angeles&forecast_days=3";

    @GET(gv_get)
    Call<Todo> getTodos();
}