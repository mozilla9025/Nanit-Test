package app.test.nanit.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.test.nanit.ui.screen.MainScreenContract.Action
import app.test.nanit.ui.screen.MainScreenContract.Event
import app.test.nanit.ui.screen.MainScreenContract.State
import app.test.nanit.ui.theme.NanitTestTheme
import app.test.nanit.ws.ConnectionState

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MainScreenContract.ViewModel = hiltViewModel<MainViewModel>()
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.action.collect {
            when (it) {
                is Action.NavigateToBirthday -> navController.navigate("birthday")
            }
        }
    }

    MainScreenContent(
        uiState = uiState,
        onIpValueTyped = { ip -> viewModel.onEvent(Event.OnIpValueTyped(ip)) },
        onConnectClick = { viewModel.onEvent(Event.Connect) },
        onDisconnectClick = { viewModel.onEvent(Event.Disconnect) },
        onSendMessageClick = { viewModel.onEvent(Event.SendMessage) }
    )
}

@Composable
private fun MainScreenContent(
    uiState: State,
    onIpValueTyped: (ip: String) -> Unit,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit,
    onSendMessageClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = uiState.ipAddress,
            onValueChange = { input ->
                onIpValueTyped(input)
            },
            label = { Text("Enter IP and port") },
        )

        Button(
            onClick = if (uiState.connectionState == ConnectionState.Connected) {
                onDisconnectClick
            } else {
                onConnectClick
            },
            enabled = uiState.connectionState == ConnectionState.Connected || uiState.connectionState == ConnectionState.Disconnected
        ) {
            val text = when (uiState.connectionState) {
                ConnectionState.Connecting -> "Connecting"
                ConnectionState.Connected -> "Disconnect"
                ConnectionState.Disconnecting -> "Disconnecting"
                ConnectionState.Disconnected -> "Connect"
            }
            Text(text = text)
        }
        Button(
            onClick = onSendMessageClick,
            enabled = uiState.connectionState == ConnectionState.Connected
        ) {
            Text(text = "Send message")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview(device = "id:pixel_5")
private fun MainScreenContentPreview() {
    NanitTestTheme {
        Scaffold {
            val uiState = State("10.20.30.40", ConnectionState.Connected)
            MainScreenContent(uiState = uiState, {}, {}, {}, {})
        }
    }
}