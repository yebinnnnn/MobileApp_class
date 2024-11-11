package com.example.myfirstapp.ui

data class GameUiState(
    //주생성자엔 val 로 선언 가능
    val currentScambledWord : String = "",//하나의 멤버 변수
    val isGuessedWordWrong : Boolean = false, //틀렸는지 안틀렸는지 전달해주는거
    val score: Int =0,
    val currentWordCount : Int =1,
    val isGameOver : Boolean = false,
    var isHighlight: Boolean = false,

    var highlightWord : String ="",
    var onceWords : String=""
)