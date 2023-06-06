package com.liamdang.englishkidslearnwithfun.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.liamdang.englishkidslearnwithfun.R;

import java.net.Authenticator;
import java.security.ProtectionDomain;

public class ChangePassFragment extends Fragment {

    private View mView;
    private EditText edtNewPass;
    private  EditText edtOldPass;
    private Button changePass;
    private ProgressDialog progressDialog;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_pass, container, false);
        initUI();
        initListener();
        return mView;
    }

    private void initUI() {
        progressDialog = new ProgressDialog(getActivity());
        edtNewPass = (EditText) mView.findViewById(R.id.edt_new_pass);
        edtOldPass = (EditText) mView.findViewById(R.id.edt_old_pass);
        changePass = (Button) mView.findViewById(R.id.btn_change_pass);

    }

    private void initListener() {
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangePass();
            }
        });
    }

    private void onClickChangePass() {
        String strNewPass = edtNewPass.getText().toString().trim();
        String strOldPass = edtOldPass.getText().toString().trim();

        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //if( user.pass)

        user.updatePassword(strNewPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                        } else {
                            //Toast.makeText(getActivity(),"Đổi mật khẩu thất bại!", Toast.LENGTH_LONG).show();
                            reAuthenticate();

                        }

                    }
                });


    }

    private void reAuthenticate() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential("user@gmail.com", "123456");

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            onClickChangePass();

                        } else {
                            Toast.makeText(getActivity(),"Thông tin không hợp lệ!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
