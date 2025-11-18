package ec.edu.uisek.githubclient.model

data class CreateRepoRequest(
    val name: String,
    val description: String,
    val private: Boolean = false
)