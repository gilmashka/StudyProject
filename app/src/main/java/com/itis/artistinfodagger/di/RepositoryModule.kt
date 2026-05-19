package com.itis.artistinfodagger.di

import com.itis.artistinfodagger.data.repositoryImpls.ArtistInfoRepositoryImpl
import com.itis.artistinfodagger.domain.repository.ArtistInfoRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindArtistInfoRepository(implementation: ArtistInfoRepositoryImpl): ArtistInfoRepository
}