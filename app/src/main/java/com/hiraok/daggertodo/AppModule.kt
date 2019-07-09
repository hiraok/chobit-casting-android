package com.hiraok.daggertodo

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: DaggerTodoApplication): Context = application
}