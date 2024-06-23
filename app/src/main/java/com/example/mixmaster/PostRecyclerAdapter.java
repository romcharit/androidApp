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
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;


// Post View Holder
class PostViewHolder extends RecyclerView.ViewHolder {
    TextView userNameTv;
    TextView cocktailNameTv;
    ImageView avatarImg;
    ImageView cocktailImg;
    ImageButton likeIbtn;
    List<Post> data;

    public PostViewHolder(@NonNull View itemView, List<Post> data, PostRecyclerAdapter.OnItemClickListener listener) {
        super(itemView);
        this.data = data;
        userNameTv = itemView.findViewById(R.id.userName_plr);
        cocktailNameTv = itemView.findViewById(R.id.cocktail_name_plr);
        avatarImg = itemView.findViewById((R.id.avatar_plr));
        cocktailImg = itemView.findViewById(R.id.cocktail_img_plr);
        likeIbtn = itemView.findViewById(R.id.like_btn_plr);

        likeIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add like ...
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });
    }

    public void bind(Post post, int pos) {

        userNameTv.setText(post.userName);
        cocktailNameTv.setText((post.cocktailName));
        
        if (!Objects.equals(post.getCocktailUrl(), " ")) {
            Picasso.get().load(post.getCocktailUrl()).placeholder(R.drawable.cocktail_blank).into(cocktailImg);
        } else{
            cocktailImg.setImageResource(R.drawable.cocktail_blank);
        }
    }

}


// Post Adapter - makes the rows of the list
public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {

    LayoutInflater inflater;
    List<Post> data;
    OnItemClickListener listener;

    public static interface OnItemClickListener {
        void onItemClick(int pos);
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

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
        return new PostViewHolder(view,data,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = data.get(position);
        holder.bind(post,position);
    }

    @Override
    public int getItemCount() {
        if (data== null) return 0;
        return data.size();
    }
}

