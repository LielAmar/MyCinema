package mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import mycinema.liel.net.mycinemaproject.R;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;
import mycinema.liel.net.mycinemaproject.Utils.Methods;

public class SelectPosterFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SelectPosterFragment";

    public static boolean changedImage = false;
    public static Bitmap originalBitmap;

    private ImageButton selectImage;
    private Uri imageUri;

    public SelectPosterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_poster, container, false);

        changedImage = false;

        Bitmap newBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_select), GlobalVars.posterWidth, GlobalVars.posterHeight, false);
        selectImage = v.findViewById(R.id.addMoviePosterImageButton);
        selectImage.setImageBitmap(newBitmap);
        selectImage.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        Methods.hideKeyboardForMain();
        if (v == selectImage) {
            openGallery();
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == 100) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, GlobalVars.posterWidth, GlobalVars.posterHeight, false);
                selectImage.setImageBitmap(newBitmap);
                originalBitmap = bitmap;
                Log.d(TAG, "Updated Image");
                changedImage = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
