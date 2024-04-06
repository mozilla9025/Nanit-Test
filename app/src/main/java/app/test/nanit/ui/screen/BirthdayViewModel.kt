package app.test.nanit.ui.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(): ViewModel(), BirthdayScreenContract.ViewModel{

    private val _state = MutableStateFlow(BirthdayScreenContract.State())
    override val state: StateFlow<BirthdayScreenContract.State> = _state.asStateFlow()


}