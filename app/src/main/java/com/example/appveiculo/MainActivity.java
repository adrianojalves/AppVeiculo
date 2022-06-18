package com.example.appveiculo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appveiculo.daos.db.AppVeiculoDataBase;
import com.example.appveiculo.model.dto.VeiculoDto;
import com.example.appveiculo.utils.BackgroundTask;
import com.example.appveiculo.utils.ConstantesUtils;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText nome;
    private EditText kmAtual;
    private RadioGroup radioTipo;
    private RadioButton radioCarro;
    private RadioButton radioMoto;
    private CheckBox status;
    private Spinner spinCores;
    private String msgErro;

    private VeiculoDto veiculo;
    private int modo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            modo = bundle.getInt(ConstantesUtils.ACTIVY_IDENTITY.MODO, ConstantesUtils.ACTIVY_IDENTITY.LIST_VEICULOS_NOVO);

            if (modo == ConstantesUtils.ACTIVY_IDENTITY.LIST_VEICULOS_NOVO){
                setTitle(getString(R.string.novo_veiculo));
            }else{

                setTitle(getString(R.string.edicao_veiculo));
            }
        }

        instanciarCampos(bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cad_veic, menu);
        return true;
    }

    private void salvar(){
        if(validarCampos()){
            System.out.println("validou campos");
            new BackgroundTask(MainActivity.this){

                @Override
                public void doInBackground() {
                    AppVeiculoDataBase database = AppVeiculoDataBase.getDatabase(MainActivity.this);

                    if(veiculo.getId()==null) {
                        Long id = database.veiculoDao().insert(veiculo.toEntity(getResources()));
                        veiculo.setId(id.intValue());
                    }
                    else{
                        database.veiculoDao().update(veiculo.toEntity(getResources()));
                    }
                }

                @Override
                public void onPostExecute() {
                    Intent intent = new Intent();
                    intent.putExtra(ConstantesUtils.PARAMETROS.VEICULO_DTO, veiculo);

                    setResult(Activity.RESULT_OK, intent);

                    finish();
                }
            }.execute();
        }
        else{
            Toast.makeText(this,
                    msgErro,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancelar();
    }

    private boolean validarCampos(){
        boolean validado = true;
        if(veiculo == null){
            veiculo = new VeiculoDto();
        }

        if(nome.getText().toString().trim().isEmpty()){
            validado=false;
            nome.requestFocus();
            msgErro = getString(R.string.nome_obrigatorio);
        }
        else if(kmAtual.getText().toString().trim().isEmpty()){
            validado=false;
            kmAtual.requestFocus();
            msgErro = getString(R.string.km_atual_obrigatoria);
        }
        else if(kmAtual.getText().toString().trim().length()>6){
            validado=false;
            kmAtual.requestFocus();
            msgErro = getString(R.string.km_maior_maximo);
        }
        else{
            Integer km = new Integer(kmAtual.getText().toString());
            if(km<0) {
                msgErro = getString(R.string.km_negativa);
                kmAtual.requestFocus();
                validado = false;
            }
        }

        if(validado){
            veiculo.setNome(nome.getText().toString());
            switch (radioTipo.getCheckedRadioButtonId()) {

                case R.id.radioCarro:
                    veiculo.setTipo(getString(R.string.carro));
                    break;

                case R.id.radioMoto:
                    veiculo.setTipo(getString(R.string.moto));
                    break;

                default:
                    Toast.makeText(this,
                            getString(R.string.tipo_nao_selecionado),
                            Toast.LENGTH_SHORT).show();
                    return false;
            }

            veiculo.setStatus(status.isChecked());
            veiculo.setCor((String)spinCores.getSelectedItem());
            veiculo.setKmAtual(new Integer(kmAtual.getText().toString()));
        }

        return validado;
    }

    private void limparCampos(){
        nome.setText("");
        kmAtual.setText("");
        radioCarro.setChecked(true);
        status.setChecked(true);
        spinCores.setSelection(0);

        Toast.makeText(this,
                getString(R.string.campos_limpos),
                Toast.LENGTH_LONG).show();
    }

    private void instanciarCampos(Bundle bundle){
        nome = findViewById(R.id.txtNomeVeiculo);
        kmAtual = findViewById(R.id.txtKmAtual);
        radioTipo = findViewById(R.id.groupVeiculos);
        status = findViewById(R.id.status);
        spinCores = findViewById(R.id.spinCores);
        radioCarro = findViewById(R.id.radioCarro);
        radioMoto = findViewById(R.id.radioMoto);

        if(bundle!=null){
            VeiculoDto veiculo = (VeiculoDto) bundle.getSerializable(ConstantesUtils.PARAMETROS.VEICULO_DTO);

            if(veiculo!=null) {
                nome.setText(veiculo.getNome());
                kmAtual.setText(veiculo.getKmAtual().toString());

                if (veiculo.getTipo().equals(getString(R.string.carro))) {
                    radioCarro.setChecked(true);
                } else {
                    radioMoto.setChecked(true);
                }

                if (veiculo.getStatus()) {
                    status.setChecked(true);
                }
                else{
                    status.setChecked(false);
                }

                List<String> cores = Arrays.asList(getResources().getStringArray(R.array.nomes_cores));
                spinCores.setSelection(cores.indexOf(veiculo.getCor()));
                this.veiculo = veiculo;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mSalvarVeiculo:
                salvar();
                return true;
            case android.R.id.home:
                cancelar();
                return true;
            case R.id.mLimparVeic:
                limparCampos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void novoVeiculo(AppCompatActivity activity){
        Intent intent = new Intent(activity, MainActivity.class);

        intent.putExtra(ConstantesUtils.ACTIVY_IDENTITY.MODO, ConstantesUtils.ACTIVY_IDENTITY.LIST_VEICULOS_NOVO);

        activity.startActivityForResult(intent, ConstantesUtils.ACTIVY_IDENTITY.LIST_VEICULOS_NOVO);
    }

    public static void alterarVeiculo(AppCompatActivity activity, VeiculoDto veiculo){
        Intent intent = new Intent(activity, MainActivity.class);

        intent.putExtra(ConstantesUtils.ACTIVY_IDENTITY.MODO, ConstantesUtils.ACTIVY_IDENTITY.LIST_VEICULOS_ALTERAR);
        intent.putExtra(ConstantesUtils.PARAMETROS.VEICULO_DTO, veiculo);

        activity.startActivityForResult(intent, ConstantesUtils.ACTIVY_IDENTITY.LIST_VEICULOS_ALTERAR);
    }
}