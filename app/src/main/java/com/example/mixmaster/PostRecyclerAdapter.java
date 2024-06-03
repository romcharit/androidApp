package com.example.mixmaster;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mixmaster.model.Post;

import java.util.List;
import java.util.zip.Inflater;


class PostViewHolder extends RecyclerView.ViewHolder {
    TextView userNameTv;
    TextView cocktailNameTv;
    ImageView avatarTv;
    ImageView cocktailImgTv;
    ImageButton likeTv;

    List<Post> data;

    public PostViewHolder(@NonNull View itemView, List<Post> data) {
        super(itemView);
        this.data = data;
        userNameTv = itemView.findViewById(R.id.userName_plr);
        cocktailNameTv = itemView.findViewById(R.id.cocktail_name_plr);
        avatarTv = itemView.findViewById((R.id.avatar_plr));
        cocktailImgTv = itemView.findViewById(R.id.cocktail_img_plr);
        likeTv = itemView.findViewById(R.id.like_btn_plr);
    }

    public void bind(Post post) {
        userNameTv.setText(post.userName);
        cocktailNameTv.setText((post.cocktailName));
        //avatarTv
        //cocktailImgTv
        //likeTv
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

    }
}

// Adapter - makes the rows of the list
public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {

//        OnItemClickListener listener;
//        public static interface OnItemClickListener {
//            void onItemClick(int pos);
//        }

    LayoutInflater inflater;
    List<Post> data;

    public void setData(List<Post> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public PostRecyclerAdapter(LayoutInflater inflater, List<Post> data){
        this.inflater = inflater;
        this.data = data;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.post_list_row, parent, false);
        return new PostViewHolder(view,data);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = data.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

