import sas.*;
import java.awt.Color;

public class bloecke
{
    Rectangle rechteck;
    int wert;
    boolean aktiv;

    bloecke(double pX, double pY, double pBreite, double pHoehe, Color pFarbe, int pWert)
    {
        rechteck = new Rectangle(pX, pY, pBreite, pHoehe, pFarbe);
        wert = pWert;
        aktiv = true;
    }

    boolean trifft(Circle pBall)
    {
        return aktiv && rechteck.intersects(pBall);
    }

    int gibWert()
    {
        return wert;
    }

    void treffer()
    {
        aktiv = false;
        rechteck.setHidden(true);
    }

    boolean istAktiv()
    {
        return aktiv;
    }
}
