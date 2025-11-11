// Archivo: app/src/main/java/ec/edu/uisek/githubclient/services/GitHubApiService.kt
package ec.edu.uisek.githubclient.services

import ec.edu.uisek.githubclient.model.RepoApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {

    /**
     * Obtiene la lista de repositorios de un usuario específico
     */
    @GET("users/{user}/repos")
    suspend fun getUserRepos(
        @Path("user") username: String
    ): Response<List<RepoApiResponse>>
}