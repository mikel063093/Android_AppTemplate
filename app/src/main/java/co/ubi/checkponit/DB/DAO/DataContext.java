package co.ubi.checkponit.DB.DAO;

import android.content.Context;
import android.util.Log;

import com.mobandme.ada.ObjectContext;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import co.ubi.checkponit.DB.Models.CheckList;
import co.ubi.checkponit.DB.Models.User;

/**
 * Created by ${Mike} on 12/28/14.
 */
public class DataContext extends ObjectContext {
    public static final String TAG = "DataContextDB";
    public static final int DATABASE_VERSION = 1;

    public static String DATABASE_NAME = "checkpoint";

    public UserObjectSet userDao;
    public CheckListObjectSet ChekListDao;

    public DataContext(Context pContext, String pDatabaseName, int pDatabaseVersion) throws AdaFrameworkException {
        super(pContext, pDatabaseName, pDatabaseVersion);

        userDao = new UserObjectSet(User.class, this);
        ChekListDao = new CheckListObjectSet(CheckList.class, this);
        Log.e(TAG, "Construc");
    }

    public CheckListObjectSet getChekListDao() {
        return ChekListDao;
    }

    public UserObjectSet getUsuariosSet() {
        return userDao;
    }
}
