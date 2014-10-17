package com.example.potions;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * TODO: document your custom view class.
 */
public class PlayerView extends RelativeLayout {

    private Toast mNegativeScoreAlert;

    private String mPlayerName = "Player: \n";
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

        findViewById(R.id.buttonPlus).setOnLongClickListener(new OnLongClickListenerImpl());
        findViewById(R.id.buttonPlus).setOnClickListener(new OnClickListenerImpl());
        findViewById(R.id.buttonMinus).setOnLongClickListener(new OnLongClickListenerImpl());
        findViewById(R.id.buttonMinus).setOnClickListener(new OnClickListenerImpl());

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PlayerView, defStyle, 0);

        mPlayerName = a.getString(R.styleable.PlayerView_playerName) == null ? "player" : a.getString(R.styleable.PlayerView_playerName);
        mPlayerColor = a.getColor(R.styleable.PlayerView_playerColor, mPlayerColor);
        mPlayerFontColor = a.getColor(R.styleable.PlayerView_playerFontColor, mPlayerFontColor);
        TextView textView = (TextView) findViewById(R.id.scoreTextView);
        textView.setText(buildPlayerScoreText(getContext().getString(R.string.default_score)));
        textView.setBackgroundColor(mPlayerColor);
        textView.setTextColor(mPlayerFontColor);

        a.recycle();

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

    private static int getScoreByTextView(TextView textView) {
        return Integer.valueOf(textView.getText().toString().split("\n")[1]);
    }


    private void changeScore(View view, int val) {

        Button button = (Button) view;
        TextView textView = (TextView) findViewById(R.id.scoreTextView);
        CharSequence operation = button.getText();
        int score = getScoreByTextView(textView);
        if (operation.equals("+")) {
            score = score + val;
        } else {
            int defaultScore = Integer.valueOf(getContext().getString(R.string.default_score));
            if (score - val >= defaultScore) {     //score must be always greater than default score
                score = score - val;
            } else {
                score = defaultScore;
                showNegativeScoreAlert();
            }
        }

        textView.setText(buildPlayerScoreText(String.valueOf(score)));
    }

    public Integer getScore() {
        TextView textView = (TextView) findViewById(R.id.scoreTextView);
        return getScoreByTextView(textView);
    }

    public void resetScore() {
        TextView textView = (TextView) findViewById(R.id.scoreTextView);
        textView.setText(buildPlayerScoreText(getContext().getString(R.string.default_score)));
    }

    private String buildPlayerScoreText(String score) {
        return mPlayerName + '\n' + score;
    }

    private class OnLongClickListenerImpl implements View.OnLongClickListener {

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
}
