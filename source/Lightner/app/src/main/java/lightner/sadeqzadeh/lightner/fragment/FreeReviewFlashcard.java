package lightner.sadeqzadeh.lightner.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
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
    private TextView answer;
    private ImageView answerImageView;
    private LinearLayout answerBox;
    private AppCompatImageButton nextBtn;
    private AppCompatImageButton previousBtn;
    private Button viewAnswerBtn;
    public boolean reviewMode;
    private Integer numberFlashcardToShow;
    private RelativeLayout viewAnswerContainer;
    private TextView currentFlashcardTextView;
    private TextView totalFlashcardsTextView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.free_review_flashcard_layout, container, false);
        answer  =  view.findViewById(R.id.answer);
        answerBox  =  view.findViewById(R.id.answer_box);
        answerImageView = view.findViewById(R.id.review_answer_image_view);
        question =  view.findViewById(R.id.question);
        questionImageView = view.findViewById(R.id.review_question_image_view);
        viewAnswerBtn = view.findViewById(R.id.view_answer_btn);
        viewAnswerContainer  = view.findViewById(R.id.view_answer_container);
        nextBtn = view.findViewById(R.id.next_btn);
        previousBtn = view.findViewById(R.id.previous_btn);
        currentFlashcardTextView = view.findViewById(R.id.current_flashcard);
        totalFlashcardsTextView = view.findViewById(R.id.total_flashcard);

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
        }

        viewAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerBox.setVisibility(View.VISIBLE);
                viewAnswerContainer.setVisibility(View.GONE);
            }
        });

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

}
