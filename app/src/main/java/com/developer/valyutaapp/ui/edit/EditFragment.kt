package com.developer.valyutaapp.ui.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.developer.valyutaapp.R
import com.developer.valyutaapp.core.database.SharedPreference
import com.developer.valyutaapp.databinding.FragmentEditBinding
import com.developer.valyutaapp.ui.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditFragment : Fragment(R.layout.fragment_edit) {

    private val viewBinding by viewBinding(FragmentEditBinding::bind)
    private val viewModel by viewModel<MainViewModel>()

    private val prefs: SharedPreference by inject()

    private lateinit var viewPager: ViewPager2
    private lateinit var pageAdapter: PagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTap()
    }

    private fun setupTap() {
        val tabs = arrayOf("Favorites", "Currency")
        pageAdapter = PagerAdapter(requireActivity(), tabs.size)
        viewPager = viewBinding.tabViewpager
        viewPager.adapter = this.pageAdapter
        viewPager.offscreenPageLimit = 2
        viewPager.isUserInputEnabled = false
        viewPager.isSaveEnabled = true
        tabLayout = viewBinding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }


}