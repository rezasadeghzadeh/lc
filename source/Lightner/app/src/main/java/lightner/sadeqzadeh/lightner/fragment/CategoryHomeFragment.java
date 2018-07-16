package lightner.sadeqzadeh.lightner.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
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
    MainActivity mainActivity;
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

    private ImageView circle1;
    private ImageView circle2;
    private ImageView circle3;
    private ImageView circle4;
    private ImageView circle5;


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
        circle1 = view.findViewById(R.id.circle1);
        circle2 = view.findViewById(R.id.circle2);
        circle3 = view.findViewById(R.id.circle3);
        circle4 = view.findViewById(R.id.circle4);
        circle5 = view.findViewById(R.id.circle5);

        populateBoxesStats();

        return  view;
    }

    private void populateBoxesStats() {
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        try {
            Date currentDate  =  simpleDateFormat.parse(simpleDateFormat.format(new Date()));

            ////////////////////////box1 ///////////////////////
            QueryBuilder<Flashcard> queryBuilder = flashcardDao.queryBuilder();
            long  total  =  queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(categoryId),
                    FlashcardDao.Properties.CurrentBox.eq(1)
            ).buildCount().count();
            long  reviewable = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.ge(currentDate),
                    FlashcardDao.Properties.CurrentBox.eq(1),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
                    ).buildCount().count();
            total1.setText(String.format("%s %d",total1.getText(),total));
            reviewable1.setText(String.format("%s %d",reviewable1.getText(), reviewable));
            if(reviewable > 0){
                circle1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_green));
            }else {
                circle1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_red));
            }

            ////////////// box 2/////////////////////////////
            queryBuilder = flashcardDao.queryBuilder();
            total  =  queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(categoryId),
                    FlashcardDao.Properties.CurrentBox.eq(2)
            ).buildCount().count();
            reviewable = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.ge(currentDate),
                    FlashcardDao.Properties.CurrentBox.eq(2),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
            ).buildCount().count();
            total2.setText(String.format("%s %d",total2.getText(),total));
            reviewable2.setText(String.format("%s %d",reviewable2.getText(), reviewable));
            if(reviewable > 0){
                circle2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_green));
            }else {
                circle2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_red));
            }

            /////////////////// box 3 //////////////////////
            queryBuilder = flashcardDao.queryBuilder();
            total  =  queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(categoryId),
                    FlashcardDao.Properties.CurrentBox.eq(3)
            ).buildCount().count();
            reviewable = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.ge(currentDate),
                    FlashcardDao.Properties.CurrentBox.eq(3),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
            ).buildCount().count();
            total3.setText(String.format("%s %d",total3.getText(),total));
            reviewable3.setText(String.format("%s %d",reviewable3.getText(), reviewable));
            if(reviewable > 0){
                circle3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_green));
            }else {
                circle3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_red));
            }

            /////////////////// box 4 //////////////////////
            queryBuilder = flashcardDao.queryBuilder();
            total  =  queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(categoryId),
                    FlashcardDao.Properties.CurrentBox.eq(4)
            ).buildCount().count();
            reviewable = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.ge(currentDate),
                    FlashcardDao.Properties.CurrentBox.eq(4),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
            ).buildCount().count();
            total4.setText(String.format("%s %d",total4.getText(),total));
            reviewable4.setText(String.format("%s %d",reviewable4.getText(), reviewable));
            if(reviewable > 0){
                circle4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_green));
            }else {
                circle4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_red));
            }
            /////////////////// box 5 //////////////////////
            queryBuilder = flashcardDao.queryBuilder();
            total  =  queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(categoryId),
                    FlashcardDao.Properties.CurrentBox.eq(5)
            ).buildCount().count();
            reviewable = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.ge(currentDate),
                    FlashcardDao.Properties.CurrentBox.eq(5),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
            ).buildCount().count();
            total5.setText(String.format("%s %d",total5.getText(),total));
            reviewable5.setText(String.format("%s %d",reviewable5.getText(), reviewable));
            if(reviewable > 0){
                circle5.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_green));
            }else {
                circle5.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_red));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                Bundle argsFragment = new Bundle();
                argsFragment.putLong(Const.CATEGORY_ID, categoryId);
                AddFlashcardMethodsFragment addFlashcardMethodsFragment = new AddFlashcardMethodsFragment();
                addFlashcardMethodsFragment.setArguments(argsFragment);
                mainActivity.replaceFragment(addFlashcardMethodsFragment, AddFlashcardMethodsFragment.TAG);
                return false;
            }case R.id.action_delete_category:{
                categoryDao.deleteByKey(categoryId);
                HomeFragment homeFragment = new HomeFragment();
                mainActivity.replaceFragment(homeFragment, HomeFragment.TAG);
                return false;
            }case R.id.action_edit_category:{
                Category  category  = categoryDao.load(categoryId);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Const.EDIT,true);
                bundle.putLong(Const.ID,category.getId());
                bundle.putString(Const.NAME,category.getName());
                bundle.putString(Const.COLOR,category.getCodeColor());
                NewCategorymFragment  newCategorymFragment= new NewCategorymFragment();
                newCategorymFragment.setArguments(bundle);
                mainActivity.replaceFragment(newCategorymFragment, NewCategorymFragment.TAG);
                return false;
            }

            default:
                break;
        }

        return false;
    }


}
