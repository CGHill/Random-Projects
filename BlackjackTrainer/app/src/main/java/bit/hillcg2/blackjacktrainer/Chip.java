package bit.hillcg2.blackjacktrainer;


public class Chip {
    private double value;
    private int color;


    public Chip(double value, int color)
    {
        this.value = value;
        this.color = color;
    }

    public double getValue(){return value;}

    public int getColor(){return color;}
}
