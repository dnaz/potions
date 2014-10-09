package com.example.potions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MyActivity extends Activity {

    private Toast mNegativeScoreAlert;
    private List<TextView> mScoreViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        fillScoreViewList((LinearLayout)findViewById(R.id.root_layout));

        mNegativeScoreAlert = Toast.makeText(this, getString(R.string.negative_score_alert), Toast.LENGTH_SHORT);
    }

    private void fillScoreViewList(LinearLayout rootView) {
        mScoreViews = new ArrayList<TextView>();
        for (int i =0; i<rootView.getChildCount(); i++) {
            LinearLayout layout = ((LinearLayout) rootView.getChildAt(i));
            for (int j = 0; j<layout.getChildCount(); j++){
                mScoreViews.add(getTextView(layout.getChildAt(j)));
            }
        }
    }

    private TextView getTextView(View view) {
      return  (TextView) ((LinearLayout) view).getChildAt(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about_menu) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.about_alert_title)
                    .setMessage(R.string.about_alert_message)
                    .setNeutralButton(R.string.close_button_title, null)
                    .show();
        } else if (id == R.id.action_clear_results_menu) {
            resetScores();
        } else if (id == R.id.action_show_winner_menu) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.show_winner_dialog_title)
                    .setView(buildWinnersView())
                    .setPositiveButton(R.string.close_button_title, null)
                    .setNegativeButton(R.string.clear_results_button_title, new ResetListener())
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private View buildWinnersView() {
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        rootLayout.setPadding(0,30,0,0);

        TextView winnersTextView = new TextView(this);
        winnersTextView.setText("Winner(s): ");
        winnersTextView.setTextSize(20);
        rootLayout.addView(winnersTextView);

        LinearLayout winnersLayout = new LinearLayout(this);
        winnersLayout.setOrientation(LinearLayout.HORIZONTAL);
        winnersLayout.setBackgroundColor(Color.GRAY);
        for(TextView winner: findWinners()) {
            TextView textView = new TextView(this);
            textView.setWidth(32);
            textView.setHeight(32);
            textView.setBackgroundResource(R.drawable.backtext);

            ColorDrawable cd = (ColorDrawable) winner.getBackground();
            int colorCode = cd.getColor();

            textView.setBackgroundColor(colorCode);
            winnersLayout.addView(textView);
        }

        rootLayout.addView(winnersLayout);
        return rootLayout;
    }

    private void resetScores() {
        String defaultScore = getString(R.string.default_score);
        for (TextView textView : mScoreViews) {
            textView.setText(defaultScore);
        }
    }

    public void onButtonClick(View view) {
        LinearLayout layout = (LinearLayout) view.getParent().getParent();
        TextView textView = (TextView) layout.getChildAt(0);

        Button button = (Button) view;
        CharSequence operation = button.getText();
        int score = getScoreByTextView(textView);
        if (operation.equals("+")) {
            score = score + 1;
        } else {
            int defaultScore = Integer.valueOf(getString(R.string.default_score));
            if (score != defaultScore) {
                score = score - 1;
            } else {
                showNegativeScoreAlert();
            }
        }

        textView.setText(String.valueOf(score));

    }

    private void showNegativeScoreAlert() {
        if (!mNegativeScoreAlert.getView().isShown()) {
            mNegativeScoreAlert.show();
        }
    }

    private List<TextView> findWinners() {
        Collections.sort(mScoreViews, new ScoreTextViewComparator());
        int maxResult = getScoreByTextView(mScoreViews.get(0));
        List<TextView> maxScores = new ArrayList<TextView>();
        for (TextView textView : mScoreViews) {
            if (getScoreByTextView(textView) == maxResult) {
                maxScores.add(textView);
            } else {
                break;
            }
        }
        return maxScores;
    }

    private static int getScoreByTextView(TextView textView) {
        return Integer.valueOf(textView.getText().toString());
    }

    private class ResetListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            resetScores();
        }
    }

    private static class ScoreTextViewComparator implements Comparator<TextView> {

        @Override
        public int compare(TextView a, TextView b) {
            int first = getScoreByTextView(a);
            int second = getScoreByTextView(b);
            return first < second ?
                    1 : first > second ? -1 : 0;
        }
    }

}
