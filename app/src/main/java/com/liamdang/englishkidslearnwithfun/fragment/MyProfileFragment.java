package com.liamdang.englishkidslearnwithfun.fragment;

import static com.liamdang.englishkidslearnwithfun.activity.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.activity.MainActivity;

import com.liamdang.englishkidslearnwithfun.model.User;

public class MyProfileFragment extends Fragment {

    //private FragmentMyProfileBinding binding;
    private View mView;
    private ImageView imgAvatar;
    private EditText edtName, edtEmail;
    private Button btnUpdateProf;
    private Button btnUpdateEmail;
    private Uri mUri;
    private MainActivity mainActivity;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        //binding = FragmentMyProfileBinding.inflate(getLayoutInflater());
        initUI();

        mainActivity = (MainActivity) getActivity();
        progressDialog = new ProgressDialog(getActivity());
        setUserInf();
        initListener();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        return mView;
    }

    private void initUI () {
        imgAvatar = mView.findViewById(R.id.img_avatar);
        edtName = mView.findViewById(R.id.edt_name);
        edtEmail = mView.findViewById(R.id.edt_email);
        btnUpdateProf = mView.findViewById(R.id.btn_update_prof);
        btnUpdateEmail = mView.findViewById(R.id.btn_update_email);

    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    private void setUserInf() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;

        edtEmail.setText(user.getEmail());
        edtName.setText(user.getDisplayName());
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.avatar_default2).into(imgAvatar);


    }

    private void initListener() {
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btnUpdateProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateProf();
            }

        });

        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateEmail();
            }
        });
    }

    private void onClickRequestPermission() {

        if(mainActivity == null) {
            return;
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mainActivity.openGallery();
            return;
        }

        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mainActivity.openGallery();
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }

    public void setBitmapImageView(Bitmap bitmapImageView) {
        imgAvatar.setImageBitmap(bitmapImageView);
    }

    private void onClickUpdateProf() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) return;
        progressDialog.show();
        String name = edtName.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            updateUserInf(user.getUid(), user.getEmail(), name);

                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            mainActivity.showUserInf();
                        }
                    }
                });

    }

    private void onClickUpdateEmail() {
        String newEmail = edtEmail.getText().toString().trim();
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        updateUserInf(user.getUid(), newEmail, user.getDisplayName());

                        Toast.makeText(getActivity(), "Cập nhật email thành công", Toast.LENGTH_SHORT).show();
                        mainActivity.showUserInf();

                    }
                });
    }

    private void updateUserInf(String userId, String email, String name) {
        User user = new User(email, name);

        mDatabase.child("list_users").child(userId).setValue(user);
    }


}
