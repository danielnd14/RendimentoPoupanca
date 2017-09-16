/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.SwingUtilities;

/**
 *
 * @author daniel
 */
public class RendimentoPoupanca {

    /**
     * @param args he command line arguments
     */
    public static void main(String[] args) {
        // TODO code ap plication logic here
        GUI tela = new GUI();
        SwingUtilities.invokeLater(()->{
            tela.setVisible(true);
        });

        
        
    }
}
    

