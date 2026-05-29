package com.itis.artistinfodagger.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.itis.artistinfodagger.data.models.ArtistDto
import com.itis.artistinfodagger.data.models.TheAudioDBResponse
import com.itis.artistinfodagger.domain.usecase.SearchArtistUseCase
import com.itis.artistinfodagger.presentation.state.SearchScreenState
import com.itis.artistinfodagger.presentation.utils.toUiModelList
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: SearchArtistUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
        viewModel = SearchViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun successArtistsSearch() = runTest {

        val query = "Depeche Mode"
        val expectedArtistsDto = listOf(
            ArtistDto(
                1,
                "Depeche Mode",
                "synth rock",
                strStyle = null,
                intFormedYear = 1980,
                intDiedYear = null,
                strMood = null,
                strLabel = null,
                strWebsite = "https://www.depechemode.com/",
                strBiography = "British electro rock industrial band",
                strArtistThumb = null,
                strArtistLogo = "https://commons.wikimedia.org/wiki/File:Depeche_mode_in_portland_Nov_2023.jpg?uselang=ru",
                strArtistBanner = null,
            )
        )

        val expectedResponse = TheAudioDBResponse(expectedArtistsDto)
        val expectedUiModels = expectedArtistsDto.toUiModelList()

        coEvery { useCase(query) } returns Result.success(expectedResponse)

        viewModel.searchArtist(query)
        advanceUntilIdle()

        assertTrue(viewModel.state.value is SearchScreenState.Success)
        val state = viewModel.state.value as SearchScreenState.Success
        assertEquals(expectedUiModels, state.artists)
        coVerify(exactly = 1) { useCase(query) }
    }

    @Test
    fun failureArtistsSearch() = runTest {
        val query = "UnknownBand"
        val errorMessage = "Network error"

        coEvery { useCase(query) } returns Result.failure(Exception(errorMessage))

        viewModel.searchArtist(query)
        advanceUntilIdle()

        assertTrue(viewModel.state.value is SearchScreenState.Error)
        val state = viewModel.state.value as SearchScreenState.Error
        assertEquals(errorMessage, state.message)
        coVerify(exactly = 1) { useCase(query) }
    }

    @Test
    fun retryLastResearch() = runTest {

        val query = "Depeche Mode"
        val expectedArtistsDto = listOf(
            ArtistDto(1,
                "Depeche Mode",
                "synth rock",
                null,
                1980,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null)
        )
        val expectedResponse = TheAudioDBResponse(expectedArtistsDto)

        coEvery { useCase(query) } returns Result.success(expectedResponse)

        viewModel.searchArtist(query)
        advanceUntilIdle()

        viewModel.retryLastSearch()
        advanceUntilIdle()

        coVerify(exactly = 2) { useCase(query) }
    }

}