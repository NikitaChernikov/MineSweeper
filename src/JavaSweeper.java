import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import sweeper.Box;
import sweeper.Coord;
import sweeper.Game;
import sweeper.Ranges;

public class JavaSweeper extends JFrame
{
    private Game game;
    private JPanel panel;
    private JLabel label;

    private enum STATE{
        MENU,
        GAME
    };

    private STATE State = STATE.MENU;

    private final int COLS = 11;
    private final int ROWS = 11;
    private  int BOMBS = 13;
    private final int IMAGE_SIZE = 50;

    public static void main(String[] args)
    {
        new JavaSweeper();
    }

    private JavaSweeper()
    {
            game = new Game(COLS, ROWS, BOMBS);
            game.start();
            setImages();
            initLabel();
            initPanel();
            initFrame();
            if (State == STATE.MENU){
                getImage("start");
            }
    }

    private void initLabel()
    {
        label = new JLabel("Welcome!");
        add(label,BorderLayout.SOUTH);
    }

    private void initPanel()
    {
        panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g) {
                if (State == STATE.GAME) {
                    super.paintComponent(g);
                    for (Coord coord : Ranges.getAllCoords()) {
                        g.drawImage((Image) game.getBox(coord).image,
                                coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, this);
                    }
                }
                else if (State == STATE.MENU){
                    super.paintComponent(g);
                    g.drawImage(getImage("start"),(COLS* IMAGE_SIZE)/4,(ROWS* IMAGE_SIZE)/4,this);
                }
            }
        };
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int x = e.getX() / IMAGE_SIZE;
                    int y = e.getY() / IMAGE_SIZE;
                    Coord coord = new Coord(x, y);
                    if (State == STATE.GAME) {
                        if (e.getButton() == MouseEvent.BUTTON1)
                            game.pressLeftButton(coord);
                        if (e.getButton() == MouseEvent.BUTTON3)
                            game.pressRightButton(coord);
                        if (e.getButton() == MouseEvent.BUTTON2)
                            game.start();
                        label.setText(getMessage());
                        panel.repaint();
                    }
                    else if (State == STATE.MENU){
                            State = STATE.GAME;
                            panel.repaint();
                    }
                }
            });
        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGE_SIZE,
                Ranges.getSize().y * IMAGE_SIZE));
        add (panel);
    }

    private String getMessage()
    {
        switch (game.getState())
        {
            case BOMBED:return "YOU LOSE! SUSPENDED!";
            case WINNER: return "CONGRATULATIONS!";
            default: return " ";
        }
    }

    private void initFrame()
    {

        String separator = File.separator;

        JMenu file = new JMenu("file");
        JMenu about = new JMenu("about");

        JMenuItem rules = about.add(new JMenuItem("rules"));
        rules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new java.io.File("res" + separator + "Rules"+ separator + "Rules.txt"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JMenu options = new JMenu("Options");
        file.add(options);
        JMenuItem easy = options.add(new JMenuItem("easy"));
        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.back.stop();
                BOMBS = 5;
                game = new Game(COLS, ROWS, BOMBS);
                game.start();
                setImages();
                initLabel();
                initPanel();
                initFrame();
                panel.repaint();
            }
        });
        JMenuItem medium = options.add(new JMenuItem("medium"));
        medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.back.stop();
                BOMBS = 12;
                game = new Game(COLS, ROWS, BOMBS);
                game.start();
                setImages();
                initLabel();
                initPanel();
                initFrame();
                panel.repaint();
            }
        });
        JMenuItem hard = options.add(new JMenuItem("hard"));
        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.back.stop();
                BOMBS = 20;
                game = new Game(COLS, ROWS, BOMBS);
                game.start();
                setImages();
                initLabel();
                initPanel();
                initFrame();
                panel.repaint();
            }
        });
        file.addSeparator();
        JMenuItem exit = file.add(new JMenuItem("exit"));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exit.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));



        JMenuBar jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);
        revalidate();

        jMenuBar.add(file);
        jMenuBar.add(about);


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cheat Sweeper");
        setResizable(false);
        setVisible(true);
        setIconImage(getImage("icon"));
        pack();
        setLocationRelativeTo(null);
    }

    private void setImages()
    {
        for (Box box : Box.values())
            box.image = getImage(box.name().toLowerCase());
    }

    private Image getImage (String name)
    {
        String separator = File.separator;
        String filename = separator + "img"+ separator + name.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }

}
