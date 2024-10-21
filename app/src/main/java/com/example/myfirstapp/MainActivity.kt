package com.example.myfirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfirstapp.ui.theme.MyFirstAppTheme
import kotlin.math.pow

//7주차 사용자 입력 및 레이아웃 클래스

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFirstAppTheme {
                val viewModel = viewModel<BmiViewModel>()
                val navController = rememberNavController()

                val bmi = viewModel.result.value //viewModel 이 바뀌면 업데이트

                NavHost(navController=navController, startDestination= "main"){
                    composable(route="main"){
                        MainScreen{height, weight, selectedOption, checked ->
                            viewModel.bmiCalculate(height, weight, selectedOption, checked)
                            navController.navigate(route= "analysis/$bmi")
                        } //viewModel 안에 있는 bmi 값 업데이트 됨
                    }
                    //analysis로 값을 넘기기 위한 수정 navBackstackentry
                    composable(route="analysis/{value}"){//i를 입력받아서
                        AnalysisScreen(
                            navController=navController,
                            result = bmi,
                            //result= it.arguments?.getString("value") ?: ""
                            //value 라는것에 대한 String을 받겠다, Nullable 에서 mismatch->?로 NUll 처리
                        )
                    }
                }
            }
        }
    }
}

//result 값의 효율적인 관리를 위해 ViewModel 사용한다.-> 클래스 생성!
class BmiViewModel : ViewModel(){
    //viewmodel 은 rememeberSavavle을 쓰지 않아도 된다. 어차피 Composer 가 아니라서 재호출안됨.
    //다른 방법으로는 result 를 사용하지 않게 val 사용.
    private val _result = mutableStateOf("Default")
    val result: State<String> = _result

    //bmi 계산 함수
    fun bmiCalculate(
        height:Double, weight: Double, selectedOption: String, checked: Boolean
    ){
        val resultList = listOf(
            "Underweight",
            "Normal",
            "Overweight",
            "Obese",
            "Not Allowed"
        )
        val bmi= weight/ (height/100.0).pow(2.0)
        if(!checked){
            _result.value=resultList[4]
            return
        }
        when(selectedOption){
            "Simplified"-> {
                if (bmi>=25.0) _result.value= resultList[2]
                else _result.value=resultList[1]
            }
            "Normal"-> {
                if (bmi>=25.0) _result.value= resultList[2]
                else if(bmi <=18.5) _result.value=resultList[0]
                else _result.value=resultList[1]
            }
            "Detailed"-> {
                if (bmi>=30.0) _result.value= resultList[3]
                else if(bmi >=25.0) _result.value=resultList[2]
                else if(bmi <=18.5) _result.value=resultList[0]
                else _result.value=resultList[1]
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onResultClicked: (Double, Double, String,Boolean)->Unit){
    var height by rememberSaveable { mutableStateOf( " ") }
    var weight by rememberSaveable { mutableStateOf( " ") }
    //아래 변수들 여기 모아놓는게 바람직
    var selectedOption by rememberSaveable { mutableStateOf("Normal") }
    var checked by rememberSaveable { mutableStateOf(false) }
    var result by rememberSaveable { mutableStateOf("Default") }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Obesity Analysis") })
        }
    ){ innerPadding->
        Column (
            modifier= Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ){
            //입력하는 것. 어떤 값에 해당되는 지 넣어줘야 함.
            //height 를 다시 반영해 MainScreen 재호출.
            OutlinedTextField(
                value=height,
                onValueChange={height=it},//하나일땐 it. {input->height = input}
                label={Text("Height")}, //라벨 입력가능!
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),//숫자 입력되게 키보드 변경
                modifier = Modifier.fillMaxWidth()
                )
            //몸무게 다루는 아웃라인텍스트 필드 하나 더 생성
            OutlinedTextField(
                value=weight,
                onValueChange={ weight=it },//하나일땐 it. {input->height = input}
                label={Text("Weight")}, //라벨 입력가능!
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),//숫자 입력되게 키보드 변경
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            Text("Choose the analysis style")
            //아래의 RadioButtonSet 함수 적용
            //변수 이동과 입력값에 따라 함수 수정
            RadioButtonSet(selectedOption = selectedOption, onChange = {selectedOption=it})
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            CheckBoxset(checked = checked, onChange = {checked=it})
            Spacer(modifier = Modifier.padding(16.dp))//component 들 사이 거리띄울 때 사용
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if(height.isNotEmpty() && weight.isNotEmpty()) {
                        onResultClicked(
                            height.toDouble(),
                            weight.toDouble(),
                            selectedOption,
                            checked
                        )
                    }
                }
            ){
                Text("Enter")

            }
        }
    }
}

//네비게이션 클래스. 의존성 추가 필수
//AnalysisScreen 에 앞에 입력한 변수들을 넘겨줘야 한다. NavHost 이용하기.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(navController: NavController, result: String) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Analysis Result")},
                navigationIcon = {
                    //onClick에 navController 반영
                    IconButton (onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Your obesity result is ....", fontSize = 15.sp)
            Spacer(modifier = Modifier.padding(vertical = 16.dp))//spacer 는 이렇게 사이에 들어가서 공간을 만듬.
            Text(text = result , fontSize = 30.sp)
        }
    }
}

@Composable
fun RadioButtonSet(selectedOption: String, onChange: (String)-> Unit){
    val radioOptions= listOf("Simplified", "Normal", "Detailed")
    //아래의 버튼 클릭에 따라 selectedOption값 변화.
    Column (){
        //Row 가 새가지로 비슷하게 세개나 생길때
        radioOptions.forEach{i ->
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                //selected 가 boolean 타입. Simplified 가 선택된게 맞으면 True.
                RadioButton(selected = (i== selectedOption), onClick = { onChange(i)})
                Text(text= i )
            }
        }
//        Row (
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            //selected 가 boolean 타입. Simplified 가 선택된게 맞으면 True.
//            RadioButton(selected = ("Simplified"== selectedOption), onClick = { onChange("Simplified")})
//            Text("Simplified")
//        }
//        Row (
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            //selected 가 boolean 타입. Simplified 가 선택된게 맞으면 True.
//            RadioButton(selected = ("Normal"== selectedOption), onClick = {onChange("Normal")})
//            Text("Normal")
//        }
//        Row (
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            //selected 가 boolean 타입. Simplified 가 선택된게 맞으면 True.
//            RadioButton(selected = ("Detailed"== selectedOption), onClick = { onChange("Detailed")})
//            Text("Detailed")
//        }
    }
}

@Composable
fun CheckBoxset(checked: Boolean, onChange: (Boolean) -> Unit){
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly


    ){
        Text("Do you agree to the analysis of your height and weight?")
        Checkbox(checked = checked, onCheckedChange= {onChange(it)},
            modifier = Modifier.padding(horizontal = 8.dp))
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