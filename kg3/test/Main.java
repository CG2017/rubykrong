package test;

import frame.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        final MainFrame[] mf = new MainFrame[1];
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mf[0] = new MainFrame();
            }
        });

    }
}
