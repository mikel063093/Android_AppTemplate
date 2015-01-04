package co.mike.apptemplate.DB;

import android.content.Context;
import android.util.Log;

import com.mobandme.ada.Entity;

import co.mike.apptemplate.DB.DAO.DataContext;
import co.mike.apptemplate.DB.Models.User;

/**
 * Created by ${Mike} on 12/28/14.
 */
public class DBUtils {
    public static DataContext appDataContext;

    public static final String TAG = "DBUtils";

    public static void initBBDD(Context ctx) {
        getApplicationDataContext(ctx);
    }

    // Inicializar los DAOs, para poder acceder a los datos
    public static DataContext getApplicationDataContext(Context ctx) {
        if (appDataContext == null) {
            try {
                appDataContext = new DataContext(ctx, DataContext.DATABASE_NAME, DataContext.DATABASE_VERSION);
            } catch (Exception e) {
                Log.e(TAG, "Error inicializando QdemosDataContext: " + e.getMessage());
            }
        }
        return appDataContext;
    }

    public static User crearUsuarioIfNotExistOnlyLocal(Context ctx, String idFB, String nombre) {
        try {
            if (getApplicationDataContext(ctx).getUsuariosSet().exist(idFB)) {
                User u = getApplicationDataContext(ctx).getUsuariosSet().getPorIdFacebook(idFB);
                u.setStatus(Entity.STATUS_UPDATED);
                //u.setNombre(nombre);
                getApplicationDataContext(ctx).getUsuariosSet().save(u);
                return u;
            }
            //User user = new User(nombre, idFB, null);
            User user = new User();
            try {
                user.setStatus(Entity.STATUS_NEW);
                getApplicationDataContext(ctx).getUsuariosSet().add(user);
                getApplicationDataContext(ctx).getUsuariosSet().save();
                Log.e(TAG, "Usuario Creado Correctamente");
                return user;
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        return null;
    }
}
