package com.jov.laughter.frame;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jov.laughter.R;
  
public class ImageFrame extends Fragment {  
    TextView mTextView; 
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
    }  
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
    }  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        return inflater.inflate(R.layout.image_frame, container, false);  
    }  
}  
