package com.example.mixmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mixmaster.model.Post;
import com.example.mixmaster.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;


// USER VIEW HOLDER
class UserViewHolder extends RecyclerView.ViewHolder{

    ImageView cocktailImg;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        cocktailImg = itemView.findViewById(R.id.cocktail_img);
    }

    public void bind(Post post, UserRecyclerAdapter.OnItemClickListener listener) {
        Picasso.get().load(post.cocktailUrl).into(cocktailImg);
        cocktailImg.setOnClickListener(v->listener.onItemClick(getAdapterPosition()));
    }
}


// USER ADAPTER
public class UserRecyclerAdapter extends RecyclerView.Adapter<UserViewHolder>{

    List<Post> data;
    LayoutInflater inflater;
    OnItemClickListener listener;

    public UserRecyclerAdapter(List<Post> data, LayoutInflater inflater){
        this.data = data;
        this.inflater = inflater;
    }

    public static interface OnItemClickListener {
        void onItemClick(int adapterPosition);
    }

    public void SetItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    public void setData(List<Post> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.user_post_list_row,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Post post = data.get(position);
        holder.bind(post,listener);
    }

    @Override
    public int getItemCount() {
        if (data== null) return 0;
        return data.size();
    }
}
