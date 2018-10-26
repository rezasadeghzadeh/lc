package lightner.sadeqzadeh.lightner.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.entity.Category;
import lightner.sadeqzadeh.lightner.entity.CategoryDao;

public class NewCategorymFragment extends Fragment {
    public static final String TAG = NewCategorymFragment.class.getName();
    EditText categoryNameEdit;
    CategoryDao categoryDao;
    MainActivity mainActivity;
    ColorPicker picker;
    long categoryId;
    Bundle args;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainActivity  = (MainActivity) getActivity();
        categoryDao = mainActivity.getDaoSession().getCategoryDao();
        args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_category_fragment_layout, container, false);
        mainActivity.setTitle(getString(R.string.new_category));
        categoryNameEdit  = view.findViewById(R.id.category_name);
        initColorPicker(view);
        if(args != null && args.getBoolean(Const.EDIT)){
            categoryId  =  args.getLong(Const.ID);
            categoryNameEdit.setText(args.getString(Const.NAME));
            picker.setColor(Integer.parseInt(args.getString(Const.COLOR)));
        }
        return  view;
    }

    private void initColorPicker(View view) {
        picker = view.findViewById(R.id.picker);
        SVBar svBar = view.findViewById(R.id.svbar);
        OpacityBar opacityBar = view.findViewById(R.id.opacitybar);
        SaturationBar saturationBar = view.findViewById(R.id.saturationbar);
        ValueBar valueBar = view.findViewById(R.id.valuebar);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        //to turn of showing the old light_green
        picker.setShowOldCenterColor(false);

        //TODO add in edit
        ///picker.setOldCenterColor(picker.getColor());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.new_category_fragment_menu, menu);
        displayHelp();
    }
    private void displayHelp() {
        if(Util.fetchFromPreferences(Const.SEEN_NEW_CATEGORY_HINT) != null){
            return;
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final TapTargetSequence sequence = new TapTargetSequence(getActivity())
                        .targets(
                                TapTarget.forToolbarMenuItem(mainActivity.toolbar, R.id.save_category, getString(R.string.save_category), getString(R.string.save_category_help)).id(1).cancelable(false)
                        )
                        .listener(new TapTargetSequence.Listener() {
                            @Override
                            public void onSequenceFinish() {
                                Util.saveInPreferences(Const.SEEN_NEW_CATEGORY_HINT,String.valueOf("1"));
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
        switch (item.getItemId()) {

            case R.id.save_category:
                saveCategory();
            default:
                break;
        }

        return false;
    }

    private void saveCategory() {
        if(categoryNameEdit.getText().length() < 1){
            categoryNameEdit.setError(getString(R.string.category_name_is_requirement));
            return;
        }
        Category category = null;
        if(categoryId != 0){
            category  =  categoryDao.load(categoryId);
        }else {
            category = new Category();
        }
        category.setName(categoryNameEdit.getText().toString());
        category.setCodeColor(String.valueOf(picker.getColor()));
        if(categoryId != 0) {
            categoryDao.update(category);
        }else {
            categoryDao.insert(category);
        }
        HomeFragment homeFragment = new HomeFragment();
        mainActivity.replaceFragment(homeFragment,HomeFragment.TAG,true);
    }
}
