package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Changelog extends JPanel {

    private final String change_log =
            "Version: 1.0   (2018-06-18)\nInitial release version\n\n" +
                    "Version: 1.1   (2018-09-03)\nNew GPA scale\nNew Conformation Boxes\nExport&Import for GPA Calculator\nVarious bug fixes\n\n" +
                    "Version: 1.2   (2018-11-09)\nMinor improvements\n\n" +
                    "Version: 1.3   (2018-12-22)\nNew settings panel\nNew theme option\nNew start-up option\nUI improvements\nNew Print function";

    // ================================================================================

    public Changelog() {
        super(new BorderLayout());

        String acknowledgments =
                "Thank you for using CourseTimetable.          " +
                        "CourseTimetable is an application by Shangru Li          " +
                        "Suggestions & Problems please contact: li.shangru@me.com.          " +
                        "Special thanks to Tianlun, D., Leo, G., Leo, R., Mach, Q. and Zoe, Z. for all the help and support.";

        JTextArea changelogText = new JTextArea(change_log);
        changelogText.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 13));
        changelogText.setEditable(false);
        changelogText.setOpaque(false);
        changelogText.setHighlighter(null);
        changelogText.setCaretPosition(changelogText.getDocument().getLength());

        JScrollPane changelogScroll = new JScrollPane(changelogText);
        changelogScroll.setOpaque(false);
        changelogScroll.getViewport().setOpaque(false);
        changelogScroll.setBorder(null);
        changelogScroll.setPreferredSize(new Dimension(50, 180));
        changelogScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        changelogScroll.getHorizontalScrollBar().setOpaque(false);

        JPanel changelogPanel = new MyPanel();
        changelogPanel.add(changelogScroll, BorderLayout.SOUTH);
        add(changelogPanel, BorderLayout.CENTER);

        MarqueePanel mp = new MarqueePanel(acknowledgments, Frame.APP_WIDTH / 4);
        mp.setPreferredSize(new Dimension(Frame.APP_WIDTH, Frame.APP_HEIGHT / 35));
        add(mp, BorderLayout.PAGE_END);
        mp.start();
    }

    // ================================================================================

    public class MyPanel extends JPanel {

        private BufferedImage image;

        public MyPanel() {
            super(new BorderLayout());
            setOpaque(false);
            try {
                if (((299 * SettingsFrame.themeColor.getRed() + 587 * SettingsFrame.themeColor.getGreen() + 114 * SettingsFrame.themeColor.getBlue()) / 1000) < 128) {
                    image = ImageIO.read(getClass().getResource("/Resources/Timetable_DARK.png"));
                } else {
                    image = ImageIO.read(getClass().getResource("/Resources/Timetable.png"));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
            if (image != null) {
                image = (BufferedImage) resizeToBig(image, getWidth(), getHeight());
                g2d.drawImage(image, 0, 0, this);
            }
            super.paintComponent(g2d);
            g2d.dispose();
        }

        private Image resizeToBig(Image resize_image, int biggerWidth, int biggerHeight) {
            if (image.getHeight() != biggerHeight || image.getWidth() != biggerWidth) {
                int type = BufferedImage.TYPE_INT_ARGB;
                Image originalImage = null;
                try {
                    if (((299 * SettingsFrame.themeColor.getRed() + 587 * SettingsFrame.themeColor.getGreen() + 114 * SettingsFrame.themeColor.getBlue()) / 1000) < 128) {
                        originalImage = ImageIO.read(getClass().getResource("/Resources/Timetable_DARK.png"));
                    } else {
                        originalImage = ImageIO.read(getClass().getResource("/Resources/Timetable.png"));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                BufferedImage resizedImage = new BufferedImage(biggerWidth, biggerHeight, type);
                Graphics2D g = resizedImage.createGraphics();

                g.setComposite(AlphaComposite.Src);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g.drawImage(originalImage, 0, 0, biggerWidth, biggerHeight, this);
                g.dispose();

                return resizedImage;
            } else {
                return resize_image;
            }
        }
    }

    // ================================================================================
    public class MarqueePanel extends JPanel implements ActionListener {

        private static final int RATE = 10;
        private final Timer timer = new Timer(600 / RATE, this);
        private final JLabel label = new JLabel();
        private final String s;
        private final int n;
        private int index;

        public MarqueePanel(String s, int n) {
            if (s == null || n < 1) {
                throw new IllegalArgumentException("Null string or n < 1");
            }
            StringBuilder sb = new StringBuilder(n);
            for (int i = 0; i < n; i++) {
                sb.append(' ');
            }
            this.s = sb + s + sb;
            this.n = n;
            label.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
            label.setText(sb.toString());
            label.setForeground(SettingsFrame.getContrastColor(SettingsFrame.themeColor));
            this.setBackground(SettingsFrame.themeColor);
            this.add(label);
        }

        public void start() {
            timer.start();
        }

        public void stop() {
            timer.stop();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            index++;
            if (index > s.length() - n) {
                index = 0;
            }
            label.setText(s.substring(index, index + n));
        }
    }
}
