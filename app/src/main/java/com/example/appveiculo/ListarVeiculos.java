package com.example.appveiculo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.example.appveiculo.adapters.VeiculosAdapter;
import com.example.appveiculo.daos.db.AppVeiculoDataBase;
import com.example.appveiculo.model.dto.VeiculoDto;
import com.example.appveiculo.utils.BackgroundTask;
import com.example.appveiculo.utils.ConstantesUtils;
import com.example.appveiculo.utils.DialogsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ListarVeiculos extends AppCompatActivity {
    private ListView listViewVeiculos;
    private ArrayList<VeiculoDto> listVeiculos;
    private VeiculosAdapter adapter;
    private Spinner spinOrdenacao;

    private View viewSelecionada;
    private ActionMode actionMode;
    private int posicaoSelecionada=-1;

    private static final int CODIGO=0;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.veiculo_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.mAlterarVeiculo:
                    alterarVeiculo();
                    actionMode.finish();
                    return true;

                case R.id.mExcluirVeiculo:
                    excluirVeiculo();
                    actionMode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (viewSelecionada != null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode         = null;
            viewSelecionada    = null;

            listViewVeiculos.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_veiculos);

        setTitle(getString(R.string.lista_de_veiculos));

        instanciarCampos();

        carregarLista();

        jogarNaTela();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_veiculos, menu);
        return true;
    }

    private void alterarVeiculo(){
        VeiculoDto veiculo = listVeiculos.get(posicaoSelecionada);

        MainActivity.alterarVeiculo(this, veiculo);
    }

    private void excluirVeiculo() {
        VeiculoDto dto = listVeiculos.get(posicaoSelecionada);
        String mensagem = getString(R.string.pergunta_exclusao) + "\n" + dto.getId() + " - " + dto.getNome();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                new BackgroundTask(ListarVeiculos.this) {

                                    @Override
                                    public void doInBackground() {
                                        AppVeiculoDataBase dataBase = AppVeiculoDataBase.getDatabase(ListarVeiculos.this);

                                        dataBase.veiculoDao().delete(dto.toEntity(getResources()));
                                    }

                                    @Override
                                    public void onPostExecute() {
                                        listVeiculos.remove(posicaoSelecionada);
                                        jogarNaTela();
                                        adapter.notifyDataSetChanged();
                                    }
                                }.execute();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(ListarVeiculos.this,
                                        getString(R.string.operacao_cancelada),
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };

        DialogsUtils.confirmaAcao(this, mensagem, listener);
    }

    private void jogarNaTela() {
        adapter = new VeiculosAdapter(this, listVeiculos);
        listViewVeiculos.setAdapter(adapter);
    }

    private void carregarLista() {
        //alternativa ao asynktask depreciado
        new BackgroundTask(ListarVeiculos.this) {
            @Override
            public void doInBackground() {
                AppVeiculoDataBase database = AppVeiculoDataBase.getDatabase(ListarVeiculos.this);

                listVeiculos = database.veiculoDao()
                                        .listAll()
                                        .stream()
                                        .map(entity ->{
                                            System.out.println(entity.getNome()+" "+entity.getId());
                                            return VeiculoDto.toDto(entity, getResources()); })
                                        .collect(Collectors.toCollection(ArrayList::new));
            }

            @Override
            public void onPostExecute() {
                jogarNaTela();
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void instanciarCampos() {
        listViewVeiculos = (ListView) findViewById(R.id.listaDeVeculos);

        listViewVeiculos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                posicaoSelecionada = position;
                alterarVeiculo();
            }
        });

        listViewVeiculos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewVeiculos.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view,
                                                   int position,
                                                   long id) {

                        if (actionMode != null){
                            return false;
                        }

                        posicaoSelecionada = position;

                        view.setBackgroundColor(Color.LTGRAY);

                        viewSelecionada = view;

                        listViewVeiculos.setEnabled(false);

                        actionMode = startSupportActionMode(mActionModeCallback);

                        return true;
                    }
                });

        spinOrdenacao = findViewById(R.id.spinOrdenacao);

        spinOrdenacao.setSelection(getOrdenacaoShared());

        spinOrdenacao.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        salvarOrdenacao(position);

                        ordenar();

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

    public void adicionarVeiculo(){
        MainActivity.novoVeiculo(this);
    }

    public void sobre(){
        Sobre.abreSobre(this);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Bundle bundle = data.getExtras();

            VeiculoDto veiculo = (VeiculoDto) bundle.getSerializable(ConstantesUtils.PARAMETROS.VEICULO_DTO);

            if (requestCode == ConstantesUtils.ACTIVY_IDENTITY.LIST_VEICULOS_NOVO) {
                veiculo.setId(listVeiculos.size() + 1);
                listVeiculos.add(veiculo);
            } else if (requestCode == ConstantesUtils.ACTIVY_IDENTITY.LIST_VEICULOS_ALTERAR) {
                listVeiculos.set(listVeiculos.indexOf(veiculo), veiculo);
            }

            ordenar();
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mAdicionarVeiculo:
                adicionarVeiculo();
                return true;
            case R.id.mVeicSobre:
                sobre();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int getOrdenacaoShared(){
        int opcao = 0;
        SharedPreferences shared = getSharedPreferences(ConstantesUtils.SHARED.ARQUIVO_LISTAS,
                Context.MODE_PRIVATE);

        opcao = shared.getInt(ConstantesUtils.SHARED.ORDENACAO_LISTA_VEICULO, opcao);

        return opcao;
    }

    private void salvarOrdenacao(int index){
        SharedPreferences shared = getSharedPreferences(ConstantesUtils.SHARED.ARQUIVO_LISTAS,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(ConstantesUtils.SHARED.ORDENACAO_LISTA_VEICULO, index);

        editor.commit();
    }

    private void ordenar(){
        if(listVeiculos!=null && listVeiculos.size()>1){
            int ordenacao = spinOrdenacao.getSelectedItemPosition();

            Collections.sort(listVeiculos, new Comparator<VeiculoDto>() {
                @Override
                public int compare(VeiculoDto t1, VeiculoDto t2) {
                    if(ordenacao==CODIGO){
                        return t1.getId().compareTo(t2.getId());
                    }
                    else{
                        return t1.getNome().compareTo(t2.getNome());
                    }
                }
            });
        }
    }
}