package com.app.habit.helpers;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;

public class Fragment {

    public static void  ChangeFragment(FragmentActivity fragmentActivity, int fragmentContainerId, Class<? extends androidx.fragment.app.Fragment> fragmentClass) {
        fragmentActivity.getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(fragmentContainerId, fragmentClass, null)
                .commit();
    }
}
