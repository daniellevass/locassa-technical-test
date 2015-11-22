package locassatechnicaltest.danielle.dev.com.technicaltest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by daniellevass on 21/11/2015.
 *
 * small Class for the data that gets held on a map marker
 */
public class MapMarkerItem implements Parcelable{


    private LatLng coordinates; //coordinates for the center of the map pin
    private String temp; //text to display the current temperature
    private int color; //R.color asset for the background of the map pin
    private String shortDescription; //short description that gets displayed to the user when they tap on a map pin


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


    private MapMarkerItem(Parcel in) {

        double latitude  = in.readDouble();
        double longitude = in.readDouble();

        coordinates = new LatLng(latitude, longitude);


        temp = in.readString();
        color = in.readInt();
        shortDescription = in.readString();
    }

    @Override
    public int describeContents() {
        return 0; //default
    }

    public static final Parcelable.Creator<MapMarkerItem> CREATOR = new Parcelable.Creator<MapMarkerItem>() {
        public MapMarkerItem createFromParcel(Parcel in) {
            return new MapMarkerItem(in);
        }

        public MapMarkerItem[] newArray(int size) {
            return new MapMarkerItem[size];
        }
    };

}
