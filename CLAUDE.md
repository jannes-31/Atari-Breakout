# Atari Breakout – Projektdokumentation

## Bibliothek: Shapes and Sprites (SaS)

Dieses Projekt verwendet die Java-Bibliothek **Shapes and Sprites (SaS) Version 5.7**.
Die vollständige API-Dokumentation liegt im Repo unter:

```
DokumentationSaS_5_7.md
```

**Wichtig:** Verwende diese Dokumentation immer als Referenz für alle SaS-Klassen,
Konstruktoren und Methoden. Greife nie auf Vermutungen zurück, wenn die Dokumentation
die gesuchte Information enthält.

### Kurzübersicht der wichtigsten SaS-Klassen

| Klasse | Paket | Beschreibung |
|---|---|---|
| `View` | `sas` | Programmfenster mit Zeichenfläche (600×400 px Standard) |
| `Circle` | `sas` | Kreis mit Position, Radius und Farbe |
| `Ellipse` | `sas` | Ellipse mit Breite und Höhe |
| `Rectangle` | `sas` | Rechteck mit Breite und Höhe |
| `Polygon` | `sas` | Vieleck mit beliebiger Eckenzahl |
| `Sprite` | `sas` | Verbund mehrerer Shapes-Objekte |
| `Text` | `sas` | Textausgabe auf der Zeichenfläche |
| `Picture` | `sas` | Bild (jpg/png) auf der Zeichenfläche |
| `Tools` | `sas` | Statische Hilfsmethoden (Zufall, Zeit, Dialoge) |
| `Textfield` | `sasio` | Editierbares Texteingabefeld |
| `Button` | `sasio` | Klickbarer Button |
| `Label` | `sasio` | Text in farbigem Rechteck (Unterklasse von Sprite) |
| `StringFileTools` | `sasio` | Statische Methoden für Datei-I/O |

### Imports

```java
import sas.*;
import sasio.*;
```

### Koordinatensystem

- Ursprung (0, 0) liegt oben links
- x-Achse zeigt nach rechts
- y-Achse zeigt nach unten
- Positionsangaben beziehen sich auf die **linke obere Ecke** des umgebenden Rechtecks

## Projektstruktur

| Datei | Beschreibung |
|---|---|
| `Spiel.java` | Hauptklasse des Breakout-Spiels |
| `bloecke.java` | Klasse für die Spielblöcke |
| `DokumentationSaS_5_7.md` | Vollständige SaS-Bibliotheksdokumentation |
