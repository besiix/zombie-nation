package zombienation;

import javax.swing.JFrame;

public class Main extends JFrame {

    public Main() {
        add(new Board()); //Adds the JPanel "Board"

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null); //Centers the frame
        setTitle("Zombie Nation");
        setResizable(false);
        setVisible(true); //Makes visible (nulls use of show() and hide()
    }

    public static void main(String[] args) {
        new Main();
    }
}