package co.ubi.checkponit.UI.Fragments.CheckList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.ubi.checkponit.DB.Models.CheckList;
import co.ubi.checkponit.R;
import co.ubi.checkponit.UI.Fragments.BaseFragment;
import me.grantland.widget.AutofitHelper;

/**
 * Created by ${Mike} on 1/13/15.
 */
public class CheckListAdapter extends BaseAdapter {
    public static final String TAG = "CheckListAdapter";
    Context context;
    ArrayList<CheckList> checkLists;
    View view;
    CircularImageView imgProfileList;
    ImageView imgCheckPointList;
    LinearLayout containerCheck;
    //  TextLocationList,
    TextView TextDescriptionList, TextNamePersonList;
    ImageLoadingListener animateFirstListener, animateFirstListener1;
    final ImageLoadingListener ani = new AnimateFirstDisplayListener();


    public CheckListAdapter(Context context, ArrayList<CheckList> checkLists) {
        this.context = context;
        this.checkLists = checkLists;
        //  EventBus.getDefault().register(this);
    }

    @Override
    public int getCount() {
        return checkLists.size();
    }

    @Override
    public Object getItem(int position) {
        return checkLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e(TAG, "getView position " + position);
        /**
         * VALIDAR CUANDO VENGA VACIO O SEA LA PRIMERA VEZ
         * MOSTRAR UNOS DATOS DE EJEMPLO
         *
         * ***/

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  if (convertView == null) {
        view = inflater.inflate(R.layout.item_check_list_final, null);


//        } else {
//            view = convertView;
//        }
        imgProfileList = (CircularImageView) view.findViewById(R.id.imgProfileList);
        imgCheckPointList = (ImageView) view.findViewById(R.id.imgCheckPointList);

        // animateFirstListener = new AnimateFirstDisplayListener(AnimateFirstDisplayListener.TYPE_CIRCLE_IMAGE, imgProfileList, context);
        //animateFirstListener1 = new AnimateFirstDisplayListener(AnimateFirstDisplayListener.TYPE_CIRCLE_IMAGE, imgCheckPointList, context);

        containerCheck = (LinearLayout) view.findViewById(R.id.containerCheck);
        TextDescriptionList = (TextView) view.findViewById(R.id.TextDescriptionList);
        AutofitHelper.create(TextDescriptionList);
        //TextLocationList = (TextView) view.findViewById(R.id.TextLocationList);
        TextNamePersonList = (TextView) view.findViewById(R.id.TextNamePersonList);
        AutofitHelper.create(TextNamePersonList);

        // EventBus.getDefault().post(new loadedImageType(null, loadedImageType.PROFILE));
//        ImageLoader.getInstance().displayImage(checkLists.get(position).getPhoto(), imageView, BaseFragment.getOptionsImgCache(), new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                imgProfileList.setImageBitmap(loadedImage);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        });
//        ImageLoader.getInstance().displayImage(checkLists.get(position).getPhoto(), imageView,
//                BaseFragment.getOptionsImgCache(),
//                ani);
        Picasso.with(imgProfileList.getContext()).load(checkLists.get(position).getPhoto()).into(imgProfileList);
//        ImageLoader.getInstance().displayImage(checkLists.get(position).getPhoto(), imageView1, BaseFragment.getOptionsImgCache(), new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                Log.e(TAG, "onLoadingStarted " + imageUri);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                Log.e(TAG, "onLoadingFailed " + imageUri);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                imgProfileList.setImageBitmap(loadedImage);
//                Log.e(TAG, "onLoadingComplete " + imageUri);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                Log.e(TAG, "onLoadingCancelled " + imageUri);
//            }
//        });
        // System.gc();
        //  RESTCient.loadWebImage(imgProfileList, checkLists.get(position).getPhoto(), context);
        if (checkLists.get(position).getPhoto_check() != null && !checkLists.get(position).getPhoto_check().equals("null")) {
            Log.e(TAG, "photoCheck " + checkLists.get(position).getPhoto_check());

            //EventBus.getDefault().post(new loadedImageType(null, loadedImageType.CHECK));
//            ImageLoader.getInstance().displayImage(checkLists.get(position).getPhoto_check(), imageView,
//                    BaseFragment.getOptionsImgCache(),
//                    ani);
//            ImageLoader.getInstance().displayImage(checkLists.get(position).getPhoto_check(),
//                    imageView, BaseFragment.getOptionsImgCache(), new ImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String imageUri, View view) {
//                            Log.e(TAG, "onLoadingStarted " + imageUri);
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                            Log.e(TAG, "onLoadingFailed " + imageUri);
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            imgCheckPointList.setImageBitmap(loadedImage);
//                            Log.e(TAG, "onLoadingComplete " + imageUri);
//                        }
//
//                        @Override
//                        public void onLoadingCancelled(String imageUri, View view) {
//                            Log.e(TAG, "onLoadingCancelled " + imageUri);
//                        }
//                    }
//            );
            //   System.gc();

            ImageLoader.getInstance().displayImage(checkLists.get(position).getPhoto_check(), imgCheckPointList, BaseFragment.getOptionsImgCache(), ani);
//            ImageLoader.getInstance().loadImage(checkLists.get(position).getPhoto_check(), BaseFragment.getOptionsImgCache(),
//                    new ImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String imageUri, View view) {
//                            Log.e(TAG, "onLoadingStarted " + imageUri);
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                            Log.e(TAG, "onLoadingFailed " + imageUri, failReason.getCause());
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            imgCheckPointList.setImageBitmap(loadedImage);
//                            Log.e(TAG, "onLoadingComplete " + imageUri);
//                        }
//
//                        @Override
//                        public void onLoadingCancelled(String imageUri, View view) {
//                            Log.e(TAG, "onLoadingCancelled " + imageUri);
//                        }
//                    });


            //  RESTCient.loadWebImage(imgCheckPointList, checkLists.get(position).getPhoto_check(), context);
        } else {
            Log.e(TAG, "photoCheck DEFAULT" + position);
            // containerCheck.setVisibility(View.INVISIBLE);
        }
        if (checkLists.get(position).getDescription() != null && !checkLists.get(position).getDescription().equals("null")) {
            TextDescriptionList.setText(checkLists.get(position).getDescription());
        } else {
            TextDescriptionList.setText("");
        }
//        if (checkLists.get(position).getAddress() != null && !checkLists.get(position).getAddress().equals("null")) {
//            TextLocationList.setText(checkLists.get(position).getAddress());
//        } else {
//            TextLocationList.setText("Haz click para ver en el mapa");
//        }
        Log.e(TAG, "PERSON NAME " + checkLists.get(position).getName());


        TextNamePersonList.setText(checkLists.get(position).getName());


        return view;

    }


}
