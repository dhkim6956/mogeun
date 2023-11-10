package io.ssafy.mogeun.ui.screens.setting.connection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ssafy.mogeun.data.bluetooth.BluetoothDevice
import io.ssafy.mogeun.data.bluetooth.ConnectedDevice
import io.ssafy.mogeun.ui.BluetoothViewModel
import kotlinx.coroutines.launch

@Composable
fun ConnectionScreen(viewModel: BluetoothViewModel, snackbarHostState: SnackbarHostState) {
    val state by viewModel.state.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = state.errorMessage) {
        state.errorMessage?.let{ message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }
    LaunchedEffect(key1 = state.isConnected[0]) {
        if(state.isConnected[0]) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("1번 기기 연결 성공")
            }
        }
    }
    LaunchedEffect(key1 = state.isConnected[1]) {
        if(state.isConnected[1]) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("2번 기기 연결 성공")
            }
        }
    }
    LaunchedEffect(key1 = state.isConnected[2]) {
        if(state.isConnected[2]) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("3번 기기 연결 성공")
            }
        }
    }
    LaunchedEffect(key1 = state.isConnected[3]) {
        if(state.isConnected[3]) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("4번 기기 연결 성공")
            }
        }
    }

    when {
        state.isConnecting -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text("Connecting ...")
            }
        }
//        state.isConnected -> {
//            ChatScreen(
//                state = state,
//                onDisconnect = viewModel::disconnectFromDevice,
//                onSendMessage = viewModel::sendMessage
//            )
//        }
        
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BluetoothDeviceList(
                    pairedDevices = state.pairedDevices,
                    scannedDevices = state.scannedDevices,
                    connectedDevices = state.connectedDevices,
                    onClick = viewModel::connectToDevice,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = viewModel::startScan) {
                        Text(text = "Start scan")
                    }
                    Button(onClick = viewModel::stopScan) {
                        Text(text = "Stop scan")
                    }
                    Button(onClick = viewModel::disconnectFromDevice) {
                        Text(text = "Disconnect")
                    }
                }
            }
        }
    }

}

@Composable
fun BluetoothDeviceList(
    pairedDevices: List<BluetoothDevice>,
    scannedDevices: List<BluetoothDevice>,
    connectedDevices: List<ConnectedDevice>,
    onClick: (BluetoothDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Text(
                text = "Connected Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(connectedDevices) { device ->
            Text(
                text = device.name ?: "(No name)",
                modifier = Modifier
                    .fillMaxWidth()
//                    .clickable { onClick(device) }
                    .padding(16.dp)
            )
        }

        item {
            Text(
                text = "Paired Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(pairedDevices) { device ->
            Text(
                text = device.name ?: "(No name)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(device) }
                    .padding(16.dp)
            )
        }

        item {
            Text(
                text = "Scanned Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(scannedDevices) { device ->
            Text(
                text = device.name ?: "(No name)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(device) }
                    .padding(16.dp)
            )
        }
    }
}