package lightner.sadeqzadeh.lightner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Date;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.entity.Flashcard;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;

import static android.app.Activity.RESULT_OK;

public class NewFlashCardFragment extends Fragment{

    public static final String TAG = NewFlashCardFragment.class.getName();
    private static final int IMAGE_HEIGHT = 400;
    private static final int IMAGE_WIDTH = 400;
    private EditText question;
    private Button questionTakeImage;
    private Button answerTakeImage;
    private EditText answer;
    FlashcardDao flashcardDao;
    private Bundle args;
    private Long categoryId;
    private MainActivity mainActivity;
    private boolean takeQuestionImage;
    private boolean takeAnswerImage;
    private ImageView questionImageView;
    private String questionImageUri;
    private ImageView answerImageView;
    private String answerImageUri;
    private NewFlashCardFragment currentFragment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        args  = getArguments();
        categoryId  = args.getLong(Const.CATEGORY_ID);
        mainActivity = (MainActivity) getActivity();
        flashcardDao  = mainActivity.getDaoSession().getFlashcardDao();
        currentFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_flashcard_layout, container, false);
        question  = view.findViewById(R.id.question);
        answer = view.findViewById(R.id.answer);
        questionImageView = view.findViewById(R.id.question_image_view);
        answerImageView = view.findViewById(R.id.answer_image_view);
        initTakeImageButtons(view);
        return  view;
    }

    private void initTakeImageButtons(View view) {
        view.findViewById(R.id.take_question_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeQuestionImage = true;
                takeAnswerImage = false;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle(getString(R.string.question_image_title))
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle("Done")
                        .setRequestedSize(IMAGE_WIDTH, IMAGE_HEIGHT)
                        .setCropMenuCropButtonIcon(R.drawable.ic_crop)
                        .start(currentFragment.getContext(),currentFragment);
            }
        });

        view.findViewById(R.id.take_answer_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAnswerImage = true;
                takeQuestionImage = false;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle(getString(R.string.question_image_title))
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle("Done")
                        .setRequestedSize(IMAGE_WIDTH, IMAGE_HEIGHT)
                        .setCropMenuCropButtonIcon(R.drawable.ic_crop)
                        .start(currentFragment.getContext(),currentFragment);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if(takeQuestionImage){
                    questionImageView.setImageURI(result.getUri());
                    questionImageUri  =  result.getUri().toString();
                    questionImageView.setVisibility(View.VISIBLE);
                }else {
                    answerImageView.setImageURI(result.getUri());
                    answerImageUri  =  result.getUri().toString();
                    answerImageView.setVisibility(View.VISIBLE);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(mainActivity, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.new_flashcard_menu, menu);
        displayHelp();
    }

    private void displayHelp() {
        if(Util.fetchFromPreferences(Const.SEEN_NEW_FLASHCARD_HINT) != null){
            return;
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final TapTargetSequence sequence = new TapTargetSequence(getActivity())
                        .targets(
                                TapTarget.forToolbarMenuItem(mainActivity.toolbar, R.id.save_flashcard, getString(R.string.save_flashcard), getString(R.string.save_flashcard_help)).id(1).cancelable(false),
                                TapTarget.forView(mainActivity.findViewById(R.id.take_question_image), getString(R.string.take_image), getString(R.string.take_image_long_help)).cancelable(false).id(2)
                        )
                        .listener(new TapTargetSequence.Listener() {
                            @Override
                            public void onSequenceFinish() {
                                Util.saveInPreferences(Const.SEEN_NEW_FLASHCARD_HINT,String.valueOf("1"));
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
        switch (item.getItemId()) {
            case R.id.save_flashcard:
                saveFlashCard();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveFlashCard() {
        boolean hasError = false;
        if(question.getText().length()  == 0 ){
            question.setError(getString(R.string.question_is_requirement));
            hasError  = true;
        }

        if(answer.getText().length()  == 0 ){
            answer.setError(getString(R.string.answer_is_requirement));
            hasError  = true;
        }

        if(hasError){
            return;
        }

        Flashcard flashcard = new Flashcard(mainActivity.encryptText(question.getText().toString()),questionImageUri, mainActivity.encryptText(answer.getText().toString()), answerImageUri,1,null,new Date(), categoryId);
        flashcardDao.insert(flashcard);
        Toast.makeText(mainActivity.getApplicationContext(), getString(R.string.flash_added_sucess),Toast.LENGTH_LONG).show();
        question.setText("");
        answer.setText("");
        questionImageView.setImageURI(null);
        questionImageView.setVisibility(View.GONE);
        answerImageView.setImageURI(null);
        answerImageView.setVisibility(View.GONE);
    }


}
