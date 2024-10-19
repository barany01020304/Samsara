package com.example.samsara.screens.home
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samsara.datasource.local.UserDataSharedPref
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(private var application: Application, var userData: UserDataSharedPref, var fusedLocationClient: FusedLocationProviderClient, var geocoder:Geocoder) : AndroidViewModel(application) {

    // LiveData for location and city data
    private val _locationLiveData = MutableLiveData<Location>()
    val locationLiveData: LiveData<Location> get() = _locationLiveData
    private val _cityNameLiveData = MutableLiveData<String>()
    val cityNameLiveData: LiveData<String> get() = _cityNameLiveData

    // LiveData for alert dialog triggers
    private val _showDialogLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
    val showDialogLiveData: LiveData<Pair<Boolean, Boolean>> get() = _showDialogLiveData

    init {
//            if (_cityNameLiveData.value==null ){
//                _cityNameLiveData.value=userData.getLocation()
//            }
    }

    // Method to check permissions and location settings
    fun checkLocationSettingsAndRequestLocation() {
        val context = application

        val isGpsEnabled = isGpsEnabled(context)
        val isPermissionGranted = isLocationPermissionGranted(context)

        if (!isGpsEnabled || !isPermissionGranted) {
            // Trigger alert dialog if either GPS or location permission is not enabled
            _showDialogLiveData.value = Pair(isGpsEnabled, isPermissionGranted)
        } else {
            // Permission granted and GPS enabled, fetch location
            getLastKnownLocation()
        }
    }

    // Check if GPS is enabled
    private fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // Check if location permission is granted
    private fun isLocationPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request location permission
    fun requestLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                _locationLiveData.value = it
                userData.setCoordinates(it.latitude, it.longitude)
                getCityName(userData.getLatitude(), userData.getLongitude())
            }
        }.addOnFailureListener {
            Log.e(TAG, "onError:get lat and long ${it.message} ")

        }
    }

    // Get city name based on latitude and longitude
    private fun getCityName(latitude: Double, longitude: Double) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For API level 33 and above, use the async method
            geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: List<Address>) {
                    if (addresses.isNotEmpty()) {
                        val city = addresses[0].locality +" "+addresses[0].countryName
                        userData.setLocation(city)
                        _cityNameLiveData.postValue(city)// Return the city name via callback
                    }
                }

                override fun onError(errorMessage: String?) {
                    Log.e(TAG, "onError:get city name $errorMessage ")
                }
            })
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {  val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses!!.isNotEmpty()) {
                    val city = addresses[0]
                   // addresses[0].locality +" "+ addresses[0].subAdminArea+" "+addresses[0].countryName
                    userData.setLocation(city.locality +","+city.countryName)
                    _cityNameLiveData.postValue(userData.getLocation())

                }} catch (e: IOException) {
                    Log.e(TAG, "Geocoder IOException internet IO : ${e.message}")
                } catch (e: IllegalArgumentException) {
                    Log.e(TAG, "Geocoder IllegalArgumentException wrong lat lon: ${e.message}")
                }

            }
        }
    }
    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 10
        private const val TAG="TAGTAG in home viewModel"
    }
}