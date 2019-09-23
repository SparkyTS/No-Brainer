package com.sparkyts.nobrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
    import android.view.View;
    import android.widget.Button;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {



    TextView go, timeLeft, question, score, finalScore;
    TextView []options = new TextView[4];
    Button playAgain;
    SecureRandom random;
    int answer, correctScore, totalQuestions;

    private void initialize(){
        go = findViewById(R.id.textViewGo);
        timeLeft = findViewById(R.id.timeLeft);
        question = findViewById(R.id.question);
        score = findViewById(R.id.score);
        finalScore = findViewById(R.id.finalScore);
        playAgain = findViewById(R.id.playAgain);
        options[0] = findViewById(R.id.option1);
        options[1] = findViewById(R.id.option2);
        options[2] = findViewById(R.id.option3);
        options[3] = findViewById(R.id.option4);

        random = new SecureRandom();
    }

    /**
     * @Param enabled : boolean
     * this function is used to enable and disable all the Options
     * */
    private void enableOptions(boolean enabled) {
        for(TextView option : options)
            option.setEnabled(enabled);
    }

    /**
     * @param : View (The Option that is clicked by user)
     * */
    public void checkAnswer(View view){
        // fetching the selected option
        int selectedOption = Integer.parseInt(((TextView)view).getText().toString());

        // update the answer based on the option selected by user
        updateScoreByOne(answer == selectedOption);

        // displaying the next question
        displayQuestion();
    }

    /**
     * @param correct true if the answer is correct
     * updating score based on the correctness of answer
     * */
    private void updateScoreByOne(boolean correct) {
        // Fetching current score form UI
        String[] currentScore = score.getText().toString().split("/");
        correctScore = Integer.parseInt(currentScore[0]);
        totalQuestions = Integer.parseInt(currentScore[1]);

        // Updating current score
        if(correct) correctScore++;
        totalQuestions++;

        // Updating the new score
        score.setText(correctScore  + "/" + totalQuestions);
    }

    public void startGame(View view){

        for(TextView option: options)
            option.setVisibility(View.VISIBLE);
        timeLeft.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);

        playAgain.setVisibility(View.INVISIBLE);
        finalScore.setVisibility(View.INVISIBLE);

        enableOptions(true);
        score.setText("0/0");

        startCountDown();
        displayQuestion();
    }

    private void displayQuestion() {
        int a = random.nextInt(20) + 1;
        int b = random.nextInt(20) + 1;
        char []operations = { '+', '-', '*', '/' };
        char operation = operations[random.nextInt(4)];
        switch (operation){
            case '+' :
                answer = a + b; break;
            case '-' :
                //making sure that always have positive answer
                answer = a - b < 0 ? (a = a + b - (b = a)) - b : a - b ; break;
            case '*'  :
                answer = a * b; break;
            case '/' :
                //making sure that it always produce non decimal point results.
                answer = (a = a + b - ( a % b ) )/ b;
        }

        question.setText(a + " " +  operation + " " + b);
        displayAnswer();
    }

    private void displayAnswer() {
        // Using set to have 4 unique option.
        HashSet<Integer> optionSet = new HashSet<>(4);
        optionSet.add(answer);
        while(optionSet.size()!=4)
            optionSet.add(answer+random.nextInt(20));

        // rendering option to the user screen
        int i = 0;
        for(int answer : optionSet)
            options[i++].setText(""+answer);
    }

    private void startCountDown() {
        new CountDownTimer(30*1000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                int time = (int) millisUntilFinished/1000;
                timeLeft.setText(time < 10 ? "0"+time : time +"S");
            }

            @Override
            public void onFinish() {
                timeLeft.setText("0S");

                //show the final score
                finalScore.setText(
                        "   Score : " + correctScore + " / " + totalQuestions + "\n" +
                                "Accuracy : " + String.format("%.2f", (float)correctScore / totalQuestions * 100) + "%");

                //disabling the buttons
                enableOptions(false);
                //stop the game and display score
                playAgain.setVisibility(View.VISIBLE);
                finalScore.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }
}
