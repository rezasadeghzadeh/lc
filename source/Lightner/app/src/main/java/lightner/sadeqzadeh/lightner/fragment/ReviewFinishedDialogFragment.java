package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;

public class ReviewFinishedDialogFragment extends DialogFragment {
    DialogFragment  dialogFragment;
    MainActivity mainActivity;
    private Bundle args;
    private Long categoryId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogFragment = this;
        mainActivity = (MainActivity) getActivity();
        args  = getArguments();
        categoryId  = args.getLong(Const.CATEGORY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.review_finished_dialog_fragment, container, false);
        v.findViewById(R.id.return_to_home_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                Bundle args = new Bundle();
                args.putLong(Const.CATEGORY_ID,categoryId);
                CategoryHomeFragment  categoryHomeFragment  = new CategoryHomeFragment();
                categoryHomeFragment.setArguments(args);
                mainActivity.replaceFragment(categoryHomeFragment, CategoryHomeFragment.TAG,false);
            }
        });
        return v;
    }
}
