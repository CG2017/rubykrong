package frame;

import histogram.Histogram;
import histogram.HistogramGroup;
import pixelsInfo.PixelsInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**

 */
public class MainFrame extends JFrame {
    private JMenuBar jMenuBar;
    private ImageIcon testImage;
    private BufferedImage sourceImage;
    private int width;
    private int height;
    private JLabel jLabelAVGR;
    private JLabel jLabelAVGG;
    private JLabel jLabelAVGB;
    private final int BRIGHTNESS_SLIDER_MAX = 200;
    private final int CONTRAST_SLIDER_MAX = 500;

    private JLabel jLabelBright;
    private JLabel jLabelContrast;

    private File selectedFile;

    private JLabel imageLabel;
    private Histogram rHistogram;
    private Histogram gHistogram;
    private Histogram bHistogram;
    private Histogram fHistogram;


    public MainFrame() throws HeadlessException {
        super();
        jMenuBar = new JMenuBar();
        testImage = new ImageIcon();
        imageLabel = new JLabel();
        rHistogram = new Histogram(Color.RED);
        gHistogram = new Histogram(Color.GREEN);
        bHistogram = new Histogram(Color.BLUE);
        fHistogram = new Histogram(Color.BLACK);
        setMenu();
        setDefaultConfig();
        setPanels();
        width = this.getWidth();
        height = this.getHeight();
    }

    private void setDefaultConfig() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1300, 700);
        this.setVisible(true);
    }

    @Override
    public void validate() {
        super.validate();
        height = this.getHeight();
        width = this.getWidth();
//        if (testImage.getImage() != null)
//            testImage = scaleImage(new ImageIcon(sourceImage), width / 2, height / 2);
        imageLabel.setIcon(testImage);
        imageLabel.repaint();
        rHistogram.repaint();
    }

    private void setMenu() {
        JMenu jMenu = new JMenu("File");
        JMenuItem jMenuItemOpen = new JMenuItem("Open");
        jMenuItemOpen.addActionListener(openFileListener());

        jMenu.add(jMenuItemOpen);
        jMenuBar.add(jMenu);
        this.setJMenuBar(jMenuBar);
    }

    private void setPanels() {
        int widthHist = 280;
        imageLabel.setPreferredSize(new Dimension((int) (width / 2.0), (int) (height / 2.3)));
        rHistogram.setPreferredSize(new Dimension(widthHist, (int) (height / 3.1)));
        gHistogram.setPreferredSize(new Dimension(widthHist, (int) (height / 3.1)));
        bHistogram.setPreferredSize(new Dimension(widthHist, (int) (height / 3.1)));
        fHistogram.setPreferredSize(new Dimension(widthHist, (int) (height / 3.1)));
        HistogramGroup hg = new HistogramGroup(rHistogram, gHistogram, bHistogram, fHistogram);
        hg.setPreferredSize(new Dimension((int) rHistogram.getPreferredSize().getWidth() * 2,
                (int) rHistogram.getPreferredSize().getHeight() * 2));

        Container pane = getContentPane();
        pane.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        JLabel jLabelSource = new JLabel("Source image:");
        jLabelBright = new JLabel("Brightness: 0");
        jLabelContrast = new JLabel("Contrast: 0");

        jLabelAVGR = new JLabel("Average R: ");
        jLabelAVGG = new JLabel("Average G: ");
        jLabelAVGB = new JLabel("Average B: ");

        JLabel jLabelHist = new JLabel("Histogram:");

        JSlider jSliderBright = new JSlider();
        JSlider jSliderContrast = new JSlider();
        jSliderBright.setMaximum(BRIGHTNESS_SLIDER_MAX);
        jSliderContrast.setMaximum(CONTRAST_SLIDER_MAX);
        jSliderBright.setValue(BRIGHTNESS_SLIDER_MAX / 2);
        jSliderContrast.setValue(CONTRAST_SLIDER_MAX / 2);
        jSliderBright.addChangeListener(brightnessListener());
        jSliderContrast.addChangeListener(contrastListener());

        int yPosition = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridwidth = 2;
        c.gridy = yPosition;
        c.gridx = 0;
        pane.add(jLabelBright, c);

        c.gridx = 0;
        c.gridy = ++yPosition;
        c.gridwidth = 1;
        pane.add(jSliderBright, c);

        c.gridx = 0;
        c.gridy = ++yPosition;
        c.gridwidth = 2;
        pane.add(jLabelContrast, c);

        c.gridx = 0;
        c.gridy = ++yPosition;
        c.gridwidth = 1;
        pane.add(jSliderContrast, c);

        c.gridx = 0;
        c.gridy = ++yPosition;
        c.gridwidth = 2;
        pane.add(jLabelSource, c);

        c.gridy = ++yPosition;
        c.insets = new Insets(20, 20, 20, 20);
        pane.add(imageLabel, c);


        yPosition = 0;
        c.insets = new Insets(0, 0, 0, 0);

        c.gridx = 2;
        c.gridy = yPosition;
        c.gridwidth = 2;
        c.weightx = 1;

        pane.add(jLabelAVGR, c);

        c.gridx = 2;
        c.gridy = ++yPosition;
        pane.add(jLabelAVGG, c);


        c.gridx = 2;
        c.gridy = ++yPosition;
        pane.add(jLabelAVGB, c);


        c.gridx = 2;
        c.gridy = ++yPosition;
        pane.add(jLabelHist, c);

        c.gridx = 2;
        c.gridy = ++yPosition;
        pane.add(new JLabel(), c);

        c.gridx = 2;
        c.gridy = ++yPosition;
        pane.add(hg, c);

        this.getContentPane().repaint();
    }

    private ChangeListener brightnessListener() {
        return new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    jLabelBright.setText(String.format("Brightness: %d", source.getValue() - BRIGHTNESS_SLIDER_MAX / 2));
                    testImage = scaleImage(changeBrightness(source.getValue()), width / 2, height / 2);
                    imageLabel.setIcon(testImage);
                    repaintHist();

                }
            }
        };
    }

    private ChangeListener contrastListener() {
        return new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    jLabelContrast.setText(String.format("Contrast: %d", source.getValue() - CONTRAST_SLIDER_MAX / 2));
                    testImage = scaleImage(changeContrast(source.getValue()), width / 2, height / 2);
                    imageLabel.setIcon(testImage);
                    repaintHist();
                }
            }
        };
    }

    private ImageIcon changeContrast(int contrast) {
        BufferedImage buf = deepCopy(sourceImage);
        contrast -= CONTRAST_SLIDER_MAX / 2;
        float factor = (259f * (contrast + 255)) / (255 * (259 - contrast));

        for (int x = 0; x < buf.getWidth(); x++) {
            for (int y = 0; y < buf.getHeight(); y++) {
                Color c = new Color(buf.getRGB(x, y));
                int newRed = trunc(Math.round(factor * (c.getRed() - 128) + 128));
                int newGreen = trunc(Math.round(factor * (c.getGreen() - 128) + 128));
                int newBlue = trunc(Math.round(factor * (c.getBlue() - 128) + 128));

                buf.setRGB(x, y, new Color(newRed, newGreen, newBlue).getRGB());
            }
        }

        return new ImageIcon(buf);
    }

    private int trunc(int val) {
        if (val > 255)
            return 255;
        else if (val < 0)
            return 0;
        else
            return val;
    }

    private BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private ImageIcon changeBrightness(int brightness) {
        BufferedImage buf = deepCopy(sourceImage);
        float br = brightness;
        br -= BRIGHTNESS_SLIDER_MAX / 2;
        br /= BRIGHTNESS_SLIDER_MAX / 2;
        br += 1;


        RescaleOp rescaleOp = new RescaleOp(br, 2, null);
        rescaleOp.filter(sourceImage, buf);
        return new ImageIcon(buf);
    }

    private ActionListener openFileListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    selectedFile = jFileChooser.getSelectedFile();
                    testImage = scaleImage(getImageFromFile(selectedFile), width / 2, height / 2);
                    if (testImage != null)
                        repaintHist();
                }
            }
        };
    }


    public void repaintHist() {
        imageLabel.setIcon(testImage);
        PixelsInfo pixelsInfo = getRGBValuesFromImage();

        rHistogram.setHist(pixelsInfo.getR());
        gHistogram.setHist(pixelsInfo.getG());
        bHistogram.setHist(pixelsInfo.getB());
        fHistogram.setHist(pixelsInfo.getFull());

        jLabelAVGR.setText(String.format("Average R: %f", pixelsInfo.getResAvgR()));
        jLabelAVGG.setText(String.format("Average G: %f", pixelsInfo.getResAvgG()));
        jLabelAVGB.setText(String.format("Average B: %f", pixelsInfo.getResAvgB()));
        rHistogram.repaint();
        gHistogram.repaint();
        bHistogram.repaint();
        fHistogram.repaint();
        imageLabel.repaint();
        this.repaint();
    }


    private PixelsInfo getRGBValuesFromImage() {
        PixelsInfo pixelsInfo = new PixelsInfo();

        BufferedImage sourceImage = new BufferedImage(testImage.getIconWidth(), testImage.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = sourceImage.createGraphics();
// paint the Icon to the BufferedImage.
        testImage.paintIcon(null, g, 0, 0);
        g.dispose();


        pixelsInfo.setPxCount(sourceImage.getHeight() * sourceImage.getWidth());
        for (int x = 0; x < sourceImage.getWidth(); x++) {
            for (int y = 0; y < sourceImage.getHeight(); y++) {
                Color c = new Color(sourceImage.getRGB(x, y));
                pixelsInfo.getR()[c.getRed()] += 1;
                pixelsInfo.getG()[c.getGreen()] += 1;
                pixelsInfo.getB()[c.getBlue()] += 1;
                pixelsInfo.setAvgR(pixelsInfo.getAvgR() + c.getRed());
                pixelsInfo.setAvgG(pixelsInfo.getAvgG() + c.getGreen());
                pixelsInfo.setAvgB(pixelsInfo.getAvgB() + c.getBlue());
            }
        }

        for (int i = 0; i < pixelsInfo.getR().length; i++) {
            int f = Math.round((pixelsInfo.getR()[i] + pixelsInfo.getG()[i] + pixelsInfo.getB()[i]) / 3f);
            pixelsInfo.getFull()[i] = f;
        }


        return pixelsInfo;
    }

    private ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        if (icon == null)
            return null;
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if (icon.getIconWidth() > width) {
            nw = width;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if (nh > height) {
            nh = height;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }


    private ImageIcon getImageFromFile(File file) {
        try {
            sourceImage = ImageIO.read(file);
            if (sourceImage == null)
                throw new IOException("Incorrect data type.");
        } catch (IOException e) {
            sourceImage = null;
            JOptionPane.showMessageDialog(this, e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Choose file.");
            return null;
        }
        return new ImageIcon(sourceImage);
    }
}
