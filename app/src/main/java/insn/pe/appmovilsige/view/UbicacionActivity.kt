package insn.pe.appmovilsige.view
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import insn.pe.appmovilsige.R
import insn.pe.appmovilsige.databinding.ActivityUbicacionBinding

class UbicacionActivity : AppCompatActivity(), OnMapReadyCallback, OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener {

    companion object{
        const val REQUEST_CODE_LOCATION=0
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityUbicacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUbicacionBinding.inflate(layoutInflater)
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
        Toast.makeText(this,"Selecciona tu ubicación y manten la calma",Toast.LENGTH_LONG).show()
        createMarker()
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        enableLocation()

    }

    private fun createMarker(){
        val insn = LatLng(-12.065580, -77.046240)
        mMap.addMarker(MarkerOptions().position(insn).title("INSN Breña"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(insn,17f),5000,null)
    }

    private fun isLocationPermisssionGranted()=ContextCompat
        .checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun enableLocation(){
        if (!::mMap.isInitialized) return
        if (isLocationPermisssionGranted()){
            mMap.isMyLocationEnabled = true
        }else{
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this,"Ve a ajustes y activa los permisos",Toast.LENGTH_LONG).show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                mMap.isMyLocationEnabled=true
            }else{
                Toast.makeText(this,"Ve a ajustes y activa los permisos",Toast.LENGTH_LONG).show()
            }
            else ->{}
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::mMap.isInitialized) return
        if (!isLocationPermisssionGranted()){
            mMap.isMyLocationEnabled=false
            Toast.makeText(this,"Ve a ajustes y activa los permisos",Toast.LENGTH_LONG).show()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        val personaId=intent.extras!!.getInt("personaId").toString()

        Toast.makeText(this,"Estas en ${p0.latitude},${p0.longitude}",Toast.LENGTH_LONG).show()
        val coordenadas:String = "${p0.latitude},${p0.longitude}"
        var intent = Intent(this, SolicitudEmergenciaGenerar::class.java)
        intent.putExtra("coordenadas",coordenadas)
        intent.putExtra("personaId",personaId)
        startActivity(intent)
    }

    override fun onMapClick(p0: LatLng) {
        Toast.makeText(this,"Estas en ${p0.latitude},${p0.longitude}",Toast.LENGTH_LONG).show()
        val coordenadas:String = "${p0.latitude},${p0.longitude}"
        var intent = Intent(this, SolicitudEmergenciaGenerar::class.java)
        intent.putExtra("coordenadas",coordenadas)
        startActivity(intent)

    }
}