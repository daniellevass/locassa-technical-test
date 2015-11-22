package locassatechnicaltest.danielle.dev.com.technicaltest;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by daniellevass on 21/11/2015.
 */
public interface WeatherService {


    //e.g. for san francisco coordinates
    // https://query.yahooapis.com/v1/public/yql?q=
    // select * from weather.forecast where woeid in
    // (select woeid from geo.placefinder where
    // text="37.7749290,-122.4194160" and gflag="R")
    // and u="c"&format=json
    @GET("/yql?format=json")
    void getWeather(@Query(value = "q", encodeName = true) String query, Callback<WeatherResponse> cb);


}
