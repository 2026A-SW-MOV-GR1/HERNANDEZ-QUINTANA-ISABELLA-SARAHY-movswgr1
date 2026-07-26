package com.example.aplicaciondisney

import aplicaciondisney.shared.generated.resources.*
import aplicaciondisney.shared.generated.resources.Res
import org.jetbrains.compose.resources.DrawableResource

data class Movie(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val localImage: DrawableResource? = null,
    val backdropUrl: String,
    val localBackdrop: DrawableResource? = null,
    val category: String,
    val description: String,
    val year: String,
    val rating: String
)

data class Brand(
    val name: String,
    val logo: DrawableResource?,
    val gradientColors: List<Long>
)

val sampleBrands = listOf(
    Brand("Disney", Res.drawable.disney_logo, listOf(0xFF0D1C4D, 0xFF1A3A8A)),
    Brand("Pixar", Res.drawable.pixar_logo, listOf(0xFF003366, 0xFF006699)),
    Brand("Marvel", null, listOf(0xFF8B0000, 0xFFED1D24)), // SVG causes crash on Android
    Brand("Star Wars", Res.drawable.starwars_logo, listOf(0xFF000000, 0xFF333333)),
    Brand("Nat Geo", Res.drawable.natgeo_logo, listOf(0xFFCC9900, 0xFFFFCC33))
)

val sampleMovies = listOf(
    Movie(
        id = 1,
        title = "Percy Jackson and the Olympians",
        imageUrl = "https://m.media-amazon.com/images/M/MV5BMjA0NzY1OTYyN15BMl5BanBnXkFtZTgwNjUwNzA0NDE@._V1_.jpg",
        localImage = Res.drawable.percy_poster,
        backdropUrl = "https://images.squarespace-cdn.com/content/v1/51b3dc8ee4b051b96ceb10de/9024c084-2a6c-4824-94c6-43d9406059d9/percy-jackson-and-the-olympians-banner.jpg",
        localBackdrop = Res.drawable.percy_banner,
        category = "Trending",
        description = "Demigod Percy Jackson leads a quest across America to prevent a war among the Olympian gods.",
        year = "2023",
        rating = "TV-PG"
    ),
    Movie(
        id = 2,
        title = "The Mandalorian",
        imageUrl = "https://lumiere-a.akamaihd.net/v1/images/p_disneyplusoriginals_themandalorian_19888_67cf63bc.jpeg",
        localImage = Res.drawable.mando_poster,
        backdropUrl = "https://prod-ripcut-delivery.disney-plus.net/v1/variant/disney/5B30948946E26F9D7887E8E7E44243A4CC1A27A7955F11F1D8A296D467946B0D/scale?width=1200&aspectRatio=1.78&format=jpeg",
        localBackdrop = Res.drawable.mando_banner,
        category = "Trending",
        description = "The travels of a lone bounty hunter in the outer reaches of the galaxy, far from the authority of the New Republic.",
        year = "2019",
        rating = "TV-14"
    ),
    Movie(
        id = 3,
        title = "Avengers: Endgame",
        imageUrl = "https://lumiere-a.akamaihd.net/v1/images/p_avengersendgame_19751_e14b041c.jpeg",
        localImage = Res.drawable.avengers_poster,
        backdropUrl = "https://images.plex.tv/photo?size=medium-360&url=https%3A%2F%2Fmetadata-static.plex.tv%2F8%2Fgracenote%2F8d90463c6c06a86c62c3e44534f3f01c.jpg",
        localBackdrop = Res.drawable.avengers_banner,
        category = "Trending",
        description = "After the devastating events of Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more.",
        year = "2019",
        rating = "PG-13"
    ),
    Movie(
        id = 4,
        title = "Moana",
        imageUrl = "https://lumiere-a.akamaihd.net/v1/images/p_moana_20530_214f419b.jpeg",
        localImage = Res.drawable.moana_poster,
        backdropUrl = "https://prod-ripcut-delivery.disney-plus.net/v1/variant/disney/4F39337C351B8380482B7D037920E306E8813C16568853F83C151B06C7882C7C/scale?width=1200&aspectRatio=1.78&format=jpeg",
        localBackdrop = Res.drawable.moana_banner,
        category = "Recommended",
        description = "In Ancient Polynesia, when a terrible curse incurred by the Demigod Maui reaches Moana's island, she answers the Ocean's call.",
        year = "2016",
        rating = "PG"
    )
)
