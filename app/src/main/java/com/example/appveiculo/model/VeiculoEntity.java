package com.example.appveiculo.model;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.appveiculo.utils.ConstantesUtils;

@Entity(tableName = ConstantesUtils.VEICULOS_DB.TABLE_NAME)
public class VeiculoEntity {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @NonNull
    private Integer tipo;

    @NonNull
    private String nome;

    @ColumnInfo(name = ConstantesUtils.VEICULOS_DB.COLUNAS.KM_ATUAL)
    @IntRange(from=0,to=999999)
    private Integer kmAtual;

    @NonNull
    private Integer cor;

    @NonNull
    private Boolean status;

    public VeiculoEntity() {
    }

    public VeiculoEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getKmAtual() {
        return kmAtual;
    }

    public void setKmAtual(Integer kmAtual) {
        this.kmAtual = kmAtual;
    }

    public Integer getCor() {
        return cor;
    }

    public void setCor(Integer cor) {
        this.cor = cor;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
