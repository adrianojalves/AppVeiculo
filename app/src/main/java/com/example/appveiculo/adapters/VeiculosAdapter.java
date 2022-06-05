package com.example.appveiculo.adapters;

import com.example.appveiculo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appveiculo.model.dto.VeiculoDto;

import java.util.ArrayList;

public class VeiculosAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<VeiculoDto> items;

    public VeiculosAdapter(Context context, ArrayList<VeiculoDto> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items!=null ? items.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return items!=null? items.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.layout_list_veiculos,
                            parent,
                            false);
        }

        VeiculoDto veiculo = (VeiculoDto) getItem(position);

        TextView nome = (TextView)
                view.findViewById(R.id.nome);
        TextView id = (TextView)
                view.findViewById(R.id.id);
        TextView cor = (TextView)
                view.findViewById(R.id.cor);
        TextView status = (TextView)
                view.findViewById(R.id.statusList);
        TextView kmAtual = (TextView)
                view.findViewById(R.id.kmAtual);
        TextView tipo = (TextView)
                view.findViewById(R.id.tipo);

        nome.setText(veiculo.getNome());
        id.setText(veiculo.getId().toString());
        cor.setText(veiculo.getCor());
        status.setText(veiculo.getStatus()?R.string.ativo:R.string.inativo);
        kmAtual.setText(veiculo.getKmAtual().toString());
        tipo.setText(veiculo.getTipo());

        return view;
    }
}
