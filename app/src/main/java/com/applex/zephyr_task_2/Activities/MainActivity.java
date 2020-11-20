package com.applex.zephyr_task_2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.applex.zephyr_task_2.Adapters.RecyclerAdapter;
import com.applex.zephyr_task_2.Models.BooksModel;
import com.applex.zephyr_task_2.Models.DataModel;
import com.applex.zephyr_task_2.Preferences.IntroPref;
import com.applex.zephyr_task_2.R;
import com.applex.zephyr_task_2.Utilities.RetrofitHelper;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.util.ArrayList;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Spinner order;
    private IntroPref introPref;
    private ImageView no_data, error;
    private RecyclerView recyclerView;
    private RelativeLayout main_layout;
    private LinearLayout orderByLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ArrayList<BooksModel> booksModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        introPref = new IntroPref(MainActivity.this);
        booksModelArrayList = new ArrayList<>();

        order = findViewById(R.id.order);
        error = findViewById(R.id.error);
        no_data = findViewById(R.id.no_data);
        main_layout = findViewById(R.id.main_layout);
        recyclerView = findViewById(R.id.recycler_list);
        orderByLayout = findViewById(R.id.orderByLayout);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settingImage(R.drawable.no_data);
        settingImage(R.drawable.error);

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);

        if(((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
            recyclerView.setVisibility(View.GONE);
            no_data.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Please check your internet connection and refresh", Toast.LENGTH_SHORT).show();
        }
        else {
            new Handler().postDelayed(this::buildRecyclerView, 1000);
        }

        swipeRefreshLayout
                .setColorSchemeColors(getResources().getColor(R.color.purple_500),
                 getResources().getColor(R.color.purple_700));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
                recyclerView.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Please check your internet connection and refresh", Toast.LENGTH_SHORT).show();
            }
            else {
                booksModelArrayList.clear();
                new Handler().postDelayed(this::buildRecyclerView, 1000);
            }
        });
    }

    private void buildRecyclerView() {
        Call<DataModel> call = RetrofitHelper.getInstance().getApiInterface().getBooksList();
        call.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(@NonNull Call<DataModel> call, @NonNull  Response<DataModel> response) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                order.setSelection(introPref.getSortOrder());
                booksModelArrayList.addAll(response.body().getData());
                sortingList(booksModelArrayList, order.getSelectedItem().toString());
            }

            @Override
            public void onFailure(@NonNull Call<DataModel> call, @NonNull Throwable t) {
                main_layout.setBackgroundColor(getResources().getColor(R.color.white));
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                orderByLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                no_data.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        if(item.getItemId() == R.id.search) {
            androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
            searchView.setQueryHint("Search");

            ArrayList<BooksModel> arrayList = new ArrayList<>();

            searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    arrayList.clear();
                    if(newText != null && !newText.isEmpty()) {
                        for(int i = 0; i < booksModelArrayList.size(); i++) {
                            if(booksModelArrayList.get(i).getBook_name().toLowerCase().contains(newText.toLowerCase())) {
                                arrayList.add(booksModelArrayList.get(i));
                            }
                        }
                        sortingList(arrayList, order.getSelectedItem().toString());

                        swipeRefreshLayout
                                .setColorSchemeColors(getResources().getColor(R.color.purple_500),
                                 getResources().getColor(R.color.purple_700));
                        swipeRefreshLayout.setOnRefreshListener(() -> {
                            if(((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
                                recyclerView.setVisibility(View.GONE);
                                no_data.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this, "Please check your internet connection and refresh", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                sortingList(arrayList, order.getSelectedItem().toString());
                            }
                        });
                    }
                    else {
                        sortingList(booksModelArrayList, order.getSelectedItem().toString());

                        swipeRefreshLayout
                                .setColorSchemeColors(getResources().getColor(R.color.purple_500),
                                 getResources().getColor(R.color.purple_700));
                        swipeRefreshLayout.setOnRefreshListener(() -> {
                            if(((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
                                recyclerView.setVisibility(View.GONE);
                                no_data.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this, "Please check your internet connection and refresh", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                sortingList(booksModelArrayList, order.getSelectedItem().toString());
                            }
                        });
                    }
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortingList(ArrayList<BooksModel> arrayList, String orderBy) {
        if(arrayList!= null && arrayList.size() > 0) {
            main_layout.setBackgroundColor(getResources().getColor(R.color.grey));
            no_data.setVisibility(View.GONE);
            orderByLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            switch (orderBy) {
                case "Book Name":
                    Collections.sort(arrayList, (booksModel1, booksModel2) -> booksModel1.getBook_name().compareTo(booksModel2.getBook_name()));
                    break;
                case "Author":
                    Collections.sort(arrayList, (booksModel1, booksModel2) -> booksModel1.getAuthor().compareTo(booksModel2.getAuthor()));
                    break;
                case "Category":
                    Collections.sort(arrayList, (booksModel1, booksModel2) -> booksModel1.getCategory().compareTo(booksModel2.getCategory()));
                    break;
                case "Publish Date":
                    Collections.sort(arrayList, (booksModel1, booksModel2) -> booksModel1.getPublish_date().compareTo(booksModel2.getPublish_date()));
                    break;
                case "Page Count":
                    Collections.sort(arrayList, (booksModel1, booksModel2) -> booksModel1.getPage_count() - booksModel2.getPage_count());
                    break;
            }

            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(MainActivity.this, arrayList);
            recyclerView.setAdapter(recyclerAdapter);
            if(swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    introPref.setSortOrder(position);
                    order.setSelection(introPref.getSortOrder());
                    sortingList(arrayList, order.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });
        }
        else {
            main_layout.setBackgroundColor(getResources().getColor(R.color.white));
            orderByLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            no_data.setVisibility(View.VISIBLE);
        }
    }

    private void settingImage(int id) {
        Display display = getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(getResources(), id, options);

        int width = options.outWidth;
        if (width > displayWidth) {
            options.inSampleSize = Math.round((float) width / (float) displayWidth);
        }
        options.inJustDecodeBounds = false;

        Bitmap scaledBitmap =  BitmapFactory.decodeResource(getResources(), id, options);

        if(id == R.drawable.no_data) {
            no_data.setImageBitmap(scaledBitmap);
        }
        else {
            error.setImageBitmap(scaledBitmap);
        }
    }
}