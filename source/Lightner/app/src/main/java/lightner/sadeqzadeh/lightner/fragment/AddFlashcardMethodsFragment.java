package lightner.sadeqzadeh.lightner.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.entity.Flashcard;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;
import lightner.sadeqzadeh.lightner.rest.LightnerAPI;
import lightner.sadeqzadeh.lightner.rest.PackageFlashcard;
import lightner.sadeqzadeh.lightner.rest.PackageWordsResponse;
import lightner.sadeqzadeh.lightner.rest.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFlashcardMethodsFragment extends Fragment {

    public static final String TAG = AddFlashcardMethodsFragment.class.getName();
    MainActivity mainActivity;
    private Bundle args;
    private Long categoryId;
    private Button addManuallyBtn;
    private Button buyFlashcardBtn;
    private Button importPackage1Btn;
    private FlashcardDao flashcardDao;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        args = getArguments();
        categoryId  = args.getLong(Const.CATEGORY_ID);
        flashcardDao =  mainActivity.getDaoSession().getFlashcardDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_flashcard_method_layout, container, false);
        addManuallyBtn = view.findViewById(R.id.add_manually_flashcard_btn);
        addManuallyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle argsFragment = new Bundle();
                argsFragment.putLong(Const.CATEGORY_ID,categoryId);
                NewFlashCardFragment fragment = new NewFlashCardFragment();
                fragment.setArguments(argsFragment);
                mainActivity.replaceFragment(fragment, fragment.TAG);
            }
        });

        buyFlashcardBtn = view.findViewById(R.id.buy_flashcard_btn);
        buyFlashcardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadPackageFragment fragment = new DownloadPackageFragment();
                mainActivity.replaceFragment(fragment, fragment.TAG);
            }
        });

        importPackage1Btn = view.findViewById(R.id.import_package1_btn);
        importPackage1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userCode = Util.fetchAndDecrypt(mainActivity.getApplicationContext(), Const.USER_CODE);
                LightnerAPI lightnerAPI = RetrofitClientInstance.getRetrofitInstance().create(LightnerAPI.class);
                Call<PackageWordsResponse> call = lightnerAPI.getPackageFlashcards(userCode,"1");
                call.enqueue(new Callback<PackageWordsResponse>() {
                    @Override
                    public void onResponse(Call<PackageWordsResponse> call, Response<PackageWordsResponse> response) {
                        if(response.body().getStatus() != null && (response.body().getStatus().equals("invalid user")  || response.body().getStatus().equals("invalid package") )){
                            Toast.makeText(mainActivity.getApplicationContext(),getString(R.string.invalid_user_code),Toast.LENGTH_LONG).show();
                            return;
                        }
                        List<PackageFlashcard> flashcardList  =  response.body().getFlashcards();
                        if (flashcardList == null){
                            return;
                        }

                        for(PackageFlashcard flashcard : flashcardList){
                            Flashcard f = new Flashcard(flashcard.getQuestion(), flashcard.getAnswer(), 1, null,null,categoryId);
                            flashcardDao.insert(f);
                        }
                    }

                    @Override
                    public void onFailure(Call<PackageWordsResponse> call, Throwable t) {
                        Toast.makeText(mainActivity.getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return  view;
    }

}
