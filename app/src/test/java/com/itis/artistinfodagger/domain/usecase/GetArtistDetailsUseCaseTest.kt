package com.itis.artistinfodagger.domain.usecase

import com.itis.artistinfodagger.data.models.ArtistDto
import com.itis.artistinfodagger.domain.repository.ArtistInfoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.intellij.lang.annotations.JdkConstants
import org.junit.Before
import org.junit.Test

class GetArtistDetailsUseCaseTest {

    private lateinit var repository: ArtistInfoRepository
    private lateinit var useCase: GetArtistDetailsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetArtistDetailsUseCase(repository)
    }

    @Test
    fun searchArtistDetails() = runTest {

        val artistId = 112024
        val expectedArtist = ArtistDto(
            idArtist = artistId,
            strArtist = "Metallica",
            strGenre = "Metal",
            strStyle = "Thrash Metal",
            intFormedYear = 1981,
            intDiedYear = null,
            strMood = null,
            strLabel = null,
            strWebsite = "https://metallica.com",
            strBiography = "American trash-metal band",
            strArtistThumb = null,
            strArtistLogo = null,
            strArtistBanner = null
        )

        coEvery { repository.getArtistDetails(artistId) } returns Result.success(expectedArtist)

        val result = useCase(artistId)

        assertTrue(result.isSuccess)
        assertEquals(expectedArtist, result.getOrNull())
        coVerify(exactly = 1) { repository.getArtistDetails(artistId) }
    }
}