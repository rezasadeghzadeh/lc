package lightner.sadeqzadeh.lightner.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.entity.CategoryDao;
import lightner.sadeqzadeh.lightner.entity.Flashcard;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;

public class FreeReviewFlashcard extends Fragment{
    public static final String TAG = FreeReviewFlashcard.class.getName();
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
    private LinearLayout optionBox1;
    private LinearLayout optionBox2;
    private LinearLayout optionBox3;
    private LinearLayout optionBox4;
    private LinearLayout optionBoxContainer1;
    private LinearLayout optionBoxContainer2;
    private LinearLayout optionBoxContainer3;
    private LinearLayout optionBoxContainer4;
    public boolean reviewMode;
    private Integer numberFlashcardToShow;
    private TextView currentFlashcardTextView;
    private TextView totalFlashcardsTextView;
    private AppCompatImageButton nextBtn;
    private AppCompatImageButton previousBtn;
    private boolean answered=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity  = (MainActivity) getActivity();
        args = getArguments();
        flashcardDao = mainActivity.getDaoSession().getFlashcardDao();
        categoryDao =  mainActivity.getDaoSession().getCategoryDao();
        categoryId  = args.getLong(Const.CATEGORY_ID);
        reviewMode  =  args.getBoolean(Const.REVIEW_MODE,false);
        numberFlashcardToShow = args.getInt(Const.FLASHCARD_TO_SHOW);
        if(numberFlashcardToShow == null || numberFlashcardToShow<0){
            numberFlashcardToShow = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.free_review_flashcard_layout, container, false);
        question =  view.findViewById(R.id.question);
        questionImageView = view.findViewById(R.id.review_question_image_view);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);
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
        currentFlashcardTextView = view.findViewById(R.id.current_flashcard);
        totalFlashcardsTextView = view.findViewById(R.id.total_flashcard);
        nextBtn = view.findViewById(R.id.next_btn);
        previousBtn = view.findViewById(R.id.previous_btn);

        //get current flash card
        List<Flashcard> flashcardList =  flashcardDao.queryBuilder().where(
                FlashcardDao.Properties.CategoryId.eq(categoryId)
        ).orderDesc(FlashcardDao.Properties.CurrentBox).list();

        if(numberFlashcardToShow < flashcardList.size() && reviewMode){
            flashcard = flashcardList.get(numberFlashcardToShow);
        }else {
            Bundle args = new Bundle();
            args.putLong(Const.CATEGORY_ID,categoryId);
            CategoryHomeFragment  categoryHomeFragment  = new CategoryHomeFragment();
            categoryHomeFragment.setArguments(args);
            mainActivity.replaceFragment(categoryHomeFragment, CategoryHomeFragment.TAG,false);
            Toast.makeText(getActivity(),getString(R.string.flashcard_ended),Toast.LENGTH_LONG).show();
            return view;
        }
        currentFlashcardTextView.setText(String.valueOf(numberFlashcardToShow+1));
        totalFlashcardsTextView.setText(String.valueOf(flashcardList.size()));
        if(flashcard != null){
            question.setText(flashcard.getQuestion());
            initOptions(view,1,flashcard.getOption1(),imageOption1,option1,optionBox1, optionBoxContainer1);
            initOptions(view,2,flashcard.getOption2(),imageOption2,option2,optionBox2, optionBoxContainer2);
            initOptions(view,3,flashcard.getOption3(),imageOption3,option3,optionBox3, optionBoxContainer3);
            initOptions(view,4,flashcard.getOption4(),imageOption4,option4,optionBox4, optionBoxContainer4);
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle boxArgs  = new Bundle();
                boxArgs.putLong(Const.CATEGORY_ID,categoryId);
                boxArgs.putInt(Const.FLASHCARD_TO_SHOW,numberFlashcardToShow+1);
                boxArgs.putBoolean(Const.REVIEW_MODE,true);
                FreeReviewFlashcard freeReviewFlashcard = new FreeReviewFlashcard();
                freeReviewFlashcard.setArguments(boxArgs);
                mainActivity.replaceFragment(freeReviewFlashcard,FreeReviewFlashcard.TAG,false);
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberFlashcardToShow == 0){
                    return;
                }
                Bundle boxArgs  = new Bundle();
                boxArgs.putLong(Const.CATEGORY_ID,categoryId);
                boxArgs.putInt(Const.FLASHCARD_TO_SHOW,numberFlashcardToShow-1);
                boxArgs.putBoolean(Const.REVIEW_MODE,true);
                FreeReviewFlashcard freeReviewFlashcard = new FreeReviewFlashcard();
                freeReviewFlashcard.setArguments(boxArgs);
                mainActivity.replaceFragment(freeReviewFlashcard,FreeReviewFlashcard.TAG,false);
            }
        });

        return  view;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.numberFlashcardToShow =0;
    }

    private void initOptions(final View view, final int optionNumber, final String optionText, ImageView imageOption, TextView option, final LinearLayout optionBox, LinearLayout optionBoxContainer){
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
        }
        optionBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answered){
                    return;
                }
                answered = true;

                if (optionNumber  == flashcard.getCorrectOption() ){
                    Drawable drawable = getContext().getResources().getDrawable(R.drawable.round_button_green);
                    optionBox.setBackground(drawable);
                }else {
                    Drawable drawable = getContext().getResources().getDrawable(R.drawable.round_button_red);
                    optionBox.setBackground(drawable);

                    // green the correct  option
                    int resID = getResources().getIdentifier("option_box_" + flashcard.getCorrectOption(), "id", getContext().getPackageName());
                    LinearLayout correctOptionBox  =  view.findViewById(resID);
                    drawable = getContext().getResources().getDrawable(R.drawable.round_button_green);
                    correctOptionBox.setBackground(drawable);
                }
            }
        });
        //////////////////////////////////
    }
}
