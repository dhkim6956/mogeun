package io.ssafy.mogeun.ui.screens.setting.connection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer3
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull
import io.ssafy.mogeun.data.bluetooth.ConnectedDevice
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.components.drawColoredShadow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait

@Composable
fun ConnectionScreen(
    viewModel: ConnectionViewModel = viewModel(factory = AppViewModelProvider.Factory),
    snackbarHostState: SnackbarHostState
) {
    val state by viewModel.state.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = state.connectedDevices[0]) {
        if(state.connectedDevices[0].isNotNull()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("1번 기기 연결 성공")
            }
        }
    }
    LaunchedEffect(key1 = state.connectedDevices[1]) {
        if(state.connectedDevices[1].isNotNull()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("2번 기기 연결 성공")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "sensor 1 : ${state.sensorVal[0]}")
        Text(text = "sensor 2 : ${state.sensorVal[1]}")


        BluetoothDeviceList(
            scannedDevices = state.scannedDevices,
            connectedDevices = state.connectedDevices,
            connect = viewModel::connect,
            disconnect = viewModel::disConnect,
            startScan = viewModel::startScan,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceList(
    scannedDevices: List<BluetoothDevice>,
    connectedDevices: List<BluetoothDevice?>,
    connect: (BluetoothDevice) -> Unit,
    disconnect: (BluetoothDevice) -> Unit,
    startScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scanning by rememberSaveable{ mutableStateOf(false)}
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier
    ) {
        stickyHeader {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color(0xFFF7F7F7))
                    .shadow(0.5.dp)
            ) {
                Text(
                    text = "연결된 장치",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        items(connectedDevices) { device ->
            if(device.isNotNull()) {
                val isLeft = connectedDevices.indexOf(device) == 0
                ScannedDeviceItem(device = device!!, onClick = disconnect, true, isLeft)
            }
        }
        stickyHeader {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color(0xFFF7F7F7))
                    .shadow(0.5.dp)
            ) {
                Text(
                    text = "검색된 장치",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                )
                IconButton(onClick = {
                    if(!scanning) {
                        startScan()
                        scanning = true
                        coroutineScope.launch {
                            delay(3000)
                            scanning = false
                        }
                    }
                }) {
                    Icon(imageVector = if(scanning) Icons.Default.Timer3 else Icons.Default.Refresh, contentDescription = "search devices")
                }
            }
        }
        items(scannedDevices.filter { device -> !connectedDevices.contains(device) }) { device ->
            ScannedDeviceItem(device = device, onClick = connect, false)
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun ScannedDeviceItem(device: BluetoothDevice, onClick: (BluetoothDevice) -> Unit, connected: Boolean, isLeft: Boolean = true) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(device) }
            .padding(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            if(connected) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(40.dp)
                        .aspectRatio(1f)
                        .shadow(1.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = if(isLeft) "좌" else "우")
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = device.name ?: "(이름없는 장치)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Text(
                    text = device.address,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
            Icon(
                imageVector = if(connected) Icons.Default.BluetoothDisabled else Icons.Default.BluetoothConnected,
                contentDescription = if(connected) "disconnect ble" else "connect ble",
                tint = Color(0xFF0000AA)
            )
            Text(text = if(connected) "연결해제" else "연결")
        }
    }
}
