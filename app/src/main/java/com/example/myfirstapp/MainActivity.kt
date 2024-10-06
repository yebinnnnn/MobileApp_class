package com.example.myfirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myfirstapp.ui.theme.MyFirstAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFirstAppTheme(dynamicColor = false) {
                Myapp(modifier =Modifier.fillMaxSize())//fillmaxsize 는 여기서 정의하기(고정)
            }
        }
    }
}

//두번째 영상 Myapp
@Composable
fun Myapp(modifier: Modifier=Modifier){
    //= .value 를 계속 쓰기 귀찮을 때-> 임포트 시켜서 starting 자체를 변수처럼 사용중
    var starting by rememberSaveable { mutableStateOf(true) }
    //savable 로 설정함으로서 화면 회전이 일어나도 상태가 저장됨.
    //다른 함수에 있던 Scaffold 들을 공통적으로 여기로 옮겨줘유
    Scaffold (modifier = modifier) { innerPadding ->
        if (starting) {
            OnboardingScreen(
                onContinueClicked = { starting = false },
                modifier= modifier.padding(innerPadding)
            )
        }
        else {
            Greetings(
                onStartClicked={starting= true},
                modifier = modifier.padding(innerPadding)
            ) //Greetings 껴 안아서 똑같이 컴파일 됨.
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier=Modifier
){
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 24.dp , vertical = 24.dp), //fillmaxsize로 화면 맞춤
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = AbsoluteAlignment.Right,
    ){
        Text( "Welcome to")
        Text("Jetpack compose!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked  //다른함수의 value를 연동 못하는 문제
        ){
            Text("Continue")
        }
    }
}

//Scaffold 의 modifier 를 외부에서 지정하도록 함수만듬
@Composable
fun Greetings(  //Modifier 전달 받아 이 안에서 modifier 로 사용.
    modifier: Modifier = Modifier,
    names: List<String> = List(9){"Student${it+1}"},
    //리스트만들어서 이름 꺼내 출력가능
    //LazyColumn 의 경우 List(1000){"$it"}
    onStartClicked:()->Unit
) {
    //보이는 그대로를 할땐 Column이 괜찮지만 너무 많은 목록이 있을때 그때 그때 필요한것만 보여주기
    LazyColumn(modifier = modifier.padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally){//horizontal추가로 외쪽 간격만듬
        item{
            Button(
                modifier = modifier.padding(24.dp),
                onClick = onStartClicked

            ) {
                Text("Back to the starting window")
            }
        }
        items(items=names){ name->
            Greeting(name=name)
        }
//        for(name in names){ //for문으로 names 안의 요소 다 받음 (자바){
//            Greeting(
//                name = name,
////                    modifier = Modifier.padding(innerPadding)// Scaffold에만사용
//            )
//        }
    }
}

//scaffold의 innerpadding 다른 UI 를 먼저 계산 후 전달한다.-> 차이점 발생 가능.
//저 modifier 가 모두에게 전달됨 그냥 Modifier 를 사용하면 디폴트 상태바등의 크기가 무시됨


//텍스트의 색깔 = 텍스트를 둘러싸는 색깔을 바꿀 수 있다.
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var expanded = rememberSaveable { mutableStateOf(false) } //변수를 단순 선언하면 안됨.
    val extraPadding by animateDpAsState(
        targetValue = if(expanded.value)48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness= Spring.StiffnessLow
        ),
        label = ""
    )
    //expanded 자체가 false 가 아니라 상태 안의 value 에 접근하는 것.
    //이것도 위에처럼 .value 를 없앨 수 있긴 함.
    Surface(
        color =  MaterialTheme.colorScheme.primary,
        modifier =modifier.padding(4.dp)
    ){
        //surface 의 성질 함수. 여기 padding 추가로 surface 간 간격도 만듬
        //surface 함수 안에 이미 초기 값을 명시 하고 있다.
        //이 중 괄호가 content 에 전하는 argument 함수다.!!!!!!! modifier 주목하기.!!
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier= modifier.padding(24.dp)
        ){
            Column(modifier = Modifier.weight(1f)
                .padding(bottom=extraPadding)){
//                .fillMaxWidth()
//                .padding(24.dp)) { //세로 배열
                Text(text = "Mobile App Development",
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize * 0.8f
                    )
                )
                Text(text = name, style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold //name 부분 디자인.
                ))
//            Text(
//                text = "Hello $name!",
//                modifier = modifier.padding(24.dp) //패딩 추가 가능.(css 처럼...)
//            )
            }
            //버튼 누름 처리
            ElevatedButton(
                onClick ={expanded.value= !expanded.value /*TODO*/},
                modifier = Modifier.align(Alignment.Top)
            )
            {
                Text(if(expanded.value) "show less" else "Show more",)
            }
        }
    }
}
//프리뷰를 다루는 어노테이션. 스플릿뷰에서 어떻게 보이게 할 지 설정해 줄 수 있다.
@Preview(showBackground = true)
@Composable
fun GreetingPreview(){
    MyFirstAppTheme {
        Myapp()
        // Greeting("World")
    }
}