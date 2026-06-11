import sas.*;
import java.awt.Color;

class Spiel
{
    View fenster;
    Rectangle schlaeger;
    Circle ball;
    bloecke[] reihe1;
    bloecke[] reihe2;
    bloecke[] reihe3;
    bloecke[][] bloeckeArray;
    Text punkteAnzeige,ergebnis,hinweis,countdown;
    
    int punkte;

    Spiel()
    {
        fenster = new View(600, 400, "Atari Breakout");
        fenster.setBackgroundColor(Color.black);

        schlaeger = new Rectangle(250, 365, 100, 12, Color.cyan);
        ball = new Circle(292, 192, 8, Color.white);



        reihe1 = new bloecke[10];
        reihe2 = new bloecke[10];
        reihe3 = new bloecke[10];
        
        for (int i = 0; i < 10; i++)
        {
            reihe1[i] = new bloecke(2 + i*60, 45, 54, 20, Color.red,3);
            reihe2[i] = new bloecke(2 + i*60, 71, 54, 20, Color.orange,2);
            reihe3[i] = new bloecke(2 + i*60, 97, 54, 20, Color.yellow,1);
        }

        punkte = 0;
        punkteAnzeige = new Text(10, 20, "Punkte: 0", Color.white);
        punkteAnzeige.setFontMonospaced(true, 18);

        ergebnis = new Text(60, 185, "", Color.red);
        ergebnis.setFontMonospaced(true, 26);
        ergebnis.setHidden(true);

        hinweis = new Text(175, 230, "Enter = Neustart", Color.white);
        hinweis.setFontMonospaced(false, 20);
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
                if (fenster.keyRightPressed() && schlaeger.getShapeX() < 500)
                    schlaeger.move(7);

                ball.move(dx, dy);

                double bx = ball.getShapeX();
                double by = ball.getShapeY();

                if (bx <= 0)
                {
                   dx = -dx; 
                }
                if (bx + 16 >= 600)
                {
                    dx = -dx;
                }
                if (by <= 0)
                {
                    dy = -dy;
                }

                if (dy > 0 && ball.intersects(schlaeger))
                {
                    double treffPos = (ball.getCenterX() - schlaeger.getShapeX()) / 100.0;
                    dx = (treffPos - 0.5) * 8;
                    dy = -dy;
                }

                boolean blockGetroffen = false;
                for (int i = 0; i < 10 && !blockGetroffen; i++)
                {
                    if (reihe1[i].trifft(ball)) { punkte += reihe1[i].gibWert(); reihe1[i].treffer(); dy = -dy; blockGetroffen = true; }
                    else if (reihe2[i].trifft(ball)) { punkte += reihe2[i].gibWert(); reihe2[i].treffer(); dy = -dy; blockGetroffen = true; }
                    else if (reihe3[i].trifft(ball)) { punkte += reihe3[i].gibWert(); reihe3[i].treffer(); dy = -dy; blockGetroffen = true; }
                }
                if (blockGetroffen)
                    punkteAnzeige.setText("Punkte: " + punkte);

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
                for (int i = 0; i < 10; i++)
                {
                    if (reihe1[i].istAktiv() || reihe2[i].istAktiv() || reihe3[i].istAktiv())
                        alleWeg = false;
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
            for (int i = 0; i < 10; i++)
            {
                reihe1[i].zuruecksetzen();
                reihe2[i].zuruecksetzen();
                reihe3[i].zuruecksetzen();
            }
        }
    }
}
