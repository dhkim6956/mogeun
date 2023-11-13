package io.ssafy.mogeun.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import io.ssafy.mogeun.model.BluetoothMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

interface BluetoothController {
    val isConnected: StateFlow<List<Boolean>>
    val scannedDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>
    val connectedDevices: StateFlow<List<ConnectedDevice>>
    val errors: SharedFlow<String>

    fun startDiscovery()
    fun stopDiscovery()

    fun connectToDevice0(device: ConnectedDevice): Flow<Connection0Result>
    fun connectToDevice1(device: ConnectedDevice): Flow<Connection1Result>

    suspend fun trySendMessage(deviceNo: Int, message: Int): BluetoothMessage?
    suspend fun closeConnection(deviceNo: Int)
    suspend fun release()
}

@SuppressLint("MissingPermission")
class AndroidBluetoothController(
    private val context: Context
): BluetoothController{
    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private var dataTransferService: MutableList<BluetoothDataTransferService?> = mutableListOf(null, null, null, null)

    private val _isConnected = MutableStateFlow(listOf(false, false, false, false))
    override val isConnected: StateFlow<List<Boolean>>
        get() = _isConnected.asStateFlow()

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>> (emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>> (emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    private val _connectedDevices = MutableStateFlow<List<ConnectedDeviceDomain>> (emptyList())
    override val connectedDevices: StateFlow<List<ConnectedDeviceDomain>>
        get() = _connectedDevices.asStateFlow()

    private val _errors = MutableSharedFlow<String>()
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    private var device0Mac = "B0:A7:32:DB:C8:46"
    private var device1Mac = "7C:87:CE:2D:22:8E"

    private val foundDeviceReceiver = FoundDeviceReceiver {device ->
        _scannedDevices.update {devices->
            val newDevice = device.toBluetoothDeviceDomain()
            if(newDevice in devices) devices else devices + newDevice
        }
    }

    private val bluetoothStateReceiver = BluetoothStateReceiver { btIsConnected, bluetoothDevice ->
        if (bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == true) {
            Log.d("bluetoothAddress", "현재 연결된 디바이스 : ${bluetoothDevice.address}")

            if(bluetoothDevice.address == device0Mac) {
                Log.d("bluetoothAddress", "1번 연결 확인")
                updateIsConnected(0, btIsConnected)
            }
            if(bluetoothDevice.address == device1Mac) {
                Log.d("bluetoothAddress", "2번 연결 확인")
                updateIsConnected(1, btIsConnected)
            }

        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _errors.tryEmit("Can't connect to a non-paired device.")
            }
        }
    }

    // private var currentClientSocket: BluetoothSocket? = null
    private var currentClientSockets: MutableList<BluetoothSocket?> = mutableListOf(null, null, null, null)

    init {
        updatePairedDevices()
        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        )
    }

    override fun startDiscovery() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }

        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND)
        )

        updatePairedDevices()

        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }

        bluetoothAdapter?.cancelDiscovery()
    }

    override fun connectToDevice0(device: ConnectedDeviceDomain): Flow<Connection0Result> {
        return flow {
            if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No BLUETOOTH_CONNECT permission")
            }

            val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(device.address)


            currentClientSockets[0] = bluetoothDevice
                ?.createRfcommSocketToServiceRecord(
                    UUID.fromString(SERVICE_UUID)
                )

            stopDiscovery()

            currentClientSockets[0]?.let {socket ->
                try {
                    socket.connect()
                    emit(Connection0Result.ConnectionEstablished)
                    updateIsConnected(0, true)

                    device0Mac = device.address

                    _connectedDevices.update {devices->
                        if(device in devices) devices else devices + device
                    }

                    BluetoothDataTransferService(socket).also {
                        dataTransferService[0] = it

                        trySendMessage(0, 0)

                        emitAll(
                            it.listenForIncomingMessage()
                                .map { message ->
                                    if(message != null) {
                                        Connection0Result.TransferSucceeded(message)
                                    }
                                    else {
                                        updateIsConnected(0, false)
                                        Connection0Result.Error("errString")
                                    }
                                }
                        )
                    }
                } catch (e: IOException) {
                    socket.close()
                    currentClientSockets[0] = null
                    emit(Connection0Result.Error("Connection was interrupted"))
                } finally {
                    Log.d("bluetooth", "Device 0 finally ended")
                }
            }
        }.onCompletion {
            Log.d("bluetooth", "Device 0 onCompletion")
            _connectedDevices.update { devices ->
                devices.filter { it != device }
            }
            closeConnection(0)

        }.flowOn(Dispatchers.IO)
    }

    override fun connectToDevice1(device: ConnectedDeviceDomain): Flow<Connection1Result> {
        return flow {
            if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No BLUETOOTH_CONNECT permission")
            }

            val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(device.address)


            currentClientSockets[1] = bluetoothDevice
                ?.createRfcommSocketToServiceRecord(
                    UUID.fromString(SERVICE_UUID)
                )

            stopDiscovery()

            currentClientSockets[1]?.let {socket ->
                try {
                    socket.connect()
                    emit(Connection1Result.ConnectionEstablished)
                    updateIsConnected(1, true)

                    device1Mac = device.address

                    _connectedDevices.update {devices->
                        if(device in devices) devices else devices + device
                    }

                    BluetoothDataTransferService(socket).also {
                        dataTransferService[1] = it

                        trySendMessage(1, 1)

                        emitAll(
                            it.listenForIncomingMessage()
                                .map { message ->
                                    if(message != null) {
                                        Connection1Result.TransferSucceeded(message)
                                    }
                                    else {
                                        updateIsConnected(1, false)
                                        Connection1Result.Error("errString")
                                    }
                                }
                        )
                    }
                } catch (e: IOException) {
                    socket.close()
                    currentClientSockets[1] = null
                    emit(Connection1Result.Error("Connection was interrupted"))
                } finally {
                    Log.d("bluetooth", "Device 1 finally ended")
                }
            }
        }.onCompletion {
            Log.d("bluetooth", "Device 1 onCompletion")
            _connectedDevices.update { devices ->
                devices.filter { it != device }
            }
            closeConnection(1)

        }.flowOn(Dispatchers.IO)
    }

    override suspend fun trySendMessage(deviceNo: Int, message: Int): BluetoothMessage? {
        if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            return null
        }

        if(dataTransferService[deviceNo] == null) {
            return null
        }

        val bluetoothMessage = BluetoothMessage(
            message = message,
            sensorId = 4,
            isFromLocalUser = true
        )

        dataTransferService[deviceNo]?.sendMessage(bluetoothMessage.toByteArray())

        return bluetoothMessage
    }

    override suspend fun closeConnection(deviceNo: Int) {
        Log.d("bluetooth","${deviceNo + 1}번 기기 연결 종료됨")
        trySendMessage(deviceNo, 4)
        currentClientSockets[deviceNo]?.close()
        currentClientSockets[deviceNo] = null
        updateIsConnected(deviceNo, false)
    }

    override suspend fun release() {
        context.unregisterReceiver(foundDeviceReceiver)
        context.unregisterReceiver(bluetoothStateReceiver)
        closeConnection(0)
        closeConnection(1)
    }

    private fun updatePairedDevices() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            return
        }
        bluetoothAdapter
            ?.bondedDevices
            ?.map { it.toBluetoothDeviceDomain() }
            ?.also { devices ->
                _pairedDevices.update { devices }
            }
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun updateIsConnected(deviceNo: Int, state: Boolean) {
        if(deviceNo == 0) {
            _isConnected.update {
                listOf(state, isConnected.value[1], isConnected.value[2], isConnected.value[3])
            }
        } else if (deviceNo == 1) {
            _isConnected.update {
                listOf(isConnected.value[0], state, isConnected.value[2], isConnected.value[3])
            }
        } else if (deviceNo == 2) {
            _isConnected.update {
                listOf(isConnected.value[0], isConnected.value[1], state, isConnected.value[3])
            }
        } else if (deviceNo == 3) {
            _isConnected.update {
                listOf(isConnected.value[0], isConnected.value[1], isConnected.value[2], state)
            }
        }
    }

    companion object {
        const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }
}