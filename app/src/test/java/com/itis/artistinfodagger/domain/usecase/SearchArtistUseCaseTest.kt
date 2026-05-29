package com.itis.artistinfodagger.domain.usecase

import com.itis.artistinfodagger.data.models.ArtistDto
import com.itis.artistinfodagger.data.models.TheAudioDBResponse
import com.itis.artistinfodagger.domain.repository.ArtistInfoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchArtistUseCaseTest {

    private lateinit var repository: ArtistInfoRepository
    private lateinit var useCase: SearchArtistUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = SearchArtistUseCase(repository)
    }

    @Test
    fun searchArtists() = runTest {
        val query = "Depeche Mode"
        val expectedArtists = listOf(
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

        val expectedResponse = TheAudioDBResponse(expectedArtists)

        coEvery { repository.getSearchRequest(query) } returns Result.success(expectedResponse)

        val result = useCase(query)

        assertTrue(result.isSuccess)
        assertEquals(expectedResponse, result.getOrNull())
        coVerify(exactly = 1) { repository.getSearchRequest(query) }
    }
}