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
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val points=ArrayList<LatLng>()
    @Inject lateinit var localNotificationManager:LocalNotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

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
        binding.btnNotification.setOnClickListener {
            localNotificationManager.createNotification()
        }
    }
}