package histogram;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DecimalFormat;

/**

 */
public class Histogram extends JLabel {
    private int[] hist;
    private int max;
    private Color color;

    public Histogram() {
        this(new int[]{}, 0, Color.BLACK);
    }

    public Histogram(int[] hist) {
        this(hist, 0, Color.BLACK);
    }

    public Histogram(int[] hist, int max, Color color) {
        super();
        this.hist = hist;
        this.max = max;
        this.color = color;
        this.max = max(hist);
        this.setBorder(new LineBorder(Color.BLACK, 1));
    }

    public Histogram(Color color) {
        this(new int[]{}, 0, color);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int[] getHist() {
        return hist;
    }

    public void setHist(int[] hist) {
        this.hist = hist;
        max = max(hist);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int BAR_WIDTH = 1;
        final int START_X = 40;
        if (hist.length == 0)
            return;



        g.setColor(color);


        for (int i = 0; i < hist.length; i++) {
            int h = getColumnHeight(hist[i]);
            g.drawRect(START_X + i, getHeight() - h, BAR_WIDTH, h);
        }


        DecimalFormat format = new DecimalFormat("\uFFFD");
        char[] maxVal = format.format(max).toCharArray();
        char[] maxVal23 = format.format(max * 2 / 3.).toCharArray();
        char[] maxVal13 = format.format(max / 3).toCharArray();

        g.setColor(Color.WHITE);
        g.fillRect(0, getHeight() - getColumnHeight(max) - 10, 50, 10);
        g.fillRect(0, getHeight() - getColumnHeight((int)(max * 2 / 3.)) - 10, 50, 10);
        g.fillRect(0, getHeight() - getColumnHeight((int)(max / 3.)) - 10, 50, 10);

        g.setColor(Color.BLACK);
        g.drawChars(maxVal, 0, maxVal.length, 1, getHeight() - getColumnHeight(max));
        g.drawChars(maxVal23, 0, maxVal23.length, 1, getHeight() - getColumnHeight((int)(max * 2 / 3.)));
        g.drawChars(maxVal13, 0, maxVal13.length, 1, getHeight() - getColumnHeight((int)(max / 3.)));
    }

    private int getColumnHeight(int val) {
        return (int)((double)val / max * 0.9 * getHeight());
    }

    private int max(int[] values) {
        if (values.length == 0)
            return 0;
        int max = values[0];
        for (int value : values) {
            if (value >= max) {
                max = value;
            }
        }
        return max;
    }
}
