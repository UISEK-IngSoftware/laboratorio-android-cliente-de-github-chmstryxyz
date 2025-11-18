// Archivo: app/src/main/java/ec/edu/uisek/githubclient/services/GitHubApiService.kt
package ec.edu.uisek.githubclient.services

import ec.edu.uisek.githubclient.model.RepoApiResponse
import ec.edu.uisek.githubclient.model.CreateRepoRequest // AÑADIDO
import retrofit2.Response
import retrofit2.http.Body // AÑADIDO
import retrofit2.http.GET
import retrofit2.http.POST // AÑADIDO
import retrofit2.http.Path

interface GitHubApiService {

    /**
     * Obtiene la lista de repositorios de un usuario específico
     */
    @GET("users/{user}/repos")
    suspend fun getUserRepos(
        @Path("user") username: String
    ): Response<List<RepoApiResponse>>

    /**
     * Crea un nuevo repositorio para el usuario autenticado
     * AÑADIDO
     */
    @POST("user/repos")
    suspend fun createRepo(
        @Body repoRequest: CreateRepoRequest
    ): Response<RepoApiResponse> // La API de GitHub devuelve el repo recién creado
}