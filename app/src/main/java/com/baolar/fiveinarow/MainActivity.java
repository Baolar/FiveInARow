package com.baolar.fiveinarow;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FiveChessView fiveChessView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        initViews();
    }

    private void initViews() {
        fiveChessView = (FiveChessView) findViewById(R.id.five_chess_view);
        findViewById(R.id.restart_game).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restart_game:
                fiveChessView.resetGame();
                break;
        }
    }
}
