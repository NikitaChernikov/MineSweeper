package sweeper;

import java.awt.*;

public class Menu {
    public void render (Graphics g){
        Font fnt0 = new Font("arial", Font.BOLD,50);
        g.setFont(fnt0);
        g.setColor(Color.BLACK);
        g.drawString("MINESWEEPER", 150,150);
    }
}
