package io.ssafy.mogeun.ui.screens.routine.execution

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import io.ssafy.mogeun.R
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExecutionScreen() {
    //val state = rememberPagerState { 10 }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Yellow)
    ) {
        ExerciseEMGScreen()


    //HorizontalPager(state = state, modifier = Modifier.fillMaxSize()) {page ->
    //            Box(
    //                modifier = Modifier
    //                    .padding(10.dp)
    //                    .background(Color.Blue)
    //                    .fillMaxWidth()
    //                    .aspectRatio(1f),
    //            ) {
    //                Text(text = page.toString(), fontSize = 32.sp)
    //            }
    //   }
    }
}



@SuppressLint("MutableCollectionMutableState")
@Composable
fun ExerciseEMGScreen(){
    val setList: MutableList<String> by remember { mutableStateOf(mutableListOf("1세트", "2세트", "3세트", "4세트")) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val debounceDuration = 300 //0.1초
    val chosenWeight = remember { mutableStateOf(preWeight) }
    val chosenRep = remember { mutableStateOf(preRep) }




    Column(modifier = Modifier
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
                        Icon(imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(0.dp),
                        )
                        Text(modifier = Modifier
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
                .background(color = Color.Green)
            ){
                DateSelectionSection(
                    onWeightChosen = { chosenWeight.value = it.toInt() },
                    onRepChosen = { chosenRep.value = it.toInt() },
                )
            }
            Box(modifier = Modifier//EMG 신호 표기
                .fillMaxHeight()
                .fillMaxWidth()
                .background(color = Color.Magenta)
            ){

            }
        }
        Box(modifier = Modifier //---------footer---------
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)),
        ) {
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .clip(RoundedCornerShape(12.dp)),
                horizontalArrangement = Arrangement.Center,
            )
            {
                Box(modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .fillMaxHeight()
                    .background(color = Color.Blue)
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
                    .background(color = Color.Cyan)
                ){
                   Row(
                       modifier = Modifier
                           .fillMaxSize(),
                       horizontalArrangement = Arrangement.End

                   ) {
                      Row(modifier = Modifier
                          .fillMaxHeight()
                          .padding(4.dp)
                          .clickable {  },
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
                       Row(modifier = Modifier
                           .fillMaxHeight()
                           .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 8.dp)
                           .clickable {  },
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

//--------------------------------------------

@Composable
fun DateSelectionSection(
    onWeightChosen: (String) -> Unit,
    onRepChosen: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
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

    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf("") }

    LaunchedEffect(key1 = !listState.isScrollInProgress) {
        onItemSelected(currentValue.value)
        listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
    }

    Box(modifier = Modifier
        .height(106.dp)
        .width(40.dp)
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

                    Text(
                        text = items[index],
                        modifier = Modifier
                            .alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.3f)
                            .background(Color(if(it == listState.firstVisibleItemIndex + 1)0xFFDDE2FD else 0xFFFFFFFF))
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                })
            }
        )
    }
}
//--------------------------------------------

@Preview(showBackground = true)
@Composable
fun PreviewEMGScreen(){
    ExerciseEMGScreen()
}


val preWeight = 50 //이전에 사용한 무계 가져오기
val preRep = 10//이전에 사용한 반복횟수 가져오기


val KgValue = (0..300).map { it.toString() }
val RepValue = (0..100).map { it.toString() }

