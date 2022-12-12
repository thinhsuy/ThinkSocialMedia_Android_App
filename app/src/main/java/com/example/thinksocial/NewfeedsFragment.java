package com.example.thinksocial;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewfeedsFragment extends Fragment {
    public Database.DataSource myDB;
    private RecyclerView recyclerView;
    ArrayList<Post> postList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //return designed fragment instead default one
        myDB = Database.OpenDB(getContext(), "ThinkSocial.db");
        return inflater.inflate(R.layout.fragment_newfeeds, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        postList = Query.getAllPosts(myDB);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerList);
        Adapter.RecyclerviewPost adapter = new Adapter.RecyclerviewPost(getActivity(), postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
    }
}