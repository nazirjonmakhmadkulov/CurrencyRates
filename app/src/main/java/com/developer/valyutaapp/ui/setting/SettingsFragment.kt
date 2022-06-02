package com.developer.valyutaapp.ui.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.developer.valyutaapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}