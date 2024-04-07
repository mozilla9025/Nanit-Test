package app.test.nanit.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.test.nanit.domain.BirthdayCache
import app.test.nanit.model.DisplayBirthday
import app.test.nanit.ui.screen.BirthdayScreenContract.Action
import app.test.nanit.ui.screen.BirthdayScreenContract.State
import app.test.nanit.util.mutate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BirthdayViewModel @AssistedInject constructor(
    @Assisted private val birthdayId: String,
    private val birthdayCache: BirthdayCache
) : ViewModel(), BirthdayScreenContract.ViewModel {

    private val _state = MutableStateFlow(State())
    private val _action = MutableSharedFlow<Action>()
    override val state: StateFlow<State> = _state.asStateFlow()
    override val action: SharedFlow<Action> = _action.asSharedFlow()

    init {
        fetchCachedBirthday()
    }

    private fun fetchCachedBirthday() {
        viewModelScope.launch {
            when (val cachedValue = birthdayCache.state.value) {
                BirthdayCache.Data.Empty -> _action.emit(Action.NavigateBack)
                is BirthdayCache.Data.Value -> processBirthdayValue(cachedValue.birthday)
            }
        }
    }

    private suspend fun processBirthdayValue(birthday: DisplayBirthday) {
        if (birthday.id != birthdayId) {
            _action.emit(Action.NavigateBack)
            return
        }

        birthdayCache.update(BirthdayCache.Data.Empty)
        _state.mutate {
            copy(
                uiModel = BirthdayScreenContract.UiModel.make(
                    birthday.theme,
                    birthday.age
                ),
                name = birthday.name,
                ageTimeUnit = birthday.ageTimeUnit
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(birthdayId: String): BirthdayViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            factory: Factory,
            birthdayId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(birthdayId) as T
            }
        }
    }
}