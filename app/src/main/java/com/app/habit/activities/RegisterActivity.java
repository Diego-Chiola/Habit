package com.app.habit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.app.habit.logic.SettingsManager;
import com.app.habit.R;
import com.app.habit.databinding.ActivityRegisterBinding;
import com.app.habit.helpers.ActivityChanger;
import com.app.habit.helpers.Keyboard;
import com.app.habit.helpers.Validator;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // enable binding
        _binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = _binding.getRoot();
        setContentView(view);

        // showing the icons of the text fields
        _binding.tfFullname.setStartIconTintList(null);
        _binding.tfEmail.setStartIconTintList(null);

        // Listener Initializer
        InitializeListener();
    }

    private void InitializeListener() {

        // Clear current edit text focus while clicking
        // on the screen (outside of the edit text)
        //_binding.registerScreen.setOnClickListener(l -> ClearCurrentFocusView());

        // clearing error
        _binding.tfFullname.getEditText().setOnFocusChangeListener((view, viewFocus) -> ClearErrors(_binding.tfFullname,  viewFocus));
        //_binding.tfEmail.getEditText().setOnFocusChangeListener((view, viewFocus) -> ClearErrors(_binding.tfEmail,  viewFocus));

        // on form submit
        _binding.btnGoToHabit.setOnClickListener(l -> {
            if(ValidateFormAndSetErrors()){

                // save user name and email as settings
                SettingsManager.getUserSharedPreferences(this).edit()
                        .putString(SettingsManager.USER_INFO_fullName, _binding.tfFullname.getEditText().getText().toString())
                        .putString(SettingsManager.USER_INFO_email, _binding.tfEmail.getEditText().getText().toString())
                        .apply();

                // set first time  user enter app to false
                SettingsManager.getFirstTimeEnterSharedPreferences(this).edit()
                        .putBoolean(SettingsManager.FIRST_ENTER_isFirstEnter, false)
                        .apply();

                ActivityChanger.ChangeActivityAndFinish(this, PermissionActivity.class);
            }
        });
    }

    // clear edit texts from errors
    private void ClearErrors(TextInputLayout textInputLayout, boolean viewFocus) {
        if(viewFocus  && textInputLayout.isErrorEnabled()) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError("");
        }
    }

    // validate the form  of the registration
    private boolean ValidateFormAndSetErrors() {

        boolean isFormValid = true;

        // name validation
        String fullName = _binding.tfFullname.getEditText().getText().toString();
        if(fullName.length() == 0) {
            _binding.tfFullname.setError(getResources().getText(R.string.fullname_validation_0));
            isFormValid = false;
        } else if(fullName.length() > 20) {
            _binding.tfFullname.setError(getResources().getText(R.string.fullname_validation_max20));
            isFormValid = false;
        }

        // email validation
        String email = _binding.tfEmail.getEditText().getText().toString();
        if(!Validator.Email(email)) {
            _binding.tfEmail.setError(getResources().getText(R.string.email_validation));
            isFormValid = false;
        }

        return isFormValid;
    }
    
}