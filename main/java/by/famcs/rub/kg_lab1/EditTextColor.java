package by.famcs.rub.kg_lab1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**

 */
public class EditTextColor extends EditText {
    private MainActivity.Palette currentPalette;
    private int sbPosition;

    public int getSbPosition() {
        return sbPosition;
    }

    public void setSbPosition(int sbPosition) {
        this.sbPosition = sbPosition;
    }

    public MainActivity.Palette getCurrentPalette() {
        return currentPalette;
    }

    public void setCurrentPalette(MainActivity.Palette currentPalette) {
        this.currentPalette = currentPalette;
    }

    public EditTextColor(Context context) {
        super(context);

    }

    public EditTextColor(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    EditText editText = (EditText) v;
                    if (editText.getText().length() == 0) {
                        editText.setText("0");
                    }

                }
            }

        });
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EditText editText = (EditText) v;
                MainActivity mainActivity = (MainActivity) context;

                try {
                    int value = Integer.parseInt(editText.getText().toString());
                    switch (getCurrentPalette()) {
                        case RGB:
                            if (value > 255) {
                                value = 255;
                            }

                            break;
                        case HLS:
                            if (value > 360 && getSbPosition() == 1) {
                                value = 360;
                            } else if (value > 100 && (getSbPosition() == 2 ||
                                    getSbPosition() == 3)) {
                                value = 100;
                            }
                            break;
                        case LUV:
                            if (value > 100 && getSbPosition() == 1) {
                                value = 100;
                            } else if (value > 100 && (getSbPosition() == 2 ||
                                    getSbPosition() == 3)) {
                                value = 200;
                            }
                            break;
                        case CMY:
                            if (value > 100)
                                value = 100;
                            break;
                    }
                    mainActivity.globalChangeColor(value, getSbPosition());
                } catch (NumberFormatException exp) {
                    mainActivity.globalChangeColor(0, getSbPosition());
                }


                return true;
            }
        });

    }

    public EditTextColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditTextColor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
