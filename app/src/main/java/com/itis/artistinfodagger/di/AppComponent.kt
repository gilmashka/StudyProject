package com.itis.artistinfodagger.di

import coil3.ImageLoader
import com.itis.artistinfodagger.MainActivity
import com.itis.artistinfodagger.presentation.viewmodel.DetailsViewModelFactory
import com.itis.artistinfodagger.presentation.viewmodel.SearchViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RepositoryModule::class,
        NetworkModule::class,
        AppModule::class
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)
    fun getSearchViewModel(): SearchViewModel

    fun getImageLoader(): ImageLoader

    fun getDetailsViewModelFactory(): DetailsViewModelFactory

}