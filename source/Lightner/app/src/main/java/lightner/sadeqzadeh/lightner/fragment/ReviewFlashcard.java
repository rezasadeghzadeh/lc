package lightner.sadeqzadeh.lightner.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
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
    private ImageView questionImageView;
    private TextView answer;
    private ImageView answerImageView;
    private LinearLayout answerBox;
    private Button viewAnswerBtn;
    private AppCompatImageButton correctBtn;
    private AppCompatImageButton inCorrectBtn;
    private boolean reviewMode;
    private ImageView speech;
    TextToSpeech textToSpeech;
    ScrollView scrollView;
    TapTargetSequence answerButtonSequence;
    private LinearLayout correctIncorrectBtnContainer;
    private RelativeLayout viewAnswerContainer;

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
        scrollView  =  view.findViewById(R.id.scroll_view);
        answer  =  view.findViewById(R.id.answer);
        answerBox  =  view.findViewById(R.id.answer_box);
        answerImageView = view.findViewById(R.id.review_answer_image_view);
        question =  view.findViewById(R.id.question);
        questionImageView = view.findViewById(R.id.review_question_image_view);
        viewAnswerBtn = view.findViewById(R.id.view_answer_btn);
        correctBtn = view.findViewById(R.id.correct_btn);
        inCorrectBtn = view.findViewById(R.id.incorrect_btn);
        speech  = view.findViewById(R.id.speech);
        viewAnswerContainer  = view.findViewById(R.id.view_answer_container);
        final Date currentDate  =  new Date();

        //get current flash card
        List<Flashcard> flashcardList  =  flashcardDao.queryBuilder().where(
                FlashcardDao.Properties.NextVisit.le(currentDate),
                FlashcardDao.Properties.CategoryId.eq(categoryId)
        ).orderDesc(FlashcardDao.Properties.CurrentBox).limit(1).list();
        if(flashcardList.size() > 0 ){
            flashcard = flashcardList.get(0);
            question.setText(mainActivity.decryptText(flashcard.getQuestion()));
            answer.setText(mainActivity.decryptText(flashcard.getAnswer()));
            if(flashcard.getQuestionUri() != null &&  !flashcard.getQuestionUri().isEmpty()){
                questionImageView.setImageURI(Uri.parse(flashcard.getQuestionUri()));
                questionImageView.setVisibility(View.VISIBLE);
                questionImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Const.URI,flashcard.getQuestionUri());
                        FullscreenImageFragment fragment = new FullscreenImageFragment();
                        fragment.setArguments(bundle);
                        mainActivity.replaceFragment(fragment,FullscreenImageFragment.class.getName(),true);
                    }
                });
            }
            if(flashcard.getAnswerUri() != null &&  !flashcard.getAnswerUri().isEmpty() ){
                answerImageView.setImageURI(Uri.parse(flashcard.getAnswerUri()));
                answerImageView.setVisibility(View.VISIBLE);
                answerImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Const.URI,flashcard.getAnswerUri());
                        FullscreenImageFragment fragment = new FullscreenImageFragment();
                        fragment.setArguments(bundle);
                        mainActivity.replaceFragment(fragment,FullscreenImageFragment.class.getName(),true);
                    }
                });
            }
        }else if(reviewMode){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(ReviewFinishedDialogFragment.class.getName());
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            DialogFragment dialogFragment = new ReviewFinishedDialogFragment();
            Bundle args = new Bundle();
            args.putLong(Const.CATEGORY_ID,categoryId);
            dialogFragment.setArguments(args);
            dialogFragment.show(ft, ReviewFinishedDialogFragment.class.getName());
            return view;
        }

        viewAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerBox.setVisibility(View.VISIBLE);
                viewAnswerBtn.setVisibility(View.INVISIBLE);
                correctBtn.setVisibility(View.VISIBLE);
                inCorrectBtn.setVisibility(View.VISIBLE);
            }
        });

        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.textToSpeech != null && mainActivity.speechStatus){
                    mainActivity.textToSpeech.speak(question.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });

        correctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcard.setNextVisit(getCorrectNextVisitDate(flashcard.getCurrentBox()));
                flashcard.setCurrentBox(flashcard.getCurrentBox() + 1);
                flashcardDao.update(flashcard);
                Bundle boxArgs  = new Bundle();
                boxArgs.putLong(Const.CATEGORY_ID,categoryId);
                boxArgs.putBoolean(Const.REVIEW_MODE,true);
                ReviewFlashcard reviewFlashcard = new ReviewFlashcard();
                reviewFlashcard.setArguments(boxArgs);
                mainActivity.replaceFragment(reviewFlashcard,ReviewFlashcard.TAG,false);
            }
        });

        inCorrectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcard.setNextVisit(getIncorrectNextVisitDate());
                flashcard.setCurrentBox(1);
                flashcardDao.update(flashcard);
                Bundle boxArgs  = new Bundle();
                boxArgs.putLong(Const.CATEGORY_ID,categoryId);
                boxArgs.putBoolean(Const.REVIEW_MODE,true);
                ReviewFlashcard reviewFlashcard = new ReviewFlashcard();
                reviewFlashcard.setArguments(boxArgs);
                mainActivity.replaceFragment(reviewFlashcard,ReviewFlashcard.TAG,false);
            }
        });
        //update last review
        Category category = categoryDao.load(categoryId);
        category.setLastVisit(new Date());
        categoryDao.update(category);
        displayHelp();
        return  view;
    }

    private void displayHelp() {
        if(Util.fetchFromPreferences(Const.SEEN_REVIEW_FLASHCARD_HINT) != null){
            return;
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final TapTargetSequence sequence = new TapTargetSequence(getActivity())
                        .targets(
                                TapTarget.forView((Button)mainActivity.findViewById(R.id.view_answer_btn),getString(R.string.answer_btn_short_hint), getString(R.string.answer_btn_long_hint))
                                        .cancelable(false).transparentTarget(true)
                                        .targetRadius(100)
                                        .id(1)

                        )
                        .listener(new TapTargetSequence.Listener() {
                            @Override
                            public void onSequenceFinish() {
                                Util.saveInPreferences(Const.SEEN_REVIEW_FLASHCARD_HINT,String.valueOf("1"));
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

    private Date getIncorrectNextVisitDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date currentDate;
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
            currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            return calendar.getTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Date getCorrectNextVisitDate(int boxNumber) {
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

    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

}
