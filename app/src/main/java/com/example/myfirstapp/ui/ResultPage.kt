package com.example.myfirstapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirstapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultPage(
    gameViewModel: GameViewModel= viewModel(),
    returnToGame : () -> Unit
){
    val gameUiState by gameViewModel.uiState.collectAsState() //가져오는거
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title={ Text(text="GameResult") },
                navigationIcon = {
                    IconButton(onClick = returnToGame){
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp), //버튼의 가로 영역 정해줌
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Card (
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text= stringResource(id= R.string.your_score_is),
                        style = typography.titleMedium
                    )
                    Text(
                        text=gameUiState.score.toString(),
                        style=typography.displayMedium
                    )
                    if(gameUiState.highlightWord.isNotEmpty()){
                        HighlightPage(gameViewModel)
                    }
                }
            }//여기까지가 Card
            Column( //버튼용 컬럼
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        gameViewModel.resetGame() //리셋 필요함
                        returnToGame()
                    }
                ) {
                    Text(text="Return to Game")
                }
            }
        }
    }
}

@Composable
fun HighlightPage(
    gameViewModel: GameViewModel
){
    val gameUiState by gameViewModel.uiState.collectAsState()
    Column(){
        Text(
            text = stringResource(id = R.string.highlight_title),
            style = typography.titleMedium
        )
        Row {
            Text(
                text = gameUiState.onceWords,
                style = typography.displayMedium
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = "to",
            )
        }
        Text(
            text =gameUiState.highlightWord,
            style = typography.displayMedium
        )
    }
}