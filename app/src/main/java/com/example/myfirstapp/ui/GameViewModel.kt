package com.example.myfirstapp.ui

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myfirstapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//뷰모델 구현 클래스
//뷰모델에서 구현한 것들만 Page 에 쓸 수 있음
class GameViewModel : ViewModel() {
    //읽을 수는 있으나 수정X
    private var _allWords= mutableStateOf<Set<String>>(emptySet())
    val allWords: State<Set<String>> = _allWords

    private var _uiState = MutableStateFlow(GameUiState())//업데이트 해주는걸 도와주는 FLow
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currentWord: String //나중에 추가하는것

    private var usedWords : MutableSet<String> = mutableSetOf()

    //과제 하이라이트 부분
    var currentScamble: String=""
    var candidateHigh : String=""
    var candidateScam : String=""
    private var _highlightDialogVisible = mutableStateOf(false) //다이얼로그 보이게 하는용
    val highlightDialogVisible: State<Boolean> = _highlightDialogVisible


    var userGuess by mutableStateOf("")
        private set

    fun loadStringSet(context : Context){ //콘텍스트는 앱 구조 접근가능
        val stringArray= context.resources.getStringArray(R.array.game_strings)
        _allWords.value =stringArray.toSet()
    }

    //Set 에서 랜덤하게 뽑아야하는데...-> 셔플부터
    private fun shuffleCurrentWord(word:String):String{
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord).equals(word)){ //너무 똑같을때 셔플
            tempWord.shuffle()
        }
        currentScamble=String(tempWord)
        return currentScamble
    }

    private fun pickRandomWordAndShuffle(): String{
        currentWord = allWords.value.random()
        if(usedWords.contains(currentWord)){//단어 겹칠경우 재귀
            return pickRandomWordAndShuffle()
        }
        else{//currentWords 에 더해주고 셔플한것 리턴
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    fun updateUserGuess(guessWord: String){
        userGuess= guessWord
    }

    private fun updateGameState(updatedScore: Int){
        if(usedWords.size==10){ //10개가 다 찼을때
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false, //맞은경우라서 false
                    score = updatedScore,
                    isGameOver = true,
                )
            }
        }
        else{
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false, //맞은경우라서 false
                    currentScambledWord = pickRandomWordAndShuffle(), //맞아서 넘어감
                    score = updatedScore,
                    currentWordCount = currentState.currentWordCount.inc(), //increase 함수 사용한거
                )
            }
        }
    }

    fun hideHighlightDialog() {
        _uiState.update { currentState ->
            currentState.copy(isHighlight = false)
        }
    }

    //단어 맞추고 점수를 얻는 시스템
    fun checkUserGuess(){
        if(userGuess.equals(currentWord, ignoreCase = true)){
            val updateScore = _uiState.value.score.plus(10) //점수 10 점 드감

            if (currentWord.length >= 7) { // 단어 길이가 7글자이고 맞춘 경우에만 하이라이트 설정
                _uiState.update { currentState ->
                    currentState.copy(
                        isHighlight = true ,
                    )
                }
                candidateHigh=currentWord
                candidateScam=currentScamble
            }

            updateGameState(updateScore)
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = false)
            }
        }
        else{ //틀렸을 때
            _uiState.update {//틀렸을 때 업데이트 시키시오
                currentState-> currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")//절차 지난 다음엔 단어 비움.
    }

    fun skipWord(){
        updateGameState(_uiState.value.score) //점수 보존
        updateUserGuess("")
    }

    fun highlightIn(){
        _uiState.update { currentState ->
            currentState.copy(
                highlightWord = candidateHigh,
                onceWords = candidateScam,
                isHighlight = true
            )
        }
    }

    fun resetGame(){
        usedWords.clear()
        _uiState.value= GameUiState(currentScambledWord = pickRandomWordAndShuffle())
    }
}