package by.famcs.rub.kg_lab1;

/**

 */
public class ColorConverter {
    public class CMY {
        private double c;
        private double m;
        private double y;

        public CMY(double c, double m, double y) {
            this.c = c;
            this.m = m;
            this.y = y;
        }

        public double getC() {
            return c;
        }

        public void setC(double c) {
            this.c = c;
        }

        public double getM() {
            return m;
        }

        public void setM(double m) {
            this.m = m;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
    public class RGB {
        private int r;
        private int g;
        private int b;

        public RGB(int r, int g, int b) {
            this.b = b;
            this.g = g;
            this.r = r;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public int getG() {
            return g;
        }

        public void setG(int g) {
            this.g = g;
        }

        public int getR() {
            return r;
        }

        public void setR(int r) {
            this.r = r;
        }
    }
    public class HLS {
        private double h;
        private double l;
        private double s;

        public HLS(double h, double l, double s) {
            this.h = h;
            this.l = l;
            this.s = s;
        }

        public double getH() {
            return h;
        }

        public void setH(double h) {
            this.h = h;
        }

        public double getL() {
            return l;
        }

        public void setL(double l) {
            this.l = l;
        }

        public double getS() {
            return s;
        }

        public void setS(double s) {
            this.s = s;
        }
    }
    public class LUV {
        private double l;
        private double u;
        private double v;

        public LUV(double l, double u, double v) {
            this.l = l;
            this.u = u;
            this.v = v;
        }

        public double getL() {
            return l;
        }

        public void setL(double l) {
            this.l = l;
        }

        public double getU() {
            return u;
        }

        public void setU(double u) {
            this.u = u;
        }

        public double getV() {
            return v;
        }

        public void setV(double v) {
            this.v = v;
        }
    }
    public class XYZ {
        private double x;
        private double y;
        private double z;

        public XYZ(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }
    }

    public long rgbToHex(int r, int g, int b) {
        return Long.parseLong(String.format("ff%02x%02x%02x", r, g, b), 16);
    }

    public RGB hexToRgb(int hex) {
        String hexS = Integer.toHexString(hex);

        int r = Integer.parseInt(hexS.substring(2, 4), 16);
        int g = Integer.parseInt(hexS.substring(4, 6), 16);
        int b = Integer.parseInt(hexS.substring(6, 8), 16);

        return new RGB(r, g, b);
    }

    public CMY rgbToCmy(int r, int g, int b) {
        double c = 1 - (r / 255.0);
        double m = 1 - (g / 255.0);
        double y = 1 - (b / 255.0);

        return new CMY(c, m, y);
    }

    public RGB cmyToRgb(double c, double m, double y) {
        double r = (1 - c) * 255;
        double g = (1 - m) * 255;
        double b = (1 - y) * 255;

        return new RGB((int)r, (int)g, (int)b);
    }

    public HLS rgbToHls(int r, int g, int b) {
        double h = 0;
        double l = 0;
        double s = 0;
        double norm_r = r / 255.0;
        double norm_g = g / 255.0;
        double norm_b = b / 255.0;

        double min_rgb = Math.min(norm_r, Math.min(norm_g, norm_b));
        double max_rgb = Math.max(norm_r, Math.max(norm_g, norm_b));
        double delta = max_rgb - min_rgb;

        l = (max_rgb + min_rgb) / 2.0;

        if (delta == 0) {
            h = 0;
            s = 0;
        } else {
            if (l < 0.5) {
                s = delta / (max_rgb + min_rgb);
            } else {
                s = delta / (2 - max_rgb - min_rgb);
            }

            double del_r = (((max_rgb - norm_r) / 6.0) + (max_rgb / 2.0)) / max_rgb;
            double del_g = (((max_rgb - norm_g) / 6.0) + (max_rgb / 2.0)) / max_rgb;
            double del_b = (((max_rgb - norm_b) / 6.0) + (max_rgb / 2.0)) / max_rgb;

            if (norm_r == max_rgb) {
                h = del_b - del_g;
            } else if (norm_g == max_rgb) {
                h = (1 / 3.0) + del_r - del_b;
            } else if (norm_b == max_rgb) {
                h = (2 / 3.0) + del_g - del_r;
            }

            if (h < 0) {
                h += 1;
            }
            if (h > 1) {
                h -= 1;
            }
        }
        return new HLS(h, l, s);
    }

    public RGB hlsToRgb(double h, double l, double s) {
        double r, g, b;

        if (s == 0) {
            r = g = b = l;
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = hue2rgb(p, q, h + 1 / 3.0);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1 / 3.0);
        }
        return new RGB((int)(255 * r), (int)(255 * g), (int)(255 * b));
    }

    private double hue2rgb(double p, double q, double t) {
        if (t < 0) {
            t += 1;
        }
        if (t > 1) {
            t -= 1;
        }
        if (t < 1 / 6.0) {
            return p + (q - p) * 6 * t;
        }
        if (t < 1 / 2.0) {
            return q;
        }
        if (t < 2 / 3.0) {
            return p + (q - p) * (2 / 3.0 - t) * 6;
        }
        return p;
    }



    public LUV rgbToLuv(int r, int g, int b) {
        double var_r = r / 255.0;
        double var_g = g / 255.0;
        double var_b = b / 255.0;

        if (var_r > 0.04045) {
            var_r = Math.pow((var_r + 0.055) / 1.055, 2.4);
        } else {
            var_r /= 12.92;
        }

        if (var_g > 0.04045) {
            var_g = Math.pow((var_g + 0.055) / 1.055, 2.4);
        } else {
            var_g /= 12.92;
        }

        if (var_b > 0.04045) {
            var_b = Math.pow((var_b + 0.055) / 1.055, 2.4);
        } else {
            var_b /= 12.92;
        }

        var_r *= 100;
        var_g *= 100;
        var_b *= 100;

        double x = var_r * 0.4124 + var_g * 0.3576 + var_b * 0.1805;
        double y = var_r * 0.2126 + var_g * 0.7152 + var_b * 0.0722;
        double z = var_r * 0.0193 + var_g * 0.1192 + var_b * 0.9505;

        return xyzToLuv(x, y, z);
    }

    private LUV xyzToLuv(double x, double y, double z) {
        double denom = (x + 15 * y + 3 * z);
        double u_ = (4 * x) / denom;
        double v_ = (9 * y) / denom;

        double y_ = y / 100.0;

        if (y_ > 0.008856) {
            y_ = Math.pow(y_, 1 / 3.0);
        } else {
            y_ = 7.787 * y_ + 16 / 116.0;
        }

        double ref_x = 95.047;
        double ref_y = 100.000;
        double ref_z = 108.883;

        double ref_u = (4 * ref_x) / (ref_x + 15 * ref_y + 3 * ref_z);
        double ref_v = (9 * ref_y) / (ref_x + 15 * ref_y + 3 * ref_z);

        double resL = 116 * y_ - 16;
        double resU = 13 * resL * (u_ - ref_u);
        double resV = 13 * resL * (v_ - ref_v);

        if (denom == 0) {
            resU = 0;
            resV = 0;
        }
        return new LUV(resL, resU, resV);
    }

    private XYZ luvToXyz(double l, double u, double v) {
        double var_y = (l + 16) / 116.0;
        if (Math.pow(var_y, 3) > 0.008856) {
            var_y = Math.pow(var_y, 3);
        } else {
            var_y = (var_y - 16 / 116.0) / 7.787;
        }

        double ref_x = 95.047;
        double ref_y = 100.000;
        double ref_z = 108.883;

        double ref_u = (4 * ref_x) / (ref_x + 15 * ref_y + 3 * ref_z);
        double ref_v = (9 * ref_y) / (ref_x + 15 * ref_y + 3 * ref_z);

        double var_u = u / (13 * l) + ref_u;
        double var_v = v / (13 * l) + ref_v;

        double y = var_y * 100;
        double x = -(9 * y * var_u) / ((var_u - 4) * var_v - var_u * var_v);
        double z = (9 * y - (15 * var_v * y) - (var_v * x)) / (3 * var_v);

        return new XYZ(x, y, z);
    }

    public RGB luvToRgb(double l, double u, double v) {
        XYZ xyz = luvToXyz(l, u, v);
        double x = xyz.getX();
        double y = xyz.getY();
        double z = xyz.getZ();

        double var_x = x / 100.0;
        double var_y = y / 100.0;
        double var_z = z / 100.0;

        double var_r = var_x *  3.2406 + var_y * -1.5372 + var_z * -0.4986;
        double var_g = var_x * -0.9689 + var_y *  1.8758 + var_z *  0.0415;
        double var_b = var_x *  0.0557 + var_y * -0.2040 + var_z *  1.0570;

        if (var_r > 0.0031308) {
            var_r = 1.055 * Math.pow(var_r, 1 / 2.4) - 0.055;
        } else {
            var_r *= 12.92;
        }
        if (var_g > 0.0031308) {
            var_g = 1.055 * Math.pow(var_g, 1 / 2.4) - 0.055;
        } else {
            var_g *= 12.92;
        }
        if (var_b > 0.0031308) {
            var_b = 1.055 * Math.pow(var_b, 1 / 2.4) - 0.055;
        }
        else {
            var_b *= 12.92;
        }


        double r = var_r * 255;
        double g = var_g * 255;
        double b = var_b * 255;

        return new RGB((int) r, (int) g, (int) b);
    }

    private double checkRgb(double value)
    {
        if (value < 0)
            return 0;
        if (value > 255)
            return 255;
        return value;
    }
}
