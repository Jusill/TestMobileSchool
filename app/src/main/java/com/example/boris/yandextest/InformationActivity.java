package com.example.boris.yandextest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by Boris on 03.04.2016.
 */
public class InformationActivity extends AppCompatActivity {

    private ImageView imageView;
    private String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right); //Анимация перехода
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_about_singer);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Ставим кнопку home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Принимаем intent
        Intent intent = getIntent();

        //Присваиваем в переменные типа String
        string = intent.getStringExtra("name");
        getSupportActionBar().setTitle(string);
        toolbar.setTitleTextColor(Color.WHITE);

        //Принимаем описание
        string = intent.getStringExtra("description");
        TextView TVinformation = (TextView) findViewById(R.id.text_information);
        TVinformation.setText(string);

        //Принимаем количество треков
        string = intent.getStringExtra("tracks");
        TextView TVtracks = (TextView) findViewById(R.id.textViewTracks);
        TVtracks.setText(string);

        //Принимаем количество альбомов
        string = intent.getStringExtra("albums");
        TextView TValbums = (TextView) findViewById(R.id.textViewAlbums);
        TValbums.setText(string);

        //Принимаем жанры
        string = intent.getStringExtra("genres");
        TextView TVgenres = (TextView) findViewById(R.id.textViewGenres);
        TVgenres.setText(string);

        //Принимаем ссылку
        final String link = intent.getStringExtra("link");

        //Принимаем url
        string = intent.getStringExtra("imageBigUrl");
        imageView = (ImageView) findViewById(R.id.image_view);

        //С помощью библиотеки Picasso загружаем картинку
        Picasso.with(InformationActivity.this)
                .load(string)
                .error(android.R.drawable.ic_dialog_alert)
                .into(imageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

         //Нажатие на кнопку fab переходит на сайт или выдает сообщение, что ссылки нету
        fab.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick (View view){
                        if(!link.equals("null")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            startActivity(browserIntent);
                        }
                        else{
                            Toast.makeText(InformationActivity.this, R.string.toast_link,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }


    //Нажатие на клавишу назад
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if(id == Config.CODE_FOR_HOME){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public  void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right); //Анимация перехода
    }
}
