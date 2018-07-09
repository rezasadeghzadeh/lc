package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;

public class CategoryHomeFragment extends Fragment {

    public static final String TAG = CategoryHomeFragment.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_home, container, false);
        LinearLayout  linearLayout = view.findViewById(R.id.category_box1);
        final MainActivity mainActivity = (MainActivity) getActivity();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewFlashcard reviewFlashcard = new ReviewFlashcard();
                mainActivity.replaceFragment(reviewFlashcard,ReviewFlashcard.TAG);
            }
        });

        return  view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.category_home_fragment_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity mainActivity  = (MainActivity) getActivity();
        switch (item.getItemId()) {
            case R.id.action_add_flashcard: {
                NewFlashCardFragment fragment = new NewFlashCardFragment();
                mainActivity.replaceFragment(fragment, fragment.TAG);
                return false;
            }
            case R.id.action_download_packages:{
                DownloadPackageFragment fragment = new DownloadPackageFragment();
                mainActivity.replaceFragment(fragment, fragment.TAG);
                return false;
            }

            default:
                break;
        }

        return false;
    }


}
