package io.ssafy.mogeun.ui.screens.routine.execution.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.screens.routine.execution.EmgUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jtransforms.fft.DoubleFFT_1D

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ExerciseProgress(emgUiState: EmgUiState/*viewModel: ExecutionViewModel = viewModel(factory = AppViewModelProvider.Factory)*/){
    val setList: MutableList<String> by remember { mutableStateOf(mutableListOf("1세트", "2세트", "3세트", "4세트")) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val debounceDuration = 300 //0.1초
    val chosenWeight = remember { mutableStateOf(preWeight) }
    val chosenRep = remember { mutableStateOf(preRep) }
    //시작 종료
    var isStarting by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth(0.95f)
            .background(color = Color(0xFFF7F7F7))
            .clip(RoundedCornerShape(12.dp)),
    ) {
        Box(modifier = Modifier //---------header---------
            .fillMaxHeight(0.15f)
            .background(color = Color(0xFFDFEAFF))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.TopStart
            ){
                ScrollableTabRow(setList, selectedTab, onTabClick = { index ->
                    selectedTab = index
                })
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(0.dp),
                contentAlignment = Alignment.TopEnd
            ){
                Button(onClick = {
                    val currentTime = System.currentTimeMillis()
                    if(currentTime - lastClickTime > debounceDuration){
                        if(setList.size<9){
                            val newItem = "${setList.size+1}세트"
                            setList.add(newItem)
                            selectedTab = setList.size - 1
                            lastClickTime = currentTime
                        }
                    }
                },
                    modifier = Modifier
                        .width(120.dp)
                        .height(36.dp)
                        .padding(0.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(0.dp),
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "세트 추가",
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        }
        Row(modifier = Modifier //---------------body-------------------
            .fillMaxHeight(0.7f)
            .fillMaxWidth()
        ) {
            Box(modifier = Modifier //무계, 횟수
                .fillMaxHeight()
                .fillMaxWidth(0.35f)
                .background(Color(0xFFF7F7F7)),
                contentAlignment = Alignment.Center
            ){
                DateSelectionSection(
                    onWeightChosen = { chosenWeight.value = it.toInt() },
                    onRepChosen = { chosenRep.value = it.toInt() },
                )
            }
            Box(
                modifier = Modifier//EMG 신호 표기
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Color(0xFFF7F7F7)),
            ){
                EMGCollector(emgUiState, isStarting)
            }
        }
        Box(
            modifier = Modifier //---------footer---------
                .fillMaxSize()
                .background(Color(0xFFF7F7F7)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(12.dp)),
                horizontalArrangement = Arrangement.Center,
            )
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight()
                        .background(color = Color.White)
                        .clickable {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastClickTime > debounceDuration) {
                                if (setList.size > 1) {
                                    setList.removeLast()
                                    selectedTab = setList.size - 1
                                }
                                lastClickTime = currentTime
                            }
                        },
                ){
                    Text(
                        text = "세트 삭제",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.End

                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(4.dp)
                                .clickable {
                                    isStarting = true
                                    //viewModel.getSet()
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ){
                            Icon(
                                imageVector = Icons.Default.PlayCircleOutline,
                                contentDescription = null,
                                tint = Color(0xFF556FF7),
                                modifier = Modifier.size(20.dp),
                            )
                            Text(text = "시작",fontSize = 15.sp, textAlign = TextAlign.Center)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 8.dp)
                                .clickable { isStarting = false },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.removecirclestop),
                                contentDescription = "contentDescription",
                                tint = Color(0xFFFFD5D5),
                                modifier = Modifier
                                    .size(21.dp)
                                    .padding(2.dp)
                            )
                            Text(text = "종료")
                        }
                    }
                }
            }
        }
    }
}

@Composable//header
private fun ScrollableTabRow(
    tabs: List<String>,
    selectedTab: Int,
    onTabClick: (Int) -> Unit
) {
    androidx.compose.material3.ScrollableTabRow(
        selectedTabIndex = selectedTab,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        edgePadding = 0.dp,
    ) {
        tabs.forEachIndexed { index, text ->
            Tab(
                selected = selectedTab == index,
                onClick = {
                    onTabClick(index)
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .size(20.dp, 36.dp)
                    .background(color = Color(0xFFDFEAFF))
            ) {
                Text(text = text, fontSize = 14.sp)
            }
        }
    }
}

//private fun ScrollButton(){}

//-------------스크롤 버튼-------------------

@Composable
fun DateSelectionSection(
    onWeightChosen: (String) -> Unit,
    onRepChosen: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.9f)
            .background(color = Color.LightGray)
            .padding(10.dp)

    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp)),
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(color = Color.Gray),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Kg",textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            InfiniteItemsPicker(
                items = KgValue,
                firstIndex = (301 * 200)+preWeight - 1,
                onItemSelected =  onWeightChosen
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.66f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp)),
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(color = Color.Gray),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Rep")
            }
            InfiniteItemsPicker(
                items = RepValue,
                firstIndex = (101 * 200) + preRep - 1,
                onItemSelected =  onRepChosen
            )
        }


    }
}


@Composable
fun InfiniteItemsPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    firstIndex: Int,
    onItemSelected: (String) -> Unit,
) {
    // 얼마나 내렸는지 기억
    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf("") }

    LaunchedEffect(key1 = !listState.isScrollInProgress) {
        onItemSelected(currentValue.value)
        listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
    }

    Box(modifier = Modifier
        .height(106.dp)
        .fillMaxWidth()
        .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            content = {
                items(count = Int.MAX_VALUE, itemContent = {
                    val index = it % items.size
                    if (it == listState.firstVisibleItemIndex + 1) {
                        currentValue.value = items[index]
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    var isEditing by remember { mutableStateOf(false) }
                    var Text by remember { mutableStateOf(String()) }

                    if (isEditing) {
                        var newText by remember { mutableStateOf(Text) }
                        val coroutineScope = rememberCoroutineScope() // CoroutineScope 생성

                        TextField(
                            value = newText,
                            onValueChange = {
                                newText = it
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number, // 숫자 입력 모드 설정
                                imeAction = androidx.compose.ui.text.input.ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    isEditing = false
                                    if (newText.isNotEmpty()) {
                                        val newPosition = (items.size * 30) + newText.toInt()-1

                                        // CoroutineScope 내에서 scrollToItem 호출
                                        coroutineScope.launch {
                                            listState.scrollToItem(newPosition)
                                        }
                                    }
                                }
                            ),
                            textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.1f)
                        )
                    } else {
                        Text(
                            text = items[index],
                            modifier = Modifier
                                .alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.3f)
                                .background(Color(if (it == listState.firstVisibleItemIndex + 1) 0xFFDDE2FD else 0xFFFFFFFF))
                                .fillMaxWidth()
                                .clickable(
                                    enabled = it == listState.firstVisibleItemIndex + 1,
                                    onClick = {
                                        isEditing = true
                                        Text = items[index]
                                    }
                                ),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                })
            }
        )
    }
}

//--------------------------------------------

// 최신값
@Composable
fun EMGCollector(emgUiState: EmgUiState, isStarting:Boolean) {
    var signal_1 by remember { mutableStateOf(0) }
    var signal_2 by remember { mutableStateOf(0) }
    var signal_3 by remember { mutableStateOf(0) }
    var signal_4 by remember { mutableStateOf(0) }

    // CoroutineScope을 만듭니다.
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isStarting) {
        while (isStarting) {
            // 0.05초당 한번씩 업데이트
            delay(5)

            signal_1++;
            if(signal_1%2==0)signal_2++;
            if(signal_1%3==0)signal_3++;
            if(signal_1%4==0)signal_4++;
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        ) {
            Box(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .background(Color.Gray),
                contentAlignment = Alignment.Center
            ){
                Text("1 : ${emgUiState.emg1?.value}")
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .size((emgUiState.emg1?.value?:0%90/*signal 최대크기로 나누고 곱하기90*/).dp)
                    .background(Color.White.copy(0.7f))
                    .wrapContentSize(Alignment.Center)
                )
            }
            Box(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.Red),
                contentAlignment = Alignment.Center
            ){
                Text("2 : ${emgUiState.emg2?.value}")
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .size((signal_2%90/*signal 최대크기로 나누고 곱하기90*/).dp)
                    .background(Color.White.copy(0.7f))
                    .wrapContentSize(Alignment.Center)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ){
            Box(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .background(Color.Red),
                contentAlignment = Alignment.Center
            ){
                Text("3 : $signal_3")
            }
            Box(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.Gray),
                contentAlignment = Alignment.Center
            ){
                Text("4 : $signal_4")
            }
        }
    }
}

//-----------------------------------------------------

//@Preview(showBackground = true)
//@Composable
//fun PreviewEMGScreen(){
//    Column {
//        ExerciseEMGScreen()
//        FFT_ready(24)
//
//    }
//}
@Composable
//build.gradle에 //implementation ("com.github.wendykierp:JTransforms:3.1")//넣자
fun FFT_ready(N:Int){//N은 신호의 갯수
    val y = DoubleArray(N) //허수값 0

    for (i in 0 until N) {
        y[i] = 0.0
    }

    val x = DoubleArray(N) //실수값 여기에 신호를 넣자

    for (i in 0 until N) {
        x[i] = Math.sin(2 * Math.PI * 24 * 0.004 * i) + Math.sin(2 * Math.PI * 97 * 0.004 * i)
    }

    val a = DoubleArray(2 * N) //fft 수행할 배열 사이즈 2N

    for (k in 0 until N) {
        a[2 * k] = x[k] //Re
        a[2 * k + 1] = y[k] //Im
    }

    val fft = DoubleFFT_1D(N.toLong()) //1차원의 fft 수행

    fft.complexForward(a) //a 배열에 output overwrite


    val mag = DoubleArray(N / 2)
    var sum = 0.0
    for (k in 0 until N / 2) {
        mag[k] = Math.sqrt(Math.pow(a[2 * k], 2.0) + Math.pow(a[2 * k + 1], 2.0))
        sum += mag[k]
    }
    val average = DoubleArray(N / 2)
    var nowSum = 0.0
    for (k in 0 until N / 2) {
        nowSum+=mag[k];
        average[k]=nowSum/sum
    }

    val avrNumbers = average.map { it as Number }.toTypedArray()
    val chartEntryModel = entryModelOf(*avrNumbers)

    Chart(
        chart = lineChart(),
        model = chartEntryModel,
        startAxis = startAxis(),
        bottomAxis = bottomAxis(),//주파수(Hz) = k / N * 샘플링 주파수(Hz)
    )

}




val preWeight = 50 //이전에 사용한 무계 가져오기
val preRep = 10//이전에 사용한 반복횟수 가져오기


val KgValue = (0..300).map { it.toString() }
val RepValue = (0..100).map { it.toString() }