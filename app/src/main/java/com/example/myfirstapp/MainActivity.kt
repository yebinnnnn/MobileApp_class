package com.example.myfirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myfirstapp.ui.theme.MyFirstAppTheme

//6주차 레이아웃 클래스

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFirstAppTheme {
                ScaffoldExample()
            }
        }
    }
}

//상단바 하단바와 뒤로 가기 버튼
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldExample() {
    //버튼을 위한 상태 부여
    var presses by rememberSaveable {mutableIntStateOf(0)} //Saveable 을 달아 회전 시에도 유지
    Scaffold( //탑바, 바텀바 등등 지정 가능.
        topBar = {
            CenterAlignedTopAppBar(//상단바 근데 이제 센터중심을 곁들인
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor=MaterialTheme.colorScheme.primary,
                ),
                title={Text("Top App Bar")}, //이 텍스트로 상단바 구성
                navigationIcon = { //뒤로 가기 버튼 넣기
                    IconButton(onClick = {/*TODO*/}) {
                        androidx.compose.material3.Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Some Desc")
                    }
                },
                actions = { //여기에 메뉴,edit 버튼 추가 기본적으로 Rowscope 로 되어있다.
                    IconButton(onClick = {/*TODO*/}) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
                    IconButton(onClick = {/*TODO*/}) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        },
        bottomBar = { //하단바
            BottomAppBar (actions = {
                    IconButton(onClick = {/*TODO*/}) { //IconButton 안에는 여러 가지 넣으면 안됨
                        androidx.compose.material3.Icon(imageVector = Icons.Filled.Check, contentDescription = "Check")
                    }
                },
//                //이렇게 바 안에도 floatingActionButton 을 넣을 수 있다.
//                floatingActionButton = {
////                    FloatingActionButton(onClick = {presses+=1}) {
//                        androidx.compose.material3.Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
//                    }
//                }
            )},//TopBar 는 함수,
        floatingActionButton = {
            FloatingActionButton(onClick = {presses+=1}) {
                androidx.compose.material3.Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.padding(16.dp)
            // 각각의 컴포넌트 사이의 간격 설정!, 및 위치 설정
            modifier = Modifier.fillMaxSize().padding(innerPadding)
            //fillMaxHeight().padding(innerPadding) 이렇게 하면 높이만
        ) {
            //Card{} 안에 아래 Text 와 Button 을 집어 넣을 수도 있다.
            OutlinedCard( //카드 꾸미기
//                elevation = CardDefaults.cardElevation(
//                    defaultElevation = 6.dp
//                ),
                colors = CardDefaults.cardColors(
                    contentColor=MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(1.dp, Color.Black)
            ){
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 32.dp)
                ){
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text="""
                                        This is an example of a Scaffold
                                        
                                        You have pressed the floating action button $presses times
                                        """.trimIndent()
                    )
                    //Elevated 버튼도 이쁨 , TextButton 은 텍스트 만 존재.
                    ElevatedButton(onClick = {/*TODO*/}){ //OutlinedButton, FilledTonerButton 등 사용 가능
                        Text("Filled")
                    }
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier=modifier
    )
}
//프리뷰를 다루는 어노테이션. 스플릿뷰에서 어떻게 보이게 할 지 설정해 줄 수 있다.
@Preview(showBackground = true)
@Composable
fun GreetingPreview(){
    MyFirstAppTheme {
        Greeting("Android")
    }
}