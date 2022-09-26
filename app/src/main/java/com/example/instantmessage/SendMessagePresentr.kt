package com.example.instantmessage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.Flow

@Composable
fun SendMessagePresenter(userInput: Flow<String>): PhoneState {
    val input by userInput.collectAsState(initial = "")

    val phone = if (input.length > 13) input.substring(0, 13) else input

    return PhoneState(
        isValid = phone.length in 11..13,
        number = phone
    )
}

data class PhoneState(
    val isValid: Boolean,
    val number: String,
)