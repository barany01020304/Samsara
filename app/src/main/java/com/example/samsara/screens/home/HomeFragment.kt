package com.example.samsara.screens.home

import android.content.Intent
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.samsara.MainActivity
import com.example.samsara.R

import com.example.samsara.adapters.RentBuyViewPagerAdapter
import com.example.samsara.databinding.FragmentHomeBinding
import com.example.samsara.datasource.local.UserDataSharedPref
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Locale
import java.util.Objects

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchViewId.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchScreenFragment())
        }
        setLocationIV()
        setViewModel()
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing=false
            restartActivity()
        }

        setupRentBuyViewpager()

        homeViewModel.cityNameLiveData.observe(viewLifecycleOwner) { cityName ->
            binding.cityNameTv.text = cityName
            //  Toast.makeText(requireContext(), "City: $cityName", Toast.LENGTH_LONG).show()
        }

        // Observe the dialog trigger LiveData
        homeViewModel.showDialogLiveData.observe(viewLifecycleOwner) { (isGpsEnabled, isPermissionGranted) ->
            showLocationAlertDialog(isGpsEnabled, isPermissionGranted)
        }


    }

    private fun setViewModel() {
        val application =requireActivity().application
        val viewModelFactory = HomeViewModelFactory(application)
        homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.checkLocationSettingsAndRequestLocation()

    }
    private fun showLocationAlertDialog(isGpsEnabled: Boolean, isPermissionGranted: Boolean) {
        val message = "After grant location services to use <b>SAMSARA</b> <br> <b>swipe down to refresh app.</b>"
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Location Services Required")
            .setIcon(R.drawable.ic_launcher_foreground)
            .setMessage(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY))
            .setCancelable(false) // Prevent closing the dialog by clicking outside

        if (!isGpsEnabled) {
            alertDialogBuilder.setPositiveButton("Enable GPS") { dialog, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.dismiss()
            }

        }

        if (!isPermissionGranted) {
            alertDialogBuilder.setNeutralButton("Allow Location Permission") { dialog, _ ->
                homeViewModel.requestLocationPermission(requireActivity())
                dialog.dismiss()
            }

        }
        alertDialogBuilder.setNegativeButton("Exit App") { dialog, _ ->
            requireActivity().finish()
            dialog.dismiss()
        }


        alertDialogBuilder.create().show()
    }

    private fun restartActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java) // Replace MainActivity with your activity's name
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) // Clear the activity stack
        startActivity(intent) // Start the activity
        requireActivity().finish() // Optionally finish the current activity
    }

    private fun setupRentBuyViewpager() {

        val adapter = RentBuyViewPagerAdapter(this)

        binding.rentOrBuyVp.adapter = adapter // Correct adapter assignment
        val tabTitle = arrayOf("I need to rent", "I need to buy")

        binding.rentOrBuyVp.isUserInputEnabled = false

        TabLayoutMediator(binding.rentOrBuyTl, binding.rentOrBuyVp) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()


    }
    private fun setLocationIV() {
        binding.locationPinIV.setOnClickListener {
            Glide.with(this.requireContext())
                .asGif()
                .load(R.drawable.location_gif_drawable)
                .listener(object : RequestListener<GifDrawable> {
                    override fun onResourceReady(
                        resource: GifDrawable,
                        model: Any,
                        target: Target<GifDrawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.setLoopCount(1) // Play the GIF once
                        resource?.registerAnimationCallback(object :
                            Animatable2Compat.AnimationCallback() {
                            override fun onAnimationEnd(drawable: Drawable?) {
                                binding.locationPinIV.setImageResource(R.drawable.location_drawable) // Switch to static image
                            }
                        })
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<GifDrawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(binding.locationPinIV)
        }
    }
}