package com.example.appveiculo.daos.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.appveiculo.daos.VeiculoDao;
import com.example.appveiculo.model.VeiculoEntity;

import java.util.concurrent.Executors;

@Database(entities = {VeiculoEntity.class}, version = 1)
public abstract class AppVeiculoDataBase extends RoomDatabase {

    public abstract VeiculoDao veiculoDao();

    private static AppVeiculoDataBase instance;

    public static AppVeiculoDataBase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (AppVeiculoDataBase.class) {
                if (instance == null) {
                    Builder builder =  Room.databaseBuilder(context,
                                                            AppVeiculoDataBase.class,
                                                      "appVeiculo.db");

                    builder.addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                        }
                    });

                    instance = (AppVeiculoDataBase) builder.build();
                }
            }
        }

        return instance;
    }
}