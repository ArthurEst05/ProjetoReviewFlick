package com.example.servico;

import java.awt.EventQueue;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TelaInicial telaInicial = new TelaInicial();
                telaInicial.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
 