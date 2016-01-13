package co.ubi.checkponit.DB.DAO;

import android.util.Log;

import com.mobandme.ada.ObjectContext;
import com.mobandme.ada.ObjectSet;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import java.util.ArrayList;
import java.util.List;

import co.ubi.checkponit.DB.Models.CheckList;

/**
 * Created by ${Mike} on 12/28/14.
 */
public class CheckListObjectSet extends ObjectSet {
    public static final String TAG = "CheckListObjectSet";

//    public CheckListObjectSet(ObjectContext pContext) throws AdaFrameworkException {
//        super(CheckList.class, pContext);
//
//    }
    @SuppressWarnings("unchecked")
    public CheckListObjectSet(Class<?> pManagedType, ObjectContext pContext)
            throws AdaFrameworkException {
        super(pManagedType, pContext);

    }

    public boolean exist(String id) {
        try {
            if (id != null && !id.trim().equals("")) {
                String wherePattern = String.format("%s = ?", this.getDataTableFieldName("_id"));

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


    public ArrayList<CheckList> getAllList() {
        ArrayList<CheckList> checkLists = null;
        CheckList checkList;

        try {

            checkLists = new ArrayList<>();
            String wherePattern = String.format("%s != ?", this.getDataTableFieldName("_id"));

            String[] whereValores = new String[]{"null"};


            List resultado = search(true, wherePattern, whereValores, null, null, null, 0, 10);
            if (resultado != null && resultado.size() > 0) {
                for (int i = 0; i < resultado.size(); i++) {
                    checkList = new CheckList();
                    checkList = (CheckList) resultado.get(i);
                    Log.e(TAG, "chechList ID" + checkList.get_id());
                    checkLists.add(checkList);

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return checkLists;
    }

}
