package com.example.instantmessage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import com.example.instantmessage.ui.theme.InstantMessageTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val scope = CoroutineScope(AndroidUiDispatcher.Main)
    private val inputFlow = MutableSharedFlow<String>()

    private val model = scope.launchMolecule(clock = RecompositionClock.ContextClock) {
        SendMessagePresenter(userInput = inputFlow)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstantMessageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scope = rememberCoroutineScope()
                    Content(
                        model = model,
                        onInputValue = { scope.launch { inputFlow.emit(it) } }
                    )
                }
            }
        }
    }
}

@Composable
fun Content(model: StateFlow<PhoneState>, onInputValue: (String) -> Unit) {
    val context = LocalContext.current
    val phone by model.collectAsState()
    PhoneScreen(
        enabled = phone.isValid,
        text = phone.number,
        onTextChanged = {
            onInputValue.invoke(it)
        },
        onButtonClick = { intentToWhatsApp(context, phone.number) }
    )
}

@Composable
fun PhoneScreen(
    text: String,
    onTextChanged: (String) -> Unit,
    onButtonClick: () -> Unit,
    enabled: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        TextField(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            value = text,
            onValueChange = onTextChanged,
            label = { Text(text = "Phone number") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            enabled = enabled,
            onClick = onButtonClick
        ) {
            Text(text = "Send")
        }
    }
}

fun intentToWhatsApp(context: Context, phone: String) {
    context.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://api.whatsapp.com/send?phone=$phone")
        )
    )
}