package co.mike.apptemplate.DB.DAO;

import android.content.Context;

import com.mobandme.ada.ObjectContext;
import com.mobandme.ada.exceptions.AdaFrameworkException;

/**
 * Created by ${Mike} on 12/28/14.
 */
public class DataContext extends ObjectContext {
    public static final int DATABASE_VERSION = 1;

    public static String DATABASE_NAME = ".bd";

    public UserObjectSet userDao;

    public DataContext(Context pContext, String pDatabaseName, int pDatabaseVersion) throws AdaFrameworkException {
        super(pContext, pDatabaseName, pDatabaseVersion);

        userDao = new UserObjectSet(this);
    }

    public UserObjectSet getUsuariosSet() {
        return userDao;
    }
}
