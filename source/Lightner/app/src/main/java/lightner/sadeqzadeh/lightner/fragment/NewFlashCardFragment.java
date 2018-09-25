package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Date;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.entity.Flashcard;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;

public class NewFlashCardFragment extends Fragment{

    public static final String TAG = NewFlashCardFragment.class.getName();
    private EditText question;
    private Button questionTakeImage;
    private Button answerTakeImage;
    private EditText answer;
    FlashcardDao flashcardDao;
    private Bundle args;
    private Long categoryId;
    private MainActivity mainActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        args  = getArguments();
        categoryId  = args.getLong(Const.CATEGORY_ID);
        mainActivity = (MainActivity) getActivity();
        flashcardDao  = mainActivity.getDaoSession().getFlashcardDao();
    }

    private void initQuestionTakeImage(View view) {
        view.findViewById(R.id.take_question_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle(getString(R.string.question_image_title))
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle("Done")
                        .setRequestedSize(400, 300)
                        .setCropMenuCropButtonIcon(R.drawable.ok_button)
                        .start(mainActivity);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_flashcard_layout, container, false);
        question  = view.findViewById(R.id.question);
        answer = view.findViewById(R.id.answer);
        initQuestionTakeImage(view);

        return  view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.new_flashcard_menu, menu);

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

        return true;
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

        Flashcard flashcard = new Flashcard(mainActivity.encryptText(question.getText().toString()), mainActivity.encryptText(answer.getText().toString()),1,null,new Date(), categoryId);
        flashcardDao.insert(flashcard);
        Toast.makeText(mainActivity.getApplicationContext(), getString(R.string.flash_added_sucess),Toast.LENGTH_LONG).show();
        question.setText("");
        answer.setText("");
/*
        Bundle args = new Bundle();
        args.putLong(Const.CATEGORY_ID,categoryId);
        CategoryHomeFragment  categoryHomeFragment  = new CategoryHomeFragment();
        categoryHomeFragment.setArguments(args);
        mainActivity.replaceFragment(categoryHomeFragment, CategoryHomeFragment.TAG,false);
*/

    }


}
