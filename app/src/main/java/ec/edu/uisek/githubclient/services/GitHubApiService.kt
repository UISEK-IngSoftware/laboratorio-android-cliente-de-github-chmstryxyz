package ec.edu.uisek.githubclient.services

import ec.edu.uisek.githubclient.model.RepoApiResponse
import ec.edu.uisek.githubclient.model.CreateRepoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.DELETE
import retrofit2.http.Path

interface GitHubApiService {

    @GET("users/{user}/repos")
    suspend fun getUserRepos(
        @Path("user") username: String
    ): Response<List<RepoApiResponse>>

    @POST("user/repos")
    suspend fun createRepo(
        @Body repoRequest: CreateRepoRequest
    ): Response<RepoApiResponse>

    @PATCH("repos/{owner}/{repo}")
    suspend fun updateRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body repoRequest: CreateRepoRequest
    ): Response<RepoApiResponse>

    @DELETE("repos/{owner}/{repo}")
    suspend fun deleteRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Unit>
}