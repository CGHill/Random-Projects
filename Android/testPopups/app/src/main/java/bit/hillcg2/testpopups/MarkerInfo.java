package bit.hillcg2.testpopups;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MarkerInfo extends DialogFragment {
    public MarkerInfo()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog myDialog = getDialog();

        myDialog.setTitle("Title");

        View dialogView = inflater.inflate(R.layout.popup_layout, container);


        Button btn = (Button)dialogView.findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity)getActivity();
                m.blah();
            }
        });

        return dialogView;
    }
}
