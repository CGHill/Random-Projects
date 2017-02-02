package bit.hillcg2.blackjacktrainer;

import java.util.ArrayList;


public class Bet {
    private double betTotal;
    private ArrayList<Chip> chips;

    public Bet(double betTotal, ArrayList<Chip> chips)
    {
        this.betTotal = betTotal;
        this.chips = chips;
    }

    public ArrayList<Chip> getChips(){return chips;}
    public double getBetTotal(){return betTotal;}
}
