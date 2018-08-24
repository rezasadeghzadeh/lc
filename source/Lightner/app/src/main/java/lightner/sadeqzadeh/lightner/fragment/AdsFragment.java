package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;

public class AdsFragment extends Fragment{
    Button viewHomeBtn;
    MainActivity mainActivity;
    public static final String TAG = AdsFragment.class.getName();

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ads_fragment, container, false);
        mainActivity = (MainActivity) getActivity();
        viewHomeBtn  =  view.findViewById(R.id.view_home_btn);
        viewHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                mainActivity.replaceFragment(homeFragment, HomeFragment.TAG,false);
            }
        });
        return view;
    }
}

