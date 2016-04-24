package com.example.boris.yandextest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Boris on 03.04.2016.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{

    public Context context;
    public String genres="";

    //Лист с певцами
    public List<SingerMark> SingerList;

    //Конструктор
    public CardAdapter(List<SingerMark> SingerList, Context context){

        //Берем всех певцов и загоняем их в лист
        this.SingerList = SingerList;
        this.context = context;
    }

    public class MusicCard extends ViewHolder implements View.OnClickListener{
        TextView textViewName, textViewTracks, textViewAlbums, textViewGenres;
        ImageView imageView;

        public MusicCard(View view, Context context) {
            super(view, CardAdapter.this.SingerList);
            view.setOnClickListener(this);
            this.context=context;
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
            this.textViewName = (TextView) view.findViewById(R.id.textViewName);
            this.textViewTracks = (TextView) view.findViewById(R.id.textViewTracks);
            this.textViewAlbums = (TextView) view.findViewById(R.id.textViewAlbums);
            this.textViewGenres = (TextView) view.findViewById(R.id.textViewGenres);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Создаем view элемент и присваеваем ему LayoutInflater с layout music_card
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_card, parent, false);

        return new MusicCard(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Обнуляем строку с жанрами
        genres="";
        SingerMark singerMark =  SingerList.get(position);

        MusicCard viewHolder = (MusicCard) holder;

        //С помощью библиотеки Picasso загружаем в каждую cardview картинку
        Picasso.with(context)
                .load(singerMark.getImageSmallUrl())
                .error(android.R.drawable.ic_dialog_alert)
                .into(viewHolder.imageView);

        //Присваивем textView к каждой карточке, String берем из list
        viewHolder.textViewName.setText(singerMark.getName());
        viewHolder.textViewTracks.setText(singerMark.getTracks());
        viewHolder.textViewAlbums.setText(singerMark.getAlbums());

        for (int i = 0; i< singerMark.getGenres().size(); i++){
            genres = genres + singerMark.getGenres().get(i);
        }

        viewHolder.textViewGenres.setText(genres);
    }

    @Override
    public int getItemCount() {
        return SingerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Context context;
        public List<SingerMark> SingerList;

        public ViewHolder(View v, List<SingerMark> SingerList) {
            super(v);
            v.setOnClickListener(this);
            this.SingerList=SingerList;
        }

        //Обрабатываем нажатие
        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();

            SingerMark singerMark =SingerList.get(position);

            //Посылаем extra в другой активити
            Intent intent= new Intent(this.context, InformationActivity.class);
            intent.putExtra("imageBigUrl", singerMark.getImageBigUrl());
            intent.putExtra("name", singerMark.getName());
            intent.putExtra("description", singerMark.getDescription());
            intent.putExtra("tracks", singerMark.getTracks());
            intent.putExtra("albums", singerMark.getAlbums());
            intent.putExtra("link", singerMark.getLink());

            genres="";
            for (int i = 0; i< singerMark.getGenres().size(); i++){
                genres = genres + singerMark.getGenres().get(i);
            }
            intent.putExtra("genres", genres);

            this.context.startActivity(intent);
        }

    }

}
