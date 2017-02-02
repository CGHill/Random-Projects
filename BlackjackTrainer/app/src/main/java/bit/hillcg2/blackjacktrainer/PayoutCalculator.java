package bit.hillcg2.blackjacktrainer;


public class PayoutCalculator {

    public enum PairsTypes {PERFECT, COLOURED, MIXED}

    public  PayoutCalculator(){
    }

    //Precondition: Acceps a bet total to calculate
    //Postcondition: Returns blackjack payout for 3:2(x1.5)
    public double blackjackPayout(double betTotal)
    {
        double payout = betTotal * 1.5;

        return payout;
    }

    //Precondition: Accepts the type of pair payout and the bet total
    //Postcondition: Returns pairs payouts of x5, x10 or x30
    public double pairsPayout(PairsTypes pairType, double betTotal)
    {
        double payout = 0.00;

        switch(pairType)
        {
            case PERFECT:
                payout = betTotal * 30;
                break;
            case COLOURED:
                payout = betTotal * 10;
                break;
            case MIXED:
                payout = betTotal * 5;
                break;
        }

        return payout;
    }
}
