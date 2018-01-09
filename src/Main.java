import java.util.*;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Main
{
    static int count;
    static int dScrD = 750, hScrD = 100;
    static int wVaiw = 400, hVaiw = 750;
    static int xPoint = 0, yPoint = 100;

    public static void main(String[] args) throws InterruptedException
    {
        count = 0;
        Figures balls = new Figures();
        while (true)
        {
            hVaiw = balls.getSize().height;
            wVaiw = balls.getSize().width;
            Thread.sleep(500);
        }
    }
}

class Figures extends Frame implements Observer, ActionListener, ItemListener
{
    private LinkedList LL = new LinkedList();
    private Color col;
    private Frame f;
    private Button b;
    private Choice c, c2, c3;
    private TextField tf;
    private int det, i = 0;
    private String[] NameFig = new String[50];
    static int func;

    Figures()
    {
        this.addWindowListener(new WindowAdapter2());
        f = new Frame();
        f.setSize(new Dimension(Main.dScrD,Main.hScrD));
        f.setTitle("Контроль");
        f.setLayout(new GridLayout());
        f.addWindowListener(new WindowAdapter2());

        b = new Button("Пуск");
        b.setSize(new Dimension(10,10));
        b.setActionCommand("OK");
        b.addActionListener(this);
        f.add(b, new Point(40,40));

        b = new Button("Выбор цвета");
        b.setSize(new Dimension(10,10));
        b.setActionCommand("цвет");
        b.addActionListener(this);
        f.add(b, new Point(40,40));

        tf = new TextField();
        f.add(tf);

        b = new Button("Изменить");
        b.setSize(new Dimension(10,10));
        b.setActionCommand("ИЗМЕНИТЬ");
        b.addActionListener(this);
        f.add(b, new Point(40,80));

        c = new Choice();
        c.addItemListener(this);
        f.add(c, new Point(100, 40));

        c2 = new Choice();
        c2.addItem("Скорость = 1");
        c2.addItem("Скорость = 2");
        c2.addItem("Скорость = 3");
        c2.addItem("Скорость = 4");
        c2.addItem("Скорость = 5");
        c2.addItem("Скорость = 6");
        c2.addItemListener(this);
        f.add(c2, new Point(140,60));

        c3 = new Choice();
        c3.addItem("Круг");
        c3.addItem("Овал");
        c3.addItem("Треугольник");
        c3.addItem("Квадрат");
        c3.addItem("Прямоугольник");
        c3.addItemListener(this);
        f.add(c3, new Point(180,80));

        tf.setText("Имя фигуры...");
        f.setVisible(true);
        this.setSize(Main.hVaiw,Main.wVaiw);
        this.setVisible(true);
        this.setLocation(Main.xPoint, Main.yPoint);
    }

    public void update(Observable o, Object arg)
    {
        Figure ball = (Figure)arg;
        repaint();
    }

    public void paint (Graphics g)
    {
        if (!LL.isEmpty())
        {
            Font font = new Font("Arial", Font.BOLD|Font.ITALIC, 20);
            for (Object LL1 : LL)
            {
                Figure figure = (Figure) LL1;
                g.setColor(figure.col);
                switch (figure.fInd)
                {
                    case 0:
                        g.fillOval(figure.x, figure.y, Figure.BallSize, Figure.BallSize);
                        break;
                    case 1:
                        g.fillOval(figure.x, figure.y, Figure.dFigureSize, Figure.hFigureSize);
                        break;
                    case 2:
                        int[] arrayX = {figure.x, figure.x + Figure.BallSize /2, figure.x + Figure.BallSize};
                        int[] arrayY = {figure.y + Figure.BallSize, figure.y, figure.y + Figure.BallSize};
                        g.fillPolygon(arrayX, arrayY, 3);
                        break;
                    case 3:
                        g.fillRect(figure.x, figure.y, Figure.BallSize, Figure.BallSize);
                        break;
                    case 4:
                        g.fillRect(figure.x, figure.y, Figure.dFigureSize, Figure.hFigureSize);
                        break;
                }
                g.setColor(Color.black);
                g.setFont(font);
                g.drawString(figure.Name, figure.x + 50, figure.y);
            }
        }
    }
    public void itemStateChanged (ItemEvent iE) {}

    public void actionPerformed (ActionEvent aE)
    {
        String str = aE.getActionCommand();

        switch (c2.getSelectedIndex())
        {
            case 0:det = 1;break;
            case 1:det = 2;break;
            case 2:det = 3;break;
            case 3:det = 4;break;
            case 4:det = 5;break;
            case 5:det = 6;break;
        }
        switch (c3.getSelectedIndex())
        {
            case 0:Figure.FigureInd = 0;break;
            case 1:Figure.FigureInd = 1;break;
            case 2:Figure.FigureInd = 2;break;
            case 3:Figure.FigureInd = 3;break;
            case 4:Figure.FigureInd = 4;break;
        }
        if(str.equals("цвет"))
        {
            this.col = JColorChooser.showDialog(this, "Выбор цвета", Color.black);
        }
        if (str.equals ("OK"))
        {
            if (!tf.getText().equals("Имя фигуры...") && !tf.getText().equals(""))
            {
                NameFig[i] = tf.getText();
                Figure ball = new Figure(col, NameFig[i], det, Figure.FigureInd, func);
                LL.add(ball);
                ball.addObserver(this);
                Random rand = new Random();
                func = rand.nextInt(5);
                tf.setText("Имя фигуры...");
                c.addItem(NameFig[i]);
                i++;
            }
        }
        if (str.equals ("ИЗМЕНИТЬ"))
        {
            if(!LL.isEmpty())
            {
                for(Object LL1 : LL)
                {
                    Figure figure = (Figure)LL1;
                    if(figure.Name.equals(NameFig[c.getSelectedIndex()]))
                    {
                        figure.oName = NameFig[c.getSelectedIndex()];
                        figure.nDet = this.det;
                    }
                }
            }
        }
        repaint();
    }
}
class Figure extends Observable implements Runnable
{
    Thread thr;
    private boolean xplus;
    private boolean yplus;
    private int det, Xv, Yv;
    private int  wFigSize, hFigSize;
    String Name;
    static String oName = "";
    static int nDet;
    int fInd;
    int x; int y;
    Color col;
    static int BallSize = 50;
    static int dFigureSize = 100, hFigureSize = 50;
    static int FigureInd = 0;

    public Figure(Color col, String text, int speed, int figInd, int func)
    {
        xplus = true; yplus = true;
        x = 0; y = 30;
        Xv = func;
        Yv = 5 - func;
        det = speed;
        fInd = figInd;
        this.col = col;
        this.Name = text;
        if(fInd == 1 || fInd == 4)
        {hFigSize = 50;wFigSize = 100;}
        else
        {hFigSize = 50;wFigSize = 50;}
        Main.count++;
        thr = new Thread(this, Main.count + text);
        thr.start();
    }
    public void run()
    {
        while (true)
        {
            if (this.Name.equals(oName))
            {
                det = nDet;
                oName = "";
            }
            if(x>=Main.wVaiw - wFigSize) xplus = false;
            if(x<=-1) xplus = true;
            if(y>=Main.hVaiw - hFigSize) yplus = false;
            if(y<=35) yplus = true;
            if(xplus) x += det + Xv; else x -= det + Xv;
            if(yplus) y += det + Yv; else y -= det + Yv;
            setChanged();
            notifyObservers (this);
            try{Thread.sleep (200);}
            catch (InterruptedException e){}
        }
    }
}
class WindowAdapter2 extends WindowAdapter
{
    public void windowClosing(WindowEvent wE) {System.exit (0);}
}
