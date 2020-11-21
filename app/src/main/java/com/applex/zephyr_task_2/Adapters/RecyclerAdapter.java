package com.applex.zephyr_task_2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.applex.zephyr_task_2.Activities.BookActivity;
import com.applex.zephyr_task_2.Models.BooksModel;
import com.applex.zephyr_task_2.R;
import com.thekhaeng.pushdownanim.PushDownAnim;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.ProgrammingViewHolder> {

    private final ArrayList<BooksModel> mList;
    private final Context context;

    public RecyclerAdapter(Context context, ArrayList<BooksModel> list) {
        this.mList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items, parent, false);
        return new ProgrammingViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position) {
        BooksModel currentItem = mList.get(position);

        if(currentItem.getBook_name() != null && !currentItem.getBook_name().isEmpty()) {
            holder.name.setText(currentItem.getBook_name());
        }
        else {
            holder.itemView.setVisibility(View.GONE);
        }

        PushDownAnim.setPushDownAnimTo(holder.itemView)
            .setScale(PushDownAnim.MODE_STATIC_DP, 3)
            .setOnClickListener(view -> {
                Intent intent = new Intent(context, BookActivity.class);
                intent.putExtra("book_name", currentItem.getBook_name());
                intent.putExtra("author", currentItem.getAuthor());
                intent.putExtra("category", currentItem.getCategory());
                intent.putExtra("publish_date", currentItem.getPublish_date());
                intent.putExtra("page_count", String.valueOf(currentItem.getPage_count()));
                context.startActivity(intent);
            });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ProgrammingViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        private ProgrammingViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.book_name);
        }
    }
}