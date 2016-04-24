package com.example.boris.yandextest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<SingerMark> SingerList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static long backPressed;

    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    MenuItem searchMenuItem;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_activity_name);
        toolbar.setTitleTextColor(Color.WHITE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SingerList = new ArrayList<>(); //Лист с данными о певцах

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        //Получаем данные
        getData();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                refresh();
            }
        }, 1);
    }

    //Метод для обновления
    private void refresh() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseData(response);
                    }
                },

                //Если случилась ошибка при загрузке
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Ошибка при загрузке",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        //Отправляем запрос в очередь
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void getData() {

        //Загрузка
        final ProgressDialog loading = ProgressDialog.show(this,"Загрузка",
                "Пожалуйста подождите...",true,false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Когда получили данные отключаем показ загрузки
                        loading.dismiss();
                        parseData(response);
                    }
                },

                //Если случилась ошибка при загрузке
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(R.string.builder_title);
                        builder.setMessage(R.string.builder_message);
                        builder.setCancelable(false);
                        builder.setNegativeButton(R.string.title_negative_button,
                                new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                loading.dismiss();
                                getData();
                            }

                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });

        //Отправляем запрос в очередь
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void parseData(JSONArray array){

        for(int i = 0; i<array.length(); i++) {
            SingerMark singerMark = new SingerMark();
            JSONObject json, json_image;

            try {
                ArrayList<String> genres = new ArrayList<String>();
                json = array.getJSONObject(i);

                json_image = json.getJSONObject(Config.TAG_IMAGE);
                singerMark.setImageSmallUrl(json_image.getString(Config.TAG_SMALL_IMAGE));
                singerMark.setImageBigUrl(json_image.getString(Config.TAG_BIG_IMAGE));

                JSONArray jsonArray = json.getJSONArray(Config.TAG_GENRES);
                for(int j=0; j<jsonArray.length(); j++){
                    //Проверяем последнее ли это слово
                    if(j!=jsonArray.length()-1){
                        //Добавляем разделитель
                        genres.add(((String)jsonArray.get(j))+", ");
                    }
                    else{
                        genres.add(((String)jsonArray.get(j)));
                    }

                }
                singerMark.setGenres(genres);

                //Преобразуем строку в байты и приводим все к UTF-8
                byte name_byte[] = json.getString(Config.TAG_NAME).getBytes("ISO-8859-1");
                String name_value = new String(name_byte, "UTF-8");
                singerMark.setName(name_value);

                singerMark.setTracks(json.getString(Config.TAG_TRACKS));
                singerMark.setAlbums(json.getString(Config.TAG_ALBUMS));

                //Проверяем на наличие поля link
                if(json.has(Config.TAG_LINK)){
                    singerMark.setLink(json.getString(Config.TAG_LINK));
                }
                else{
                    singerMark.setLink("null");
                }

                //Преобразуем строку в байты и приводим все к UTF-8
                byte description_byte[] = json.getString(Config.TAG_DESCRIPTION).getBytes("ISO-8859-1");
                String description_value = new String(description_byte, "UTF-8");
                String sub=description_value.substring(0, 1).toUpperCase();
                description_value=description_value.replaceFirst(description_value.substring(0, 1), sub);

                singerMark.setDescription(description_value);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            SingerList.add(singerMark); //Добавяем в ArrayList

        }

        adapter = new CardAdapter(SingerList, this);

        //Добавляем адаптер к recyclerView
        recyclerView.setAdapter(adapter);
    }

    //Если нажали два раза на кнопку назад, делаем задержку в 2 секунды
    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        }
        else {
            Toast.makeText(MainActivity.this, R.string.toast_back_pressed, Toast.LENGTH_SHORT).show();
            backPressed = System.currentTimeMillis();
        }
    }

    //Поиск
    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase().trim(); //Уменьшаем до нижнего регистра и убираем пробелы

            final List<SingerMark> FilteredList = new ArrayList<>();

            for (int i = 0; i < SingerList.size(); i++) {
                SingerMark singerMark =SingerList.get(i);

                final String text = singerMark.getName().toLowerCase().trim();

                if (text.contains(query)) {
                    FilteredList.add(singerMark);
                }
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapter = new CardAdapter(FilteredList, MainActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();  //Обновляем адаптер
            return true;

        }
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Добавляем searchview
        getMenuInflater().inflate(R.menu.menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        search = (SearchView) searchMenuItem.getActionView();
        search.setOnQueryTextListener(listener);
        return true;
    }

}



