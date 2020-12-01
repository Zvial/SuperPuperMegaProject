package com.example.superpupermegaproject

data class Movie(
    val id: Long,
    val name: String,
    val tags: List<String>,
    val reviewersCount: Int,
    val storyline: String,
    val actors: List<Actor>,
    val ageLimit: Int,
    val imageID: Int,
    val lengthOfMovie: Int,
    var isLiked: Boolean = false,
    var starsCount: Int = 0
)

fun Movie.getMovies() = listOf(
    //1
    Movie(
        id = 1,
        name = "Avengers: End Game",
        tags = listOf("Action", "Adventure", "Fantasy"),
        reviewersCount = 125,
        storyline = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
        ageLimit = 13,
        imageID = R.drawable.movie_avangers_end_game,
        starsCount = 4,
        actors = getDefaultActors(),
        lengthOfMovie = 137
    ),
    //2
    Movie(
        id = 2,
        name = "Tenet",
        tags = listOf("Action", "Sci-Fi", "Thriller"),
        reviewersCount = 98,
        storyline = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
        ageLimit = 16,
        imageID = R.drawable.movie_tenet,
        starsCount = 5,
        actors = getDefaultActors(),
        isLiked = true,
        lengthOfMovie = 97
    ),
    //3
    Movie(
        id = 4,
        name = "Black Widow",
        tags = listOf("Action", "Adventure", "Sci-Fi"),
        reviewersCount = 38,
        storyline = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
        ageLimit = 13,
        imageID = R.drawable.movie_black_widow,
        starsCount = 4,
        actors = getDefaultActors(),
        lengthOfMovie = 102
    ),
    //4
    Movie(
        id = 3,
        name = "Wonder Woman 1984",
        tags = listOf("Action", "Adventure", "Fantasy"),
        reviewersCount = 74,
        storyline = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
        ageLimit = 13,
        imageID = R.drawable.movie_wonder_woman,
        starsCount = 5,
        actors = getDefaultActors(),
        lengthOfMovie = 120
    )
)

inline fun getDefaultActors() = listOf(
    Actor(name = "Robert Downey Jr.", imageID = R.drawable.robert_downy_jr),
    Actor(name = "Chris Evans", imageID = R.drawable.chris_evans),
    Actor(name = "Mark Ruffalo", imageID = R.drawable.mark_ruffalo),
    Actor(name = "Chris Hemsworth", imageID = R.drawable.chris_hemsworth)
)