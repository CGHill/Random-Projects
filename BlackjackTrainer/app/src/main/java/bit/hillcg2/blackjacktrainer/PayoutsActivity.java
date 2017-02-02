package bit.hillcg2.blackjacktrainer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class PayoutsActivity extends AppCompatActivity {

    private String payoutType;
    private PayoutCalculator.PairsTypes pairType;

    //Screen controls
    private TextView boxFeedback;
    private TextView boxDollarSign;
    private TextView boxPairType;
    private EditText boxUserAnswer;


    private boolean toggleNewQuestion;
    private ChipDrawer cd;
    private RelativeLayout screenLayout;
    private Random rGen;
    private BetGenerator betGenerator;
    private Bet currBet;
    private int attemptNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payouts);

        payoutType = getIntent().getStringExtra("PAYOUT_TYPE");

        setup();
    }

    //Precondition:
    //Postcondition: Run through initial setup and creates a question for user
    private void setup()
    {
        betGenerator = new BetGenerator(getSharedPreferences("BlackjackTrainer", MODE_PRIVATE));
        rGen = new Random();
        toggleNewQuestion = false;
        currBet = null;

        boxFeedback = (TextView)findViewById(R.id.boxFeedback);
        boxDollarSign = (TextView)findViewById(R.id.boxDollarSign);
        boxUserAnswer = (EditText)findViewById(R.id.boxUserAnswer);
        boxPairType = (TextView) findViewById(R.id.boxPairType);

        boxDollarSign.setTextColor(Color.BLACK);

        boxUserAnswer.setOnEditorActionListener(new checkUserAnswer());

        attemptNumber = 0;

        createQuestion();
    }

    //Precondition:
    //Postcondition: Creates a random question for user and draws it to the screen
    private void createQuestion()
    {
        //Clear previous bet from screen
        try {
            screenLayout.removeView(cd);
            screenLayout.postInvalidate();
        }
        catch(Exception e){}

        //Create question based on what type of payout it should be
        if(payoutType.equals("blackjack"))
        {
            currBet = betGenerator.generateBlackjackBet();
        }
        else if(payoutType.equals("pairs"))
        {
            currBet = betGenerator.generatePairsBet();

            //Pick a type of pair for it to be
            int randPair =  rGen.nextInt(3);

            switch(randPair) {
                case 0:
                    pairType = PayoutCalculator.PairsTypes.PERFECT;
                    boxPairType.setText("Perfect Pair");
                    break;
                case 1:
                    pairType = PayoutCalculator.PairsTypes.COLOURED;
                    boxPairType.setText("Coloured Pair");
                    break;
                case 2:
                    pairType = PayoutCalculator.PairsTypes.MIXED;
                    boxPairType.setText("Mixed Pair");
                    break;
            }
        }


        ArrayList<Chip> betChips = currBet.getChips();

        //Draw chips to screen
        screenLayout = (RelativeLayout)findViewById(R.id.activity_payouts);
        cd = new ChipDrawer(this, betChips);
        screenLayout.addView(cd);
        screenLayout.postInvalidate();

        //Put focus on edittext for user to answer question
        boxUserAnswer.requestFocus();

        //Open keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    //Precondition:
    //Postcondition: Checks user input to see if they were correct
    private void checkAnswer() {
        //Get user answer
        String stringUserAnswer = boxUserAnswer.getText().toString();

        //Check not empty
        if (stringUserAnswer.equals("") == false)
        {
            double userAnswer = Double.valueOf(stringUserAnswer);
            double payoutAnswer = 0;

            PayoutCalculator pc = new PayoutCalculator();

            //Get payout answer
            if(payoutType.equals("blackjack"))
                payoutAnswer = pc.blackjackPayout(currBet.getBetTotal());
            else
                payoutAnswer = pc.pairsPayout(pairType, currBet.getBetTotal());

            //Check if user answered correctly and give user feedback
            if (userAnswer == payoutAnswer)
            {
                boxFeedback.setText("Correct");
                boxFeedback.setTextColor(Color.GREEN);

                toggleNewQuestion = !toggleNewQuestion;

                attemptNumber = 0;
            }
            else
            {
                //Check attempt number
                if(attemptNumber != 2)
                {
                    attemptNumber++;

                    //Inform user of number of attempts
                    String userMessage = "Attempt " + attemptNumber + " of 2";
                    boxFeedback.setText(userMessage);
                    boxFeedback.setTextColor(Color.RED);
                }
                else    //No attempts left, give answer
                {
                    boxFeedback.setText(String.format("$%.2f", payoutAnswer));
                    boxFeedback.setTextColor(Color.RED);

                    toggleNewQuestion = !toggleNewQuestion;

                    attemptNumber = 0;
                }
            }
        }

        boxUserAnswer.setText("");

        boxUserAnswer.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(boxUserAnswer, InputMethodManager.SHOW_IMPLICIT);
    }

    //Handler to check user answer
    public class checkUserAnswer implements TextView.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //Only check answer when user hits "Done" button
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //Time for new question
                if(toggleNewQuestion)
                {
                    toggleNewQuestion = !toggleNewQuestion;
                    boxFeedback.setText("");
                    createQuestion();
                }
                //Check answer
                else {
                    checkAnswer();
                }

                return true;
            }
            return false;
        }
    }

}
