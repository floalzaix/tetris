package fr.eseo.e3.poo.projet.blox;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.nd4j.linalg.factory.Nd4j;

import fr.eseo.e3.poo.projet.blox.modele.UsineDePiece;
import fr.eseo.e3.poo.projet.blox.modele.ai.IA;
import fr.eseo.e3.poo.projet.blox.vue.VueIA;

public class FallingBloxIATrainer {
    public static void main(String[] args) {
        final boolean VUE = true;
        
        System.out.println(Nd4j.getExecutioner().getClass().getName());
        
        // IA
        IA ia = new IA(6, 15, UsineDePiece.CYCLIC, true);
        
        
        if (VUE) {
            JFrame frame = new JFrame("IA Training ...");
            
            // Vue IA
            VueIA vueIA = new VueIA(ia, frame);
            
            // Config de la frame
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.add(vueIA);
            frame.pack();
            frame.setResizable(false);
        }
        
        // Taining
        ia.train(10000);
    }
}
