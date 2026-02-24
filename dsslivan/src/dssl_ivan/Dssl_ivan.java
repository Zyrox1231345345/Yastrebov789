package dssl_ivan;

/* 
@author Шайдт Иван Вадимович
Предметная область: Система электронной регистрации и учета налогоплательщиков: упрощение уплаты налогов.
*/

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import javax.imageio.ImageIO;
import java.io.IOException;
public class Dssl_ivan extends JFrame {
    
    private static Dssl_ivan game_window;
    private static long last_frame_time;
    private static Image dssl_bg;
    private static Image snowflake;
    private static final int SNOWFLAKE_COUNT = 15;
    private static float[] snow_x = new float[SNOWFLAKE_COUNT];
    private static float[] snow_y = new float[SNOWFLAKE_COUNT];
    private static float[] snow_speed = new float[SNOWFLAKE_COUNT];
    private static float[] snow_size = new float[SNOWFLAKE_COUNT];
    private static float[] snow_sway = new float[SNOWFLAKE_COUNT];
    
    private static final int[][] LIGHT_POSITIONS = {
        {774, 339},
        {820, 336},
        {768, 437},
        {815, 421},
        {855, 427},
        {694, 510},
        {742, 517},
        {869, 505},
        {660, 630},
        {706, 609},
        {821, 614},
        {867, 588},
        {928, 607}
    };
    
    private static final int LIGHTS_COUNT = LIGHT_POSITIONS.length;
    private static Color[] light_colors = new Color[LIGHTS_COUNT];
    private static float[] light_phase = new float[LIGHTS_COUNT];
    private static float[] light_blink_speed = new float[LIGHTS_COUNT];
    
    private static Color[] LIGHT_COLOR_PALETTE = {
        new Color(255, 50, 50),
        new Color(50, 255, 50),
        new Color(50, 100, 255),
        new Color(255, 255, 50),
        new Color(255, 50, 255),
        new Color(255, 150, 50),
        new Color(50, 255, 255),
        new Color(255, 200, 200),
        new Color(255, 255, 255)
    };
    
    public static void main(String[] args) throws IOException {
        dssl_bg = ImageIO.read(Dssl_ivan.class.getResourceAsStream("/dssl_bg.png"));
        snowflake = ImageIO.read(Dssl_ivan.class.getResourceAsStream("/snowflake.png"));
        
        game_window = new Dssl_ivan();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setTitle("DSSL - Шайдт Иван");
        game_window.setLocation(100, 50);
        game_window.setSize(1200, 700);
        game_window.setResizable(false);
        
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        game_window.add(game_field);
        
        initializeSnowflakes();
        initializeLights();
        
        game_window.setVisible(true);
    }
    
    private static void initializeSnowflakes() {
        for (int i = 0; i < SNOWFLAKE_COUNT; i++) {
            snow_x[i] = (float)(Math.random() * 1200);
            snow_y[i] = (float)(-100 - Math.random() * 500);
            snow_speed[i] = (float)(50 + Math.random() * 100);
            snow_size[i] = (float)(0.3 + Math.random() * 0.7);
            snow_sway[i] = (float)(Math.random() * Math.PI * 2);
        }
    }
    
    private static void initializeLights() {
        for (int i = 0; i < LIGHTS_COUNT; i++) {
            light_colors[i] = LIGHT_COLOR_PALETTE[(int)(Math.random() * LIGHT_COLOR_PALETTE.length)];
            light_phase[i] = (float)(Math.random() * Math.PI * 2);
            light_blink_speed[i] = (float)(1.0 + Math.random() * 3.0);
        }
    }
    
    private static void onRepaint(Graphics g, int panelWidth, int panelHeight) {
        Graphics2D g2d = (Graphics2D) g;
        
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;
        
        double bgAspectRatio = 1200.0 / 675.0;
        int bgWidth = panelWidth;
        int bgHeight = (int)(bgWidth / bgAspectRatio);
        
        if (bgHeight > panelHeight) {
            bgHeight = panelHeight;
            bgWidth = (int)(bgHeight * bgAspectRatio);
        }
        
        int bgX = (panelWidth - bgWidth) / 2;
        int bgY = (panelHeight - bgHeight) / 2;
        
        g.drawImage(dssl_bg, bgX, bgY, bgWidth, bgHeight, null);
        
        for (int i = 0; i < LIGHTS_COUNT; i++) {
            light_phase[i] += delta_time * light_blink_speed[i];
            
            float brightness = (float)(0.5 + 0.5 * Math.abs(Math.sin(light_phase[i])));
            
            int lightX = LIGHT_POSITIONS[i][0];
            int lightY = LIGHT_POSITIONS[i][1];
            int lightSize = 14;
            
            Color glowColor = new Color(
                light_colors[i].getRed(),
                light_colors[i].getGreen(),
                light_colors[i].getBlue(),
                (int)(60 * brightness)
            );
            
            Point2D center = new Point2D.Float(lightX, lightY);
            float radius = lightSize * 2.5f;
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {glowColor, new Color(0, 0, 0, 0)};
            RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
            g2d.setPaint(gradient);
            g2d.fillOval(lightX - (int)radius, lightY - (int)radius, (int)(radius * 2), (int)(radius * 2));
            
            Color lightColor = new Color(
                Math.min(255, (int)(light_colors[i].getRed() * brightness + 100)),
                Math.min(255, (int)(light_colors[i].getGreen() * brightness + 100)),
                Math.min(255, (int)(light_colors[i].getBlue() * brightness + 100))
            );
            
            g2d.setColor(lightColor);
            g2d.fillOval(lightX - lightSize/2, lightY - lightSize/2, lightSize, lightSize);
            
            g2d.setColor(new Color(255, 255, 255, (int)(200 * brightness)));
            g2d.fillOval(lightX - 3, lightY - 3, 6, 6);
        }
        
        for (int i = 0; i < SNOWFLAKE_COUNT; i++) {
            snow_y[i] = snow_y[i] + snow_speed[i] * delta_time;
            snow_sway[i] = snow_sway[i] + delta_time * 2;
            
            float swayOffset = (float)(Math.sin(snow_sway[i]) * 30);
            
            if (snow_y[i] > panelHeight + 100) {
                snow_y[i] = -100;
                snow_x[i] = (float)(Math.random() * panelWidth);
                snow_speed[i] = (float)(50 + Math.random() * 100);
            }
            
            int flakeSize = (int)(96 * snow_size[i]);
            int drawX = (int)(snow_x[i] + swayOffset);
            int drawY = (int)snow_y[i];
            
            g.drawImage(snowflake, drawX, drawY, flakeSize, flakeSize, null);
        }
    }
    
    public static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g, getWidth(), getHeight());
            repaint();
        }
    }
}