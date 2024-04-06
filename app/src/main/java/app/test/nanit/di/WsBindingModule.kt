package app.test.nanit.di

import app.test.nanit.ws.WsClient
import app.test.nanit.ws.WsClientImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface WsBindingModule {

    @Binds
    fun bindWsClient(impl: WsClientImpl): WsClient
}