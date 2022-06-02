import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ListIterator;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class Canvas extends JApplet {


    private int height;
    private int width;
    private CopyOnWriteArrayList<Square> squares;

    public Canvas() {
        JFrame f = new JFrame("Canvas");
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        height = 800;
        width = 800;
        f.setSize(width, height);
        f.getContentPane().add(this);
        squares = new CopyOnWriteArrayList<Square>();
        f.setVisible(true);
    }

    public Canvas(int h, int w) {
        JFrame f = new JFrame("Canvas");
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        height = h;
        width = w;
        f.setSize(width, height);
        f.getContentPane().add(this);
        squares = new CopyOnWriteArrayList<Square>();
        f.setVisible(true);
    }

    public void paint(Graphics g) {
        Image buffer = createImage(width,height);
        drawToBuffer(buffer.getGraphics());
        g.drawImage(buffer,0,0,null);
    }

    public void drawToBuffer(Graphics g){
        ListIterator<Square> sqrItr = squares.listIterator();

        while (sqrItr.hasNext()) {
            Square curSquare = sqrItr.next();
            g.setColor(curSquare.getColor());
            g.fillRect(curSquare.getXPos(), curSquare.getYPos(), curSquare.getWidth(), curSquare.getHeight());
        }
    }

    public void drawShape(Square sqr) { squares.add(sqr); }

    public void clear(){
        squares.clear();
    }


}

