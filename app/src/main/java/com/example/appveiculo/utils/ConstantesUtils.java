package com.example.appveiculo.utils;

public class ConstantesUtils {
    public interface ACTIVY_IDENTITY{
        public static final String MODO="MODO";
        public static final int LIST_VEICULOS_NOVO=1;
        public static final int LIST_VEICULOS_ALTERAR=2;
    }

    public interface PARAMETROS{
        public static final String VEICULO_DTO="veiculo_dto";
    }

    public interface SHARED{
        public static final String ARQUIVO_LISTAS="com.example.appveiculo.LIST_ORDN";

        public static final String ORDENACAO_LISTA_VEICULO="ORDN_LIST_VEIC";
    }

    public interface VEICULOS_DB{
        public static final String TABLE_NAME="veiculos";
        public interface COLUNAS{
            public static final String
                             ID="id",
                             TIPO="tipo",
                             NOME="nome",
                             KM_ATUAL="km_atual",
                             COR="cor",
                             status="STATUS";
        }
    }
}
