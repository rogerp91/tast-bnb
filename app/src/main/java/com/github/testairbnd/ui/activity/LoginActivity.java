package com.github.testairbnd.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.testairbnd.R;
import com.github.testairbnd.data.model.DataReplace;
import com.github.testairbnd.ui.fragment.LoginFragment;
import com.github.testairbnd.util.Usefulness;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            Usefulness.gotoFragment(new DataReplace(getSupportFragmentManager(), LoginFragment.newInstance(), R.id.fragment_container));
        }
    }
}
