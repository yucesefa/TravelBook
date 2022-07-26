package com.safayuce.maps.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.safayuce.maps.R;
import com.safayuce.maps.adapter.PlaceAdapter;
import com.safayuce.maps.databinding.ActivityMainBinding;
import com.safayuce.maps.model.Place;
import com.safayuce.maps.roomdb.PlaceDao;
import com.safayuce.maps.roomdb.PlaceDatabase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();

    PlaceDatabase db;
    PlaceDao placeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        ConstraintLayout view=binding.getRoot();
        setContentView(view);

        db= Room.databaseBuilder(getApplicationContext(),PlaceDatabase.class,"Place").build();
        placeDao= db.placeDao();

        compositeDisposable.add(placeDao.getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MainActivity.this::handleResponse));
    }

    private void handleResponse(List<Place> placeList){

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PlaceAdapter placeAdapter=new PlaceAdapter(placeList);
        binding.recyclerView.setAdapter(placeAdapter);

    }

    //Menu baglantisi

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Menu baglantisi
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.travel_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.add_place){
            Intent intent=new Intent(MainActivity.this,MapsActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    protected void onDestroy(){
        super.onDestroy();
        compositeDisposable.clear();

    }
}