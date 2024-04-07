package app.test.nanit.di

import app.test.nanit.domain.CheckBirthdayValidityUseCase
import app.test.nanit.domain.impl.CheckBirthdayValidityUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseBindingModule {

    @Binds
    fun bindCheckBirthdayValidityUseCase(impl: CheckBirthdayValidityUseCaseImpl): CheckBirthdayValidityUseCase
}