package com.example.homework2

import android.app.Application
import org.koin.android.ext.android.startKoin
import com.example.homework2.di.appModule
import com.example.homework2.di.mainModule
import com.example.homework2.di.noteModule
import com.example.homework2.di.splashModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}