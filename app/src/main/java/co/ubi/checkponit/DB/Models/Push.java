package co.ubi.checkponit.DB.Models;

/**
 * Created by ${Mike} on 1/19/15.
 */
public class Push {
    public static final String KEY_title = "title";
    public static final String KEY_name = "name";
    public static final String KEY_photo = "photo";
    public static final String KEY_photo_check = "photo_check";
    public static final String KEY_lat_lng = "lat_lng";
    public static final String KEY_address = "address";
    public static final String KEY_description = "description";

    String title, name, photo, photo_check, lat_lng, address, description;

    public Push(String title, String name, String photo,
                String photo_check, String lat_lng, String address,
                String description) {
        this.title = title;
        this.name = name;
        this.photo = photo;
        this.photo_check = photo_check;
        this.lat_lng = lat_lng;
        this.address = address;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhoto_check() {
        return photo_check;
    }

    public String getLat_lng() {
        return lat_lng;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }
}
