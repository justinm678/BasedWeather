package com.example.basedweather.models;

import androidx.annotation.NonNull;

import java.util.List;

public class Todo {
    public float latitude;
    public float longitude;
    public float generationtime_ms;
    public float utc_offset_seconds;
    public String timezone;
    public String timezone_abbreviation;
    public float elevation;
    public DailyClass daily;
    public DailyUnitsClass daily_units;
    public HourlyClass hourly;
    public HourlyUnitsClass hourly_units;

    public HourlyClass getHourly() {
        return hourly;
    }

    public void setHourly(HourlyClass hourly) {
        this.hourly = hourly;
    }

    public HourlyUnitsClass getHourly_units() {
        return hourly_units;
    }

    public void setHourly_units(HourlyUnitsClass hourly_units) {
        this.hourly_units = hourly_units;
    }

    public DailyClass getDaily() {
        return daily;
    }

    public void setDaily(DailyClass daily) {
        this.daily = daily;
    }

    public DailyUnitsClass getDaily_units() {
        return daily_units;
    }

    public void setDaily_units(DailyUnitsClass daily_units) {
        this.daily_units = daily_units;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getGenerationtime_ms() {
        return generationtime_ms;
    }

    public void setGenerationtime_ms(float generationtime_ms) {
        this.generationtime_ms = generationtime_ms;
    }

    public float getUtc_offset_seconds() {
        return utc_offset_seconds;
    }

    public void setUtc_offset_seconds(float utc_offset_seconds) {
        this.utc_offset_seconds = utc_offset_seconds;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone_abbreviation() {
        return timezone_abbreviation;
    }

    public void setTimezone_abbreviation(String timezone_abbreviation) {
        this.timezone_abbreviation = timezone_abbreviation;
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public class DailyUnitsClass{
        public String time,temperature_2m_max;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTemperature_2m_max() {
            return temperature_2m_max;
        }

        public void setTemperature_2m_max(String temperature_2m_max) {
            this.temperature_2m_max = temperature_2m_max;
        }
    }
    public class DailyClass{
        public List<String> time;
        public List<Float> temperature_2m_max;

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public List<Float> getTemperature_2m_max() {
            return temperature_2m_max;
        }

        public void setTemperature_2m_max(List<Float> temperature_2m_max) {
            this.temperature_2m_max = temperature_2m_max;
        }
    }

    public class HourlyUnitsClass{
        public String time, temperature_2m, cloud_cover, uv_index, precipitation_probability, dew_point_2m;

        public String getDew_point_2m() {
            return dew_point_2m;
        }

        public void setDew_point_2m(String dew_point_2m) {
            this.dew_point_2m = dew_point_2m;
        }

        public String getprecipitation_probability() {
            return precipitation_probability;
        }

        public void setprecipitation_probability(String precipitation_probability) {
            this.precipitation_probability = precipitation_probability;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTemperature_2m() {
            return temperature_2m;
        }

        public void setTemperature_2m(String temperature_2m) {
            this.temperature_2m = temperature_2m;
        }

        public String getCloud_cover() {
            return cloud_cover;
        }

        public void setCloud_cover(String cloud_cover) {
            this.cloud_cover = cloud_cover;
        }

        public String getUv_index() {
            return uv_index;
        }

        public void setUv_index(String uv_index) {
            this.uv_index = uv_index;
        }
    }
    public class HourlyClass{
        public List<Float> temperature_2m, cloud_cover, uv_index, precipitation_probability, dew_point_2m;
        public List<String> time;

        public List<Float> getDew_point_2m() {
            return dew_point_2m;
        }

        public void setDew_point_2m(List<Float> dew_point_2m) {
            this.dew_point_2m = dew_point_2m;
        }

        public List<Float> getprecipitation_probability() {
            return precipitation_probability;
        }

        public void setprecipitation_probability(List<Float> precipitation_probability) {
            this.precipitation_probability = precipitation_probability;
        }

        public List<Float> getTemperature_2m() {
            return temperature_2m;
        }

        public void setTemperature_2m(List<Float> temperature_2m) {
            this.temperature_2m = temperature_2m;
        }

        public List<Float> getCloud_cover() {
            return cloud_cover;
        }

        public void setCloud_cover(List<Float> cloud_cover) {
            this.cloud_cover = cloud_cover;
        }

        public List<Float> getUv_index() {
            return uv_index;
        }

        public void setUv_index(List<Float> uv_index) {
            this.uv_index = uv_index;
        }

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }
    }
//    //The string representation is the body
//    public String toString() {
//        String listString = String.join(", ", Daily.temperature_2m_max);
//        return timezone_abbreviation;
//    }
}