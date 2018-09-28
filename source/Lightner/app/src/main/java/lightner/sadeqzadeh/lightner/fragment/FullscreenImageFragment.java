package lightner.sadeqzadeh.lightner.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.net.URI;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;

public class FullscreenImageFragment extends Fragment{
    MainActivity mainActivity;
    ImageView imageView;
    Bundle args;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args  = getArguments();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fullscreen_image_layout, container, false);
        mainActivity = (MainActivity) getActivity();
        imageView = view.findViewById(R.id.fullscreen_image_view);
        imageView.setImageURI(Uri.parse(args.getString(Const.URI)));
        return view;
    }
}
