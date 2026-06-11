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
    Text punkteAnzeige;
    Text ergebnisText;
    Text hinweisText;
    Text countdownText;
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
        double startX = (600 - (spalten * bBreite + (spalten - 1) * abstandX)) / 2.0;

        reihe1 = new bloecke[spalten];
        reihe2 = new bloecke[spalten];
        reihe3 = new bloecke[spalten];
        for (int i = 0; i < spalten; i++)
        {
            double x = startX + i * (bBreite + abstandX);
            reihe1[i] = new bloecke(x, 45, bBreite, bHoehe, Color.red,    3);
            reihe2[i] = new bloecke(x, 71, bBreite, bHoehe, Color.orange, 2);
            reihe3[i] = new bloecke(x, 97, bBreite, bHoehe, Color.yellow, 1);
        }

        punkte = 0;
        punkteAnzeige = new Text(10, 20, "Punkte: 0", Color.white);
        punkteAnzeige.setFontSansSerif(true, 18);

        ergebnisText = new Text(60, 185, "", Color.red);
        ergebnisText.setFontMonospaced(true, 26);
        ergebnisText.setHidden(true);

        hinweisText = new Text(175, 230, "Enter = Neustart", Color.white);
        hinweisText.setFontSansSerif(false, 20);
        hinweisText.setHidden(true);

        countdownText = new Text(262, 140, "3", Color.white);
        countdownText.setFontMonospaced(true, 80);

        while (true)
        {
            // Countdown
            countdownText.setText("3");
            countdownText.setHidden(false);
            fenster.wait(1000);
            countdownText.setText("2");
            fenster.wait(1000);
            countdownText.setText("1");
            fenster.wait(1000);
            countdownText.setText("Los!");
            fenster.wait(600);
            countdownText.setHidden(true);

            int dxBetrag = Tools.randomNumber(2, 4);
            int vorzeichen = (Tools.randomNumber(0, 1) == 0) ? 1 : -1;
            double dx = dxBetrag * vorzeichen;
            double dy = -4;

            boolean spielLaeuft = true;

            while (spielLaeuft)
            {
                if (fenster.keyLeftPressed() && schlaeger.getShapeX() > 0)
                    schlaeger.move(-5);
                if (fenster.keyRightPressed() && schlaeger.getShapeX() + 100 < 600)
                    schlaeger.move(5);

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
                for (int i = 0; i < spalten && !blockGetroffen; i++)
                {
                    if (reihe1[i].trifft(ball)) { punkte += reihe1[i].gibWert(); reihe1[i].treffer(); dy = -dy; blockGetroffen = true; }
                    else if (reihe2[i].trifft(ball)) { punkte += reihe2[i].gibWert(); reihe2[i].treffer(); dy = -dy; blockGetroffen = true; }
                    else if (reihe3[i].trifft(ball)) { punkte += reihe3[i].gibWert(); reihe3[i].treffer(); dy = -dy; blockGetroffen = true; }
                }
                if (blockGetroffen)
                    punkteAnzeige.setText("Punkte: " + punkte);

                if (ball.getShapeY() > 400)
                {
                    spielLaeuft = false;
                    ball.setHidden(true);
                    ergebnisText.setFontColor(Color.red);
                    ergebnisText.setText("Game Over!  Punkte: " + punkte);
                    ergebnisText.setHidden(false);
                    hinweisText.setHidden(false);
                }

                boolean alleWeg = true;
                for (int i = 0; i < spalten; i++)
                {
                    if (reihe1[i].istAktiv() || reihe2[i].istAktiv() || reihe3[i].istAktiv())
                        alleWeg = false;
                }
                if (alleWeg)
                {
                    spielLaeuft = false;
                    ball.setHidden(true);
                    ergebnisText.setFontColor(Color.green);
                    ergebnisText.setText("Gewonnen!  Punkte: " + punkte);
                    ergebnisText.setHidden(false);
                    hinweisText.setHidden(false);
                }

                fenster.wait(10);
            }

            while (!fenster.keyEnterPressed())
                fenster.wait(10);

            // Neustart
            ergebnisText.setHidden(true);
            hinweisText.setHidden(true);
            ball.setHidden(false);
            ball.moveTo(292, 192);
            schlaeger.moveTo(250, 365);
            punkte = 0;
            punkteAnzeige.setText("Punkte: 0");
            for (int i = 0; i < spalten; i++)
            {
                reihe1[i].zuruecksetzen();
                reihe2[i].zuruecksetzen();
                reihe3[i].zuruecksetzen();
            }
        }
    }
}
