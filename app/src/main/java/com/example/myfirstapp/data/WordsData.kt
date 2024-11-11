package com.example.myfirstapp.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.myfirstapp.R

@Composable
fun LoadStringArray(): Set<String>{ //Set을 리턴하는 함수
    val context = LocalContext.current
    //Array를 Set 으로 바꿔서 리턴
    val stringArray = context.resources.getStringArray(R.array.game_strings)
    val stringSet = stringArray.toSet()

    return stringSet
}