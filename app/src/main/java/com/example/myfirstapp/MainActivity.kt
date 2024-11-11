package com.example.myfirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfirstapp.ui.GamePage
import com.example.myfirstapp.ui.GameViewModel
import com.example.myfirstapp.ui.ResultPage
import com.example.myfirstapp.ui.theme.MyFirstAppTheme

//7주차 사용자 입력 및 레이아웃 클래스

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFirstAppTheme {
                //단어 띄워서 전달중
                val gameViewModel = viewModel <GameViewModel>()
                gameViewModel.loadStringSet(this)
                gameViewModel.resetGame()

                val navController= rememberNavController()

                NavHost(navController =navController , startDestination = "game") {
                    composable(route="game"){
                        GamePage(
                            gameViewModel=gameViewModel,
                            checkScore = {navController.navigate(route = "result")},
                        )
                    }
                    composable(route = "result") {
                        ResultPage(
                            gameViewModel= gameViewModel,
                            returnToGame = {navController.navigateUp()})
                    }
                }
            }
        }
    }
}