package co.ubi.checkponit.UI.Activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.RequestParams;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.ubi.checkponit.DB.DBUtils;
import co.ubi.checkponit.DB.Models.CheckList;
import co.ubi.checkponit.DB.Models.Push;
import co.ubi.checkponit.DB.Models.User;
import co.ubi.checkponit.R;
import co.ubi.checkponit.Services.Location.LocationService;
import co.ubi.checkponit.UI.CustomEvent.LocationEvent;
import co.ubi.checkponit.UI.Fragments.ListFragment_;
import co.ubi.checkponit.UI.Fragments.MapsFragment_;
import co.ubi.checkponit.Utils.ServerUtils.RESTCient;
import co.ubi.checkponit.Utils.ServerUtils.URL;
import co.ubi.checkponit.Utils.imgUtils;
import co.ubi.checkponit.alexbbb.uploadservice.AbstractUploadServiceReceiver;
import co.ubi.checkponit.alexbbb.uploadservice.UploadRequest;
import co.ubi.checkponit.alexbbb.uploadservice.UploadService;
import de.greenrobot.event.EventBus;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

@EActivity(R.layout.activity_app_main)
public class app_main extends AppCompatActivity implements MaterialTabListener, RESTCient.onResponseServer {
    public static final String TAG = app_main.class.getSimpleName();
    Location myLocation, cameraLocation;
    @ViewById
    Toolbar toolbar_app;

    @ViewById
    MaterialTabHost tabHost;

    @ViewById
    ViewPager pager;

    ViewPagerAdapter adapter;
    private List<Fragment> mFragments;
    User user;

    CircularImageView imgUserToolbar;
    TextView toolbar_title, toolbar_sbu_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_app_main);

    }

    @AfterViews
    public void _iniApp() {
//        try {
//            Intent intent = getIntent();
//            if (intent != null) {
//                String action = intent.getAction();
//                if (action != null) {
//                    Log.e(TAG, "ACtion ::: " + action);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        imgUserToolbar = (CircularImageView) toolbar_app.findViewById(R.id.imgUserToolbar);
        toolbar_title = (TextView) toolbar_app.findViewById(R.id.toolbar_title);
        toolbar_sbu_title = (TextView) toolbar_app.findViewById(R.id.toolbar_sbu_title);

        myLocation = LocationService.getServiceLocation();

        user = DBUtils.getApplicationDataContext(this).getUsuariosSet().getMyUser();
        if (user != null) {
            Picasso.with(this).load(user.getPhoto()).into(userImgTarget);
            getChekListFromServer(true);
            toolbar_title.setText(user.getName());
            toolbar_sbu_title.setText(user.getEmail());
        }
        mFragments = new ArrayList<>();
        mFragments.add(new MapsFragment_());
        mFragments.add(new ListFragment_());

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setIcon(getIcon(i))
                                    //.setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
        toolbar_app.inflateMenu(R.menu.menu_login);
        toolbar_app.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.add:

                        openCheckponitDialog();
                        break;
                    case R.id.update:
                        getChekListFromServer(true);
                        break;
                }

                return true;
            }
        });
        //  setSupportActionBar(toolbar_app);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        uploadReceiver.unregister(this);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        pager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    public void getChekListFromServer(boolean show) {
        Log.e(TAG, "getChekListFromServer");
        if (myLocation == null) {
            myLocation = LocationService.getServiceLocation();
            if (myLocation == null) {
                myLocation = getLastLocationDefault();
            }
        }


        RequestParams params = new RequestParams();
        if (myLocation != null)
            params.put("lat_lng", myLocation.getLatitude() + "," + myLocation.getLongitude());

        if (show)
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
        dismissDialog();
        switch (satusCode) {
            case RESTCient.STATUS_CODE_OK:
                switch (type) {
                    case RESTCient.TYPE_CHECKPOINT:
                        try {
                            sendNotification(RESTCient.getCheckListFromJson(new JSONObject(response)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case RESTCient.TYPE_LIST_CHECKPOINT:
                        ArrayList<CheckList> checkLists = getCheckList(response);
                        if (checkLists != null) {
                            EventBus.getDefault().post(checkLists);
                            //  checkListAdapter = new CheckListAdapter(getActivity(), checkLists);
                            //list.setAdapter(checkListAdapter);
                        }

                        break;

                }
                break;
            case RESTCient.STATUS_CODE_ITERNAL_SERVER_ERROR:

                break;
            default:
                break;
        }
    }

    ProgressDialog progressDialog;

    public void showDialog(String text) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(text);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int num) {
            Bundle args = new Bundle();

            Fragment fragment = mFragments.get(num);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tittle = "";
            Log.e(TAG, "FRAGMENT POS " + position);
            switch (position) {
                case 0:
                    tittle = "Mapa";
                    break;
                case 1:
                    tittle = "Lista";
                    break;

            }

            return tittle;
        }

    }

    public void onEvent(ArrayList<CheckList> checkLists) {

    }


    public void onEvent(Location location) {

        // log(TAG,"Location ::"+location.getProvider());
        myLocation = location;
    }

    public void onEvent(LocationEvent locationEvent) {
        cameraLocation = locationEvent.getCameraLocation();
    }

    private EditText Input;
    TextView textLocationDescription;
    private View positiveAction;
    CircularImageView imgUser, imgCheckpointUser;
    LinearLayout container_add_photo;
    Bitmap Profilebitmap;
    private Target userImgTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            log(TAG, "onBitmapLoaded");
            Profilebitmap = bitmap;
            if (imgUserToolbar != null) {
                imgUserToolbar.setImageBitmap(bitmap);
            }
//            if (imgUser != null) {
//                imgUser.setImageBitmap(bitmap);
//            }

        }

        @Override
        public void onBitmapFailed(Drawable drawable) {
            log(TAG, "onBitmapFailed");

        }

        @Override
        public void onPrepareLoad(Drawable drawable) {
            log(TAG, "onPrepareLoad");
        }


    };

    public void openCheckponitDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.add_checkpoint)
                .customView(R.layout.custom_checkpoint,true)
                .positiveText(R.string.send)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                              @Override
                              public void onNegative(MaterialDialog materialDialog) {
                                  log(TAG, "checkPointDialog negative");
                              }

                              @Override
                              public void onPositive(MaterialDialog materialDialog) {
                                  log(TAG, "checkPointDialog positive");
                                  if (ImgUploadSelect) {
                                      if (filePath != null) {
                                          upLoadToserver(filePath);
                                      } else {
                                          upLoadToserver(outputFileUri.getPath());
                                      }

                                  } else {
                                      log(TAG, "checkPointDialog setSinPhoto");
                                      setCheckWhitOutPhoto();
                                  }
                              }
                          }
                ).build();
        container_add_photo = (LinearLayout) dialog.getCustomView().findViewById(R.id.container_add_photo);
        textLocationDescription = (TextView) dialog.getCustomView().findViewById(R.id.textLocationDescription);

        textLocationDescription.setText(user.getName());

        imgUser = (CircularImageView) dialog.getCustomView().findViewById(R.id.imgUser);
        if (Profilebitmap != null) {
            imgUser.setImageBitmap(Profilebitmap);
        }

        imgCheckpointUser = (CircularImageView) dialog.getCustomView().findViewById(R.id.imgCheckpointUser);
        container_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log(TAG, "onCLickAddPhto");
                openImageIntent();
            }
        });
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        Input = (EditText) dialog.getCustomView().findViewById(R.id.edtDescription);
        Input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // positiveAction.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dialog.show();
        // positiveAction.setEnabled(false);

    }

    private Uri outputFileUri;
    private String filePath;
    public static int YOUR_SELECT_PICTURE_REQUEST_CODE = 100;
    public static int YOUR_SELECT_PICTURE_REQUEST_CODE_KK = 101;
    private boolean ImgUploadSelect = false;

    private void openImageIntent() {

// Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "checkPoint" + File.separator);
        root.mkdirs();
        final String fname = "checkImg_"; //Utils.getUniqueImageFilename();
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = this.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }


        if (Build.VERSION.SDK_INT < 19) {
            // Filesystem.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

            // Chooser of filesystem options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.select));

            // Add the camera options.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

            startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);
        } else {
            // Filesystem.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

            // Chooser of filesystem options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.select));

            // Add the camera options.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

            startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE_KK);
        }


    }

    public void sendNotification(CheckList checkList) {
        log(TAG, "sendNotification");
        RequestParams params = new RequestParams();
        params.put(CheckList.KEY_checkid, checkList.get_id());

        //showDialog(getString(R.string.loading));
        RESTCient.setPostAsync(URL.SEND_NOTIFICATION, RESTCient.TYPE_SEND_NOTIFICATION, params, this);
    }

    public void setCheckWhitOutPhoto() {
        RequestParams params = new RequestParams();
        if (user != null)
            params.put("user_id", user.getIdServer());

        if (cameraLocation != null) {
            params.put("lat_lng", cameraLocation.getLatitude() + "," + cameraLocation.getLongitude());
        } else {
            if (myLocation != null) {
                params.put("lat_lng", myLocation.getLatitude() + "," + myLocation.getLongitude());
            }
        }
        if (Input != null && Input.getText().toString().length() > 0)
            params.put("description", Input.getText().toString().toLowerCase());
        else {
            params.put("description", "");
        }
        params.put("photo", "");
        showDialog(getString(R.string.loading));
        RESTCient.setPostAsync(URL.CHECKPOINT, RESTCient.TYPE_CHECKPOINT, params, this);

    }

    public void upLoadToserver(String filePath) {
        ImgUploadSelect = false;
        String boundary = "*****";
        final String serverUrlString = URL.CHECKPOINT;
        final String fileToUploadPath = filePath;
        final String paramNameString = "photo";


//        if (!userInputIsValid(serverUrlString, fileToUploadPath, paramNameString))
//            return;

        final UploadRequest request = new UploadRequest(this, UUID.randomUUID().toString(), serverUrlString);

        request.addFileToUpload(fileToUploadPath, paramNameString, "test", "multipart/form-data;boundary=" + boundary);
        request.addParameter("user_id", user.getIdServer());


        if (cameraLocation != null) {
            request.addParameter("lat_lng", cameraLocation.getLatitude() + "," + cameraLocation.getLongitude());
        } else {
            if (myLocation != null)
                request.addParameter("lat_lng", myLocation.getLatitude() + "," + myLocation.getLongitude());
        }


        if (Input != null && Input.getText().toString().length() > 0)
            request.addParameter("description", Input.getText().toString().toLowerCase());
        else {
            request.addParameter("description", "");
        }


//        request.setNotificationConfig(R.drawable.ic_launcher,
//                getString(R.string.app_name),
//                getString(R.string.uploading),
//                getString(R.string.upload_success),
//                getString(R.string.upload_error), false);

        try {
            log(TAG, "UploadService user id: " + user.getIdServer() + "photo " + filePath + " lat_lng " + myLocation.getLatitude() + "," + myLocation.getLongitude());
            showDialog(getString(R.string.loading));
            UploadService.startUpload(request);
        } catch (Exception exc) {
            //Toast.makeText(this, "Malformed upload request. " + exc.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private final AbstractUploadServiceReceiver uploadReceiver = new AbstractUploadServiceReceiver() {

        @Override
        public void onProgress(String uploadId, int progress) {
            // progressBar.setProgress(progress);

            // Log.e(TAG, "The progress of the upload with ID " + uploadId + " is: " + progress);
        }

        @Override
        public void onError(String uploadId, Exception exception) {
            //    progressBar.setProgress(0);
            dismissDialog();
            showMaterialDialog(getString(R.string._error), getString(R.string.interneterror),
                    getString(R.string.retry), getString(R.string.cancel), 1);

            String message = "Error in upload with ID: " + uploadId + ". " + exception.getLocalizedMessage();
            Log.e(TAG, message, exception);
        }

        @Override
        public void onCompleted(String uploadId, int serverResponseCode, String serverResponseMessage) {
            //  progressBar.setProgress(0);
            dismissDialog();

            String message = "Upload with ID " + uploadId + " is completed: " + serverResponseCode + ", "
                    + serverResponseMessage;

            try {
                sendNotification(RESTCient.getCheckListFromJson(new JSONObject(serverResponseMessage)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            log(TAG, message);


        }
    };

    public void onEvent(Push push) {
        log(TAG, "onEvent PUSH");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getChekListFromServer(false);
            }
        });

    }

    public void showMaterialDialog(String title, String content, String positive, String negative, final int type) {
        new MaterialDialog.Builder(this)
                .title(title)
                .content(content)
                .positiveColor(getResources().getColor(R.color.primary))
                .positiveText(positive)
                .negativeColor(getResources().getColor(R.color.primary))
                .negativeText(negative)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog materialDialog) {

                    }

                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        switch (type) {
                            case 1:
                                if (outputFileUri != null) {
                                    upLoadToserver(outputFileUri.getPath());
                                }
                                break;

                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    public void log(String TAG, String text) {
        Log.e(TAG, text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {

                    selectedImageUri = outputFileUri;
                    log(TAG, "selecte IMG formCaemra " + selectedImageUri.toString());
                    if (imgCheckpointUser != null) {
                        try {
                            ImgUploadSelect = true;
                            imgCheckpointUser.setImageBitmap(imgUtils.decodeUri(selectedImageUri, this));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                    outputFileUri = data.getData();

                    filePath = imgUtils.getPath(outputFileUri, this);
                    selectedImageUri = data == null ? null : data.getData();
                    log(TAG, "selecte IMG formGalllery " + selectedImageUri);
                    if (imgCheckpointUser != null) {
                        try {
                            ImgUploadSelect = true;
                            imgCheckpointUser.setImageBitmap(imgUtils.decodeUri(selectedImageUri, this));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE_KK) {
                    final boolean isCamera;
                    if (data == null) {
                        isCamera = true;
                    } else {
                        final String action = data.getAction();
                        if (action == null) {
                            isCamera = false;
                        } else {
                            isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                    }

                    Uri selectedImageUri;
                    if (isCamera) {

                        selectedImageUri = outputFileUri;
                        log(TAG, "selecte IMG formCaemraKK " + selectedImageUri.toString());
                        if (imgCheckpointUser != null) {
                            try {
                                ImgUploadSelect = true;
                                imgCheckpointUser.setImageBitmap(imgUtils.decodeUri(selectedImageUri, this));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {

                        outputFileUri = data.getData();
                        filePath = imgUtils.getPath(this, outputFileUri);
                        selectedImageUri = data == null ? null : data.getData();


                        log(TAG, "selecte IMG formGallleryKK " + selectedImageUri);
                        if (imgCheckpointUser != null) {
                            try {
                                ImgUploadSelect = true;
                                imgCheckpointUser.setImageBitmap(imgUtils.decodeUri(selectedImageUri, this));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        new MaterialDialog.Builder(this)
                .title("Salir?")
                .content("Realmente deseas salir")
                .positiveText("Aceptar")
                .positiveColor(getResources().getColor(R.color.primary))
                .negativeText("Cancelar")
                .negativeColor(getResources().getColor(R.color.midnight_blue))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        log(TAG, "onEvent BackPressed onNegative");
                    }

                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        finish();
                    }
                })
                .show();
        // super.onBackPressed();

    }

    private Drawable getIcon(int position) {
        switch (position) {
            case 0:
                return getResources().getDrawable(R.drawable.ic_explore);
            case 1:
                return getResources().getDrawable(R.drawable.ic_list);

        }
        return null;
    }

    public Location getLastLocationDefault() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        String provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }
        myLocation = lm.getLastKnownLocation(provider);
        Location loc = lm.getLastKnownLocation(provider);
        if (loc != null) {
            return loc;
        } else {
            return null;
        }
    }
}
