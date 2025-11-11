package ec.edu.uisek.githubclient.model

data class Repository(
    val name: String,
    val description: String,
    val owner: String,
    val avatarUrl: String
)