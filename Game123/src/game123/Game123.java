package game123;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * @author HYPERPC
 */
public class Game123 extends JFrame {
    private static Game123 game_game;
    private BufferedImage backgroundImage;
    private BufferedImage foregroundImage;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        game_game = new Game123();
        game_game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_game.setLocation(200, 50);
        game_game.setSize(900, 600);
        game_game.setResizable(false);
        GameField game_field = game_game.new GameField();
        game_game.add(game_field); // Исправлено: было game_field.add(game_field)
        game_game.setVisible(true);
    }

    public class GameField extends JPanel {
        public GameField() {
            // Загрузка изображений
            try {
                // Попробуем несколько вариантов путей
                File backgroundFile = new File("nalog.png");
                File foregroundFile = new File("Dexter.png");
                
                // Выведем информацию о путях для отладки
                System.out.println("Текущая директория: " + System.getProperty("user.dir"));
                System.out.println("Путь к nalog.png: " + backgroundFile.getAbsolutePath());
                System.out.println("Существует ли nalog.png: " + backgroundFile.exists());
                System.out.println("Путь к Dexter.png: " + foregroundFile.getAbsolutePath());
                System.out.println("Существует ли Dexter.png: " + foregroundFile.exists());
                
                backgroundImage = ImageIO.read(backgroundFile);
                foregroundImage = ImageIO.read(foregroundFile);
                
                System.out.println("Изображения успешно загружены!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ошибка загрузки изображений. Проверьте пути к файлам.");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Отрисовка фонового изображения (nalog.png)
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }

            // Отрисовка переднего изображения (Dexter.png)
            if (foregroundImage != null) {
                int x = (getWidth() - foregroundImage.getWidth()) / 2;
                int y = (getHeight() - foregroundImage.getHeight()) / 2;
                g.drawImage(foregroundImage, x, y, this);
            }
        }
    }
}