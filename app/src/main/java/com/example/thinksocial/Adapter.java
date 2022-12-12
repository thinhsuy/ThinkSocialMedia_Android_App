package com.example.thinksocial;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter {
//    public static class ListviewPost extends BaseAdapter {
//        private ArrayList<Post> arrPosts;
//        private Activity activity;
//        private View view;
//
//        public ListviewPost(Activity paraActivity, ArrayList<Post> arrList){
//            this.arrPosts = arrList;
//            this.activity = paraActivity;
//        }
//
//        @Override
//        public int getCount() {
//            return arrPosts.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return arrPosts.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            Post pst = arrPosts.get(i);
//            LayoutInflater inflater = this.activity.getLayoutInflater();
//            view = inflater.inflate(R.layout.layout_home_post, null);
//
//            ImageView postImage = (ImageView) view.findViewById(R.id.ivPost);
//            new Internet.DownloadImageTask(postImage).execute(pst.getImgLink());
//            ((TextView)view.findViewById(R.id.tvUsername)).setText(pst.getUsername());
//            ((TextView)view.findViewById(R.id.tvHeader)).setText(pst.getHeader());
//            ((TextView)view.findViewById(R.id.tvContent)).setText(pst.getContent());
//            return view;
//        }
//    }

    public static class RecyclerviewPost extends RecyclerView.Adapter<RecyclerviewPost.DataViewHolder> {
        private Activity activity;
        private ArrayList<Post> arrPosts;

        public RecyclerviewPost(Activity activity, ArrayList<Post> items) {
            this.activity = activity;
            this.arrPosts = items;
        }

        @Override
        public RecyclerviewPost.DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int ViewType) {
            //in here ViewType would let you know the differences of View Object and seperate the operation via Switch Case
            View itemView;
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_home_post, viewGroup, false);
            return new DataViewHolder(itemView);
        }

        //this method would bind the saved View Holder to the Object and leads to easy accessing
        @Override
        public void onBindViewHolder(RecyclerviewPost.DataViewHolder viewHolder, int position) {
            Post pst = arrPosts.get(position);

            new Internet.DownloadImageTask(viewHolder.ivPost).execute(pst.getImgLink());
            viewHolder.tvUsername.setText(pst.getUsername());
            viewHolder.tvHeader.setText(pst.getHeader());
            viewHolder.tvContent.setText(pst.getContent());
        }

        @Override
        public int getItemCount() {
            return arrPosts.size();
        }


        /*-------------------------- View Holder ----------------------*/
        //holder will remember the object after the first call, make the next-time-calling take less complexity
        public static class DataViewHolder extends RecyclerView.ViewHolder {
            private Button btn;
            private TextView tvUsername;
            private TextView tvHeader;
            private TextView tvContent;
            private ImageView ivPost;

            public DataViewHolder(View view) {
                super(view);
                ivPost = (ImageView) view.findViewById(R.id.ivPost);
                tvUsername = ((TextView)view.findViewById(R.id.tvUsername));
                tvHeader = ((TextView)view.findViewById(R.id.tvHeader));
                tvContent = ((TextView)view.findViewById(R.id.tvContent));
            }
        }
    }
}
