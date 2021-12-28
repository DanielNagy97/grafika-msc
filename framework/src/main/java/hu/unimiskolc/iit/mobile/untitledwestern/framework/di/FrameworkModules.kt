package hu.unimiskolc.iit.mobile.untitledwestern.framework.di

import hu.unimiskolc.iit.mobile.untitledwestern.core.data.datasource.GameDataSource
import hu.unimiskolc.iit.mobile.untitledwestern.core.data.repository.GameRepository
import hu.unimiskolc.iit.mobile.untitledwestern.core.interactor.EndGame
import hu.unimiskolc.iit.mobile.untitledwestern.core.interactor.GameInteractors
import hu.unimiskolc.iit.mobile.untitledwestern.core.interactor.StartGame
import hu.unimiskolc.iit.mobile.untitledwestern.framework.db.WesternDatabase
import hu.unimiskolc.iit.mobile.untitledwestern.framework.db.datasource.RoomGameDataSource
import hu.unimiskolc.iit.mobile.untitledwestern.framework.db.mapper.GameMapper
import org.koin.dsl.module

val daoModule = module {
    single { get<WesternDatabase>().gameDao() }
}

val dataSourceModule = module {
    single<GameDataSource> { RoomGameDataSource(get(), GameMapper()) }
}

val repositoryModule = module {
    single<GameRepository> { GameRepository(get()) }
}

val interactorModule = module {
    single { StartGame(get()) }
    single { EndGame(get()) }
    single { GameInteractors(get(), get()) }
}