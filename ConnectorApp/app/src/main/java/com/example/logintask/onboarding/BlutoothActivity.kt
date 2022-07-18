package com.example.logintask.onboarding

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.logintask.R
import com.example.logintask.databinding.ActivityBlutoothBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.showToast
import java.io.*
import java.util.*
//comment   ok

//test 1
//test 2


class BlutoothActivity : BaseActivity() {

    var bAdapter: BluetoothAdapter? = null
    lateinit var binding:ActivityBlutoothBinding
    private val UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805F9B34FB"

    private var mSocket: BluetoothSocket? = null
    private var mBufferedReader: BufferedReader? = null
    override fun getLayout() = com.example.logintask.R.layout.activity_blutooth

    override fun init() {
        val bAdapter = BluetoothAdapter.getDefaultAdapter()
        binding = DataBindingUtil.setContentView(this,getLayout())

        //setListAdapter(ArrayAdapter(this, R.layout.list, s))

        binding.btnGet.setOnClickListener {
            binding.nameTv.text =""
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val pairedDevices = mBluetoothAdapter.bondedDevices

            val name: MutableList<String> = ArrayList()
            val address: MutableList<String> = ArrayList()

            for (bt in pairedDevices) {
                name.add(bt.name + " "+ bt.address)
                address.add(bt.address)

            }
            binding.nameTv.text = name.toString()+"\n"
          //  binding.macAddressTv.text = address.toString() + "\n"


            // Checks if Bluetooth Adapter is present
            if (bAdapter == null) {
               showToast(this, "Bluetooth Not Supported")
            } else {
                // Arraylist of all the bonded (paired) devices
                val pairedDevices = when {
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED -> {

                        return@setOnClickListener
                    }
                    else -> {}
                }
                    bAdapter!!.bondedDevices

                if (!pairedDevices.equals(0)) {
                    for (device in listOf(pairedDevices)) {

                      /*  // get the device name
                        val deviceName = device.name

                        // get the mac address
                        val macAddress = device.address*/

                    showToast(this,device.toString())

                        /*// append in the two separate views
                        binding.nameTv.append("$deviceName\n")
                        binding.macAddressTv.append("$macAddress\n")*/
                    }
                }
            }
        }
    }
    @Throws(IOException::class)
    private fun openDeviceConnection(aDevice: BluetoothDevice) {
        var aStream: InputStream? = null
        var aReader: InputStreamReader? = null
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mSocket = aDevice.createRfcommSocketToServiceRecord(getSerialPortUUID())
          /*  mSocket.connect()
            aStream = mSocket.getInputStream()*/
            aReader = InputStreamReader(aStream)
            mBufferedReader = BufferedReader(aReader)
        } catch (e: IOException) {
            Log.e("TAG", "Could not connect to device", e)
            close(mBufferedReader)
            close(aReader)
            close(aStream)
            close(mSocket)
            throw e
        }
    }

    private fun close(aConnectedObject: Closeable?) {
        var aConnectedObject: Closeable? = aConnectedObject ?: return
        try {
            aConnectedObject?.close()
        } catch (e: IOException) {
        }
        aConnectedObject = null
    }

    private fun getSerialPortUUID(): UUID? {
        return UUID.fromString(UUID_SERIAL_PORT_PROFILE)
    }

    override fun setObserver() {
    }

    override fun setViewModel() {
    }
}