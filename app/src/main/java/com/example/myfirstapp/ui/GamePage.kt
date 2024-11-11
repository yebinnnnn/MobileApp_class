package com.example.myfirstapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirstapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamePage(
    gameViewModel: GameViewModel = viewModel(),
    checkScore : ()->Unit, //호이스팅. 넘기기
){
    val gameUiState by gameViewModel.uiState.collectAsState()//State 형태로 업데이트
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title={ Text(text="Unscrambling Game") })
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
            GameLayout( //이벤트는 여기가 아니라 바깥에 구현하는게 좋음.
                onUserGuessChanged = { gameViewModel.updateUserGuess(it)},
                userGuess = gameViewModel.userGuess, //Getter는 private 하지 않아서 사용가능
                wordCount = gameUiState.currentWordCount,
                onKeyboardDone={gameViewModel.checkUserGuess()},//넘겨주기
                currentScrambledWord = gameUiState.currentScambledWord,
                isGuessWrong = gameUiState.isGuessedWordWrong,
                modifier = Modifier
                    .fillMaxWidth(),
            )
            Column( //버튼용 컬럼
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {gameViewModel.checkUserGuess()
                    }
                ) {
                    Text(text="Submit")
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {gameViewModel.skipWord()}
                ) {
                    Text(text = "skip")
                }
            }
        }
        if(gameUiState.isGameOver){ //checkscore 누를 때 다음 페이지 나오도록 네비게이션
            FinalDialog(checkScore = checkScore)
        }
        if(gameUiState.isHighlight){
            HighlightDialog(
                gameViewModel= gameViewModel
            )
        }
    }
}

@Composable
fun GameLayout(
    onUserGuessChanged : (String) -> Unit,
    wordCount : Int,
    isGuessWrong : Boolean,
    userGuess : String,
    onKeyboardDone : ()-> Unit,
    currentScrambledWord: String,
    modifier: Modifier= Modifier
){
    Card(
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .clip(shapes.medium) //둥근모서리
                    .background(color= colorScheme.surfaceTint)
                    .padding(vertical = 4.dp, horizontal = 10.dp)
                    .align(alignment = Alignment.End), //이 텍스트만 정렬
                text = stringResource(id= R.string.current_score, wordCount),
                style = typography.titleMedium,
                color= colorScheme.onPrimary
            )
            Text(
                text=currentScrambledWord,
                style=typography.displayMedium
            )
            Text(
                text="Unscramble the above vocabulary",
                style = typography.titleMedium
            )
            OutlinedTextField( //입력창
                modifier = Modifier.fillMaxWidth(),
                value=userGuess,
                onValueChange = onUserGuessChanged,
                colors=TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                    errorContainerColor = colorScheme.surface,
                ),
                label={
                    if(isGuessWrong){
                        Text(text= stringResource(id= R.string.wrong_guess))
                    }
                    else{
                        Text(text= stringResource(id = R.string.enter_your_word))
                    }
                },
                //틀렸는지 텍스트에 보이게 하기
                isError = isGuessWrong,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone={onKeyboardDone()}
                )
            )
        }
    }//여기까지가 Card
}

@Composable
private fun FinalDialog( //10개가 끝나는 순간 얘가 튀어나올 수 있게.
    checkScore : ()->Unit,
    modifier: Modifier = Modifier,
){
    AlertDialog(
        onDismissRequest = {},
        title ={
            Text(
                text = stringResource(id = R.string.alert_title)
            )
        },
        text = { Text(text= stringResource(id = R.string.alert_content)) },
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = checkScore) {
                Text( text = stringResource(id = R.string.check_score))
            }
        }
    )
}

@Composable
private fun HighlightDialog( //10개가 끝나는 순간 얘가 튀어나올 수 있게.
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel
){
    AlertDialog(
        onDismissRequest = {},
        title ={
            Text(
                text = "Wonderful!"
            )
        },
        text = {
            Column {
                Text(text = stringResource(id = R.string.highlight_content))
                Text(text = stringResource(id = R.string.highlight_content2))
            }
        },
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = {
                gameViewModel.highlightIn()
                gameViewModel.hideHighlightDialog()
            }){
                Text( text ="Register")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                gameViewModel.hideHighlightDialog()
            }){
                Text(text = "Cancel")
            }
        }
    )
}