package hu.unimiskolc.iit.mobile.untitledwestern.application

import android.app.Application
import hu.unimiskolc.iit.mobile.untitledwestern.application.di.appModule
import hu.unimiskolc.iit.mobile.untitledwestern.framework.di.daoModule
import hu.unimiskolc.iit.mobile.untitledwestern.framework.di.dataSourceModule
import hu.unimiskolc.iit.mobile.untitledwestern.framework.di.interactorModule
import hu.unimiskolc.iit.mobile.untitledwestern.framework.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WesternApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WesternApplication)
            modules(listOf(appModule, daoModule, dataSourceModule, repositoryModule, interactorModule))
        }
    }
}
