package zombienation;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

//Board is a JPanel and uses action listener for key inputs
public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Survivor survivor;
    private ArrayList zombies;
    private boolean ingame;
    private int B_WIDTH;
    private int B_HEIGHT;

    //All the locations for zombie spawning
    private int[][] pos = {
        {2380, 29}, {2500, 59}, {1380, 89},
        {780, 109}, {580, 139}, {680, 239},
        {790, 259}, {760, 50}, {790, 150},
        {980, 209}, {560, 45}, {510, 70},
        {930, 159}, {590, 80}, {530, 60},
        {940, 59}, {990, 30}, {920, 200},
        {900, 259}, {660, 50}, {540, 90},
        {810, 220}, {860, 20}, {740, 180},
        {820, 128}, {490, 170}, {700, 30}
     };

    public Board() {

        addKeyListener(new TAdapter());
        setFocusable(true); // Makes sure all actions are read by this program
        setBackground(Color.GRAY);

        setDoubleBuffered(true); // Draws in memory first, once complete offscreen buffer will be copied to the screen
        ingame = true;

        setSize(400, 400); //Size of JPanel

        survivor = new Survivor();

        initZombies();

        timer = new Timer(5, this); //5ms delay
        timer.start();
    }

    //addNotify() - This method is often used for various initialization tasks
    public void addNotify() {
        super.addNotify();
        B_WIDTH = getWidth();
        B_HEIGHT = getHeight();
    }

    public void initZombies() {
        zombies = new ArrayList();

        //To increase the amount of zombies, add new spawn points to pos
        for (int i=0; i<pos.length; i++ ) {
            zombies.add(new Zombie(pos[i][0], pos[i][1]));
        }
    }


    public void paint(Graphics g) {
        super.paint(g);

        if (ingame) {

            Graphics2D g2D = (Graphics2D)g;

            if (survivor.isVisible())
                g2D.drawImage(survivor.getImage(), survivor.getX(), survivor.getY(),
                              this);

            ArrayList bs = survivor.getBullets();

            for (int i = 0; i < bs.size(); i++) {
                Bullet b = (Bullet)bs.get(i);
                g2D.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }

            for (int i = 0; i < zombies.size(); i++) {
                Zombie z = (Zombie)zombies.get(i);
                if (z.isVisible())
                    g2D.drawImage(z.getImage(), z.getX(), z.getY(), this);
            }

            g2D.setColor(Color.WHITE);
            g2D.drawString("Zombies left: " + zombies.size(), 5, 15);


        } else {
            String msg = "Game Over";
            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metr = this.getFontMetrics(small);

            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2,
                         B_HEIGHT / 2);
        }

        Toolkit.getDefaultToolkit().sync(); //This must be done on Linux machines to make animation smooth
        g.dispose();
    }


    public void actionPerformed(ActionEvent e) {

        if (zombies.size()==0) {
            ingame = false;
        }

        ArrayList bs = survivor.getBullets();

        for (int i = 0; i < bs.size(); i++) {
            Bullet b = (Bullet) bs.get(i);
            if (b.isVisible())
                b.move();
            else bs.remove(i);
        }

        for (int i = 0; i < zombies.size(); i++) {
            Zombie z = (Zombie) zombies.get(i);
            if (z.isVisible())
                z.move();
            else zombies.remove(i);
        }

        survivor.move();
        checkCollisions();
        repaint();
    }

    public void checkCollisions() {

        Rectangle r3 = survivor.getBounds();

        for (int j = 0; j<zombies.size(); j++) {
            Zombie z = (Zombie) zombies.get(j);
            Rectangle r2 = z.getBounds();

            if (r3.intersects(r2)) {
                survivor.setVisible(false);
                z.setVisible(false);
                ingame = false;
            }
        }

        ArrayList bs = survivor.getBullets();

        for (int i = 0; i < bs.size(); i++) {
            Bullet b = (Bullet) bs.get(i);

            Rectangle r1 = b.getBounds();

            for (int j = 0; j<zombies.size(); j++) {
                Zombie z = (Zombie) zombies.get(j);
                Rectangle r2 = z.getBounds();

                if (r1.intersects(r2)) {
                    b.setVisible(false);
                    z.setVisible(false);
                }
            }
        }
    }


    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            survivor.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            survivor.keyPressed(e);
        }
    }
}