package com.itis.artistinfodagger.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.itis.artistinfodagger.data.models.ArtistDto
import com.itis.artistinfodagger.domain.usecase.GetArtistDetailsUseCase
import com.itis.artistinfodagger.presentation.state.DetailsScreenState
import com.itis.artistinfodagger.presentation.utils.toUiModel
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
class DetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetArtistDetailsUseCase
    private val artistId = 1

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun successArtistDetailsSearch() = runTest {

        val expectedArtist = ArtistDto(
            idArtist = artistId,
            strArtist = "Metallica",
            strGenre = "metal",
            strStyle = null,
            intFormedYear = 1981,
            intDiedYear = null,
            strMood = null,
            strLabel = null,
            strWebsite = null,
            strBiography = "American trash-metal band",
            strArtistThumb = null,
            strArtistLogo = null,
            strArtistBanner = null
        )

        coEvery { useCase(artistId) } returns Result.success(expectedArtist)

        val viewModel = DetailsViewModel(useCase, artistId)
        advanceUntilIdle()

        assertTrue(viewModel.state.value is DetailsScreenState.Success)
        val state = viewModel.state.value as DetailsScreenState.Success
        assertEquals(expectedArtist.toUiModel(), state.artist)
        coVerify(exactly = 1) { useCase(artistId) }
    }

    @Test
    fun reloadArtistDetails() = runTest {

        val expectedArtist = ArtistDto(
            idArtist = artistId,
            strArtist = "Metallica",
            strGenre = "metal",
            strStyle = null,
            intFormedYear = 1981,
            intDiedYear = null,
            strMood = null,
            strLabel = null,
            strWebsite = "https://metallica.com",
            strBiography = "American trash-metal band band",
            strArtistThumb = null,
            strArtistLogo = null,
            strArtistBanner = null
        )

        coEvery { useCase(artistId) } returns Result.failure(Exception("Network error"))

        val viewModel = DetailsViewModel(useCase, artistId)
        advanceUntilIdle()

        assertTrue(viewModel.state.value is DetailsScreenState.Error)

        coEvery { useCase(artistId) } returns Result.success(expectedArtist)

        viewModel.retry()
        advanceUntilIdle()

        assertTrue(viewModel.state.value is DetailsScreenState.Success)
        val state = viewModel.state.value as DetailsScreenState.Success
        assertEquals(expectedArtist.toUiModel(), state.artist)
        coVerify(exactly = 2) { useCase(artistId) }
    }

}