package co.ubi.checkponit.DB.DAO;

import com.mobandme.ada.ObjectContext;
import com.mobandme.ada.ObjectSet;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import java.util.List;

import co.ubi.checkponit.DB.Models.User;

/**
 * Created by ${Mike} on 12/28/14.
 */
public class UserObjectSet extends ObjectSet {


    @SuppressWarnings("unchecked")
    public UserObjectSet(Class<?> pManagedType, ObjectContext pContext)
            throws AdaFrameworkException {
        super(pManagedType, pContext);

    }

    public boolean exist(String id) {
        try {
            if (id != null && !id.trim().equals("")) {
                String wherePattern = String.format("%s = ?", this.getDataTableFieldName("email"));

                String[] whereValores = new String[]{id};

                List resultado = search(true, wherePattern, whereValores, null, null, null, 0, 1);
                if (resultado != null && resultado.size() > 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public User getPorIdFacebook(String id){
        return getPorIdFacebook(id, null, null, false);
    }

    public User getPorIdFacebook(String id, String nombre, String idgcm, boolean anadirSiNoExiste){

        User user = null;

        try{
            if (id != null && !id.trim().equals("")){
                String wherePattern = String.format("%s = ?", this.getDataTableFieldName("idfacebook"));

                String[] whereValores = new String[] { id };

                List resultado = search(true, wherePattern, whereValores, null, null, null, 0, 1);
                if (resultado != null && resultado.size() > 0) {
                    user = (User) resultado.get(0);
                } else if (anadirSiNoExiste) {
//                    user = new User(nombre, id, idgcm);
//
//                    save(user);
//
//                    add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public User getMyUser() {

        User user = null;

        try {
            String wherePattern = String.format("%s != ?", this.getDataTableFieldName("email"));

            String[] whereValores = new String[]{"null"};

            List resultado = search(true, wherePattern, whereValores, null, null, null, 0, 1);
            if (resultado != null && resultado.size() > 0) {

                user = (User) resultado.get(0);

                //  Log.e("getMyUser", "BD " + user.getName() + " id : " + user.getID());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

}
