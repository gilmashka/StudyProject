package com.artistinfo.network

import com.artistinfo.network.dto_and_response_objects.ArtistDTO

data class TheAudioDBResponse(
    var artists: List<ArtistDTO>?
) {
}