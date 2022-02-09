package com.nic.sqlite.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nic.sqlite.R;
import com.williamww.silkysignature.views.SignaturePad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignaturePadFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignaturePadFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    /*private BottomSheetBehavior mBottomSheetBehavior;*/
    private TextView mTextViewState;
    RelativeLayout tool_bar;

    SignaturePad mSignaturePad;
    Button clear_btn,save_btn;
    ImageView signature_img;
    public SignaturePadFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignaturePad.
     */
    // TODO: Rename and change types and number of parameters
    public static SignaturePadFrag newInstance(String param1, String param2) {
        SignaturePadFrag fragment = new SignaturePadFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signature_pad, container, false);

        signature_img=view.findViewById(R.id.signature_img);



        mSignaturePad = (SignaturePad) view.findViewById(R.id.signature_pad);
        clear_btn = (Button) view.findViewById(R.id.clear_btn);
        save_btn = (Button)view.findViewById(R.id.save_btn);
        signature_img.setVisibility(View.GONE);
        mSignaturePad.setVisibility(View.VISIBLE);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSignaturePad.getSignatureBitmap()==null){

                }
                else {
                    mSignaturePad.getSignatureBitmap();
                    signature_img.setVisibility(View.VISIBLE);
                    mSignaturePad.setVisibility(View.GONE);
                    signature_img.setImageBitmap(mSignaturePad.getTransparentSignatureBitmap());
                }

            }
        });

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
                mSignaturePad.setVisibility(View.VISIBLE);
                signature_img.setVisibility(View.GONE);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
