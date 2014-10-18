package com.example.potions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PlayerView extends RelativeLayout {

    private Toast mNegativeScoreAlert;

    private Integer mScore = 0;
    private String mPlayerName = "Player";
    private int mPlayerColor = Color.BLACK;
    private int mPlayerFontColor = Color.WHITE;

    public PlayerView(Context context) {
        super(context);
        init(null, 0);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            layoutInflater.inflate(R.layout.sample_player, this);
        }

        mNegativeScoreAlert = Toast.makeText(getContext(), getContext().getString(R.string.negative_score_alert), Toast.LENGTH_SHORT);

        findViewById(R.id.buttonPlus).setOnLongClickListener(new OnButtonLongClickListenerImpl());
        findViewById(R.id.buttonPlus).setOnClickListener(new OnClickListenerImpl());
        findViewById(R.id.buttonMinus).setOnLongClickListener(new OnButtonLongClickListenerImpl());
        findViewById(R.id.buttonMinus).setOnClickListener(new OnClickListenerImpl());

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PlayerView, defStyle, 0);

        mPlayerName = a.getString(R.styleable.PlayerView_playerName) == null ? "player" : a.getString(R.styleable.PlayerView_playerName);
        mPlayerColor = a.getColor(R.styleable.PlayerView_playerColor, mPlayerColor);
        mPlayerFontColor = a.getColor(R.styleable.PlayerView_playerFontColor, mPlayerFontColor);
        mScore = Integer.valueOf(getContext().getString(R.string.default_score));
        TextView textView = resetTextView();
        textView.setBackgroundColor(mPlayerColor);
        textView.setTextColor(mPlayerFontColor);
        textView.setOnLongClickListener(new OnTextViewLongClickListenerImpl());
        a.recycle();

    }

    private TextView resetTextView() {
        TextView textView = (TextView) findViewById(R.id.scoreTextView);
        textView.setText(buildPlayerScoreText());
        return textView;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public int getPlayerColor() {
        return mPlayerColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mPlayerName;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mPlayerName = exampleString;
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mPlayerColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mPlayerColor = exampleColor;
    }

    private void showNegativeScoreAlert() {
        if (!mNegativeScoreAlert.getView().isShown()) {
            mNegativeScoreAlert.show();
        }
    }


    private void changeScore(View view, int val) {

        Button button = (Button) view;
        CharSequence operation = button.getText();
        if (operation.equals("+")) {
            mScore = mScore + val;
        } else {
            int defaultScore = Integer.valueOf(getContext().getString(R.string.default_score));
            if (mScore - val >= defaultScore) {     //mScore must be always greater than default mScore
                mScore = mScore - val;
            } else {
                mScore = defaultScore;
                showNegativeScoreAlert();
            }
        }

        resetTextView();
    }

    public Integer getScore() {
        return mScore;
    }

    public void resetScore() {
        mScore = Integer.valueOf(getContext().getString(R.string.default_score));
        resetTextView();
    }

    private String buildPlayerScoreText() {
        return mPlayerName + '\n' + mScore;
    }

    private class OnButtonLongClickListenerImpl implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View view) {
            changeScore(view, 10);
            return true;
        }
    }

    private class OnClickListenerImpl implements OnClickListener {

        @Override
        public void onClick(View view) {
            changeScore(view, 1);
        }
    }

    private class OnTextViewLongClickListenerImpl implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View view) {
            final EditText editText = new EditText(view.getContext());
            editText.setText(mPlayerName);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
            editText.setSelectAllOnFocus(true);
            new AlertDialog.Builder(view.getContext())
                    .setTitle(R.string.set_player_name_title)
                    .setView(editText)
                    .setPositiveButton(R.string.close_button_title, null)
                    .setNegativeButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mPlayerName = editText.getText().toString();
                            resetTextView();
                        }
                    })
                    .show();
            return true;
        }
    }
}
