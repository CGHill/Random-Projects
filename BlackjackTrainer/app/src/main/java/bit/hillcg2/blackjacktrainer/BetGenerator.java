package bit.hillcg2.blackjacktrainer;


import android.content.SharedPreferences;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.Random;

public class BetGenerator {
    private enum PayoutType {BLACKJACK, PAIR}
    private Random rGen;
    private boolean pinksActive, blacksActive, greensActive, redsActive, yellowsActive, bluesActive;
    private int highestBetTotal;

    private SharedPreferences prefs;

    //Constructor
    public BetGenerator(SharedPreferences prefs)
    {
        rGen = new Random();

        highestBetTotal = 0;

        this.prefs = prefs;
    }


    //Precondition:
    //Postcondition: Returns a random blackjack bet created within the bounds of users settings
    public Bet generateBlackjackBet(){
        //Update the current settings
        checkSettings(PayoutType.BLACKJACK);

        //Round number down to a value within the table limits ($5) increments
        int numberToCalculateFrom = (int)(highestBetTotal / 5);
        int maxBet = numberToCalculateFrom * 5;

        double betTotal = 0;

        ArrayList<Chip> chips = new ArrayList<>();
        ArrayList<Chip> tempChips;

        //Check to see if any chips can be created withing user settings
        if((pinksActive && (maxBet >= 500)) || (blacksActive && (maxBet >= 100))  ||(greensActive && (maxBet >= 25)) || (redsActive && (maxBet >= 5)))
        {
            //Stops blank bets
            while (chips.size() == 0)
            {
                //Go through each kind of chip colour and get a random amount where possible and add them to the arraylist
                //Pinks
                if (pinksActive) {
                    tempChips = makeChips(500, Color.argb(200, 247, 111, 231), maxBet, betTotal);

                    for (int i = 0; i < tempChips.size(); i++) {
                        Chip newChip = tempChips.get(i);
                        chips.add(newChip);
                        betTotal += newChip.getValue();
                    }
                    tempChips.clear();
                }

                //Blacks
                if (blacksActive) {
                    tempChips = makeChips(100, Color.BLACK, maxBet, betTotal);

                    for (int i = 0; i < tempChips.size(); i++) {
                        Chip newChip = tempChips.get(i);
                        chips.add(newChip);
                        betTotal += newChip.getValue();
                    }
                    tempChips.clear();
                }

                //Greens
                if (greensActive) {
                    tempChips = makeChips(25, Color.argb(200, 17, 122, 5), maxBet, betTotal);

                    for (int i = 0; i < tempChips.size(); i++) {
                        Chip newChip = tempChips.get(i);
                        chips.add(newChip);
                        betTotal += newChip.getValue();
                    }
                    tempChips.clear();
                }

                //Reds
                if (redsActive) {
                    tempChips = makeChips(5, Color.argb(500, 214, 0, 0), maxBet, betTotal);

                    for (int i = 0; i < tempChips.size(); i++) {
                        Chip newChip = tempChips.get(i);
                        chips.add(newChip);
                        betTotal += newChip.getValue();
                    }
                    tempChips.clear();
                }
            }
        }

        //Create the bet from the random amount of chips
        Bet newBet = new Bet(betTotal, chips);

        return newBet;
    }

    //Precondition:
    //Postcondition: Returns a random pairs bet created within the bounds of users settings
    public Bet generatePairsBet()
    {
        //Update the settings
        checkSettings(PayoutType.PAIR);

        ArrayList<Chip> chips = new ArrayList<Chip>();

        int maxBet = highestBetTotal;
        double betTotal = 0;

        //Check to see if any chips can be created withing user settings
        if((greensActive && (maxBet >= 25)) || (redsActive && (maxBet >= 5)) || (yellowsActive && (maxBet >= 2.50)) || (bluesActive && (maxBet >= 1)))
        {
            //Stops blank bets
            while (chips.size() == 0)
            {
                //Go through each kind of chip colour and get a random amount where possible and add them to the arraylist
                //Greens
                if (greensActive && (maxBet - betTotal >= 25)) {
                    int highestNumberOfGreens = 2;
                    int numGreens = rGen.nextInt(highestNumberOfGreens + 1);

                    for (int i = 0; i < numGreens; i++) {
                        Chip newChip = new Chip(25, Color.argb(200, 17, 122, 5));
                        chips.add(newChip);

                        betTotal += 25;
                    }
                }
                //Reds
                if (redsActive && (maxBet - betTotal >= 5)) {
                    int highestNumReds = (int) (maxBet - betTotal) / 5;
                    int numReds = 0;
                    if (highestNumReds != 0)
                        numReds = rGen.nextInt(highestNumReds + 1);

                    for (int i = 0; i < numReds; i++) {
                        Chip newChip = new Chip(5, Color.argb(500, 214, 0, 0));
                        chips.add(newChip);

                        betTotal += 5;
                    }
                }
                //Yellows
                if (yellowsActive && (maxBet - betTotal >= 2.50)) {
                    int highestNumYellow = (int) ((maxBet - betTotal) / 2.50);
                    int numYellows = 0;

                    //Max out number of yellows at 5, gets silly with more than that
                    if (highestNumYellow <= 5 && highestNumYellow != 0)
                        numYellows = rGen.nextInt(highestNumYellow + 1);
                    else if (highestNumYellow != 0)
                        numYellows = rGen.nextInt(5);

                    for (int i = 0; i < numYellows; i++) {
                        Chip newChip = new Chip(2.50, Color.argb(500, 242, 239, 36));
                        chips.add(newChip);

                        betTotal += 2.50;
                    }
                }
                //Blues
                if (bluesActive && (maxBet - betTotal >= 1)) {
                    int highestNumBlues = (int) (maxBet - betTotal) / 1;
                    int numBlues = 0;

                    //Max out number of blues at 5, gets silly with more than that
                    if (highestNumBlues <= 5 && highestNumBlues != 0)
                        numBlues = rGen.nextInt(highestNumBlues + 1);
                    else if (highestNumBlues != 0)
                        numBlues = rGen.nextInt(5);

                    for (int i = 0; i < numBlues; i++) {
                        Chip newChip = new Chip(1, Color.argb(500, 77, 221, 240));
                        chips.add(newChip);

                        betTotal += 1;
                    }
                }
            }
        }

        //Create random pairs bet using chips
        Bet newBet = new Bet(betTotal, chips);

        return newBet;
    }

    //Precondition: Needs a maximum bet and the total amount currently added to bet to give accurate results
    //Postcondition: Returns list of random amount of chips of the passed in value
    private ArrayList<Chip> makeChips(int chipValue, int chipColour, double maxBet, double betTotal)
    {
        ArrayList<Chip> chips = new ArrayList<Chip>();

        if ((maxBet - betTotal) >= chipValue) {
            int highestNumChips = (int) (maxBet - betTotal) / chipValue;
            int numChips = 0;
            if(highestNumChips != 0)
                numChips = rGen.nextInt(highestNumChips + 1);

            for(int i=0; i < numChips; i++)
            {
                Chip newChip = new Chip(chipValue, chipColour);
                chips.add(newChip);
            }
        }

        return chips;
    }

    //Precondition:
    //Postcondition: Gets all of the users settings to work from
    private void checkSettings(PayoutType type){
        switch (type)
        {
            case BLACKJACK:
            {
                highestBetTotal = prefs.getInt("blackjackHighest", 200);
                pinksActive = prefs.getBoolean("BJPinksActive", false);
                blacksActive = prefs.getBoolean("BJBlacksActive", false);
                greensActive = prefs.getBoolean("BJGreensActive", true);
                redsActive = prefs.getBoolean("BJRedsActive", true);
            }
                break;
            case PAIR:
            {
                highestBetTotal = prefs.getInt("pairsHighest", 50);
                greensActive = prefs.getBoolean("PPGreensActive", true);
                redsActive = prefs.getBoolean("PPRedsActive", true);
                yellowsActive = prefs.getBoolean("PPYellowsActive", false);
                bluesActive = prefs.getBoolean("PPBluesActive", false);
            }
                break;
        }
    }
}
