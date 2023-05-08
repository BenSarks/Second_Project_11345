package com.example.second_project_11345.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.second_project_11345.Interfaces.TiltCallback;
import com.example.second_project_11345.Logic.GameManager;
import com.example.second_project_11345.R;
import com.example.second_project_11345.Utilities.RecordManager;
import com.example.second_project_11345.Utilities.TiltDetector;
import com.example.second_project_11345.Utilities.VB_Producer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String SENSOR_MODE = "com.example.second_project.Activities.MainActivity.SENSOR_MODE";
    public static final String SAVE_RECORD = "com.example.second_project.Activities.MainActivity.SAVE_RECORD";
    public static final String LATLNG_VALUE = "com.example.second_project.Activities.MainActivity.LATLNG_VALUE";
    public static final String DELAY_DURATION = "com.example.second_project.Activities.MainActivity.DELAY_DURATION";
    private final Handler mHandler = new Handler();
    private boolean pauseFlag;
    private boolean sensorMode = false;
    private boolean hasGrantedLocation;
    private boolean endGame = false;
    private LatLng latLng;
    private ShapeableImageView[] lifes;
    private ShapeableImageView[] cars;
    private ShapeableImageView[][] main_IMG_Obstacle_Matrix;
    private ShapeableImageView[][] main_IMG_Coin_Matrix;
    private AppCompatImageButton[] main_Button_Options;
    private SoundPool soundPool;
    MediaPlayer mediaPlayer;
    private int coin_Collected_Sound;
    private int hit_Obstacle_Sound;
    private int delay = 700;
    private int time_count = 0;
    private TextView coinCountTextView;
    private TextView millisecondTextView;
    private GameManager gameManager;
    private final Runnable UPDATE_UI = new Runnable() {
        public void run() {
            if (MainActivity.this.endGame) {
                MainActivity.this.mediaPlayer.start();
            } else {
                MainActivity.this.mHandler.postDelayed(this,delay);
                MainActivity.this.updateGameUI();
            }

        }
    };
    private TiltDetector tiltDetector;

    public MainActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.initAllValues();
        this.setButtonListeners();
        this.displayFirstCoinAndObstacle();
        this.mHandler.postDelayed(this.UPDATE_UI,delay);
    }

    private void initAllValues() {
        this.initSound();
        this.initIntentValues();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.initViewObjectsAndGameManger();
        this.initTiltDetector(this.sensorMode);
    }

    private void initViewObjectsAndGameManger() {
        this.coinCountTextView = this.findViewById(R.id.coin_count_textview);
        this.millisecondTextView = this.findViewById(R.id.time_count_TextView);
        this.lifes = new ShapeableImageView[]{this.findViewById(R.id.life1), this.findViewById(R.id.life2), this.findViewById(R.id.life3)};
        this.main_Button_Options = new AppCompatImageButton[]{this.findViewById(R.id.left_arrow), this.findViewById(R.id.right_arrow)};
        this.cars = new ShapeableImageView[]{this.findViewById(R.id.car),this.findViewById(R.id.car2), this.findViewById(R.id.car3), this.findViewById(R.id.car4), this.findViewById(R.id.car5)};
        GridLayout gameLayout =this.findViewById(R.id.game_gridLayout);
        GridLayout coinLayout = this.findViewById(R.id.coin_gridLayout);
        this.gameManager = new GameManager(gameLayout.getRowCount(), gameLayout.getColumnCount());
        this.main_IMG_Obstacle_Matrix = new ShapeableImageView[this.gameManager.getRows()][this.gameManager.getCols()];
        this.main_IMG_Coin_Matrix = new ShapeableImageView[coinLayout.getRowCount()][coinLayout.getColumnCount()];

        for(int i = 0; i < this.gameManager.getRows(); ++i) {
            for(int j = 0; j < this.gameManager.getCols(); ++j) {
                this.main_IMG_Obstacle_Matrix[i][j] = (ShapeableImageView)gameLayout.getChildAt(i * this.gameManager.getCols() + j);
                if (i < coinLayout.getRowCount()) {
                    this.main_IMG_Coin_Matrix[i][j] = (ShapeableImageView)coinLayout.getChildAt(i * this.gameManager.getCols() + j);
                }
            }
        }

    }

    private void initSound() {
        AudioAttributes audioAttributes = (new AudioAttributes.Builder()).setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        this.soundPool = (new SoundPool.Builder()).setMaxStreams(1).setAudioAttributes(audioAttributes).build();
        this.coin_Collected_Sound = this.soundPool.load(this,R.raw.coin_alert, 1);
        this.hit_Obstacle_Sound = this.soundPool.load(this,R.raw.failure_alert, 1);
        this.mediaPlayer = MediaPlayer.create(this, R.raw.game_over);
        this.mediaPlayer.setOnCompletionListener((mp) -> this.endGame());
    }

    private void initIntentValues() {
        Intent intent = this.getIntent();
        this.delay = intent.getIntExtra(DELAY_DURATION, this.delay);
        this.sensorMode = intent.getBooleanExtra(SENSOR_MODE, this.sensorMode);
        this.hasGrantedLocation = intent.getBooleanExtra(SAVE_RECORD, false);
        if (this.hasGrantedLocation) {
            this.latLng = this.getIntent().getParcelableExtra(LATLNG_VALUE);
        }
    }

    private void initTiltDetector(boolean isSensor) {
        if (isSensor) {
            this.main_Button_Options[0].setVisibility(View.INVISIBLE);
            this.main_Button_Options[1].setVisibility(View.INVISIBLE);
            this.tiltDetector = new TiltDetector(this, new TiltCallback() {
                public void turnLeft() {
                    MainActivity.this.moveCar(MainActivity.this.gameManager.getCurrentCarIndex(), MainActivity.this.gameManager.getCurrentCarIndex() - 1);
                }
                public void turnRight() {
                    MainActivity.this.moveCar(MainActivity.this.gameManager.getCurrentCarIndex(), MainActivity.this.gameManager.getCurrentCarIndex() + 1);
                }
            });
        }

    }

    private void playSound(int sound) {
        this.soundPool.play(sound, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    private void endGame() {
        Intent intent = new Intent(this, ScoreBoardActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void setButtonListeners() {
        this.main_Button_Options[0].setOnClickListener((v) -> this.moveCar(this.gameManager.getCurrentCarIndex(), this.gameManager.getCurrentCarIndex() - 1));
        this.main_Button_Options[1].setOnClickListener((v) -> this.moveCar(this.gameManager.getCurrentCarIndex(), this.gameManager.getCurrentCarIndex() + 1));
    }

    public void moveCar(int previousIndex, int futureIndex) {
        if (this.gameManager.canCarMove(futureIndex)) {
            this.gameManager.setCurrentCarIndex(futureIndex);
            this.setINVISIBLE(this.cars[previousIndex]);
            this.setINVISIBLE(this.main_IMG_Obstacle_Matrix[0][previousIndex]);
            this.setVISIBLE(this.cars[futureIndex]);
            this.setINVISIBLE(this.main_IMG_Obstacle_Matrix[0][futureIndex]);
        }

    }

    protected void onPause() {
        super.onPause();
        if (this.sensorMode) {
            this.tiltDetector.stop();
        }

        this.pauseFlag = true;
        this.mHandler.removeCallbacksAndMessages(null);
    }

    protected void onResume() {
        super.onResume();
        if (this.pauseFlag) {
            this.mHandler.postDelayed(this.UPDATE_UI, this.delay);
        }

        if (this.sensorMode) {
            this.tiltDetector.start();
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        this.soundPool.release();
        this.soundPool = null;
    }

    @SuppressLint({"DefaultLocale"})
    private void updateGameUI() {
        this.millisecondTextView.setText(String.format("%d", this.time_count));
        ++this.time_count;
        if (this.gameManager.getObstacleAtIndex(0) > -1) {
            this.setINVISIBLE(this.main_IMG_Obstacle_Matrix[0][this.gameManager.getObstacleAtIndex(0)]);
        }

        if (this.gameManager.getCoinAtIndex(0) > -1) {
            this.setINVISIBLE(this.main_IMG_Coin_Matrix[0][this.gameManager.getCoinAtIndex(0)]);
        }

        for(int i = 0; i < this.gameManager.getRows() - 1; ++i) {
            this.swapMatrixVisibility(i, this.gameManager.shiftGameIndexForward(i));
        }

        this.displayFirstCoinAndObstacle();
        this.updateLifeAndCoinCount();
    }

    private void displayFirstCoinAndObstacle() {
        if (this.gameManager.setRandomCoinAndObstacle()) {
            this.setVISIBLE(this.main_IMG_Coin_Matrix[this.gameManager.getRows() - 2][this.gameManager.getCoinAtIndex(this.gameManager.getRows() - 2)]);
        }

        this.setVISIBLE(this.main_IMG_Obstacle_Matrix[this.gameManager.getRows() - 1][this.gameManager.getObstacleAtIndex(this.gameManager.getRows() - 1)]);
    }

    @SuppressLint({"SetTextI18n"})
    private void updateLifeAndCoinCount() {
        if (this.gameManager.updateLifeCount()) {
            this.playSound(this.hit_Obstacle_Sound);
            this.setINVISIBLE(this.cars[this.gameManager.getCurrentCarIndex()]);
            this.setINVISIBLE(this.lifes[this.gameManager.disableLifeImageAtIndex()]);
            VB_Producer.getInstance().vibrate(500L);
            VB_Producer.getInstance().toast("💥", 0);
        } else if (this.gameManager.getObstacleAtIndex(0) > -1) {
            this.setINVISIBLE(this.main_IMG_Obstacle_Matrix[0][this.gameManager.getObstacleAtIndex(0)]);
            this.setVISIBLE(this.cars[this.gameManager.getCurrentCarIndex()]);
        }

        if (this.gameManager.updateCoinCount()) {
            this.playSound(this.coin_Collected_Sound);
            this.coinCountTextView.setText(String.valueOf(this.gameManager.getCoinCount()));
        }

        if (this.gameManager.endGame()) {
            if (this.hasGrantedLocation) {
                RecordManager.getInstance().saveRecord(this.time_count, this.gameManager.getCoinCount(), this.latLng);
            }

            this.endGame = this.gameManager.endGame();
        }

    }

    private void swapMatrixVisibility(int index, int matCount) {
        if (matCount > 0) {
            this.setVISIBLE(this.main_IMG_Obstacle_Matrix[index][this.gameManager.getObstacleAtIndex(index)]);
            this.setINVISIBLE(this.main_IMG_Obstacle_Matrix[index + 1][this.gameManager.getObstacleAtIndex(index)]);
            if (matCount > 1) {
                this.setVISIBLE(this.main_IMG_Coin_Matrix[index][this.gameManager.getCoinAtIndex(index)]);
                this.setINVISIBLE(this.main_IMG_Coin_Matrix[index + 1][this.gameManager.getCoinAtIndex(index)]);
            }
        }

    }

    private void setVISIBLE(ShapeableImageView view) {
        view.setVisibility(View.VISIBLE);
    }

    private void setINVISIBLE(ShapeableImageView view) {
        view.setVisibility(View.INVISIBLE);
    }
}
