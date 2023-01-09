package fr.delcey.paging.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import fr.delcey.paging.data.paging.PagingRepositoryImpl
import fr.delcey.paging.domain.paging.PagingRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelScopedDataBindingModule {
    @Binds
    @ViewModelScoped
    abstract fun bindPagingRepository(impl: PagingRepositoryImpl): PagingRepository
}