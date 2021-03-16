package com.elevenzon.sqlite.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.elevenzon.sqlite.Adapetr.ImageListAdapter;
import com.elevenzon.sqlite.MainActivity;
import com.elevenzon.sqlite.NotesAdapter;
import com.elevenzon.sqlite.R;
import com.elevenzon.sqlite.Utils.Utils;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.elevenzon.sqlite.MainActivity.database_helper;

public class AddlayoutInflater extends AppCompatActivity {
    private static final int CAMERA_REQUEST =100 ;
    LinearLayout layoutToAdd;
    ScrollView scrollView;
    Button save,add,show;

    ImageView imageView,image_view_preview;

    RecyclerView image_recycler;
    ImageListAdapter imageListAdapter;

    ArrayList<String> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_existed);
        layoutToAdd = (LinearLayout) findViewById(R.id.add_layout);
        scrollView=findViewById(R.id.scroll);

        show=findViewById(R.id.Show);
        image_recycler=findViewById(R.id.image_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        image_recycler.setLayoutManager(linearLayoutManager);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotes();
            }
        });


        save=findViewById(R.id.save);
         save.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 int count =layoutToAdd.getChildCount();
                 for (int i=0; i<count;i++){
                     JSONArray imageArray = new JSONArray();

                     View vv = layoutToAdd.getChildAt(i);
                     EditText myEditTextView = (EditText) vv.findViewById(R.id.description);

                     ImageView imageView = (ImageView) vv.findViewById(R.id.image_view);
                     byte[] imageInByte = new byte[0];
                     String image_str = "";
                     try {
                         Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                         ByteArrayOutputStream baos = new ByteArrayOutputStream();
                         bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                         imageInByte = baos.toByteArray();
                         image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);
                         // String string = new String(imageInByte);
                         //Log.d("imageInByte_string",string);
                         Log.d("image_str", image_str);
                     } catch (Exception e) {
                         //imageboolean = false;
                         Utils.showAlert(AddlayoutInflater.this, "Atleast Capture one Photo");
                         break;
                         //e.printStackTrace();
                     }


                     database_helper.addImages(image_str);

                 }
             }
         });
         add = (Button) findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateView(AddlayoutInflater.this,layoutToAdd);
                focusOnView(scrollView);
            }

        });
    }

    public View updateView(final Activity activity, final LinearLayout emailOrMobileLayout) {
        final View hiddenInfo = activity.getLayoutInflater().inflate(R.layout.layout_toinfliate, emailOrMobileLayout, false);
        final ImageView imageView_close = (ImageView) hiddenInfo.findViewById(R.id.imageView_close);
        imageView = (ImageView) hiddenInfo.findViewById(R.id.image_view);
        image_view_preview = (ImageView) hiddenInfo.findViewById(R.id.image_view_preview);
        final EditText myEditTextView = (EditText) hiddenInfo.findViewById(R.id.description);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imageView.setVisibility(View.VISIBLE);
                        ((LinearLayout) hiddenInfo.getParent()).removeView(hiddenInfo);

                } catch (IndexOutOfBoundsException a) {
                    a.printStackTrace();
                }
            }
        });
        image_view_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getLatLong();
                cameraAction();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getLatLong();
                cameraAction();
            }
        });
        emailOrMobileLayout.addView(hiddenInfo);

        return hiddenInfo;
    }

    private final void focusOnView(final ScrollView your_scrollview) {
        your_scrollview.post(new Runnable() {
            @Override
            public void run() {
                your_scrollview.fullScroll(View.FOCUS_DOWN);
                //your_scrollview.scrollTo(0, your_EditBox.getY());
            }
        });
    }

    public void cameraAction(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST&& data!=null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            image_view_preview.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void displayNotes() {
        arrayList =new ArrayList<String>();
        arrayList.addAll(database_helper.getImages());
        image_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        image_recycler.setItemAnimator(new DefaultItemAnimator());
         imageListAdapter = new ImageListAdapter(arrayList,AddlayoutInflater.this);
        image_recycler.setAdapter(imageListAdapter);
    }

}

