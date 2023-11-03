package io.ssafy.mogeun.ui.screens.setting.connection

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.theme.MogeunTheme
import kotlinx.coroutines.launch

@Composable
fun ConnectionScreen(viewModel: ConnectionViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    Log.d("Connection", "확인용 로그")

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val localActivity = context as Activity
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

    val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
        .build()

    val permissions = arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)

    requestPermissions(context, localActivity, permissions)

    for (permission in permissions) {
        if(ActivityCompat.checkSelfPermission( context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(localActivity, permissions, 0)
        }
    }

    bluetoothLeScanner.startScan(null, scanSettings, scanCallback)



    val emgLastData by viewModel.emgStream.collectAsState()
    val emgInput = viewModel.emgInput.value

    Column(modifier = Modifier
        .fillMaxSize()
    ) {

        Text("마지막 db emg 값 -------------")
        Text("디바이스 번호 : ${emgLastData?.deviceId ?: "데이터 없음"}")
        Text("부작 위치 : ${emgLastData?.sensingPart ?: "데이터 없음"}")
        Text("센서 값 : ${emgLastData?.value ?: "데이터 없음"}")

        Spacer(modifier = Modifier.height(40.dp))

        Text("로컬 emg 값 -------------")
        TextField(value = "${emgInput.deviceId}", onValueChange = {
            Log.d("check", "왜안댐? $it")
            viewModel.setDeviceId(it.toInt())
        })
        TextField(value = "${emgInput.sensingPart}", onValueChange = {
            viewModel.setSensingPart(it)
        })
        TextField(value = "${emgInput.value}", onValueChange = {
            viewModel.setValue(it.toDouble())
        })

        Button(onClick = {
            coroutineScope.launch {
                viewModel.saveData()
            }
        }) {
            Text(text = "DB에 저장")
        }
    }
}

val scanCallback: ScanCallback = object : ScanCallback() {
    @SuppressLint("MissingPermission")
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        Log.d("Connection", "run")
        Log.d("Connection", result.toString())

        if(result?.device?.name != null) {
            var uuid = "null"

            Log.d("Connection", result.scanRecord!!.serviceUuids.toString())

            if(result.scanRecord?.serviceUuids != null) {
                uuid = result.scanRecord!!.serviceUuids.toString()
            }
        }
    }
}

fun requestPermissions(context: Context, localActivity: Activity, permissions: Array<String>)  {
    for(permission in permissions) {
        if(ActivityCompat.checkSelfPermission( context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(localActivity, permissions, 0)
        }
    }
}


@Preview
@Composable
fun ConnectionScreenPreview() {
    MogeunTheme {
        ConnectionScreen()
    }
}