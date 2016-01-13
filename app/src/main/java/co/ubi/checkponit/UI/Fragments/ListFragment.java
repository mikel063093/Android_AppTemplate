package co.ubi.checkponit.UI.Fragments;


import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import co.ubi.checkponit.DB.Models.CheckList;
import co.ubi.checkponit.R;
import co.ubi.checkponit.UI.CustomEvent.LocationEvent;
import co.ubi.checkponit.UI.Fragments.CheckList.CheckListAdapter;
import co.ubi.checkponit.Utils.ServerUtils.RESTCient;
import co.ubi.checkponit.Utils.ServerUtils.URL;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_list)
public class ListFragment extends BaseFragment implements RESTCient.onResponseServer {

    @ViewById
    ListView list;

    Location myLocation;
    CheckListAdapter checkListAdapter;
    public static final String TAG = ListFragment.class.getSimpleName();

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onEvent(Location location) {
        super.onEvent(location);
        myLocation = location;

    }

    @Override
    public void onEvent(ArrayList<CheckList> checkLists) {
        super.onEvent(checkLists);
        setListAdapter(checkLists);
    }

    public void setListAdapter(ArrayList<CheckList> checkLists) {
        checkListAdapter = new CheckListAdapter(getActivity(), checkLists);
        list.setAdapter(checkListAdapter);
    }

    public void getChekListFromServer() {

        log(TAG, "getChekListFromServer");

        RequestParams params = new RequestParams();
        if (myLocation != null)
            params.put("lat_lng", myLocation.getLatitude() + "," + myLocation.getLongitude());

        showDialog(getString(R.string.loading));
        RESTCient.setPostAsync(URL.LIST_CHECKPOINT, RESTCient.TYPE_LIST_CHECKPOINT, params, this);
    }

    public ArrayList<CheckList> getCheckList(String response) {
        ArrayList<CheckList> checkLists = null;

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(response);
            checkLists = new ArrayList<>();
            //arrayList= new ArrayList<>();
            CheckList checkList;
            for (int i = 0; i < jsonArray.length(); i++) {

                checkList = RESTCient.getCheckListFromJson(jsonArray.getJSONObject(i));
                if (checkList != null) {
                    checkLists.add(checkList);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return checkLists;
    }


    @Override
    public void OnResponse(String response, int satusCode, int type) {

    }

    @Override
    public void onEvent(LocationEvent locationEvent) {
        super.onEvent(locationEvent);

    }
}
