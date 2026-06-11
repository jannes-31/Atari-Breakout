import sas.*;
import java.awt.Color;

class Spiel
{
    View fenster;
    Rectangle schlaeger;
    Circle ball;
    bloecke[][] bloeckeArray;
    Text punkteAnzeige,ergebnis,hinweis,countdown;
    
    int punkte;

    Spiel()
    {
        fenster = new View(600, 400, "Atari Breakout");
        fenster.setBackgroundColor(Color.black);

        schlaeger = new Rectangle(250, 365, 100, 12, Color.cyan);
        ball = new Circle(292, 192, 8, Color.white);

        int spalten = 10;
        double bBreite = 54;
        double bHoehe = 20;
        double abstandX = 5;
        double abstandY = 6;
        double startX = (600 - (spalten * bBreite + (spalten - 1) * abstandX)) / 2.0;
        double startY = 45;

        bloeckeArray = new bloecke[3][spalten];
        for (int spalte = 0; spalte < spalten; spalte++)
        {
            double x = startX + spalte * (bBreite + abstandX);
            bloeckeArray[0][spalte] = new bloecke(x, startY,                         bBreite, bHoehe, Color.red,    3);
            bloeckeArray[1][spalte] = new bloecke(x, startY + bHoehe + abstandY,     bBreite, bHoehe, Color.orange, 2);
            bloeckeArray[2][spalte] = new bloecke(x, startY + 2*(bHoehe + abstandY), bBreite, bHoehe, Color.yellow, 1);
        }

        punkte = 0;
        punkteAnzeige = new Text(10, 20, "Punkte: 0", Color.white);
        punkteAnzeige.setFontSansSerif(true, 18);

        ergebnis = new Text(60, 185, "", Color.red);
        ergebnis.setFontMonospaced(true, 26);
        ergebnis.setHidden(true);

        hinweis = new Text(175, 230, "Enter = Neustart", Color.white);
        hinweis.setFontSansSerif(false, 20);
        hinweis.setHidden(true);

        countdown = new Text(262, 140, "3", Color.white);
        countdown.setFontMonospaced(true, 80);

        while (true)
        {
            // Countdown
            countdown.setText("3");
            countdown.setHidden(false);
            fenster.wait(1000);
            countdown.setText("2");
            fenster.wait(1000);
            countdown.setText("1");
            fenster.wait(1000);
            countdown.setText("Los!");
            fenster.wait(600);
            countdown.setHidden(true);

            int dxBetrag = Tools.randomNumber(2, 4);
            int vorzeichen = (Tools.randomNumber(0, 1) == 0) ? 1 : -1;
            double dx = dxBetrag * vorzeichen;
            double dy = -4;

            boolean gameOver = false;

            while (!gameOver)
            {
                if (fenster.keyLeftPressed() && schlaeger.getShapeX() > 0)
                    schlaeger.move(-7);
                if (fenster.keyRightPressed() && schlaeger.getShapeX() + 100 < 600)
                    schlaeger.move(7);

                ball.move(dx, dy);

                double bx = ball.getShapeX();
                double by = ball.getShapeY();

                if (bx <= 0 && dx < 0)        dx = -dx;
                if (bx + 16 >= 600 && dx > 0) dx = -dx;
                if (by <= 0 && dy < 0)         dy = -dy;

                if (dy > 0 && ball.intersects(schlaeger))
                {
                    double treffPos = (ball.getCenterX() - schlaeger.getShapeX()) / 100.0;
                    dx = (treffPos - 0.5) * 8;
                    dy = -dy;
                }

                boolean blockGetroffen = false;
                for (int reihe = 0; reihe < 3 && !blockGetroffen; reihe++)
                {
                    for (int spalte = 0; spalte < spalten && !blockGetroffen; spalte++)
                    {
                        if (bloeckeArray[reihe][spalte].trifft(ball))
                        {
                            punkte += bloeckeArray[reihe][spalte].gibWert();
                            bloeckeArray[reihe][spalte].treffer();
                            punkteAnzeige.setText("Punkte: " + punkte);
                            dy = -dy;
                            blockGetroffen = true;
                        }
                    }
                }

                if (ball.getShapeY() > 400)
                {
                    gameOver = true;
                    ball.setHidden(true);
                    ergebnis.setFontColor(Color.red);
                    ergebnis.setText("Game Over!  Punkte: " + punkte);
                    ergebnis.setHidden(false);
                    hinweis.setHidden(false);
                }

                boolean alleWeg = true;
                pruefen:
                for (int reihe = 0; reihe < 3; reihe++)
                {
                    for (int spalte = 0; spalte < spalten; spalte++)
                    {
                        if (bloeckeArray[reihe][spalte].istAktiv())
                        {
                            alleWeg = false;
                            break pruefen;
                        }
                    }
                }
                if (alleWeg)
                {
                    gameOver = true;
                    ball.setHidden(true);
                    ergebnis.setFontColor(Color.green);
                    ergebnis.setText("Gewonnen!  Punkte: " + punkte);
                    ergebnis.setHidden(false);
                    hinweis.setHidden(false);
                }

                fenster.wait(10);
            }

            while (!fenster.keyEnterPressed())
                fenster.wait(10);

            // Neustart
            ergebnis.setHidden(true);
            hinweis.setHidden(true);
            ball.setHidden(false);
            ball.moveTo(292, 192);
            schlaeger.moveTo(250, 365);
            punkte = 0;
            punkteAnzeige.setText("Punkte: 0");
            for (int reihe = 0; reihe < 3; reihe++)
            {
                for (int spalte = 0; spalte < spalten; spalte++)
                    bloeckeArray[reihe][spalte].zuruecksetzen();
            }
        }
    }
}
