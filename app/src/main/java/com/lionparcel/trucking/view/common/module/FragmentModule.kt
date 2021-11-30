package com.lionparcel.trucking.view.common.module

import androidx.fragment.app.Fragment
import com.lionparcel.trucking.view.common.PermissionHelper
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    fun providePermissionHelper(): PermissionHelper {
        return PermissionHelper(fragment)
    }
}
