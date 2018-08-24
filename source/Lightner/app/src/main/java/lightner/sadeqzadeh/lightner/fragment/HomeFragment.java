package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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
        mainActivity.backPressed = false;
        mainActivity.setTitle(getString(R.string.app_name));
        return  view;

    }

    private void initCategoriesCardView() {
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        List<Category> categoryList  = categoryDao.queryBuilder().list();
        CategoryCardViewAdapter categoryCardViewAdapter  = new CategoryCardViewAdapter(getContext(), mainActivity, categoryList);
        categoryRecyclerView.setAdapter(categoryCardViewAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_fragment_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity mainActivity  = (MainActivity) getActivity();
        switch (item.getItemId()) {
            case R.id.action_add_category:
                NewCategorymFragment fragment = new NewCategorymFragment();
                mainActivity.replaceFragment(fragment, fragment.TAG,true);
                return false;
            default:
                break;
        }

        return false;
    }

}
