package lightner.sadeqzadeh.lightner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.entity.BoxStat;
import lightner.sadeqzadeh.lightner.entity.Category;
import lightner.sadeqzadeh.lightner.entity.CategoryDao;
import lightner.sadeqzadeh.lightner.entity.Flashcard;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;

public class CategoryHomeFragment extends Fragment {
    private Long categoryId;
    private Bundle args;
    private CategoryDao categoryDao;
    private FlashcardDao flashcardDao;
    private Category category;
    MainActivity mainActivity;
    Button startReviewBtn;
    AppCompatImageButton freeReviewBtn;
    private LinearLayout box1;
    private LinearLayout box2;
    private LinearLayout box3;
    private LinearLayout box4;
    private LinearLayout box5;

    private TextView total1;
    private TextView total2;
    private TextView total3;
    private TextView total4;
    private TextView total5;

    private TextView reviewable1;
    private TextView reviewable2;
    private TextView reviewable3;
    private TextView reviewable4;
    private TextView reviewable5;

    private Button whatIsLightnerButton;

    private HashMap<Integer,BoxStat> boxStatMap = new HashMap<>();

    public static final String TAG = CategoryHomeFragment.class.getName();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainActivity = (MainActivity) getActivity();
        args = getArguments();
        categoryId = args.getLong(Const.CATEGORY_ID);
        categoryDao = mainActivity.getDaoSession().getCategoryDao();
        flashcardDao = mainActivity.getDaoSession().getFlashcardDao();
        category  = categoryDao.load(categoryId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_home, container, false);

        final MainActivity mainActivity = (MainActivity) getActivity();
        startReviewBtn  =  view.findViewById(R.id.start_review_btn);
        freeReviewBtn  = view.findViewById(R.id.free_review_btn);
        box1 = view.findViewById(R.id.category_box1);
        box2 = view.findViewById(R.id.category_box2);
        box3 = view.findViewById(R.id.category_box3);
        box4 = view.findViewById(R.id.category_box4);
        box5 = view.findViewById(R.id.category_box5);

        total1  =  view.findViewById(R.id.total_words_1);
        total2  =  view.findViewById(R.id.total_words_2);
        total3  =  view.findViewById(R.id.total_words_3);
        total4  =  view.findViewById(R.id.total_words_4);
        total5  =  view.findViewById(R.id.total_words_5);
        reviewable1  = view.findViewById(R.id.reviewable_1);
        reviewable2  = view.findViewById(R.id.reviewable_2);
        reviewable3  = view.findViewById(R.id.reviewable_3);
        reviewable4  = view.findViewById(R.id.reviewable_4);
        reviewable5  = view.findViewById(R.id.reviewable_5);

        whatIsLightnerButton = view.findViewById(R.id.what_is_lightner);
        whatIsLightnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://en.wikipedia.org/wiki/Leitner_system");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
        mainActivity.setTitle(category.getName());

        populateBoxesStats();
        initStartReviewBtn();
        initFreeReviewBtn();
        Util.hideKeyboard(getActivity());

        return  view;
    }

    private void initFreeReviewBtn() {
        freeReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle  = new Bundle();
                bundle.putLong(Const.CATEGORY_ID,categoryId);
                bundle.putBoolean(Const.REVIEW_MODE,true);
                FreeReviewFlashcard freeReviewFlashcard = new FreeReviewFlashcard();
                freeReviewFlashcard.setArguments(bundle);
                mainActivity.replaceFragment(freeReviewFlashcard,FreeReviewFlashcard.TAG,true);
            }
        });
    }

    private void initStartReviewBtn() {

        startReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle boxArgs  = new Bundle();
                boxArgs.putBoolean(Const.REVIEW_MODE,true);
                boxArgs.putLong(Const.CATEGORY_ID,categoryId);
                ReviewFlashcard reviewFlashcard = new ReviewFlashcard();
                reviewFlashcard.setArguments(boxArgs);
                mainActivity.replaceFragment(reviewFlashcard,ReviewFlashcard.TAG,true);
            }
        });
    }

    private void populateBoxesStats() {
        try {
            boolean hasReviewItems = false;
            Date currentDate  =  new Date();

            ////////////////////////box1 ///////////////////////
            QueryBuilder<Flashcard> queryBuilder = flashcardDao.queryBuilder();
            long  total  =  queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(categoryId),
                    FlashcardDao.Properties.CurrentBox.eq(1)
            ).buildCount().count();

            final List<Flashcard> reviewableList = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.le(currentDate),
                    FlashcardDao.Properties.CurrentBox.eq(1),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
                    ).list();
            long reviewable = reviewableList.size();
            total1.setText(String.format("%s %d",total1.getText(),total));
            reviewable1.setText(String.format("%s %d",reviewable1.getText(), reviewable));
            if(reviewable > 0){
                //circle1.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
                hasReviewItems  = true;
            }else {
                //circle1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_red));
            }


            ////////////// box 2/////////////////////////////
            queryBuilder = flashcardDao.queryBuilder();
            total  =  queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(categoryId),
                    FlashcardDao.Properties.CurrentBox.eq(2)
            ).buildCount().count();
            reviewable = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.le(currentDate),
                    FlashcardDao.Properties.CurrentBox.eq(2),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
            ).buildCount().count();
            total2.setText(String.format("%s %d",total2.getText(),total));
            reviewable2.setText(String.format("%s %d",reviewable2.getText(), reviewable));
            if(reviewable > 0){
                //circle2.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
                hasReviewItems  = true;
            }else {
                //circle2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_red));
            }

            /////////////////// box 3 //////////////////////
            queryBuilder = flashcardDao.queryBuilder();
            total  =  queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(categoryId),
                    FlashcardDao.Properties.CurrentBox.eq(3)
            ).buildCount().count();
            reviewable = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.le(currentDate),
                    FlashcardDao.Properties.CurrentBox.eq(3),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
            ).buildCount().count();
            total3.setText(String.format("%s %d",total3.getText(),total));
            reviewable3.setText(String.format("%s %d",reviewable3.getText(), reviewable));
            if(reviewable > 0){
                //circle3.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
                hasReviewItems  = true;
            }else {
                //circle3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_red));
            }

            /////////////////// box 4 //////////////////////
            queryBuilder = flashcardDao.queryBuilder();
            total  =  queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(categoryId),
                    FlashcardDao.Properties.CurrentBox.eq(4)
            ).buildCount().count();
            reviewable = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.le(currentDate),
                    FlashcardDao.Properties.CurrentBox.eq(4),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
            ).buildCount().count();
            total4.setText(String.format("%s %d",total4.getText(),total));
            reviewable4.setText(String.format("%s %d",reviewable4.getText(), reviewable));
            if(reviewable > 0){
                //circle4.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
                hasReviewItems  = true;
            }else {
                //circle4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_red));
            }
            /////////////////// box 5 //////////////////////
            queryBuilder = flashcardDao.queryBuilder();
            total  =  queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(categoryId),
                    FlashcardDao.Properties.CurrentBox.eq(5)
            ).buildCount().count();
            reviewable = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.le(currentDate),
                    FlashcardDao.Properties.CurrentBox.eq(5),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
            ).buildCount().count();
            total5.setText(String.format("%s %d",total5.getText(),total));
            reviewable5.setText(String.format("%s %d",reviewable5.getText(), reviewable));
            if(reviewable > 0){
                //circle5.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
                hasReviewItems  = true;
            }else {
                //circle5.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_red));
            }

            startReviewBtn.setEnabled(hasReviewItems);
            if(!hasReviewItems){
                startReviewBtn.setTextColor(Color.parseColor("#C0C0C0"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.category_home_fragment_menu, menu);

        displayHelp();

    }

    private void displayHelp() {
        if(Util.fetchFromPreferences(Const.SEEN_CATEGORY_HOME_HINT) != null){
            return;
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
            final TapTargetSequence sequence = new TapTargetSequence(getActivity())
                    .targets(
                            /*TapTarget.forToolbarMenuItem(mainActivity.toolbar, R.id.action_add_flashcard, getString(R.string.add_flashcard), getString(R.string.add_flashcard_help)).id(1),*/
                            TapTarget.forView((Button)mainActivity.findViewById(R.id.start_review_btn),getString(R.string.review_short_hint), getString(R.string.review_long_hint))
                                    .transparentTarget(true)
                                    .targetRadius(100)
                                    .cancelable(false)
                                    .id(2),
                            TapTarget.forView((AppCompatImageButton)mainActivity.findViewById(R.id.free_review_btn),getString(R.string.free_review_short_hint), getString(R.string.free_review_long_hint))
                                    .transparentTarget(true)
                                    .targetRadius(100)
                                    .cancelable(true)
                                    .id(3)

                    )
                    .listener(new TapTargetSequence.Listener() {
                        @Override
                        public void onSequenceFinish() {
                            Util.saveInPreferences(Const.SEEN_CATEGORY_HOME_HINT,String.valueOf("1"));
                        }

                        @Override
                        public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                        }

                        @Override
                        public void onSequenceCanceled(TapTarget lastTarget) {
                            Util.saveInPreferences(Const.SEEN_CATEGORY_HOME_HINT,String.valueOf("1"));
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
            case R.id.action_add_flashcard: {
                Bundle argsFragment = new Bundle();
                argsFragment.putLong(Const.CATEGORY_ID, categoryId);
                AddFlashcardMethodsFragment addFlashcardMethodsFragment = new AddFlashcardMethodsFragment();
                addFlashcardMethodsFragment.setArguments(argsFragment);
                mainActivity.replaceFragment(addFlashcardMethodsFragment, AddFlashcardMethodsFragment.TAG,true);
                return true;
            }case R.id.action_delete_category:{
                mainActivity.showProgressbar();
                categoryDao.deleteByKey(categoryId);
                List<Flashcard> flashcardList  =  flashcardDao.queryBuilder().where(
                        FlashcardDao.Properties.CategoryId.eq(categoryId)
                ).list();
                for(Flashcard flashcard: flashcardList){
                    flashcardDao.deleteByKey(flashcard.getId());
                }
                HomeFragment homeFragment = new HomeFragment();
                mainActivity.replaceFragment(homeFragment, HomeFragment.TAG,true);
                mainActivity.hideProgressbar();
                return true;
            }case R.id.action_edit_category:{
                Category  category  = categoryDao.load(categoryId);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Const.EDIT,true);
                bundle.putLong(Const.ID,category.getId());
                bundle.putString(Const.NAME,category.getName());
                bundle.putString(Const.COLOR,category.getCodeColor());
                NewCategorymFragment  newCategorymFragment= new NewCategorymFragment();
                newCategorymFragment.setArguments(bundle);
                mainActivity.replaceFragment(newCategorymFragment, NewCategorymFragment.TAG,true);
                return true;
            }

            default:
                break;
        }*/

        return super.onOptionsItemSelected(item);
    }


}
