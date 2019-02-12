package lightner.sadeqzadeh.lightner.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private TextView option1;
    private TextView option2;
    private TextView option3;
    private TextView option4;
    private ImageView imageOption1;
    private ImageView imageOption2;
    private ImageView imageOption3;
    private ImageView imageOption4;
    private TextView optionLetter1;
    private TextView optionLetter2;
    private TextView optionLetter3;
    private TextView optionLetter4;
    private LinearLayout optionBox1;
    private LinearLayout optionBox2;
    private LinearLayout optionBox3;
    private LinearLayout optionBox4;
    private LinearLayout optionBoxContainer1;
    private LinearLayout optionBoxContainer2;
    private LinearLayout optionBoxContainer3;
    private LinearLayout optionBoxContainer4;
    private boolean reviewMode;
    private ImageView speech;
    TextToSpeech textToSpeech;
    public static final String TAG = ReviewFlashcard.class.getName();
    private boolean answered=false;
    private ProgressBar progressBar;
    private Integer numberFlashcardToShow=0;
    private Integer reviewableFlashcardsCount = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity  = (MainActivity) getActivity();
        args  = getArguments();
        flashcardDao = mainActivity.getDaoSession().getFlashcardDao();
        categoryDao =  mainActivity.getDaoSession().getCategoryDao();
        categoryId  = args.getLong(Const.CATEGORY_ID);
        reviewMode  =  args.getBoolean(Const.REVIEW_MODE,false);
        numberFlashcardToShow = args.getInt(Const.FLASHCARD_TO_SHOW);
        if(numberFlashcardToShow == null || numberFlashcardToShow<0){
            numberFlashcardToShow = 0;
        }
        if(reviewableFlashcardsCount == null){
            reviewableFlashcardsCount =  args.getInt(Const.REVIEWABLE_FLASH_CARDS_COUNTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.review_flashcard_layout, container, false);
        question =  view.findViewById(R.id.question);
        questionImageView = view.findViewById(R.id.review_question_image_view);
        speech  = view.findViewById(R.id.speech);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);
        optionLetter1 = view.findViewById(R.id.option_letter_1);
        optionLetter2 = view.findViewById(R.id.option_letter_2);
        optionLetter3 = view.findViewById(R.id.option_letter_3);
        optionLetter4 = view.findViewById(R.id.option_letter_4);
        imageOption1 = view.findViewById(R.id.image_option_1);
        imageOption2 = view.findViewById(R.id.image_option_2);
        imageOption3 = view.findViewById(R.id.image_option_3);
        imageOption4 = view.findViewById(R.id.image_option_4);
        optionBox1 = view.findViewById(R.id.option_box_1);
        optionBox2 = view.findViewById(R.id.option_box_2);
        optionBox3 = view.findViewById(R.id.option_box_3);
        optionBox4 = view.findViewById(R.id.option_box_4);
        optionBoxContainer1= view.findViewById(R.id.option_box_container_1);
        optionBoxContainer2= view.findViewById(R.id.option_box_container_2);
        optionBoxContainer3= view.findViewById(R.id.option_box_container_3);
        optionBoxContainer4= view.findViewById(R.id.option_box_container_4);
        progressBar  = view.findViewById(R.id.progress);

        final Date currentDate  =  new Date();

        if(reviewableFlashcardsCount == null || reviewableFlashcardsCount == 0){
            //get current flash card
            List<Flashcard> reviwableFlashcards  =  flashcardDao.queryBuilder().where(
                    FlashcardDao.Properties.NextVisit.le(currentDate),
                    FlashcardDao.Properties.CategoryId.eq(categoryId)
            ).orderDesc(FlashcardDao.Properties.CurrentBox).list();
            reviewableFlashcardsCount  =  reviwableFlashcards.size();
        }


        List<Flashcard> flashcardList  =  flashcardDao.queryBuilder().where(
                FlashcardDao.Properties.NextVisit.le(currentDate),
                FlashcardDao.Properties.CategoryId.eq(categoryId)
        ).orderDesc(FlashcardDao.Properties.CurrentBox).limit(1).list();
        //set progress values
        progressBar.setMax(reviewableFlashcardsCount);
        progressBar.setProgress(numberFlashcardToShow);
        if(flashcardList.size() > 0 ){
            flashcard = flashcardList.get(0);
            question.setText(flashcard.getQuestion());
            initOptions(view,1,flashcard.getOption1(),imageOption1,option1,optionBox1, optionBoxContainer1,optionLetter1);
            initOptions(view,2,flashcard.getOption2(),imageOption2,option2,optionBox2, optionBoxContainer2,optionLetter2);
            initOptions(view,3,flashcard.getOption3(),imageOption3,option3,optionBox3, optionBoxContainer3,optionLetter3);
            initOptions(view,4,flashcard.getOption4(),imageOption4,option4,optionBox4, optionBoxContainer4,optionLetter4);
            if(Util.fetchFromPreferences(Const.AUTO_SPELL_QUESTION) == null || Util.fetchFromPreferences(Const.AUTO_SPELL_QUESTION).equals("true")) {
                if (mainActivity.textToSpeech != null && mainActivity.speechStatus) {
                    mainActivity.textToSpeech.speak(question.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
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


        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.textToSpeech != null && mainActivity.speechStatus){
                    mainActivity.textToSpeech.speak(question.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });

     /*   correctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

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

/*
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final TapTargetSequence sequence = new TapTargetSequence(getActivity())
                        .targets(
                                TapTarget.forView((Button)mainActivity.findViewById(R.id.option_box_1),getString(R.string.answer_btn_short_hint), getString(R.string.answer_btn_long_hint))
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
*/

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
                    calendar.add(Calendar.DAY_OF_MONTH,3);
                    return calendar.getTime();
                case 4:
                    calendar.add(Calendar.DAY_OF_MONTH,4);
                    return calendar.getTime();
                case 5:
                    calendar.add(Calendar.DAY_OF_MONTH,5);
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

    private void initOptions(final View view, final int optionNumber, final String optionText, ImageView imageOption, TextView option, final LinearLayout optionBox, LinearLayout optionBoxContainer, final TextView optionLetter){
        if(optionText == null || optionText.isEmpty()){
            optionBoxContainer.setVisibility(View.GONE);
            return;
        }
        ///////////////////////////////
        if(optionText.contains(".jpg")){
            InputStream ims = null;
            try {
                ims = getContext().getAssets().open(optionText);
                Drawable d = Drawable.createFromStream(ims, null);
                imageOption.setImageDrawable(d);
                imageOption.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            option.setText(optionText);
            option.setVisibility(View.VISIBLE);
        }

        optionBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickHandler(view, optionNumber, optionLetter);
            }
        });
        optionLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickHandler(view, optionNumber, optionLetter);
            }
        });
    }
    private void onClickHandler(View view, int optionNumber, TextView optionLetter){
        if(answered){
            return;
        }
        answered = true;
        if (optionNumber  == flashcard.getCorrectOption() ){
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.correct_option_letter);
            optionLetter.setBackground(drawable);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    flashcard.setNextVisit(getCorrectNextVisitDate(flashcard.getCurrentBox()));
                    flashcard.setCurrentBox(flashcard.getCurrentBox() + 1);
                    flashcardDao.update(flashcard);
                    Bundle boxArgs  = new Bundle();
                    boxArgs.putLong(Const.CATEGORY_ID,categoryId);
                    boxArgs.putInt(Const.FLASHCARD_TO_SHOW,numberFlashcardToShow+1);
                    boxArgs.putInt(Const.REVIEWABLE_FLASH_CARDS_COUNTS,reviewableFlashcardsCount);
                    boxArgs.putBoolean(Const.REVIEW_MODE,true);
                    ReviewFlashcard reviewFlashcard = new ReviewFlashcard();
                    reviewFlashcard.setArguments(boxArgs);
                    mainActivity.replaceFragment(reviewFlashcard,ReviewFlashcard.TAG,false);
                }
            }, 3000);

        }else {
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.incorrect_option_letter);
            optionLetter.setBackground(drawable);

            // green the correct  option
            int resID = getResources().getIdentifier("option_letter_" + flashcard.getCorrectOption(), "id", getContext().getPackageName());
            TextView correctOptionLetter  =  view.findViewById(resID);
            drawable = getContext().getResources().getDrawable(R.drawable.correct_option_letter);
            correctOptionLetter.setBackground(drawable);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    flashcard.setNextVisit(getIncorrectNextVisitDate());
                    flashcard.setCurrentBox(1);
                    flashcardDao.update(flashcard);
                    Bundle boxArgs  = new Bundle();
                    boxArgs.putLong(Const.CATEGORY_ID,categoryId);
                    boxArgs.putInt(Const.FLASHCARD_TO_SHOW,numberFlashcardToShow+1);
                    boxArgs.putInt(Const.REVIEWABLE_FLASH_CARDS_COUNTS,reviewableFlashcardsCount);
                    boxArgs.putBoolean(Const.REVIEW_MODE,true);
                    ReviewFlashcard reviewFlashcard = new ReviewFlashcard();
                    reviewFlashcard.setArguments(boxArgs);
                    mainActivity.replaceFragment(reviewFlashcard,ReviewFlashcard.TAG,false);
                }
            }, 3000);
        }
    }
}

