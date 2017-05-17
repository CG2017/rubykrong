package pixelsInfo;

/**

 */
public class PixelsInfo {
    private int[] r;
    private int[] g;
    private int[] b;
    private int[] full;
    private int avgR;
    private int avgG;
    private int avgB;
    private int pxCount;

    public int getPxCount() {
        return pxCount;
    }

    public void setPxCount(int pxCount) {
        this.pxCount = pxCount;
    }

    public int[] getR() {
        return r;
    }

    public void setR(int[] r) {
        this.r = r;
    }

    public int[] getG() {
        return g;
    }

    public void setG(int[] g) {
        this.g = g;
    }

    public int[] getB() {
        return b;
    }

    public void setB(int[] b) {
        this.b = b;
    }

    public int[] getFull() {
        return full;
    }

    public void setFull(int[] full) {
        this.full = full;
    }

    public float getResAvgR() {
        return (float)avgR / pxCount;
    }

    public void setAvgR(int avgR) {
        this.avgR = avgR;
    }

    public float getResAvgG() {
        return (float)avgG / pxCount;
    }

    public void setAvgG(int avgG) {
        this.avgG = avgG;
    }

    public float getResAvgB() {
        return (float)avgB / pxCount;
    }

    public int getAvgR() {
        return avgR;
    }

    public int getAvgG() {
        return avgG;
    }

    public int getAvgB() {
        return avgB;
    }

    public void setAvgB(int avgB) {
        this.avgB = avgB;
    }

    public PixelsInfo() {
        this(new int[256], new int[256], new int[256], new int[256], 0, 0, 0);
    }

    public PixelsInfo(int[] r, int[] g, int[] b, int[] full, int avgR, int avgG, int avgB) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.full = full;
        this.avgR = avgR;
        this.avgG = avgG;
        this.avgB = avgB;
    }
}
