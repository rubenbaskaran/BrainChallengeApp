package rubenbaskaran.com.brainspeedchallenge.Highscores;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rubenbaskaran.com.brainspeedchallenge.Enums.GameTypes;
import rubenbaskaran.com.brainspeedchallenge.R;

public class HighscoreActivity extends AppCompatActivity
{
    //region Fields
    Button ShowLocalHighscoreButton;
    Button ShowGlobalHighscoreButton;
    FragmentManager fragmentManager;
    public static Context context;
    LocalHighscoreFragment localHighscoreFragment;
    GameTypes gameTypes;
    TextView highscoresFragmentContainerTitle;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        context = getApplicationContext();

        Intent receivedIntent = getIntent();
        gameTypes = (GameTypes) receivedIntent.getSerializableExtra("gametype");

        highscoresFragmentContainerTitle = findViewById(R.id.highscoresFragmentContainerTitle);
        String title = String.format(gameTypes.toString() + " highscores").toLowerCase();
        highscoresFragmentContainerTitle.setText(title);
        ShowLocalHighscoreButton = findViewById(R.id.ShowLocalHighscoreButton);
        ShowGlobalHighscoreButton = findViewById(R.id.ShowGlobalHighscoreButton);

        ShowGlobalHighscoreButton.setEnabled(true);
        ShowLocalHighscoreButton.setEnabled(false);

        localHighscoreFragment = new LocalHighscoreFragment();
        localHighscoreFragment.setGameType(gameTypes);
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, localHighscoreFragment, null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.about_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.about)
        {
            Toast.makeText(getApplicationContext(), "Made by Ruben Baskaran", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void ShowLocalHighscore(View view)
    {
        ShowLocalHighscoreButton.setEnabled(false);
        ShowGlobalHighscoreButton.setEnabled(true);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, localHighscoreFragment, null);
        fragmentTransaction.commit();
    }

    public void ShowGlobalHighscore(View view)
    {
        ShowGlobalHighscoreButton.setEnabled(false);
        ShowLocalHighscoreButton.setEnabled(true);
        GlobalHighscoreFragment globalHighscoreFragment = new GlobalHighscoreFragment();
        globalHighscoreFragment.setGameType(gameTypes);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, globalHighscoreFragment, null);
        fragmentTransaction.commit();
    }
}
