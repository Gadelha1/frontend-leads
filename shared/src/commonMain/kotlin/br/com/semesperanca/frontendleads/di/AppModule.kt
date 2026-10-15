package br.com.semesperanca.frontendleads.di

import br.com.semesperanca.frontendleads.data.remote.ApiClient
import br.com.semesperanca.frontendleads.data.remote.LeadApi
import br.com.semesperanca.frontendleads.data.repository.DefaultLeadRepository
import br.com.semesperanca.frontendleads.data.storage.KeyValueStore
import br.com.semesperanca.frontendleads.data.storage.createKeyValueStore
import br.com.semesperanca.frontendleads.domain.repository.LeadRepository
import br.com.semesperanca.frontendleads.presentation.leads.LeadDetailViewModel
import br.com.semesperanca.frontendleads.presentation.leads.LeadsViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val dataModule = module {
    single<KeyValueStore> { createKeyValueStore() }
    single { ApiClient() }
    single { LeadApi(get()) }
    single<LeadRepository> { DefaultLeadRepository(get()) }
}

private val presentationModule = module {
    viewModelOf(::LeadsViewModel)
    viewModelOf(::LeadDetailViewModel)
}

object AppModules {
    val all: List<Module> = listOf(dataModule, presentationModule)
    fun init() {
        startKoin { modules(all) }
    }
}
