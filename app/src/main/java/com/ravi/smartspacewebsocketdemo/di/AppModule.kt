package com.ravi.smartspacewebsocketdemo.di

import com.ravi.smartspacewebsocketdemo.data.remote.WebSocketClient
import com.ravi.smartspacewebsocketdemo.data.repository.WebSocketRepositoryImpl
import com.ravi.smartspacewebsocketdemo.domain.repository.WebSocketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWebSocketClient(): WebSocketClient = WebSocketClient()

    @Provides
    @Singleton
    fun provideWebSocketRepository(
        client: WebSocketClient
    ): WebSocketRepository = WebSocketRepositoryImpl(client)
}
