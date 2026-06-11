import sas.*;
import java.awt.Color;

public class Spiel {

    public Spiel() {
        View view = new View(600, 400, "Atari Breakout");
        view.setBackgroundColor(Color.BLACK);

        // Score-Anzeige
        int score = 0;
        Text scoreText = new Text(10, 20, "Score: 0", Color.WHITE);
        scoreText.setFontSansSerif(true, 18);

        // Paddle
        final double PADDLE_W = 100;
        final double PADDLE_H = 12;
        final double PADDLE_Y = 365;
        double paddleX = (600 - PADDLE_W) / 2.0;
        Rectangle paddle = new Rectangle(paddleX, PADDLE_Y, PADDLE_W, PADDLE_H, Color.CYAN);

        // Ball – Startposition in der Mitte
        final double BALL_R = 8;
        Circle ball = new Circle(300 - BALL_R, 200 - BALL_R, BALL_R, Color.WHITE);

        // Zufällige Startrichtung (immer nach oben)
        int dxAbs = Tools.randomNumber(2, 4);
        int dxSign = (Tools.randomNumber(0, 1) == 0) ? 1 : -1;
        double dx = dxAbs * dxSign;
        double dy = -4;

        // Blöcke: 3 Reihen, 10 Spalten
        // Reihe 1 (oben, rot)   = 3 Punkte
        // Reihe 2 (mitte, orange) = 2 Punkte
        // Reihe 3 (unten, gelb) = 1 Punkt
        final int COLS = 10;
        final double BW = 54;
        final double BH = 20;
        final double GAP_X = 5;
        final double GAP_Y = 6;
        final double START_X = (600 - (COLS * BW + (COLS - 1) * GAP_X)) / 2.0;
        final double START_Y = 45;

        Color[] rowColors = { Color.RED, Color.ORANGE, Color.YELLOW };
        int[] rowPoints  = { 3, 2, 1 };

        bloecke[][] blocks = new bloecke[3][COLS];
        for (int row = 0; row < 3; row++) {
            double y = START_Y + row * (BH + GAP_Y);
            for (int col = 0; col < COLS; col++) {
                double x = START_X + col * (BW + GAP_X);
                blocks[row][col] = new bloecke(x, y, BW, BH, rowColors[row], rowPoints[row]);
            }
        }

        // Spielschleife
        final double PADDLE_SPEED = 5;
        boolean running = true;

        while (running) {

            // --- Paddle bewegen ---
            if (view.keyLeftPressed()) {
                double newX = paddle.getShapeX() - PADDLE_SPEED;
                if (newX < 0) newX = 0;
                paddle.moveTo(newX, PADDLE_Y);
            }
            if (view.keyRightPressed()) {
                double newX = paddle.getShapeX() + PADDLE_SPEED;
                if (newX + PADDLE_W > 600) newX = 600 - PADDLE_W;
                paddle.moveTo(newX, PADDLE_Y);
            }

            // --- Ball bewegen ---
            ball.move(dx, dy);

            double bx = ball.getShapeX();
            double by = ball.getShapeY();
            double bd = BALL_R * 2;

            // Wand links / rechts
            if (bx <= 0) {
                dx = Math.abs(dx);
            }
            if (bx + bd >= 600) {
                dx = -Math.abs(dx);
            }

            // Decke
            if (by <= 0) {
                dy = Math.abs(dy);
            }

            // Paddle-Kollision (nur wenn Ball sich nach unten bewegt)
            if (dy > 0 && ball.intersects(paddle)) {
                double hitPos = (ball.getCenterX() - paddle.getShapeX()) / PADDLE_W;
                dx = (hitPos - 0.5) * 8;
                if (Math.abs(dx) < 0.5) dx = (dx >= 0) ? 0.5 : -0.5;
                dy = -Math.abs(dy);
            }

            // Block-Kollisionen
            boolean hitBlock = false;
            for (int row = 0; row < 3 && !hitBlock; row++) {
                for (int col = 0; col < COLS && !hitBlock; col++) {
                    if (blocks[row][col].intersects(ball)) {
                        score += blocks[row][col].getValue();
                        blocks[row][col].hit();
                        scoreText.setText("Score: " + score);
                        dy = -dy;
                        hitBlock = true;
                    }
                }
            }

            // Ball unten raus → Game Over
            if (ball.getShapeY() > 400) {
                running = false;
                ball.setHidden(true);
                Text gameOver = new Text(95, 195, "Game Over! Score: " + score, Color.RED);
                gameOver.setFontSansSerif(true, 28);
            }

            // Alle Blöcke weg → Gewonnen
            boolean allCleared = true;
            outerLoop:
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (blocks[row][col].isActive()) {
                        allCleared = false;
                        break outerLoop;
                    }
                }
            }
            if (allCleared) {
                running = false;
                ball.setHidden(true);
                Text win = new Text(155, 195, "You Win! Score: " + score, Color.GREEN);
                win.setFontSansSerif(true, 28);
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {}
        }
    }
}
