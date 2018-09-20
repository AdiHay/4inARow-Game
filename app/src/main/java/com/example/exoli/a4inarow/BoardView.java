package com.example.exoli.a4inarow;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exoli.a4inarow.classes.AI;
import com.example.exoli.a4inarow.classes.Game;
import com.example.exoli.a4inarow.classes.Logic;
import com.example.exoli.a4inarow.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.exoli.a4inarow.GameplayControl.ROWS;
import static com.example.exoli.a4inarow.GameplayControl.COLS;

public class BoardView extends RelativeLayout {

    private class PlayerInfo {
        public TextView txtName;
        public ImageView coin;
        public final View turn;

        public PlayerInfo(int playerNameID, int playerCoinID,int playerTurn) {
            txtName = (TextView) findViewById(playerNameID);
            coin = (ImageView) findViewById(playerCoinID);
            turn = findViewById(playerTurn);
        }
    }

    private GameRules gameRules;
    private GameplayControl gameplayControl;
    private PlayerInfo p1, p2;
    private ImageView[][] cells;
    private View board;
    private TextView winner;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private User user = new User(currentUser.getEmail() , currentUser.getDisplayName(), currentUser.getUid());
    private Game game;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference("games");

    public ImageView[][] getCells() {
        return cells;
    }

    public BoardView(Context context) {
        super(context);
        init(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.game_board, this);
        p1 = new PlayerInfo(R.id.player1_name, R.id.player1_disc, R.id.player1_indicator);
        p2 = new PlayerInfo(R.id.player2_name, R.id.player2_disc, R.id.player2_indicator);
        board = findViewById(R.id.game_board);
        winner = (TextView) findViewById(R.id.winner_text);
    }

    public void initialize(GameplayControl gameplayControl, GameRules gameRules) {
        this.gameRules = gameRules;
        this.gameplayControl = gameplayControl;
        setPlayer1();
        setPlayer2();
        togglePlayer(gameRules.getRule(GameRules.FIRST_TURN));
        buildCells();
    }

    private void setPlayer1() {
        p1.coin.setImageResource(R.drawable.red_disc);
       // p1.coin.setImageResource(gameRules.getRule(GameRules.COIN1));
        p1.txtName.setText(context.getString(R.string.you));
    }

    private void setPlayer2() {
        p2.coin.setImageResource(R.drawable.yellow_disc);
       // p2.coin.setImageResource(gameRules.getRule(GameRules.COIN2));
        p2.txtName.setText(gameRules.getRule(GameRules.OPPONENT) == R.string.opponent_ai ?
                context.getString(R.string.opponent_ai) : context.getString(R.string.opponent_player));
    }

    private void buildCells() {
        cells = new ImageView[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            ViewGroup row = (ViewGroup) ((ViewGroup) board).getChildAt(r);
            row.setClipChildren(false);
            for (int c = 0; c < COLS; c++) {
                ImageView imageView = (ImageView) row.getChildAt(c);
                imageView.setImageResource(android.R.color.transparent);
                imageView.setOnClickListener(gameplayControl);
                cells[r][c] = imageView;
            }
        }
    }

    public void resetBoard() {
        //clear board mCells
        for (ImageView[] cell : cells) {
            for (ImageView imageView : cell) {
                imageView.setImageResource(android.R.color.transparent);
            }
        }
        togglePlayer(gameRules.getRule(GameRules.FIRST_TURN));
        showWinStatus(Logic.Status.CONTINUE, null);
    }

    private void playEffect(int id) {

        MediaPlayer effect = MediaPlayer.create(this.getContext(), id);
        effect.start();
    }



    public void dropCoin(int row, int col, final int playerTurn) {

        final ImageView cell = cells[row][col];
        float move = -(cell.getHeight() * row + cell.getHeight() + 15);
        cell.setY(move);
        cell.setImageResource(playerTurn == AI.USER ?
                R.drawable.red_disc : R.drawable.yellow_disc);
        //gameRules.getRule(GameRules.COIN1) : gameRules.getRule(GameRules.COIN2));
        playEffect(R.raw.coindrop);
        cell.animate().translationY(0).setInterpolator(new BounceInterpolator()).start();
    }

    public int colAtX(float x) {
        float colWidth = cells[0][0].getWidth();
        int col = (int) x / (int) colWidth;
        if (col < 0)
            return 0;
        if (col > 6)
            return 6;
        return col;
    }

    /**
     * toggle player indicator
     *
     * @param playerTurn next players value
     */
    public void togglePlayer(int playerTurn) {
        p1.turn.setVisibility(playerTurn == AI.USER ? VISIBLE : INVISIBLE);
        p2.turn.setVisibility(playerTurn == AI.AI_USER ? VISIBLE : INVISIBLE);
       // comp.turn.setVisability(playerTurn == AI.AI_USER ? VISIBLE : INVISIBLE);
    }

    public void showWinStatus(Logic.Status status, ArrayList<ImageView> winDiscs) {
        if (status != Logic.Status.CONTINUE) {

            winner.setVisibility(VISIBLE);
            p1.turn.setVisibility(INVISIBLE);
            p2.turn.setVisibility(INVISIBLE);
            switch (status) {
                case DRAW:
                    winner.setText(context.getString(R.string.draw));
                    playEffect(R.raw.lose);
                    break;
                case WIN_P1:
                    winner.setText(context.getString(R.string.you_win));
                    for (ImageView winDisc : winDiscs) {
                        winDisc.setImageResource(R.drawable.win_red);
                        //winDisc.setImageResource(gameRules.getRule(GameRules.COIN1) == GameRules.Coin.RED ?
                        //        R.drawable.win_red : R.drawable.win_yellow);
                    }
                    game = new Game(user, User.guest());
                    db.child(game.getGameTag()).setValue(game);
                    playEffect(R.raw.win);
                    break;
                case WIN_P2:
                    if(gameRules.getRule(GameRules.OPPONENT) == R.string.opponent_ai) {
                        winner.setText(R.string.comp_win);
                        game = new Game(User.AIUser(), user);
                        playEffect(R.raw.lose);
                    }
                    else {
                        winner.setText(gameRules.getRule(GameRules.OPPONENT) == GameRules.Opponent.AI ?
                                context.getString(R.string.you_lose) : context.getString(R.string.friend_win));
                        game = new Game(User.guest(), user);
                        db.child(game.getGameTag()).setValue(game);
                        playEffect(R.raw.win);
                    }
                    for (ImageView winDisc : winDiscs) {
                        winDisc.setImageResource(R.drawable.win_yellow);

                        // winDisc.setImageResource(gameRules.getRule(GameRules.COIN2) == GameRules.Coin.RED ?
                        //R.drawable.win_yellow : R.drawable.win_red);


                    }
                    break;
                default: {
                    break;
                }
            }
        } else {
            winner.setVisibility(INVISIBLE);
        }
    }
}
