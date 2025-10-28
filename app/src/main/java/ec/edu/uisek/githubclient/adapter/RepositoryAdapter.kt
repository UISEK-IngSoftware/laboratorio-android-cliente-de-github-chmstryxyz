package ec.edu.uisek.githubclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ec.edu.uisek.githubclient.R
import ec.edu.uisek.githubclient.model.Repository

class RepositoryAdapter(private val repositoryList: List<Repository>) :
    RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = repositoryList[position]
        holder.repoName.text = repository.name
        holder.repoDescription.text = repository.description
    }

    override fun getItemCount() = repositoryList.size

    class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val repoName: TextView = itemView.findViewById(R.id.tv_repo_name)
        val repoDescription: TextView = itemView.findViewById(R.id.tv_repo_description)
        val repoIcon: ImageView = itemView.findViewById(R.id.iv_repo_icon)
    }
}