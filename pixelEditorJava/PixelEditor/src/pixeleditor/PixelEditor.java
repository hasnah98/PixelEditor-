
package pixeleditor;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class PixelEditor extends JFrame {

    private BufferedImage image1, image2;
    private JLabel imgA, imgB;

    private JButton loadBtn, startBtn;
    private JButton stepBtnA;

    private Timer timer;
    private boolean running = false;

    //  Constructor 
    public PixelEditor() {
        super("Pixel Editor Pro");

        setLookAndFeel();
        buildUI();
        initTimer();

        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    //  UI 
    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        add(topBar(), BorderLayout.NORTH);
        add(centerPanel(), BorderLayout.CENTER);

        applyTheme();
    }

    private JPanel topBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("Pixel Editor Pro", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        loadBtn = modernButton("Load Image");
        loadBtn.addActionListener(e -> loadImage());
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(loadBtn);

        top.add(title, BorderLayout.NORTH);
        top.add(centerPanel, BorderLayout.CENTER);

        return top;
    }

    private JPanel centerPanel() {
        JPanel grid = new JPanel(new GridLayout(1, 2, 20, 0));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel cardA = new JPanel(new BorderLayout(10, 10));
        imgA = imageBox("Image A");
        stepBtnA = modernButton("Step");
        stepBtnA.addActionListener(e -> { if(image1 != null) applyFilterWorker(image1, imgA); });
        JPanel bottomA = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomA.setOpaque(false);
        bottomA.add(stepBtnA);
        cardA.add(imgA, BorderLayout.CENTER);
        cardA.add(bottomA, BorderLayout.SOUTH);

        JPanel cardB = new JPanel(new BorderLayout(10, 10));
        imgB = imageBox("Image B");
        startBtn = modernButton("Start");
        startBtn.addActionListener(e -> toggleAnimation());
        JPanel bottomB = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomB.setOpaque(false);
        bottomB.add(startBtn);
        cardB.add(imgB, BorderLayout.CENTER);
        cardB.add(bottomB, BorderLayout.SOUTH);

        grid.add(cardA);
        grid.add(cardB);
        return grid;
    }

    //  Components 
    private JLabel imageBox(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setPreferredSize(new Dimension(400, 400));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lbl.setForeground(Color.GRAY);
        lbl.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private JButton modernButton(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(140, 40));
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    //  Theme 
    private void applyTheme() {
        Color bg = Color.WHITE;
        Color panel = new Color(245, 245, 245);
        Color fg = Color.BLACK;

        Color buttonBg = new Color(100, 150, 250);
        Color buttonFg = Color.BLACK;
        Color buttonBorder = Color.DARK_GRAY;

        getContentPane().setBackground(bg);

        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JPanel p) {
                p.setBackground(panel);
                for (Component child : p.getComponents()) {
                    child.setForeground(fg);
                    if (child instanceof JButton) {
                        ((JComponent) child).setOpaque(true);
                        child.setBackground(buttonBg);
                        child.setForeground(buttonFg);
                        ((JComponent) child).setBorder(BorderFactory.createLineBorder(buttonBorder, 2));
                    }
                }
            }
        }

        imgA.setForeground(fg);
        imgB.setForeground(fg);

        stepBtnA.setBackground(buttonBg);
        stepBtnA.setForeground(buttonFg);
        stepBtnA.setBorder(BorderFactory.createLineBorder(buttonBorder, 2));

        startBtn.setBackground(buttonBg);
        startBtn.setForeground(buttonFg);
        startBtn.setBorder(BorderFactory.createLineBorder(buttonBorder, 2));

        loadBtn.setBackground(buttonBg);
        loadBtn.setForeground(buttonFg);
        loadBtn.setBorder(BorderFactory.createLineBorder(buttonBorder, 2));

        repaint();
    }

    //  Image 
    private void loadImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                image1 = ImageIO.read(chooser.getSelectedFile());
                image2 = ImageIO.read(chooser.getSelectedFile());

                imgA.setIcon(new ImageIcon(getScaledImage(image1, imgA.getWidth(), imgA.getHeight())));
                imgB.setIcon(new ImageIcon(getScaledImage(image2, imgB.getWidth(), imgB.getHeight())));
                imgA.setText("");
                imgB.setText("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading image");
            }
        }
    }

    private Image getScaledImage(BufferedImage srcImg, int w, int h) {
        return srcImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    }

    //  Timer 
    private void initTimer() {
        timer = new Timer(120, e -> {
            if (image2 != null)
                applyFilterWorker(image2, imgB);
        });
    }

    private void toggleAnimation() {
        if (timer.isRunning()) {
            timer.stop();
            startBtn.setText("Start");
            running = false;
        } else {
            if (image2 != null) {
                timer.start();
                startBtn.setText("Stop");
                running = true;
            } else {
                JOptionPane.showMessageDialog(this, "Please load Image B first!");
            }
        }
    }

    //  Filter 
    private synchronized void applyFilter(BufferedImage img) {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                int a = (rgb >> 24) & 0xff;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;

                r = (r + 8) % 256;
                g = (g + 5) % 256;
                b = (b + 12) % 256;

                img.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
    }

    private void applyFilterWorker(BufferedImage img, JLabel label) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                applyFilter(img);
                return null;
            }

            @Override
            protected void done() {
                label.setIcon(new ImageIcon(getScaledImage(img, label.getWidth(), label.getHeight())));
            }
        };
        worker.execute();
    }

    //  Look 
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

    //  Main 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PixelEditor::new);
    }
}
