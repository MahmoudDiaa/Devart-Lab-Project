package com.ekc.devartlabproject.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekc.devartlabproject.Constants
import com.ekc.devartlabproject.R
import com.ekc.devartlabproject.databinding.HomeFragmentLayoutBinding

import com.google.android.gms.location.LocationServices
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var binding: HomeFragmentLayoutBinding
    private val destLatitude = 30.013206310414322
    private val destLongitude = 31.443124614950257
    private lateinit var currentLocation: Location

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentLayoutBinding.inflate(inflater, container, false)

        init()

        if (!hasPermissions(requireContext(), PERMISSIONS))
            permReqLauncher.launch(PERMISSIONS)


        return binding.root

    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                getCurrentLocation()
            }
        }

    private fun init() {
        val sharedPref = activity?.getSharedPreferences(Constants.SharedPrefName,Context.MODE_PRIVATE) ?: return
        val isDelivered = sharedPref.getBoolean(Constants.IsDelivered, false)
        Log.e("Home Fragment", "init: $isDelivered", )
        binding.let {
            if(isDelivered)
                it.ivDelivered.visibility=View.VISIBLE
            else
                it.ivDelivered.visibility=View.GONE
            it.tvAddress.text = getAddress(destLatitude, destLongitude)
            it.btGetDirection.setOnClickListener {
                currentLocation.run {
                    if (hasPermissions(requireContext(), PERMISSIONS)) {

                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=${latitude},${longitude}&daddr=$destLatitude,$destLongitude")
                        ).run {
                            startActivity(this)
                        }
                    } else {
                        permReqLauncher.launch(PERMISSIONS)

                    }


                }

            }
            it.btArrived.setOnClickListener {
                createNotification()
            }

            it.btDeliver.setOnClickListener {

                findNavController().navigate(R.id.action_global_request_frag)

            }
        }


    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        LocationServices.getFusedLocationProviderClient(requireContext()).run {

            if (hasPermissions(requireContext(), PERMISSIONS)) {
                this.lastLocation.addOnSuccessListener {

                        location: Location? ->

                    currentLocation = location!!
                }
            } else {

                permReqLauncher.launch(PERMISSIONS)
            }
        }


    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())

        val addresses: List<Address> = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        return addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    }

    companion object {
        val PERMISSIONS =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
    }

    private fun createNotification() {

        val builder = NotificationCompat.Builder(requireContext(), "101")
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle(getString(R.string.channel_name))
            .setContentText(getString(R.string.channel_description))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(getString(R.string.channel_description))
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(false)


        with(NotificationManagerCompat.from(requireContext())) {
            // notificationId is a unique int for each notification that you must define
            notify(101, builder.build())
        }
    }
}