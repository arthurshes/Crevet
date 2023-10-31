package workwork.test.andropediagits.presenter.setting

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

import dagger.hilt.android.AndroidEntryPoint
import workwork.test.andropediagits.R


@AndroidEntryPoint
class SettingFragment : PreferenceFragmentCompat() {
    //    private val viewModel: SettingViewModel by viewModels()
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey)
        val currentTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val listPreferenceTextSize = findPreference<ListPreference>("text_size_key") as ListPreference
        val listPreferenceCodeSize = findPreference<ListPreference>("code_size_key") as ListPreference
        val listPreferenceVictorineClue = findPreference<ListPreference>("victorine_clue_key") as ListPreference

        if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
            listPreferenceTextSize.setIcon(R.drawable.ic_text_light)
            listPreferenceCodeSize.setIcon(R.drawable.ic_text_light)
            listPreferenceVictorineClue.setIcon(R.drawable.ic_clue_light)
        } else {
            listPreferenceVictorineClue.setIcon(R.drawable.ic_clue)
            listPreferenceTextSize.setIcon(R.drawable.ic_text_night)
            listPreferenceCodeSize.setIcon(R.drawable.ic_text_night)
        }
    }
}