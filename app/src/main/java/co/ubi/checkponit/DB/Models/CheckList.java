package co.ubi.checkponit.DB.Models;

import com.google.android.gms.maps.model.LatLng;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by ${Mike} on 12/28/14.
 */
@Table(name = "CheckList")
public class CheckList extends Entity {
    //institutions create your table to the database
    public static String KEY_id = "id";
    public static String KEY_name = "name";
    public static String KEY_photo = "photo";
    public static String KEY_lat_lng = "lat_lng";
    public static String KEY_description = "description";
    public static String KEY_photo_check = "photo_check";
    public static String KEY_address = "address";
    public static String KEY_checkid = "check_id";


    @TableField(name = "_id", datatype = Entity.DATATYPE_STRING)
    private String _id;

    @TableField(name = "name", datatype = Entity.DATATYPE_STRING)
    String name;

    @TableField(name = "photo", datatype = Entity.DATATYPE_STRING)
    String photo;

    @TableField(name = "lat_lat", datatype = Entity.DATATYPE_STRING)
    String lat_lat;

    @TableField(name = "address", datatype = Entity.DATATYPE_STRING)
    String address;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @TableField(name = "description", datatype = Entity.DATATYPE_STRING, required = false)
    String description;

    @TableField(name = "photo_check", datatype = Entity.DATATYPE_STRING, required = false)
    String photo_check;


    public String getName() {
        return name;
    }


    public String get_id() {
        return _id;
    }

    public String getPhoto() {
        return photo;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void set_id(String id) {
        this._id = id;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLat_lat() {
        return lat_lat;
    }

    public void setLat_lat(String lat_lat) {
        this.lat_lat = lat_lat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto_check() {
        return photo_check;
    }

    public void setPhoto_check(String photo_check) {
        this.photo_check = photo_check;
    }

    public LatLng GetLatLng(String latlng) {

        String[] split = latlng.split(",");
        LatLng latlngDouble = new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
        return latlngDouble;

    }
}
