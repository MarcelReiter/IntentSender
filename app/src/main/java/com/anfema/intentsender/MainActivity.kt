package com.anfema.intentsender

import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TaskStackBuilder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.anfema.intentsender.ui.theme.IntentSenderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntentSenderTheme {
                var textFieldValue by rememberSaveable {
                    mutableStateOf("https://www.example.com/123")
                }

                MainScreen(
                    textFieldValue = textFieldValue,
                    onTextFieldValueChange = { textFieldValue = it },
                    onSendIntent = {
                        // based on https://youtu.be/z6VlP0o_sDc?si=Gp_wbph2OS5vaqfW&t=1018

                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(textFieldValue)
                        )

                        TaskStackBuilder.create(applicationContext)
                            .apply { addNextIntentWithParentStack(intent) }
                            .getPendingIntent(0, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
                            .send()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    textFieldValue: String,
    onTextFieldValueChange: (String) -> Unit,
    onSendIntent: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IntentSender") }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = textFieldValue,
                singleLine = true,
                label = { Text("Intent Url") },
                onValueChange = { onTextFieldValueChange(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onAny = { onSendIntent() }),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onSendIntent,
                content = { Text("Send Intent") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}
