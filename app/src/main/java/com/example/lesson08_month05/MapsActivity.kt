package com.example.lesson08_month05

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.lesson08_month05.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val points=ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener {
            mMap.addMarker(MarkerOptions().position(it))
            val updateFactory= CameraUpdateFactory.newLatLngZoom(it,10f)
            mMap.moveCamera(updateFactory)
            points.add(it)
        }
        setListeners()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),0)
            return
        }
        mMap.isMyLocationEnabled = true
    }
    private fun drawPolyline(){
        val options =PolylineOptions().addAll(points).color(ContextCompat.getColor(this,R.color.purple_200))
            .width(20f)
        mMap.addPolyline(options)
    }
    private fun drawPolygon(){
        val options = PolygonOptions().addAll(points).fillColor(ContextCompat.getColor(this,R.color.m3_color))
            .strokeColor(ContextCompat.getColor(this,R.color.white))
            .strokeWidth(10f)
        mMap.addPolygon(options)
    }
    private fun clearDraw() {
         mMap.clear()
    }
    private fun setListeners(){
        binding.btnPolyline.setOnClickListener {
            drawPolyline()
        }
        binding.btnPolygon.setOnClickListener {
            drawPolygon()
        }
        binding.btnClear.setOnClickListener {
            clearDraw()
        }
    }
}