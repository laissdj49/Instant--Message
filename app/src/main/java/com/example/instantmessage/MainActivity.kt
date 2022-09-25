package com.example.instantmessage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.instantmessage.ui.theme.InstantMessageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstantMessageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Content()
                }
            }
        }
    }
}

@Composable
fun Content() {
    val context = LocalContext.current
    val textInput = remember { mutableStateOf("") }
    PhoneScreen(
        text = textInput.value,
        onTextChanged = { text -> textInput.value = text },
        onButtonClick = { intentToWhatsApp(context, textInput.value) }
    )
}

@Composable
fun PhoneScreen(text:String, onTextChanged: (String) -> Unit, onButtonClick: () -> Unit) {
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

        Button(onClick = onButtonClick) {
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


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InstantMessageTheme {
        Content()
    }
}