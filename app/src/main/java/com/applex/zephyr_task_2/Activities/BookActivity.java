package com.applex.zephyr_task_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.applex.zephyr_task_2.R;
import java.util.Objects;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        TextView book_name_text_view = findViewById(R.id.book_name);
        TextView author_text_view = findViewById(R.id.author);
        TextView category_text_view = findViewById(R.id.category);
        TextView publish_date_text_view = findViewById(R.id.publish_date);
        TextView page_count_text_view = findViewById(R.id.page_count);

        LinearLayout book_name_layout = findViewById(R.id.book_name_layout);
        LinearLayout author_layout = findViewById(R.id.author_layout);
        LinearLayout category_layout = findViewById(R.id.category_layout);
        LinearLayout publish_date_layout = findViewById(R.id.publish_date_layout);
        LinearLayout page_count_layout = findViewById(R.id.page_count_layout);

        String book_name = getIntent().getStringExtra("book_name");
        String author = getIntent().getStringExtra("author");
        String category = getIntent().getStringExtra("category");
        String publish_date = getIntent().getStringExtra("publish_date");
        String page_count = getIntent().getStringExtra("page_count");

        if(book_name != null && !book_name.isEmpty()) {
            book_name_text_view.setText(book_name);
        }
        else {
            book_name_layout.setVisibility(View.GONE);
        }

        if(author != null && !author.isEmpty()) {
            author_text_view.setText(author);
        }
        else {
            author_layout.setVisibility(View.GONE);
        }

        if(category != null && !category.isEmpty()) {
            category_text_view.setText(category);
        }
        else {
            category_layout.setVisibility(View.GONE);
        }

        if(publish_date != null && !publish_date.isEmpty()) {
            publish_date_text_view.setText(publish_date);
        }
        else {
            publish_date_layout.setVisibility(View.GONE);
        }

        if(page_count != null && !page_count.isEmpty()) {
            page_count_text_view.setText(page_count);
        }
        else {
            page_count_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}