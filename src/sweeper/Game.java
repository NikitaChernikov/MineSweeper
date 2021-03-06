package sweeper;


import java.io.File;

public class Game
{
    private Bomb bomb;
    private Flag flag;


    public static Audio expl;
    public static Audio klick;
    public static Audio mark;
    public static Audio back;
    public static Audio win;

    private GameStat state;
    public GameStat getState()
    {
        return state;
    }

    public Game (int cols, int rows, int bombs)
    {
        Ranges.setSize(new Coord(cols, rows));
        bomb = new Bomb(bombs);
        flag = new Flag();

        String separator = File.separator;

        expl = new Audio("res" + separator + "Sounds" + separator + "explosion.wav",1.0);
        klick = new Audio("res" + separator + "Sounds" + separator + "klick.wav",1.0);
        mark = new Audio("res" + separator + "Sounds" + separator + "mark.wav",1.0);
        back = new Audio("res" + separator + "Sounds" + separator + "fone.wav",0.8);
        win = new Audio("res" + separator + "Sounds" + separator + "winsound.wav",1);
    }

    public void start()
    {
        if (state == GameStat.PLAYED) back.stop();
       back.play();
       back.setVolume();
       back.repeat();

       bomb.start();
       flag.start();
       state = GameStat.PLAYED;
    }

    public Box getBox (Coord coord)
    {
        if (flag.get(coord) == Box.OPENED)
            return bomb.get(coord);
        else
            return flag.get(coord);
    }

    public void pressLeftButton (Coord coord)
    {
        if (state == GameStat.WINNER) win.stop();
        if (gameOver()) return;
        klick.sound();
        klick.setVolume();
        openBox (coord);
        checkWinner();
    }

    private void checkWinner()
    {
        if (state == GameStat.PLAYED)
            if (flag.getCountOfClosedBoxes() == bomb.getTotalBombs()) {
                state = GameStat.WINNER;
                back.stop();
                win.play();
                win.setVolume();
            }
    }

    private void openBox (Coord coord)
    {
        switch (flag.get(coord))
        {
            case OPENED:setOpenedToClosedBoxesAroundNumber (coord);return;
            case FLAGED:return;
            case CLOSED:
                switch (bomb.get(coord))
                {
                    case ZERO: openBoxesAround(coord);return;
                    case BOMB: openBombs(coord); return;
                    default  : flag.setOpenedToBox(coord);return;
                }
        }
    }

    void setOpenedToClosedBoxesAroundNumber (Coord coord)
    {
        if (bomb.get(coord) != Box.BOMB)
            if (flag.getCountOfFlagedBoxesAround(coord) == bomb.get(coord).getNumber())
                for (Coord around : Ranges.getCoordsAround(coord))
                    if (flag.get(around) == Box.CLOSED)
                        openBox(around);
    }


    private void openBombs(Coord bombed)
    {
        state = GameStat.BOMBED;
        back.stop();
        expl.sound();
        expl.setVolume();

        flag.setBombedToBox(bombed);
        for (Coord coord : Ranges.getAllCoords())
            if (bomb.get(coord) == Box.BOMB)
                flag.setOpenedToClosedBombBox (coord);
            else
                flag.setNobombToFlagedSafeBox (coord);
    }

    private void openBoxesAround(Coord coord)
    {
        flag.setOpenedToBox(coord);
        for (Coord around : Ranges.getCoordsAround(coord))
            openBox(around);
    }

    public void pressRightButton (Coord coord)
    {
        if (gameOver()) return;
        mark.sound();
        mark.setVolume();
        flag.toggleFlagedToBox (coord);
    }

    private boolean gameOver()
    {
        if (state == GameStat.PLAYED)
            return false;
        start();
        return true;
    }
}
