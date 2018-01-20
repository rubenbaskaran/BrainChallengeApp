package rubenbaskaran.com.brainchallenge.Menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import rubenbaskaran.com.brainchallenge.Enums.GameTypes;
import rubenbaskaran.com.brainchallenge.GameCategories.NumbersGameActivity;
import rubenbaskaran.com.brainchallenge.Highscore.HighscoreActivity;
import rubenbaskaran.com.brainchallenge.R;

public class SubtractionMenuActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtraction_menu);
    }

    public void StartSubtractionGame(View view)
    {
        Intent i = new Intent(getApplicationContext(), NumbersGameActivity.class);
        i.putExtra("gametype", GameTypes.Subtraction);
        startActivity(i);
    }

    public void ShowSubtractionHighscores(View view)
    {
        Intent i = new Intent(getApplicationContext(), HighscoreActivity.class);
        i.putExtra("gametype", GameTypes.Subtraction);
        startActivity(i);
    }
}