package br.com.semesperanca.frontendleads

import android.app.Application
import br.com.semesperanca.frontendleads.di.AppModules

class MobileLeadsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidContextHolder.init(this)
        AppModules.init()
    }
}