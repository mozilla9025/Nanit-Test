package app.test.nanit.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.test.nanit.R
import app.test.nanit.model.Birthday
import app.test.nanit.model.DisplayBirthday
import app.test.nanit.ui.MainActivity
import app.test.nanit.ui.theme.NanitTestTheme
import app.test.nanit.ui.theme.Typography
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import dagger.hilt.android.EntryPointAccessors

@Composable
fun BirthdayScreen(
    navController: NavController,
    birthdayId: String,
    viewModelFactory: @Composable () -> BirthdayViewModel.Factory = {
        EntryPointAccessors.fromActivity(
            LocalContext.current as Activity,
            MainActivity.ViewModelFactoryProvider::class.java
        ).birthdayViewModelFactory()
    },
    viewModel: BirthdayScreenContract.ViewModel = viewModel<BirthdayViewModel>(
        factory = BirthdayViewModel.provideFactory(
            factory = viewModelFactory(),
            birthdayId = birthdayId
        )
    )
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.action.collect {
            when (it) {
                BirthdayScreenContract.Action.NavigateBack -> navController.popBackStack()
            }
        }
    }

    BirthdayScreenContent(uiState)
}

@Composable
private fun BirthdayScreenContent(
    uiState: BirthdayScreenContract.State
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(uiState.uiModel.backgroundColor),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Today ${uiState.name} is".uppercase(),
                style = Typography.titleLarge,
                fontStyle = FontStyle.Normal,
                modifier = Modifier.padding(
                    top = 20.dp,
                    bottom = 13.dp
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_left_swirls),
                    contentDescription = null
                )
                uiState.uiModel.ageImage?.let { ageImg ->
                    Image(
                        painter = painterResource(id = ageImg),
                        contentDescription = null
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.img_right_swirls),
                    contentDescription = null
                )
            }

            Text(
                text = when (uiState.ageTimeUnit) {
                    DisplayBirthday.TimeUnit.Month -> "month(s) old"
                    DisplayBirthday.TimeUnit.Year -> "year(s) old"
                }.uppercase(),
                style = Typography.bodyLarge,
                fontStyle = FontStyle.Normal,
                modifier = Modifier.padding(
                    top = 14.dp,
                    bottom = 15.dp
                )
            )

            Image(
                painter = painterResource(id = uiState.uiModel.placeholderImage),
                contentDescription = null
            )
            Image(
                painter = painterResource(id = R.drawable.nanit_logo),
                contentDescription = null,
                modifier = Modifier.padding(top = 15.dp)
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uiState.uiModel.backgroundImage)
                .decoderFactory(SvgDecoder.Factory())
                .crossfade(300)
                .build(),
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.BottomCenter,
            contentDescription = null
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview(device = "id:pixel_5")
private fun BirthdayScreenContentPreview() {
    NanitTestTheme {
        Scaffold {
            val uiState = BirthdayScreenContract.State(
                uiModel = BirthdayScreenContract.UiModel.make(Birthday.Theme.Fox, 2),
                name = "asdasd",
                ageTimeUnit = DisplayBirthday.TimeUnit.Year
            )
            BirthdayScreenContent(uiState)
        }
    }
}