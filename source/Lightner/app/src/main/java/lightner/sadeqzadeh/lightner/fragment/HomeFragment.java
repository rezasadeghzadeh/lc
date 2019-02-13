package lightner.sadeqzadeh.lightner.fragment;

import android.content.Context;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.ArrayList;
import java.util.List;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.adapter.CategoryCardViewAdapter;
import lightner.sadeqzadeh.lightner.adapter.KeyValueAdapter;
import lightner.sadeqzadeh.lightner.entity.Category;
import lightner.sadeqzadeh.lightner.entity.CategoryDao;

public class HomeFragment extends Fragment {
    public static final String TAG = HomeFragment.class.getName();
    MainActivity mainActivity;
    CategoryDao categoryDao;
    RecyclerView categoryRecyclerView;
    Spinner categoriesSpinner;
    List<Category> categoryList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainActivity  = (MainActivity) getActivity();
        categoryDao  =  mainActivity.getDaoSession().getCategoryDao();
        categoryList  = categoryDao.queryBuilder().list();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        categoryRecyclerView  =  view.findViewById(R.id.category_recycler);
        categoriesSpinner = view.findViewById(R.id.categories_spinner);
        initCategoriesSpinner();
        initCategoriesCardView();
        Util.hideKeyboard(mainActivity);
        mainActivity.setTitle(getString(R.string.available_categories));
        return  view;
    }

    private void initCategoriesSpinner() {
        String[] keys = new String[categoryList.size()];
        String[] values = new String[categoryList.size()];
        for(int i=0;i< categoryList.size(); i++){
            Category category = categoryList.get(i);
            keys[i] = String.valueOf(category.getId());
            values[i]=category.getName();
        }
        KeyValueAdapter spinnerAdapter = new KeyValueAdapter(mainActivity,keys,values);
        categoriesSpinner.setAdapter(spinnerAdapter);
        categoriesSpinner.post(new Runnable() {
            public void run() {
                categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Category category  = categoryList.get(position);
                            Bundle args = new Bundle();
                            args.putLong(Const.CATEGORY_ID,category.getId());
                            CategoryHomeFragment  categoryHomeFragment  = new CategoryHomeFragment();
                            categoryHomeFragment.setArguments(args);
                            mainActivity.replaceFragment(categoryHomeFragment, HomeFragment.TAG,true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    private void initCategoriesCardView() {
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
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
