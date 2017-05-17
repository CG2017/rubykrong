package histogram;

import javax.swing.*;
import java.awt.*;

/**

 */
public class HistogramGroup extends JLabel {
    private Histogram rHistogram;
    private Histogram gHistogram;
    private Histogram bHistogram;
    private Histogram fHistogram;


    public HistogramGroup(Histogram rHistogram, Histogram gHistogram, Histogram bHistogram, Histogram fHistogram) {
        super();
        this.rHistogram = rHistogram;
        this.gHistogram = gHistogram;
        this.bHistogram = bHistogram;
        this.fHistogram = fHistogram;
        positionedHistograms();
    }

    private void positionedHistograms() {
        GridLayout gridLayout = new GridLayout(2, 2);
        this.setLayout(gridLayout);

        this.add(rHistogram);
        this.add(gHistogram);
        this.add(bHistogram);
        this.add(fHistogram);
    }

    public Histogram getrHistogram() {
        return rHistogram;
    }

    public void setrHistogram(Histogram rHistogram) {
        this.rHistogram = rHistogram;
    }

    public Histogram getgHistogram() {
        return gHistogram;
    }

    public void setgHistogram(Histogram gHistogram) {
        this.gHistogram = gHistogram;
    }

    public Histogram getbHistogram() {
        return bHistogram;
    }

    public void setbHistogram(Histogram bHistogram) {
        this.bHistogram = bHistogram;
    }

    public Histogram getfHistogram() {
        return fHistogram;
    }

    public void setfHistogram(Histogram fHistogram) {
        this.fHistogram = fHistogram;
    }
}
