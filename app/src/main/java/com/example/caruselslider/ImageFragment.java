package com.example.caruselslider;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImageFragment extends Fragment {

    private MyApi myApi;
    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURE = "resource";

    private int screenWidth;
    private int screenHeight;





    public ImageFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(MainActivity context, int pos, float scale) {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);

        return Fragment.instantiate(context, ImageFragment.class.getName(), b);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(container == null){
            return  null;
        }


        final int position = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);

        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth/2,screenHeight/2);
        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_image, container, false);
        CarouselLinearLayout root = (CarouselLinearLayout) linearLayout.findViewById(R.id.root_container);
        myApi= RetrofitInstance.getRetrofitInstance().create(MyApi.class);
        Call<List<List_Data>>call=myApi.geData();
        call.enqueue(new Callback<List<List_Data>>() {
            @Override
            public void onResponse(Call<List<List_Data>> call, Response<List<List_Data>> response) {
                List <List_Data> list_data=response.body();
                for (int i=0; i<list_data.size(); i++){
                    List_Data listData=list_data.get(position);
                    final String img=listData.getImageurl();
                    final String name=listData.getName();
                    TextView textView = (TextView) linearLayout.findViewById(R.id.text);
                    CarouselLinearLayout root = (CarouselLinearLayout) linearLayout.findViewById(R.id.root_container);
                    ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pagerImg);

                    textView.setText(name);
                    imageView.setLayoutParams(layoutParams);
//
                    Glide.with(getContext()).load(img).into(imageView);

                }

            }

            @Override
            public void onFailure(Call<List<List_Data>> call, Throwable t) {

            }
        });
        root.setScaleBoth(scale);
        //handling click event




        return linearLayout;
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }

}