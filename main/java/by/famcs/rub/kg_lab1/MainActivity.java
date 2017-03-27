package by.famcs.rub.kg_lab1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {
    public enum Palette {
        RGB,
        HLS,
        LUV,
        CMY
    }
    private ColorConverter colorConverter;
    private Palette currentPalette;

    private ImageButton colorArea;
    private AmbilWarnaDialog colorPicker;

    private int currentColor;

    private int currentFirstValue;
    private int currentSecondValue;
    private int currentThirdValue;

    private SeekBar sbFirst;
    private SeekBar sbSecond;
    private SeekBar sbThird;

    private TextView mainFirstLabel;
    private TextView mainSecondLabel;
    private TextView mainThirdLabel;

    private TextView mainFirstValue;
    private TextView mainSecondValue;
    private TextView mainThirdValue;

    private RadioGroup rgPaletteRadioGroup;

    private RadioButton rbRGB;
    private RadioButton rbHLS;
    private RadioButton rbLuv;
    private RadioButton rbCMY;

    private EditTextColor etRRgb;
    private EditTextColor etGRgb;
    private EditTextColor etBRgb;

    private EditTextColor etHHls;
    private EditTextColor etLHls;
    private EditTextColor etSHls;

    private EditTextColor etLLuv;
    private EditTextColor etULuv;
    private EditTextColor etVLuv;

    private EditTextColor etCCmy;
    private EditTextColor etMCmy;
    private EditTextColor etYCmy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setComponents();
        setSeekBarListeners();
        setRadioButtonListeners();
        colorArea.setOnClickListener(getColorAreaClickListener());
    }

    private void setSeekBarListeners() {
        sbFirst.setOnSeekBarChangeListener(getFirstSBChangeListener(1));
        sbSecond.setOnSeekBarChangeListener(getFirstSBChangeListener(2));
        sbThird.setOnSeekBarChangeListener(getFirstSBChangeListener(3));
    }

    private void setRadioButtonListeners() {
        rbRGB.setOnClickListener(getRGBChangeListener());
        rbHLS.setOnClickListener(getHLSChangeListener());
        rbLuv.setOnClickListener(getLUVChangeListener());
        rbCMY.setOnClickListener(getCMYChangeListener());

    }

    private SeekBar.OnSeekBarChangeListener getFirstSBChangeListener(final int pos) {
        SeekBar.OnSeekBarChangeListener seekBarChangeListener =
                new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                globalChangeColor(progress, pos);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        return seekBarChangeListener;
    }

    public void globalChangeColor(int progress, int sbPos) {
        final ColorConverter.CMY[] cmy = new ColorConverter.CMY[1];
        final ColorConverter.HLS[] hls = new ColorConverter.HLS[1];
        final ColorConverter.LUV[] luv = new ColorConverter.LUV[1];
        final ColorConverter.RGB[] rgb = new ColorConverter.RGB[1];
        final long[] hexColor = new long[1];
        switch (sbPos) {
            case 1:
                mainFirstValue.setText(String.valueOf(progress));
                currentFirstValue = progress;
                sbFirst.setProgress(progress);
                break;
            case 2:
                if (currentPalette.equals(Palette.LUV)) {
                    mainSecondValue.setText(String.valueOf(progress - 100));
                    currentSecondValue = progress - 100;

                } else {
                    mainSecondValue.setText(String.valueOf(progress));
                    currentSecondValue = progress;

                }
                sbSecond.setProgress(progress);
                break;
            case 3:
                if (currentPalette.equals(Palette.LUV)) {
                    mainThirdValue.setText(String.valueOf(progress - 100));
                    currentThirdValue = progress - 100;
                } else {
                    mainThirdValue.setText(String.valueOf(progress));
                    currentThirdValue = progress;
                }
                sbThird.setProgress(progress);
                break;
        }

        switch (currentPalette) {
            case RGB:
                cmy[0] = colorConverter.rgbToCmy(currentFirstValue,
                        currentSecondValue, currentThirdValue);
                hls[0] = colorConverter.rgbToHls(currentFirstValue,
                        currentSecondValue, currentThirdValue);
                luv[0] = colorConverter.rgbToLuv(currentFirstValue,
                        currentSecondValue, currentThirdValue);
                rgb[0] = new ColorConverter().new RGB(currentFirstValue,
                        currentSecondValue, currentThirdValue);
                break;
            case HLS:
                rgb[0] = colorConverter.hlsToRgb(currentFirstValue / 360.0,
                        currentSecondValue / 100.0, currentThirdValue / 100.0);
                luv[0] = colorConverter.rgbToLuv(rgb[0].getR(), rgb[0].getG(),
                        rgb[0].getB());
                cmy[0] = colorConverter.rgbToCmy(rgb[0].getR(), rgb[0].getG(),
                        rgb[0].getB());
                hls[0] = new ColorConverter().new HLS((double)currentFirstValue / 360,
                        (double)currentSecondValue / 100, (double)currentThirdValue / 100);

                break;
            case LUV:
                rgb[0] = colorConverter.luvToRgb(currentFirstValue,
                        currentSecondValue, currentThirdValue);
                cmy[0] = colorConverter.rgbToCmy(rgb[0].getR(), rgb[0].getG(),
                        rgb[0].getB());
                hls[0] = colorConverter.rgbToHls(rgb[0].getR(), rgb[0].getG(),
                        rgb[0].getB());
                luv[0] = new ColorConverter().new LUV(currentFirstValue,
                        currentSecondValue, currentThirdValue);
                break;
            case CMY:
                rgb[0] = colorConverter.cmyToRgb(currentFirstValue / 100.0,
                        currentSecondValue / 100.0, currentThirdValue / 100.0);
                luv[0] = colorConverter.rgbToLuv(rgb[0].getR(), rgb[0].getG(),
                        rgb[0].getB());
                hls[0] = colorConverter.rgbToHls(rgb[0].getR(), rgb[0].getG(),
                        rgb[0].getB());
                cmy[0] = new ColorConverter().new CMY((double)currentFirstValue / 100,
                        (double)currentSecondValue / 100, (double)currentThirdValue / 100);
                break;
        }
        if (rgb[0].getR() < 0 || rgb[0].getR() > 255 || rgb[0].getG() < 0 || rgb[0].getG() > 255 ||
                rgb[0].getB() < 0 || rgb[0].getB() > 255) {

            return;
        }
        hexColor[0] = colorConverter.rgbToHex(rgb[0].getR(), rgb[0].getG(),
                rgb[0].getB());

        etRRgb.setText(String.valueOf(rgb[0].getR()));
        etGRgb.setText(String.valueOf(rgb[0].getG()));
        etBRgb.setText(String.valueOf(rgb[0].getB()));
        etHHls.setText(String.format("%.2f", hls[0].getH() * 360));
        etLHls.setText(String.format("%.2f", hls[0].getL() * 100));
        etSHls.setText(String.format("%.2f", hls[0].getS() * 100));
        etLLuv.setText(String.format("%.3f", luv[0].getL()));
        etULuv.setText(String.format("%.3f", luv[0].getU()));
        etVLuv.setText(String.format("%.3f", luv[0].getV()));
        etCCmy.setText(String.format("%.3f", cmy[0].getC() * 100));
        etMCmy.setText(String.format("%.3f", cmy[0].getM() * 100));
        etYCmy.setText(String.format("%.3f", cmy[0].getY() * 100));

        currentColor = (int)hexColor[0];

        colorArea.setBackgroundColor(currentColor);
    }

    private void setCurrentValues(int f, int s, int t) {
        currentFirstValue = f;
        currentSecondValue = s;
        currentThirdValue = t;
    }

    private RadioButton.OnClickListener getRGBChangeListener() {
        RadioButton.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPalette = Palette.RGB;

                etRRgb.setCurrentPalette(Palette.RGB);
                etGRgb.setCurrentPalette(Palette.RGB);
                etBRgb.setCurrentPalette(Palette.RGB);

                etRRgb.requestFocus();

                ColorConverter.RGB rgb = new ColorConverter().hexToRgb(currentColor);
                setCurrentValues(rgb.getR(), rgb.getG(), rgb.getB());

                mainFirstLabel.setText(R.string.component_R_rgb);
                mainSecondLabel.setText(R.string.component_G_rgb);
                mainThirdLabel.setText(R.string.component_B_rgb);

                sbFirst.setMax(255);
                sbSecond.setMax(255);
                sbThird.setMax(255);

                sbFirst.setProgress(rgb.getR());
                sbSecond.setProgress(rgb.getG());
                sbThird.setProgress(rgb.getB());

                etRRgb.setEnabled(true);
                etGRgb.setEnabled(true);
                etBRgb.setEnabled(true);

                etHHls.setEnabled(false);
                etLHls.setEnabled(false);
                etSHls.setEnabled(false);

                etLLuv.setEnabled(false);
                etULuv.setEnabled(false);
                etVLuv.setEnabled(false);

                etCCmy.setEnabled(false);
                etMCmy.setEnabled(false);
                etYCmy.setEnabled(false);
            }
        };
        return clickListener;
    }



    private RadioButton.OnClickListener getHLSChangeListener() {
        RadioButton.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPalette = Palette.HLS;

                etHHls.setCurrentPalette(Palette.HLS);
                etLHls.setCurrentPalette(Palette.HLS);
                etSHls.setCurrentPalette(Palette.HLS);

                etHHls.requestFocus();

                mainFirstLabel.setText(R.string.component_H_hls);
                mainSecondLabel.setText(R.string.component_L_hls);
                mainThirdLabel.setText(R.string.component_S_hls);

                ColorConverter.RGB rgb = new ColorConverter().hexToRgb(currentColor);
                ColorConverter.HLS hls = new ColorConverter().rgbToHls(rgb.getR(),
                        rgb.getG(), rgb.getB());
                setCurrentValues((int) hls.getH(), (int) hls.getL(), (int) hls.getS());

                sbFirst.setMax(359);
                sbSecond.setMax(100);
                sbThird.setMax(100);

                sbFirst.setProgress((int) (hls.getH() * 360));
                sbSecond.setProgress((int) (hls.getL() * 100));
                sbThird.setProgress((int) (hls.getS() * 100));



                etRRgb.setEnabled(false);
                etGRgb.setEnabled(false);
                etBRgb.setEnabled(false);

                etHHls.setEnabled(true);
                etLHls.setEnabled(true);
                etSHls.setEnabled(true);

                etLLuv.setEnabled(false);
                etULuv.setEnabled(false);
                etVLuv.setEnabled(false);

                etCCmy.setEnabled(false);
                etMCmy.setEnabled(false);
                etYCmy.setEnabled(false);
            }
        };
        return clickListener;
    }

    private RadioButton.OnClickListener getLUVChangeListener() {
        RadioButton.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPalette = Palette.LUV;

                etLLuv.setCurrentPalette(Palette.LUV);
                etULuv.setCurrentPalette(Palette.LUV);
                etVLuv.setCurrentPalette(Palette.LUV);

                etLLuv.requestFocus();

                mainFirstLabel.setText(R.string.component_L_luv);
                mainSecondLabel.setText(R.string.component_u_luv);
                mainThirdLabel.setText(R.string.component_v_luv);

                ColorConverter.RGB rgb = new ColorConverter().hexToRgb(currentColor);
                ColorConverter.LUV luv = new ColorConverter().rgbToLuv(rgb.getR(),
                        rgb.getG(), rgb.getB());
                setCurrentValues((int) luv.getL(), (int) luv.getU(), (int) luv.getV());

                sbFirst.setMax(100);
                sbSecond.setMax(200);
                sbThird.setMax(200);

                sbFirst.setProgress((int)luv.getL());
                sbSecond.setProgress((int) (luv.getU() + 100));
                sbThird.setProgress((int)(luv.getV() + 100));



                etRRgb.setEnabled(false);
                etGRgb.setEnabled(false);
                etBRgb.setEnabled(false);

                etHHls.setEnabled(false);
                etLHls.setEnabled(false);
                etSHls.setEnabled(false);

                etLLuv.setEnabled(true);
                etULuv.setEnabled(true);
                etVLuv.setEnabled(true);

                etCCmy.setEnabled(false);
                etMCmy.setEnabled(false);
                etYCmy.setEnabled(false);
            }
        };
        return clickListener;
    }

    private RadioButton.OnClickListener getCMYChangeListener() {
        RadioButton.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPalette = Palette.CMY;

                etCCmy.setCurrentPalette(Palette.CMY);
                etMCmy.setCurrentPalette(Palette.CMY);
                etYCmy.setCurrentPalette(Palette.CMY);

                etCCmy.requestFocus();

                mainFirstLabel.setText(R.string.component_C_cmy);
                mainSecondLabel.setText(R.string.component_M_cmy);
                mainThirdLabel.setText(R.string.component_Y_cmy);

                ColorConverter.RGB rgb = new ColorConverter().hexToRgb(currentColor);
                ColorConverter.CMY cmy = new ColorConverter().rgbToCmy(rgb.getR(),
                        rgb.getG(), rgb.getB());
                setCurrentValues((int) cmy.getC(), (int) cmy.getM(), (int) cmy.getY());

                sbFirst.setMax(100);
                sbSecond.setMax(100);
                sbThird.setMax(100);

                sbFirst.setProgress((int) (cmy.getC() * 100));
                sbSecond.setProgress((int) (cmy.getM() * 100));
                sbThird.setProgress((int) (cmy.getY() * 100));



                etRRgb.setEnabled(false);
                etGRgb.setEnabled(false);
                etBRgb.setEnabled(false);

                etHHls.setEnabled(false);
                etLHls.setEnabled(false);
                etSHls.setEnabled(false);

                etLLuv.setEnabled(false);
                etULuv.setEnabled(false);
                etVLuv.setEnabled(false);

                etCCmy.setEnabled(true);
                etMCmy.setEnabled(true);
                etYCmy.setEnabled(true);
            }
        };
        return clickListener;
    }

    private void setComponents() {
        currentColor = 0xff000000;
        currentFirstValue = 0;
        currentSecondValue = 0;
        currentThirdValue = 0;
        currentPalette = Palette.RGB;

        colorConverter = new ColorConverter();
        colorArea = (ImageButton) findViewById(R.id.ibColorArea);
        colorPicker = getAmbilWarnaDialog();

        sbFirst = (SeekBar) findViewById(R.id.sbFirstValue);
        sbSecond = (SeekBar) findViewById(R.id.sbSecondValue);
        sbThird = (SeekBar) findViewById(R.id.sbThirdValue);

        mainFirstLabel = (TextView) findViewById(R.id.tvMainFirstLabel);
        mainSecondLabel = (TextView) findViewById(R.id.tvMainSecondLabel);
        mainThirdLabel = (TextView) findViewById(R.id.tvMainThirdLabel);

        mainFirstValue = (TextView) findViewById(R.id.tvMainFirstValue);
        mainSecondValue = (TextView) findViewById(R.id.tvMainSecondValue);
        mainThirdValue = (TextView) findViewById(R.id.tvMainThirdValue);

        rgPaletteRadioGroup = (RadioGroup) findViewById(R.id.rgPalette);

        ColorConverter.RGB rgb = new ColorConverter().new RGB(currentFirstValue,
                currentSecondValue, currentThirdValue);
        ColorConverter.HLS hls = new ColorConverter().rgbToHls(rgb.getR(), rgb.getG(), rgb.getB());
        ColorConverter.LUV luv = new ColorConverter().rgbToLuv(rgb.getR(), rgb.getG(), rgb.getB());
        ColorConverter.CMY cmy = new ColorConverter().rgbToCmy(rgb.getR(), rgb.getG(), rgb.getB());

        rbRGB = (RadioButton) findViewById(R.id.rbRGB);
        rbRGB.setChecked(true);
        rbHLS = (RadioButton) findViewById(R.id.rbHLS);
        rbLuv = (RadioButton) findViewById(R.id.rbLuv);
        rbCMY = (RadioButton) findViewById(R.id.rbCMY);

        etRRgb = (EditTextColor) findViewById(R.id.etR_rgb);
        etGRgb = (EditTextColor) findViewById(R.id.etG_rgb);
        etBRgb = (EditTextColor) findViewById(R.id.etB_rgb);

        etHHls = (EditTextColor) findViewById(R.id.etH_hls);
        etLHls = (EditTextColor) findViewById(R.id.etL_hls);
        etSHls = (EditTextColor) findViewById(R.id.etS_hls);

        etLLuv = (EditTextColor) findViewById(R.id.etL_luv);
        etULuv = (EditTextColor) findViewById(R.id.etU_luv);
        etVLuv = (EditTextColor) findViewById(R.id.etV_luv);

        etCCmy = (EditTextColor) findViewById(R.id.etC_cmy);
        etMCmy = (EditTextColor) findViewById(R.id.etM_cmy);
        etYCmy = (EditTextColor) findViewById(R.id.etY_cmy);

        etRRgb.setCurrentPalette(Palette.RGB);
        etGRgb.setCurrentPalette(Palette.RGB);
        etBRgb.setCurrentPalette(Palette.RGB);

        etHHls.setCurrentPalette(Palette.RGB);
        etLHls.setCurrentPalette(Palette.RGB);
        etSHls.setCurrentPalette(Palette.RGB);

        etLLuv.setCurrentPalette(Palette.RGB);
        etULuv.setCurrentPalette(Palette.RGB);
        etVLuv.setCurrentPalette(Palette.RGB);

        etCCmy.setCurrentPalette(Palette.RGB);
        etMCmy.setCurrentPalette(Palette.RGB);
        etYCmy.setCurrentPalette(Palette.RGB);

        etRRgb.setSbPosition(1);
        etGRgb.setSbPosition(2);
        etBRgb.setSbPosition(3);

        etHHls.setSbPosition(1);
        etLHls.setSbPosition(2);
        etSHls.setSbPosition(3);

        etLLuv.setSbPosition(1);
        etULuv.setSbPosition(2);
        etVLuv.setSbPosition(3);

        etCCmy.setSbPosition(1);
        etMCmy.setSbPosition(2);
        etYCmy.setSbPosition(3);

        etRRgb.setText(String.valueOf(rgb.getR()));
        etGRgb.setText(String.valueOf(rgb.getG()));
        etBRgb.setText(String.valueOf(rgb.getB()));

        etHHls.setText(String.format("%.3f", hls.getH()));
        etLHls.setText(String.format("%.3f", hls.getL()));
        etSHls.setText(String.format("%.3f", hls.getS()));

        etLLuv.setText(String.format("%.3f", luv.getL()));
        etULuv.setText(String.format("%.3f", luv.getU()));
        etVLuv.setText(String.format("%.3f", luv.getV()));

        etCCmy.setText(String.format("%.3f", cmy.getC()));
        etMCmy.setText(String.format("%.3f", cmy.getM()));
        etYCmy.setText(String.format("%.3f", cmy.getY()));

        etHHls.setEnabled(false);
        etLHls.setEnabled(false);
        etSHls.setEnabled(false);

        etLLuv.setEnabled(false);
        etULuv.setEnabled(false);
        etVLuv.setEnabled(false);

        etCCmy.setEnabled(false);
        etMCmy.setEnabled(false);
        etYCmy.setEnabled(false);


        colorArea.setBackgroundColor(currentColor);
    }

    private AmbilWarnaDialog getAmbilWarnaDialog() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, currentColor, false,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        colorArea.setBackgroundColor(color);
                        currentColor = color;
                    }
                });
        return ambilWarnaDialog;
    }

    private View.OnClickListener getColorAreaClickListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker.show();
            }
        };
        return clickListener;
    }
}
