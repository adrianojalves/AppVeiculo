package com.example.appveiculo.model.dto;

import java.io.Serializable;
import java.util.Objects;

public class VeiculoDto implements Serializable {
    public static final long serialVersionUID=1L;

    private Integer id;
    private String tipo;
    private String nome;
    private Integer kmAtual;
    private String cor;
    private Boolean status;

    public VeiculoDto() {

    }

    public VeiculoDto(Integer id, String tipo, String nome, Integer kmAtual, String cor, Boolean status) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
        this.kmAtual = kmAtual;
        this.cor = cor;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
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

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VeiculoDto that = (VeiculoDto) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
