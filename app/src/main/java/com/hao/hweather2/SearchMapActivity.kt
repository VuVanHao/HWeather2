package com.hao.hweather2

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hao.hweather2.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchMapActivity : FragmentActivity(),
    OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    var mMap : GoogleMap ? = null
    var mapFragment : SupportMapFragment ? = null
    var latLng : LatLng  ?= null
    var marker : Marker ? = null
    var geocoder : Geocoder ? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var lat = ""
    var lon = ""
    var backMap : ImageView ? = null
    var actionBar : TextView ? = null
    var custom_view : View? = null
    var nameCityDetails = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_map)
        backMap = findViewById(R.id.btnBackMap)
        actionBar = findViewById(R.id.tvactionbarMap)
        geocoder = Geocoder(this, Locale.getDefault())

        setUpMapIfNeeded()
        initView()
    }

    private fun initView() {
        val lang = MySharedPreferences.getLanguage(this)
        if (Objects.equals(lang,"vi"))
        {
            actionBar?.text = "Tìm kiếm bản đồ"
        }
        else
        {
            actionBar?.text = "Search by maps"
        }

        backMap?.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        setUpMapIfNeeded()
    }

    private fun setUpMapIfNeeded() {
        if (mapFragment == null)
        {
            mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment!!.getMapAsync(this)
        }
    }

    @Synchronized
    public fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient()
                mMap?.isMyLocationEnabled = true
            }
            else
            {
                checkLocationPermission()
            }
        }

        mMap?.setOnMapClickListener{ point ->
            latLng = point
            var nameCity = ""
            var addresses : List<Address> = ArrayList()
            try {
                addresses = geocoder!!.getFromLocation(point.latitude,point.longitude,1)
            }catch (e: Exception)
            {
                e.printStackTrace()
            }
            if (addresses.size > 0)
            {
                nameCity = addresses[0].adminArea
                if (Objects.equals(nameCity,"Ha Tay"))
                {
                    nameCity = "Hà nội"
                }
                nameCityDetails = nameCity
                val inflater = this.layoutInflater
                custom_view = inflater.inflate(R.layout.notimaps,null)
                val builder = AlertDialog.Builder(this)
                builder.setView(custom_view)
                var tvLat : TextView = custom_view!!.findViewById(R.id.tvLat)
                var tvLong : TextView = custom_view!!.findViewById(R.id.tvLong)
                var Add : TextView = custom_view!!.findViewById(R.id.tvNameCityInfor)
                var tv : TextView = custom_view!!.findViewById(R.id.tvInfor)
                var tv1 : TextView = custom_view!!.findViewById(R.id.tv1)
                var tv2 : TextView = custom_view!!.findViewById(R.id.tv2)
                var tv3 : TextView = custom_view!!.findViewById(R.id.tv3)
                tvLat.text = point.latitude.toString()
                tvLong.text = point.longitude.toString()
                Add.text = nameCityDetails
                val lang = MySharedPreferences.getLanguage(this)
                var search = ""
                var cancel = ""
                if (Objects.equals(lang, "vi"))
                {
                    tv.text = "Thông tin";
                    tv3.text = "Địa chỉ: ";
                    tv1.text = "Vĩ độ: ";
                    tv2.text = "Kinh độ: ";
                    search = "Tìm";
                    cancel = "Hủy";
                }
                else
                {
                    tv.text = "Information";
                    tv3.text = "Address: ";
                    tv1.text = "Latitude: ";
                    tv2.text = "Longitude: ";
                    search = "Search";
                    cancel = "Cancel";
                }
                builder.setPositiveButton(search) { _, _ ->
                    val intent = Intent(this,InforCityMapActivity::class.java)
                    intent.putExtra("LAT",point.latitude.toString())
                    intent.putExtra("LONG",point.longitude.toString())
                    startActivity(intent)
                }
                builder.setNegativeButton(cancel) { _, _ -> }

                val alert : AlertDialog = builder.create()
                alert.setCanceledOnTouchOutside(false)
                alert.show()
            }
            else
            {
                Toast.makeText(this, "Đợi tí, chưa load được !!!", Toast.LENGTH_SHORT).show()
            }
            if (marker != null) {
                marker!!.remove()
            }

            //place marker where user just clicked

            //place marker where user just clicked
            marker = mMap!!.addMarker(
                MarkerOptions().position(point).title("Marker")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )
        }

    }

    val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ -> //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                MY_PERMISSIONS_REQUEST_LOCATION
                            )
                        })
                    .create()
                    .show()
            }
            else
            {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onLocationChanged(p0: Location) {
        mLastLocation = p0
        if (mCurrLocationMarker != null)
        {
            mCurrLocationMarker!!.remove()
        }

        var latLng = LatLng(p0.latitude,p0.longitude)
        var markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Vị trí hiện tại")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11f))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap!!.isMyLocationEnabled = true
                    }
                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}