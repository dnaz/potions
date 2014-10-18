package com.example.potions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MyActivity extends Activity {

    private static final List<Integer> playerIds = new ArrayList<Integer>();

    static {
        playerIds.add(R.id.player1);
        playerIds.add(R.id.player2);
        playerIds.add(R.id.player3);
        playerIds.add(R.id.player4);
        playerIds.add(R.id.player5);
        playerIds.add(R.id.player6);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
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
        } else if (id == R.id.action_reset_player_names_menu) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.reset_player_names_title)
                    .setMessage(getString(R.string.reset_score_message))
                    .setNegativeButton(R.string.yes_title, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (Integer playerId : playerIds) {
                                ((PlayerView) (findViewById(playerId))).setPlayerName(getString(R.string.default_player_name));
                            }
                        }
                    }
                    )
                    .setPositiveButton(R.string.no_title, null)
                    .show();

        }
        return super.onOptionsItemSelected(item);
    }

    private View buildWinnersView() {

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView winnersTextView = new TextView(this);
        winnersTextView.setTextSize(20);
        rootLayout.addView(winnersTextView);
        StringBuilder sb = new StringBuilder();

        for (String winner : findWinnerNames()) {
            sb.append(winner);
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        winnersTextView.setText(getString(R.string.winners_header) + sb.toString());
        return rootLayout;
    }

    private void resetScores() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.reset_score_dialog_title)
                .setMessage(getString(R.string.reset_score_message))
                .setNegativeButton(R.string.yes_title, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (Integer playerId : playerIds) {
                                    ((PlayerView) (findViewById(playerId))).resetScore();
                                }
                            }
                        }
                )
                .setPositiveButton(R.string.no_title, null)
                .show();

    }

    private List<String> findWinnerNames() {

        List<PlayerView> allPlayerViews = new ArrayList<PlayerView>();
        for (Integer playerId : playerIds) {
            allPlayerViews.add((PlayerView) findViewById(playerId));
        }
        Collections.sort(allPlayerViews, new ScoreComparator());
        List<String> winnerColors = new ArrayList<String>();
        int maxScore = allPlayerViews.get(0).getScore();
        for (PlayerView playerView : allPlayerViews) {
            if (playerView.getScore() == maxScore) {
                winnerColors.add(playerView.getPlayerName());
            }
        }


        return winnerColors;
    }


    private class ResetListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            resetScores();
        }
    }

    private static class ScoreComparator implements Comparator<PlayerView> {

        @Override
        public int compare(PlayerView a, PlayerView b) {
            int first = a.getScore();
            int second = b.getScore();
            return first < second ?
                    1 : first > second ? -1 : 0;
        }
    }

}
