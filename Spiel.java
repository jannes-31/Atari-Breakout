import sas.*;
import java.awt.Color;

class Spiel
{
    View fenster;
    Rectangle schlaeger;
    Circle ball;
    bloecke[][] bloeckeArray;
    Text punkteAnzeige;
    int punkte;

    Spiel()
    {
        fenster = new View(600, 400, "Atari Breakout");
        fenster.setBackgroundColor(Color.black);

        punkte = 0;
        punkteAnzeige = new Text(10, 20, "Punkte: 0", Color.white);
        punkteAnzeige.setFontSansSerif(true, 18);

        // Schläger – Richtung 90° (Ost) für Links-Rechts-Bewegung
        schlaeger = new Rectangle(250, 365, 100, 12, Color.cyan);

        // Ball in der Mitte, zufällige Startrichtung
        ball = new Circle(292, 192, 8, Color.white);

        int dxBetrag = Tools.randomNumber(2, 4);
        int vorzeichen = (Tools.randomNumber(0, 1) == 0) ? 1 : -1;
        double dx = dxBetrag * vorzeichen;
        double dy = -4;

        // Blöcke: 3 Reihen, 10 Spalten
        // Reihe 1 (rot)    = 3 Punkte
        // Reihe 2 (orange) = 2 Punkte
        // Reihe 3 (gelb)   = 1 Punkt
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
            bloeckeArray[0][spalte] = new bloecke(x, startY,                    bBreite, bHoehe, Color.red,    3);
            bloeckeArray[1][spalte] = new bloecke(x, startY + bHoehe + abstandY, bBreite, bHoehe, Color.orange, 2);
            bloeckeArray[2][spalte] = new bloecke(x, startY + 2*(bHoehe + abstandY), bBreite, bHoehe, Color.yellow, 1);
        }

        boolean spielLaeuft = true;

        while (spielLaeuft)
        {
            // Schläger bewegen (wie Rakete: move(5) = Ost, move(-5) = West)
            if (fenster.keyLeftPressed() && schlaeger.getShapeX() > 0)
            {
                schlaeger.move(-5);
            }
            if (fenster.keyRightPressed() && schlaeger.getShapeX() + 100 < 600)
            {
                schlaeger.move(5);
            }

            // Ball bewegen
            ball.move(dx, dy);

            double bx = ball.getShapeX();
            double by = ball.getShapeY();

            // Wände links / rechts
            if (bx <= 0 && dx < 0)             dx = -dx;
            if (bx + 16 >= 600 && dx > 0)      dx = -dx;
            // Decke
            if (by <= 0 && dy < 0)              dy = -dy;

            // Schläger-Kollision (nur wenn Ball nach unten fliegt)
            if (dy > 0 && ball.intersects(schlaeger))
            {
                double treffPos = (ball.getCenterX() - schlaeger.getShapeX()) / 100.0;
                dx = (treffPos - 0.5) * 8;
                dy = -dy;
            }

            // Block-Kollisionen
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

            // Ball unten raus → Game Over
            if (ball.getShapeY() > 400)
            {
                spielLaeuft = false;
                ball.setHidden(true);
                Text gameOver = new Text(80, 195, "Game Over! Punkte: " + punkte, Color.red);
                gameOver.setFontMonospaced(true, 28);
            }

            // Alle Blöcke weg → Gewonnen
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
                spielLaeuft = false;
                ball.setHidden(true);
                Text gewonnen = new Text(155, 195, "Gewonnen! Punkte: " + punkte, Color.green);
                gewonnen.setFontMonospaced(true, 28);
            }

            fenster.wait(10);
        }
    }
}
