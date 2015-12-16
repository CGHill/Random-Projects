package bit.hillcg2.agilitytracker;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

//Class to bring up an alert to confirm delete data for the class ViewData
public class ConfirmDialog extends DialogFragment {

    private String title;

    //Constructor with message passed in
    public ConfirmDialog(String title){
        this.title = title;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder builder = new  AlertDialog.Builder(getActivity());

        //Setup dialog
        builder.setTitle(title);
        builder.setPositiveButton("Yes", new yesButtonHandler());
        builder.setNegativeButton("No", new noButtonHandler());

        //Create the dialogue
        Dialog customDialog = builder.create();

        return customDialog;
    }

    //Handler for positive button push
    public class yesButtonHandler implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            ViewData vd = (ViewData)getActivity();
            vd.confirmDelete(true);
        }

    }

    //Handler for negative button push
    public class noButtonHandler implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            ViewData vd = (ViewData)getActivity();
            vd.confirmDelete(false);
        }

    }



}
