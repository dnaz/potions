package com.example.potions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

        mPlayerName = getContext().getString(R.string.default_player_name);
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

    public void setPlayerName(String mPlayerName) {
        this.mPlayerName = mPlayerName;
        resetTextView();
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
        public boolean onLongClick(final View view) {
            final EditText editText = new EditText(view.getContext());
            editText.setText(mPlayerName);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
            editText.setSelectAllOnFocus(true);

            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
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
                    .create();

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    editText.requestFocus();
                    final InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
            });
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    final InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            });
            alertDialog.show();
            return true;
        }
    }
}
