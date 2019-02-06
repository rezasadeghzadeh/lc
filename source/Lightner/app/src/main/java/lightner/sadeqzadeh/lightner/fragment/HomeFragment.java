package lightner.sadeqzadeh.lightner.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.List;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.adapter.CategoryCardViewAdapter;
import lightner.sadeqzadeh.lightner.entity.Category;
import lightner.sadeqzadeh.lightner.entity.CategoryDao;

public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getName();
    MainActivity mainActivity;
    CategoryDao categoryDao;
    RecyclerView categoryRecyclerView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainActivity  = (MainActivity) getActivity();
        categoryDao  =  mainActivity.getDaoSession().getCategoryDao();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        categoryRecyclerView  =  view.findViewById(R.id.category_recycler);
        initCategoriesCardView();
        Util.hideKeyboard(mainActivity);
        mainActivity.setTitle(getString(R.string.available_categories));
        return  view;
    }

    private void initCategoriesCardView() {
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        List<Category> categoryList  = categoryDao.queryBuilder().list();
        CategoryCardViewAdapter categoryCardViewAdapter  = new CategoryCardViewAdapter(getContext(), mainActivity, categoryList);
        categoryRecyclerView.setAdapter(categoryCardViewAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home_fragment_menu, menu);

        displayHelp();

    }

    private void displayHelp() {
        if(Util.fetchFromPreferences(Const.SEEN_HOME_HINT) != null){
            return;
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final TapTargetSequence sequence = new TapTargetSequence(getActivity())
                        .targets(
                              /*  TapTarget.forToolbarMenuItem(mainActivity.toolbar, R.id.action_add_category, getString(R.string.add_category), getString(R.string.add_category_help)).id(1).cancelable(false),*/
                                TapTarget.forToolbarNavigationIcon(mainActivity.toolbar, getString(R.string.action_settings), getString(R.string.action_settings_help)).id(2).cancelable(false)
                        )
                        .listener(new TapTargetSequence.Listener() {
                            @Override
                            public void onSequenceFinish() {
                                Util.saveInPreferences(Const.SEEN_HOME_HINT,String.valueOf("1"));
                            }

                            @Override
                            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                            }

                            @Override
                            public void onSequenceCanceled(TapTarget lastTarget) {
                            }
                        });
                sequence.start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity mainActivity  = (MainActivity) getActivity();
    /*    switch (item.getItemId()) {
            case R.id.action_add_category:
                NewCategorymFragment fragment = new NewCategorymFragment();
                mainActivity.replaceFragment(fragment, fragment.TAG,true);
                return false;
            default:
                break;
        }
*/
        return false;
    }

}
