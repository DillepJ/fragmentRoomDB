package com.elevenzon.sqlite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.elevenzon.sqlite.Activity.LoginScreen;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {




    ArrayList<NoteModel> arrayList;
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    DatabaseHelper database_helper1;
    public  static  SqliteHelepr database_helper;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    WebView webView;

    //dialog view ui
    EditText title, des;
    Button submit;
    ImageView close;
    RelativeLayout edit_image;
    CircleImageView circleImageView, priview_image;

    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    Bitmap photo;

    ImageView show_list;
    double latitude,longitude;

    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    GPSTracker gpsTracker;

    Handler handler;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mp = MediaPlayer.create(this,R.raw.clock_ticking1);
        //ChessTimer.mp.stop();
        show_list = findViewById(R.id.show);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        actionButton = (FloatingActionButton) findViewById(R.id.add);
        database_helper = new SqliteHelepr(MainActivity.this);
        gpsTracker=new GPSTracker(this);

        webView=findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        // check if GPS enabled
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
        }else{
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
        //displayNotes();
        locationsget();
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        show_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotes();
            }
        });
        gotoGoogle();
    }

    //display notes list
    public void displayNotes() {
        arrayList = new ArrayList<>(database_helper.getNotes());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        NotesAdapter adapter = new NotesAdapter(getApplicationContext(), this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    //display dialog
    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(params);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title = (EditText) dialog.findViewById(R.id.title);
        des = (EditText) dialog.findViewById(R.id.description);
        submit = (Button) dialog.findViewById(R.id.submit);
        close = (ImageView) dialog.findViewById(R.id.close);
        circleImageView = dialog.findViewById(R.id.profile_image);
        priview_image = dialog.findViewById(R.id.profile_image_preview);
        edit_image = dialog.findViewById(R.id.edit_image);


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

               /* if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }*/
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    requestStoragePermission();
                }


            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()) {
                    title.setError("Please Enter Title");
                } else if (des.getText().toString().isEmpty()) {
                    des.setError("Please Enter Description");
                } else {


                    if (circleImageView.getDrawable() != null) {
                        database_helper.addNotes(title.getText().toString(), des.getText().toString(),gpsTracker.latitude,gpsTracker.longitude,DatetoString(), imageViewToByte(circleImageView));
                    } else {
                        circleImageView.setImageResource(R.drawable.profile);
                        database_helper.addNotes(title.getText().toString(), des.getText().toString(),latitude,longitude,DatetoString(), imageViewToByte(circleImageView));

                    }

                    dialog.cancel();
                    displayNotes();
                }
            }
        });
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, 100);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }

       if(requestCode==101) {
           Log.d(TAG, "onRequestPermissionsResult");
           for (String perms : permissionsToRequest) {
               if (!hasPermission(perms)) {
                   permissionsRejected.add(perms);
               }
           }

           if (permissionsRejected.size() > 0) {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                       showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                               new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                           requestPermissions(permissionsRejected.toArray(
                                                   new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                       }
                                   }
                               });
                       return;
                   }
               }
           } else {
               Log.d(TAG, "No rejected permissions.");
               canGetLocation = true;
               getLocation();
           }
       }
    }

public void locationsget(){
    locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
    isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
    permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
    permissions.add(CAMERA);
    permissionsToRequest = findUnAskedPermissions(permissions);

    if (!isGPS && !isNetwork) {
        Log.d(TAG, "Connection off");
        showSettingsAlert();
        getLastLocation();
    } else {
        Log.d(TAG, "Connection on");
        // check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
                Log.d(TAG, "Permission requests");
                canGetLocation = false;
            }
        }
        //getLocation();
    }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {


            photo = (Bitmap) data.getExtras().get("data");
            circleImageView.setImageBitmap(photo);
            priview_image.setVisibility(View.GONE);
            priview_image.setImageBitmap(photo);
            //getLatitude();
            //gpsTracker.getAddressFromLocation(gpsTracker.latitude,gpsTracker.longitude,MainActivity.this,handler);
        }
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String ima = new String(byteArray);
        String image3 = ima;
        return byteArray;
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null){
                            //updateUI(loc);
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null) {
                            updateUI(loc);
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    } else {
                        loc.setLatitude(0);
                        loc.setLongitude(0);
                        updateUI(loc);
                    }
                } else {
                    Log.d(TAG, "Can't get location");
                }

            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        //tvLatitude.setText(Double.toString(loc.getLatitude()));
        //tvLongitude.setText(Double.toString(loc.getLongitude()));
        //tvTime.setText(DateFormat.getTimeInstance().format(loc.getTime()));
        //latitude=loc.getLatitude();
        //longitude=loc.getLongitude();
    }

    public String DatetoString(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate ;
    }

    public void  getAEList(){

    }

    public void gotoGoogle(){
      webView.setWebViewClient(new WebViewClient());
      webView.loadUrl("https://www.google.com");
      webView.requestFocus();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        Intent intent=new Intent(MainActivity.this, LoginScreen.class);
                        startActivity(intent);
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


}