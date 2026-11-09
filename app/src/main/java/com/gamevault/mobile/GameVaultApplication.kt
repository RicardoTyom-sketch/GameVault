package com.gamevault.mobile

import android.app.Application
import com.gamevault.mobile.di.AppContainer

class GameVaultApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
