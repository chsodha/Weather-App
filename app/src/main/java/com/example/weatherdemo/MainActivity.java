package com.example.weatherdemo;

import android.graphics.Color;
import android.graphics.text.TextRunShaper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String apiKey = "b7b1ed873192409f21a98ab6e0125c53";

    SearchView searchView;
    TextView temperature, today, day, date, txtSunny, humidity, windspeed, sunnyCloud, sea;
    LottieAnimationView lottieAV;
    ConstraintLayout cl;
    ImageView imageView2;
    TextView cityName, SunRiseTextView, SunSetTextView , designBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initialize();
        fetchWeatherData("Jamnagar");
        searchCity(searchView);
    }


    public void initialize() {

        searchView = findViewById(R.id.searchView);
        temperature = findViewById(R.id.temperature);
        today = findViewById(R.id.today);
        day = findViewById(R.id.day);
        date = findViewById(R.id.date);
//        txtMin = findViewById(R.id.txtMin);
//        txtMax = findViewById(R.id.txtMax);
        txtSunny = findViewById(R.id.txtSunny);
        humidity = findViewById(R.id.humidity);
        windspeed = findViewById(R.id.windspeed);
        sunnyCloud = findViewById(R.id.sunnyCloud);
        lottieAV = findViewById(R.id.lottieAV);
        cl = findViewById(R.id.main);
        imageView2 = findViewById(R.id.imageView2);
        cityName = findViewById(R.id.cityName);
        sea = findViewById(R.id.sea);
        SunRiseTextView = findViewById(R.id.sunrise_text_view);
        SunSetTextView = findViewById(R.id.sunset_text_view);
        designBy = findViewById(R.id.designBy);

    }

    public void searchCity(SearchView sv) {
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchWeatherData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void fetchWeatherData(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiinterface = retrofit.create(ApiInterface.class);
        Call<model> weatherAppCall = apiinterface.getWeather(city, apiKey, "metric");

        weatherAppCall.enqueue(new Callback<model>() {
            @Override
            public void onResponse(Call<model> call, Response<model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    model mydata = response.body();

                    updateUi(mydata, city);
                } else {
                    showError();
                    customToast("Enter a Valid City" , R.drawable.invalid);
                }
            }

            @Override
            public void onFailure(Call<model> call, Throwable t) {
                showError();

            }
        });

    }

    public void updateUi(model mydata, String city) {

        Log.d("WeatherApp", "API Response: " + mydata);

        String temperature1 = String.valueOf(mydata.getMain().getTemp());
        System.out.println(temperature1);
        temperature.setText(temperature1 + "°C");

//        String maxT = String.valueOf(mydata.getMain().getTempMax());
//        System.out.println(maxT);
//        txtMax.setText("Max Temp: " + maxT + "°C");
//
//        String minT = String.valueOf(mydata.getMain().getTempMin());
//        System.out.println(minT);
//        txtMin.setText("Min Temp: " + minT + "°C");

        String humidity2 = String.valueOf(mydata.getMain().getHumidity());
        humidity.setText(humidity2 + "%");

        String windspeed2 = String.valueOf(mydata.getWind().getSpeed());
        windspeed.setText(windspeed2 + " m/s");

        String condition = String.valueOf(mydata.getweatherCondition());
        txtSunny.setText(condition);

        String sunriseString = convertUnixToTime(mydata.getSys().getSunrise());
        System.out.println(sunriseString);
        SunRiseTextView.setText(sunriseString);

        String sunSetString = convertUnixToTime(mydata.getSys().getSunset());
        System.out.println(sunSetString);
        SunSetTextView.setText(sunSetString);

        day.setText(dayName());
        date.setText(currentDate());

        sunnyCloud.setText(condition);

        cityName.setText(city);

        String sea1 = String.valueOf(mydata.getMain().getPressure());
        sea.setText(sea1 + " hPa");

        changeImageAccordingToCondition(condition);

    }

    public void showError() {
        temperature.setText("Failed");
//        txtMin.setText("Failed");
//        txtMax.setText("Failed");
        humidity.setText("Failed");
        SunSetTextView.setText("Failed");
        SunRiseTextView.setText("Failed");
        windspeed.setText("Failed");
        sea.setText("Failed");
        txtSunny.setText("Failed");
        sunnyCloud.setText("Failed");
    }

    public static String dayName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String day = LocalDate.now().getDayOfWeek().toString();
            return day;
        }
        return "Not Support";
    }

    public String currentDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d MMMM yyyy");
            String date = LocalDate.now().format(dtf);
            return date;
        }
        return "Not Support";
    }

    public void changeImageAccordingToCondition(String condition) {

        if (condition.equals("Rain") || condition.equals("Heavy Rain") || condition.equals("Light Rain") || condition.equals("Drizzle")) {
            cl.setBackgroundResource(R.drawable.rain_b1);
            lottieAV.setAnimation(R.raw.rain_lottie);
            lottieAV.playAnimation();
            designBy.setTextColor(Color.WHITE);
        } else if (condition.equals("Clouds") || condition.equals("Mist") || condition.equals("Foggy")) {
            cl.setBackgroundResource(R.drawable.cloud_b1);
            lottieAV.setAnimation(R.raw.cloud_lottie);
            lottieAV.playAnimation();
            designBy.setTextColor(Color.WHITE);
        } else if (condition.equals("Snow") || condition.equals("Light Snow") || condition.equals("Heavy Snow")) {
            cl.setBackgroundResource(R.drawable.snow_b1);
            lottieAV.setAnimation(R.raw.snow_lootie2);
            lottieAV.playAnimation();
            designBy.setTextColor(Color.BLACK);
        } else {
            cl.setBackgroundResource(R.drawable.sun3);
            lottieAV.setAnimation(R.raw.animation_sun2);
            lottieAV.playAnimation();
            designBy.setTextColor(Color.WHITE);
        }


    }

    public String convertUnixToTime(long timeStamp) {
        long timestamp = timeStamp * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(timestamp));
    }

    public void customToast(String text , int iconId){

        LayoutInflater inflater = getLayoutInflater();
        View layout =inflater.inflate(R.layout.custom_toast , null);

        TextView custom_toast_textview =layout.findViewById(R.id.custom_toast_textview);
        custom_toast_textview.setText(text);

        ImageView custom_toast_icon = layout.findViewById(R.id.custom_toast_icon);
        custom_toast_icon.setImageResource(iconId);

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

}