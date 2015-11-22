package locassatechnicaltest.danielle.dev.com.technicaltest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by daniellevass on 21/11/2015.
 */
public class MapMarkerItem implements Parcelable{


    private LatLng coordinates;
    private String temp;
    private int color;
    private String shortDescription;

    public MapMarkerItem(LatLng coordinates, String temp, int color, String shortDescription) {
        this.coordinates = coordinates;
        this.temp = temp;
        this.color = color;
        this.shortDescription = shortDescription;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public String getTemp() {
        return temp;
    }

    public int getColor() {
        return color;
    }

    public String getShortDescription() {
        return shortDescription;
    }


    //parcelable things


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(coordinates.latitude);
        dest.writeDouble(coordinates.longitude);

        dest.writeString(temp);
        dest.writeInt(color);
        dest.writeString(shortDescription);

    }

    @Override
    public int describeContents() {
        return 0; //default
    }

    private MapMarkerItem(Parcel in) {

        double latitude  = in.readDouble();
        double longitude = in.readDouble();

        coordinates = new LatLng(latitude, longitude);


        temp = in.readString();
        color = in.readInt();
        shortDescription = in.readString();
    }

}
