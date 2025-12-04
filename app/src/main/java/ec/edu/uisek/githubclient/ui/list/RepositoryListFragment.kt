package ec.edu.uisek.githubclient.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ec.edu.uisek.githubclient.R
import ec.edu.uisek.githubclient.adapter.RepositoryAdapter
import ec.edu.uisek.githubclient.model.Repository
import ec.edu.uisek.githubclient.services.RetrofitClient
import ec.edu.uisek.githubclient.utils.SessionManager
import kotlinx.coroutines.launch

class RepositoryListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var fabAddRepo: FloatingActionButton
    private lateinit var adapter: RepositoryAdapter
    private lateinit var retrofitClient: RetrofitClient
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repository_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofitClient = RetrofitClient(requireContext())
        sessionManager = SessionManager(requireContext())

        // Obtener usuario dinámico
        val githubUsername = sessionManager.getUserName() ?: ""

        recyclerView = view.findViewById(R.id.rv_repositories)
        progressBar = view.findViewById(R.id.progress_bar)
        fabAddRepo = view.findViewById(R.id.fab_add_repo)

        adapter = RepositoryAdapter(
            mutableListOf(),
            onEditClick = { repository ->
                val bundle = Bundle().apply {
                    putString("repoName", repository.name)
                    putString("repoDescription", repository.description)
                    putString("repoOwner", repository.owner)
                }
                findNavController().navigate(R.id.projectFormFragment, bundle)
            },
            onDeleteClick = { repository ->
                deleteRepository(repository.owner, repository.name)
            }
        )

        recyclerView.adapter = adapter

        fabAddRepo.setOnClickListener {
            // El FAB lanza el formulario para crear nuevo repo
            findNavController().navigate(R.id.projectFormFragment)
        }

        if (githubUsername.isNotEmpty()) {
            fetchData(githubUsername)
        } else {
            Toast.makeText(requireContext(), "Error de sesión: Usuario no encontrado", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchData(username: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                progressBar.isVisible = true
                recyclerView.isVisible = false
                val response = retrofitClient.gitHubApiService.getUserRepos(username)
                if (response.isSuccessful) {
                    val apiRepos = response.body() ?: emptyList()
                    val uiRepos = apiRepos.map { apiRepo ->
                        Repository(
                            name = apiRepo.name,
                            description = apiRepo.description ?: "Sin descripción",
                            owner = apiRepo.owner.login,
                            avatarUrl = apiRepo.owner.avatarUrl
                        )
                    }
                    adapter.updateData(uiRepos)
                } else {
                    Log.e("RepoListFragment", "Error en la API: ${response.code()}")
                    Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RepoListFragment", "Excepción al llamar a la API", e)
            } finally {
                progressBar.isVisible = false
                recyclerView.isVisible = true
            }
        }
    }

    private fun deleteRepository(owner: String, repoName: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = retrofitClient.gitHubApiService.deleteRepo(owner, repoName)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Repositorio eliminado", Toast.LENGTH_SHORT).show()
                    val user = sessionManager.getUserName()
                    if (user != null) fetchData(user)
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RepoListFragment", "Excepción al eliminar", e)
                Toast.makeText(requireContext(), "Error de red al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}