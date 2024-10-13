package com.example.starratingsapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.starratingsapp.adapter.BookStarAdapter;
import com.example.starratingsapp.beans.BookStar;
import com.example.starratingsapp.services.BookStarService;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private List<BookStar> stars;
    private RecyclerView recyclerView;
    private BookStarAdapter starAdapter = null;
    private BookStarService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stars = new ArrayList<>();
        service = BookStarService.getInstance();
        init();
        recyclerView = findViewById(R.id.recycle_view);
        starAdapter = new BookStarAdapter(this, service.findAll());
        recyclerView.setAdapter(starAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bar_color));
        }



    }
    public void init(){
        service.create(new BookStar("Then She Was Gone", "https://m.media-amazon.com/images/I/A1pfB2YpR1L._SX385_.jpg", 4.5f));
        service.create(new BookStar("The Housemaid", "https://m.media-amazon.com/images/I/81AHTyq2wVL._SY522_.jpg", 3.5f));
        service.create(new BookStar("Atomic Habits", "https://m.media-amazon.com/images/I/419CqGgAdZL._SY445_SX342_.jpg", 5f));
        service.create(new BookStar("Ikigai", "https://m.media-amazon.com/images/I/71xH0ALI4-L._SY522_.jpg", 4.5f));
        service.create(new BookStar("The 48 Laws of Power", "https://m.media-amazon.com/images/I/31hSni7bS6L._SY445_SX342_.jpg", 4f));
        service.create(new BookStar("Capitalism", "https://m.media-amazon.com/images/I/41ls4gy01rL._SY445_SX342_.jpg", 2f));
        service.create(new BookStar("The Silent Patient", "https://m.media-amazon.com/images/I/91RVshgQn1S._SX385_.jpg", 5f));
        service.create(new BookStar("Good Energy", "https://m.media-amazon.com/images/I/41T76P2Fm7L._SY445_SX342_.jpg", 4f));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share){
            String txt = "BookStars";
            String mimeType = "text/plain";
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(mimeType)
                    .setChooserTitle("Stars")
                    .setText(txt)
                    .startChooser();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;}
            @Override
            public boolean onQueryTextChange(String newText) {
                if (starAdapter != null){
                    starAdapter.getFilter().filter(newText);
                }
                return true;
            }
             });
        return true;
    }


}