package lightner.sadeqzadeh.lightner.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;

public class AddFlashcardMethodsFragment extends Fragment {

    public static final String TAG = AddFlashcardMethodsFragment.class.getName();
    MainActivity mainActivity;
    private Bundle args;
    private Long categoryId;
    private Button addManuallyBtn;
    private Button buyFlashcardBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        args = getArguments();
        categoryId  = args.getLong(Const.CATEGORY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_flashcard_method_layout, container, false);
        addManuallyBtn = view.findViewById(R.id.add_manually_flashcard_btn);
        addManuallyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle argsFragment = new Bundle();
                argsFragment.putLong(Const.CATEGORY_ID,categoryId);
                NewFlashCardFragment fragment = new NewFlashCardFragment();
                fragment.setArguments(argsFragment);
                mainActivity.replaceFragment(fragment, fragment.TAG);
            }
        });

        buyFlashcardBtn = view.findViewById(R.id.buy_flashcard_btn);
        buyFlashcardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadPackageFragment fragment = new DownloadPackageFragment();
                mainActivity.replaceFragment(fragment, fragment.TAG);
            }
        });

        return  view;
    }

}
