package rubenbaskaran.com.brainchallenge.GameCategories;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import rubenbaskaran.com.brainchallenge.Data.Contracts.DatabaseContract;
import rubenbaskaran.com.brainchallenge.Data.Managers.LocalDatabaseManager;
import rubenbaskaran.com.brainchallenge.Enums.GameTypes;
import rubenbaskaran.com.brainchallenge.Highscore.HighscoreActivity;
import rubenbaskaran.com.brainchallenge.Models.Score;
import rubenbaskaran.com.brainchallenge.R;

public class NumbersGameActivity extends AppCompatActivity
{
    TextView equationTextView;
    TextView scoreTextView;
    TextView counterTextView;
    int correctAnswer;
    int correctAnswerIndexValue;
    int answeredCorrectly = 0;
    int questionsAnswered = 0;
    int counter = 5;
    GridLayout gridLayout;
    Timer timer = null;
    GameTypes gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers_game);

        GameTypes clickedNumbersGame = (GameTypes)getIntent().getSerializableExtra("gametype");
        gameType = clickedNumbersGame;

        scoreTextView = findViewById(R.id.scoreTextView);
        equationTextView = findViewById(R.id.equationTextView);
        counterTextView = findViewById(R.id.counterTextView);
        gridLayout = findViewById(R.id.gridLayout);

        new AlertDialog.Builder(this)
                .setTitle("Welcome!")
                .setMessage("Solve as many equations as you can within 30 seconds. Are you ready?")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setCancelable(false)
                .setPositiveButton("Start game!", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        StartGame();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        System.exit(0);
                    }
                })
                .show();
    }

    public void StartGame()
    {
        GenerateEquation();
        UpdateScore();
        StartTimer();
    }

    public void GridlayoutSetEnabled(boolean bool)
    {
        final boolean input = bool;
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < gridLayout.getChildCount(); i++)
                {
                    gridLayout.getChildAt(i).setEnabled(input);
                }
            }
        });
    }

    public void StartTimer()
    {
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                counter--;

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        String counterText = "Time left: " + String.valueOf(counter);
                        counterTextView.setText(counterText);
                    }
                });

                if (counter == 0 || counter < 0)
                {
                    GridlayoutSetEnabled(false);
                    timer.cancel();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            OpenDialog();
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    public void OpenDialog()
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.btn_star)
                .setTitle(SetResultComment())
                .setMessage("You scored " + answeredCorrectly + " out of " + questionsAnswered)
                .setPositiveButton("Finish", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SaveScore();
                        finish();
                        Intent i = new Intent(getApplicationContext(), HighscoreActivity.class);
                        i.putExtra("gametype", gameType);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Restart level", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        Restart(null);
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void SaveScore()
    {
        Score score = new Score();

        switch (gameType)
        {
            case Addition:
                score.Table = DatabaseContract.AdditionHighscore.TABLE_NAME;
                break;
            case Subtraction:
                score.Table = DatabaseContract.SubtractionHighscore.TABLE_NAME;
                break;
            case Multiplication:
                score.Table = DatabaseContract.MultiplicationHighscore.TABLE_NAME;
                break;
            case Division:
                score.Table = DatabaseContract.DivisionHighscore.TABLE_NAME;
                break;
        }
        score.Answered = questionsAnswered;
        score.AnsweredCorrectly = answeredCorrectly;
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        score.Percentage = Double.valueOf(decimalFormat.format((((double) answeredCorrectly) / questionsAnswered) * 100));

        LocalDatabaseManager localDatabaseManager = new LocalDatabaseManager(getApplicationContext());
        localDatabaseManager.SaveNewScore(score);
    }

    public String SetResultComment()
    {
        if (answeredCorrectly == questionsAnswered && answeredCorrectly != 0)
        {
            return "Perfect! :D";
        }
        else if (answeredCorrectly == 0)
        {
            return "Oooh noo! :(";
        }
        else if (answeredCorrectly > questionsAnswered / 2)
        {
            return "Awesome! :)";
        }
        else if (answeredCorrectly == questionsAnswered / 2 || answeredCorrectly < questionsAnswered / 2)
        {
            return "Too bad! :|";
        }

        return "Congratulations";
    }

    public void GenerateEquation()
    {
        Random random = new Random();
        int number1;
        int number2;
        String equation = "Equation is missing";

        switch (gameType)
        {
            case Addition:
                number1 = random.nextInt(50);
                number2 = random.nextInt(50);
                equation = String.format(String.valueOf(number1) + " + " + String.valueOf(number2));
                correctAnswer = number1 + number2;
                break;
            case Subtraction:
                number1 = random.nextInt(100);
                number2 = random.nextInt(100);
                while (number2 > number1)
                {
                    number2 = random.nextInt(100);
                }
                equation = String.format(String.valueOf(number1) + " - " + String.valueOf(number2));
                correctAnswer = number1 - number2;
                break;
            case Multiplication:
                number1 = random.nextInt(10);
                number2 = random.nextInt(10);
                equation = String.format(String.valueOf(number1) + " X " + String.valueOf(number2));
                correctAnswer = number1 * number2;
                break;
            case Division:
                number1 = random.nextInt(100);
                number2 = random.nextInt(10);
                while (number1 < number2)
                {
                    number2 = random.nextInt(100);
                }
                equation = String.format(String.valueOf(number1) + " / " + String.valueOf(number2));
                correctAnswer = number1 / number2;
                break;
        }

        equationTextView.setText(equation);
        GenerateAnswers();
    }

    private void GenerateAnswers()
    {
        Random random = new Random();
        String[] answers = new String[4];

        correctAnswerIndexValue = random.nextInt(3);
        answers[correctAnswerIndexValue] = String.valueOf(correctAnswer);

        for (int i = 0; i < 4; i++)
        {
            if (answers[i] == null)
            {
                String randomNumber = String.valueOf(random.nextInt(100));
                while (randomNumber.equals(answers[0]) || randomNumber.equals(answers[1]) || randomNumber.equals(answers[2]) || randomNumber.equals(answers[3]))
                {
                    randomNumber = String.valueOf(random.nextInt(100));
                }
                answers[i] = randomNumber;
                Button button = gridLayout.findViewWithTag(String.valueOf(i));
                button.setText(String.valueOf(answers[i]));
            }
            else if (answers[i] != null)
            {
                Button button = gridLayout.findViewWithTag(String.valueOf(i));
                button.setText(String.valueOf(answers[correctAnswerIndexValue]));
            }
        }
    }

    public void Restart(View view)
    {
        answeredCorrectly = 0;
        questionsAnswered = 0;
        counter = 5;
        GenerateEquation();
        UpdateScore();
        GridlayoutSetEnabled(true);

        if (timer != null)
        {
            timer.cancel();
        }

        StartTimer();
    }

    public void AnswerSelected(View view)
    {
        Button button = (Button) view;
        if (Integer.parseInt(button.getTag().toString()) == correctAnswerIndexValue)
        {
            answeredCorrectly++;
        }

        questionsAnswered++;
        UpdateScore();
        if (counter != 0 && !(counter < 0))
        {
            GenerateEquation();
        }
    }

    public void UpdateScore()
    {
        String score = answeredCorrectly + "/" + questionsAnswered;
        scoreTextView.setText(score);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        timer.cancel();
    }
}
