package co.ubi.checkponit.DB.Models;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by ${Mike} on 12/28/14.
 */
@Table(name = "User")
public class User extends Entity {
    //institutions create your table to the database
    public static String KEY_name = "name";
    public static String KEY_email = "email";
    public static String KEY_id = "id";
    public static String KEY_photo = "photo";
    public static String KEY_creation_date = "creation_date";
    public static String KEY_notification_type = "notification_type";
    public static String KEY_facebook_id = "facebook_id";
    public static String KEY_rate = "rate";
    public static final String TYPE_KEY_notification_type = "GCM";


    @TableField(name = "name", datatype = Entity.DATATYPE_STRING)
    String name;

    @TableField(name = "email", datatype = Entity.DATATYPE_STRING)
    String email;

    @TableField(name = "idServer", datatype = Entity.DATATYPE_STRING)
    private String idServer;

    @TableField(name = "photo", datatype = Entity.DATATYPE_STRING)
    String photo;

    String phone;

    String creation_date;

    String notification_type;

    String notification_api;

    String facebook_id;

    String rate;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getIdServer() {
        return idServer;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public String getNotification_api() {
        return notification_api;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public String getRate() {
        return rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdServer(String id) {
        this.idServer = id;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public void setNotification_api(String notification_api) {
        this.notification_api = notification_api;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
