package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.entity.Category;
import lightner.sadeqzadeh.lightner.entity.CategoryDao;
import lightner.sadeqzadeh.lightner.entity.Flashcard;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;

public class ReviewFlashcard extends Fragment {
    private MainActivity mainActivity;
    private Bundle args;
    private FlashcardDao flashcardDao;
    private CategoryDao categoryDao;
    private Flashcard flashcard;
    private Long  categoryId;
    private TextView question;
    private TextView answer;
    private LinearLayout answerBox;
    private Button viewAnswerBtn;
    private AppCompatImageButton correctBtn;
    private AppCompatImageButton inCorrectBtn;
    private boolean reviewMode;

    public static final String TAG = ReviewFlashcard.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity  = (MainActivity) getActivity();
        args  = getArguments();
        flashcardDao = mainActivity.getDaoSession().getFlashcardDao();
        categoryDao =  mainActivity.getDaoSession().getCategoryDao();
        categoryId  = args.getLong(Const.CATEGORY_ID);
        reviewMode  =  args.getBoolean(Const.REVIEW_MODE,false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_flashcard_layout, container, false);
        answer  =  view.findViewById(R.id.answer);
        answerBox  =  view.findViewById(R.id.answer_box);
        question =  view.findViewById(R.id.question);
        viewAnswerBtn = view.findViewById(R.id.view_answer_btn);
        correctBtn = view.findViewById(R.id.correct_btn);
        inCorrectBtn = view.findViewById(R.id.incorrect_btn);

        final Date currentDate  =  new Date();

        //get current flash card
        List<Flashcard> flashcardList  =  flashcardDao.queryBuilder().where(
                FlashcardDao.Properties.NextVisit.le(currentDate),
                FlashcardDao.Properties.CategoryId.eq(categoryId)
        ).orderDesc(FlashcardDao.Properties.CurrentBox).limit(1).list();
        if(mainActivity.backPressed){
            mainActivity.backPressed  = false;
            HomeFragment  homeFragment  = new HomeFragment();
            mainActivity.replaceFragment(homeFragment, HomeFragment.TAG);
            return view;
        }
        if(flashcardList.size() > 0 ){
            flashcard = flashcardList.get(0);
            question.setText(flashcard.getQuestion());
            answer.setText(flashcard.getAnswer());
        }else if(reviewMode){
            Bundle args = new Bundle();
            args.putLong(Const.CATEGORY_ID,categoryId);
            CategoryHomeFragment  categoryHomeFragment  = new CategoryHomeFragment();
            categoryHomeFragment.setArguments(args);
            mainActivity.replaceFragment(categoryHomeFragment, CategoryHomeFragment.TAG);
            return view;
        }

        viewAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerBox.setVisibility(View.VISIBLE);
            }
        });

        correctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcard.setNextVisit(getNextVisitDate(flashcard.getCurrentBox()));
                flashcard.setCurrentBox(flashcard.getCurrentBox() + 1);
                flashcardDao.update(flashcard);
                Bundle boxArgs  = new Bundle();
                boxArgs.putLong(Const.CATEGORY_ID,categoryId);
                boxArgs.putBoolean(Const.REVIEW_MODE,true);
                ReviewFlashcard reviewFlashcard = new ReviewFlashcard();
                reviewFlashcard.setArguments(boxArgs);
                mainActivity.replaceFragment(reviewFlashcard,ReviewFlashcard.TAG);
            }
        });

        //update last review
        Category category = categoryDao.load(categoryId);
        category.setLastVisit(new Date());
        categoryDao.update(category);
        return  view;
    }

    private Date getNextVisitDate(int boxNumber) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date currentDate;
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
            currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            switch (boxNumber){
                case 1:
                    calendar.add(Calendar.DAY_OF_MONTH,1);
                    return calendar.getTime();
                case 2:
                    calendar.add(Calendar.DAY_OF_MONTH,2);
                    return calendar.getTime();
                case 3:
                    calendar.add(Calendar.DAY_OF_MONTH,4);
                    return calendar.getTime();
                case 4:
                    calendar.add(Calendar.DAY_OF_MONTH,8);
                    return calendar.getTime();
                case 5:
                    calendar.add(Calendar.DAY_OF_MONTH,16);
                    return calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



}
