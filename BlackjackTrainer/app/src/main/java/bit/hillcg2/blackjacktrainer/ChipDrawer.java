package bit.hillcg2.blackjacktrainer;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;

public class ChipDrawer extends View {

    private Paint paint;

    //X and Y coordinates to draw chips at
    private int x;
    private int y;
    private boolean heelToggle;

    private ArrayList<Chip> chips;

    //Constructor
    public ChipDrawer(Context context, ArrayList<Chip> chips) {
        super(context);

        this.chips = chips;

        x = 340;
        y = 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int totalChips = chips.size();

        paint = new Paint();
        heelToggle = false;

        int chipHeight = 20;
        int chipWidth = 250;
        int drawSpaceHeight = 900;

        //height in pixels including spacing between
        int totalChipHeight = (totalChips * chipHeight) + (totalChips * 2);

        //Check if chips go out of draw space
        if(totalChipHeight > drawSpaceHeight)
        {
            boolean tooTall = true;

            //Decrease chip size and check if it's still too tall
            while(tooTall)
            {
                chipHeight -= 1;
                chipWidth -= 2;

                totalChipHeight = (totalChips * chipHeight) + (totalChips * 2);

                if(totalChipHeight < drawSpaceHeight)
                {
                    tooTall = false;
                }
            }
        }

        //Start drawing Y axis
        y = 1150;

        int heelCounter = 0;
        int currColour = -1;
        Chip currChip;

        //Loop over all chips drawing them
        for(int i=0; i < chips.size(); i++)
        {
            //Get current chip to work with
            currChip = chips.get(i);

            //Check if chip colour changed
            if(currChip.getColor() != currColour)
            {
                currColour = currChip.getColor();
                paint.setColor(currColour);

                if(i != 0) {
                    heelBet();
                    heelCounter = 0;
                }
            }

            //Check if chip is green or yellow and heel every four chips, other colours every 5
            if((currColour == Color.argb(200, 17, 122, 5) || currColour == Color.argb(500, 242, 239, 36)) && heelCounter == 4)
            {
                heelBet();
                heelCounter = 0;
            }
            else if(heelCounter == 5)
            {
                heelBet();
                heelCounter = 0;
            }

            //Draw bet
            Rect newRect = new Rect(x, y, x + chipWidth, y + chipHeight);
            canvas.drawRect(newRect, paint);

            //Draw border around chips, makes it easier to look at
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(2);
            canvas.drawRect(newRect, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(currColour);

            //Move nexts chips draw position higher
            y -= chipHeight + 2;

            heelCounter++;
        }
    }

    //Precondition:
    //Postcondition: Heels the bet (moves it side to side so it's easier to see total chips)
    private void heelBet()
    {
        //Heel Right
        if(heelToggle)
        {
            x += 50;
        }
        else //Heel left
            x -= 50;

        heelToggle = !heelToggle;
    }
}