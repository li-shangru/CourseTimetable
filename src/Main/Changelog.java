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
                    "Version: 1.3   (2018-12-22)\nNew settings panel\nNew theme option\nNew start-up option\nUI improvements\nNew Print function" +
                    "Version: 1.4   (2019-01-20)\nNew option to enable/disable sound effects\nOptimization\nBug fixes";
    private final String acknowledgments =
            "Thank you for using CourseTimetable.          " +
                    "CourseTimetable is an application by Shangru Li          " +
                    "Suggestions & Feedback please contact: li.shangru@me.com.          " +
                    "Special thanks to Tianlun, D., Leo, G., Leo, R., Mach, Q. and Zoe, Z. for all the help and support.";

    // ================================================================================

    /*
    Setup the changelog panel to display.
     */
    public Changelog() {
        super(new BorderLayout());
        /*
        Create a new JTextArea for changelog, with features:
            user cannot edit,
            transparent background,
            user cannot select its text,
            auto scroll to the bottom of its content.
         */
        JTextArea changelogText = new JTextArea(change_log);
        changelogText.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 13));
        changelogText.setEditable(false);
        changelogText.setOpaque(false);
        changelogText.setHighlighter(null);
        changelogText.setCaretPosition(changelogText.getDocument().getLength());

        /*
        Create a new JScrollPane to place changelogText, with features:
            transparent background,
            no border,
            size specified,
            no vertical scroll bar,
            no horizontal scroll bar background.
         */
        JScrollPane changelogScroll = new JScrollPane(changelogText);
        changelogScroll.setOpaque(false);
        changelogScroll.getViewport().setOpaque(false);
        changelogScroll.setBorder(null);
        changelogScroll.setPreferredSize(new Dimension(50, 180));
        changelogScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        changelogScroll.getHorizontalScrollBar().setOpaque(false);

        /*
        Create a new JPanel to display the background picture and place the changelogScroll.
        then add the JPanel to the current panel
         */
        JPanel changelogPanel = new ImageDisplayPanel();
        changelogPanel.add(changelogScroll, BorderLayout.SOUTH);
        this.add(changelogPanel, BorderLayout.CENTER);

        /*
        Create a new Marquee to display acknowledgments, with scrolling speed set to Frame.APP_WIDTH/4.
        add the Marquee to the current panel, then start scrolling.
         */
        MarqueePanel mp = new MarqueePanel(acknowledgments, acknowledgments.length());
        this.add(mp, BorderLayout.PAGE_END);
        mp.start();
    }

    // ================================================================================
    // ================================================================================
    // ================================================================================

    public class ImageDisplayPanel extends JPanel {

        private BufferedImage image;

        /*
        Instantiate this panel.
         */
        private ImageDisplayPanel() {
            super(new BorderLayout());
            this.setOpaque(false);
            image = getImageByTheme();
        }

        /*
        Return either the dark-text or the light-text version of the background image,
        determined by the current theme color.
         */
        private BufferedImage getImageByTheme() {
            try {
                if (((299 * SettingsFrame.themeColor.getRed() + 587 * SettingsFrame.themeColor.getGreen() + 114 * SettingsFrame.themeColor.getBlue()) / 1000) < 128) {
                    return ImageIO.read(getClass().getResource("/Resources/Timetable_DARK.png"));
                } else {
                    return ImageIO.read(getClass().getResource("/Resources/Timetable.png"));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /*
        Paint the image, if the size of the Panel change, will try to resize the image.
         */
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

        /*
        Resize the image if the size the container changed.
        Resize the input image: resize_image to to desired dimension: resizeWidth and resizeHeight.
         */
        private Image resizeToBig(Image resize_image, int resizeWidth, int resizeHeight) {
            if (image.getHeight() != resizeHeight || image.getWidth() != resizeWidth) {
                int type = BufferedImage.TYPE_INT_ARGB;
                Image originalImage = getImageByTheme();

                BufferedImage resizedImage = new BufferedImage(resizeWidth, resizeHeight, type);
                Graphics2D g = resizedImage.createGraphics();

                g.setComposite(AlphaComposite.Src);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g.drawImage(originalImage, 0, 0, resizeWidth, resizeHeight, this);
                g.dispose();

                return resizedImage;
            } else {
                return resize_image;
            }
        }
    }

    // ================================================================================
    public class MarqueePanel extends JPanel implements ActionListener {

        private static final int RATE = 25;
        private final Timer timer = new Timer(1000 / RATE, this);
        private final JLabel label = new JLabel();
        private final String s;
        private final int n;
        private int index;

        /*
        Construct the MarqueePanel with given text s and length n.
        By appending char ' ' to the end to the given string,
        we can achieve the marquee effect.
         */
        private MarqueePanel(String s, int n) {
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
