package com.example.thinksocial;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class PostingFragment extends Fragment {
    public Database.DataSource myDB;
    public TextInputEditText tfImageLink;
    public TextInputEditText tfHeader;
    public TextInputEditText tfContent;
    public ImageView ivPost;
    public Button btnUpload;
    public Button btnPost;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //return designed fragment instead default one
        myDB = Database.OpenDB(getContext(), "ThinkSocial.db");
        return inflater.inflate(R.layout.fragment_posting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tfImageLink = (TextInputEditText)view.findViewById(R.id.tfImage);
        tfHeader = (TextInputEditText)view.findViewById(R.id.tfHeader);
        tfContent = (TextInputEditText)view.findViewById(R.id.tfContent);
        ivPost = (ImageView)view.findViewById(R.id.ivImage);
        setUpButton(view);
    }

    public void setUpButton(View view){
        String http = tfImageLink.getText().toString();
        btnUpload = (Button)view.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadImageHTTP(http);
            }
        });

        btnPost = (Button)view.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIsAcceptable(http))
                    addPost();
            }
        });
    }

    public void LoadImageHTTP(String http){
        try {new Internet.DownloadImageTask(ivPost).execute(http);}
        catch (Exception ex) {Toast.makeText(getContext(), "Image link is undefined!", Toast.LENGTH_SHORT).show();}
    }

    public void addPost(){
        int userId = ((Global)getActivity().getApplication()).getIdCurrentUser();
        boolean result = Query.createPost(myDB, userId,
                                tfHeader.getText().toString(),
                                tfContent.getText().toString(),
                                tfImageLink.getText().toString());
        if (result){
            Toast.makeText(getContext(), "Posted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Post failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkIsAcceptable(String http){
        if (tfHeader.equals("") || tfContent.equals("") || tfImageLink.equals(""))
            return false;
        try {new Internet.DownloadImageTask(ivPost).execute(http);}
        catch (Exception ex) {return false;}
        return true;
    }
}