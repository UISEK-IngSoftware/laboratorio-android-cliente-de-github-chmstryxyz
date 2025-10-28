package ec.edu.uisek.githubclient.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ec.edu.uisek.githubclient.R
import ec.edu.uisek.githubclient.adapter.RepositoryAdapter
import ec.edu.uisek.githubclient.model.Repository

class RepositoryListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RepositoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repository_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repositoryList = getHardcodedRepositories()
        adapter = RepositoryAdapter(repositoryList)

        recyclerView = view.findViewById(R.id.rv_repositories)
        recyclerView.adapter = adapter
    }

    private fun getHardcodedRepositories(): List<Repository> {
        return listOf(
            Repository("ProyectoAndroid", "Cliente de GitHub para laboratorio.", "user1"),
            Repository("WebApp", "Aplicación web con React y Node.", "user2"),
            Repository("DataScience", "Notebooks de Python para análisis.", "user3"),
            Repository("GameDev", "Juego simple hecho en Unity.", "user1"),
            Repository("KotlinMultiplatform", "App móvil KMP.", "user4")
        )
    }
}