package com.example.appveiculo.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.appveiculo.model.VeiculoEntity;
import com.example.appveiculo.utils.ConstantesUtils;

import java.util.List;

@Dao
public interface VeiculoDao {

    @Insert
    long insert(VeiculoEntity veiculo);

    @Delete
    void delete(VeiculoEntity veiculo);

    @Update
    void update(VeiculoEntity veiculo);

    @Query("SELECT * FROM "+ ConstantesUtils.VEICULOS_DB.TABLE_NAME +" WHERE "+ConstantesUtils.VEICULOS_DB.COLUNAS.ID+" = :id")
    VeiculoEntity getById(Integer id);

    @Query("SELECT * FROM "+ConstantesUtils.VEICULOS_DB.TABLE_NAME+" ORDER BY "+ConstantesUtils.VEICULOS_DB.COLUNAS.NOME+" ASC")
    List<VeiculoEntity> listAll();
}