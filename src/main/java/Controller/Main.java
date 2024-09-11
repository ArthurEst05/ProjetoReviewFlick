package Controller;


import java.awt.EventQueue;

import View.TelaInicial;

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
 