// Archivo: app/src/main/java/ec/edu/uisek/githubclient/model/RepoApiResponse.kt
package ec.edu.uisek.githubclient.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos que coincide con la respuesta de la API de GitHub
 */
data class RepoApiResponse(
    val id: Long,
    val name: String,
    val description: String?, // Puede ser nulo
    val owner: OwnerApiResponse
)

data class OwnerApiResponse(
    val login: String,
    @SerializedName("avatar_url") // El JSON usa 'avatar_url', lo mapeamos
    val avatarUrl: String
)