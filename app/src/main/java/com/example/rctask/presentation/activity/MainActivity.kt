package com.example.rctask.presentation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.rctask.R
import com.example.rctask.databinding.ActivityMainBinding
import com.example.rctask.presentation.fragment.MovieFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val PERMISSION_ID = 101
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var address: String = ""
    private val fragment = MovieFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.address.text = address
        binding.getAddress.setOnClickListener {
            getUserLocation()
        }

        binding.navButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        checkPermissions()
    }

    private fun checkPermissions() {
        if (checkUserPermission()) {
            if (isLocationEnabled()) {
                getLastLocation()
            } else {
                enableLocation()
            }
        } else {
            requestPermission()
        }
    }

    private fun getUserLocation() {
        checkPermissions()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d(
                    "Location",
                    "Latitude: ${location?.latitude}, Longitude: ${location?.longitude}"
                )
                if (location?.latitude != null && location?.longitude != null) {
                    getAddressFromLocation(location.latitude, location.longitude)
                }
            }
            .addOnFailureListener {
                Log.e("failure status-->", it.message.toString())
            }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geoCoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: MutableList<Address>? = geoCoder.getFromLocation(latitude, longitude, 1)
            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                val addressText =
                    "${address.getAddressLine(0)}:, ${address.locality}"
                Log.d("Location", addressText)
                binding.address.text = addressText
            }
        } catch (e: IOException) {
            Log.e("Location", "Error getting address: ${e.message}")
        }
    }

    private fun enableLocation() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    private fun checkUserPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("status--->", "permission grnt by user")
                getUserLocation()
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package",this.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }
}
