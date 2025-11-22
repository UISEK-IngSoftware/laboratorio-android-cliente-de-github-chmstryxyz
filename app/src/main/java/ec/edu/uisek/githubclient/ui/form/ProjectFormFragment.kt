package ec.edu.uisek.githubclient.ui.form

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ec.edu.uisek.githubclient.R
import ec.edu.uisek.githubclient.model.CreateRepoRequest
import ec.edu.uisek.githubclient.services.RetrofitClient
import kotlinx.coroutines.launch

class ProjectFormFragment : Fragment() {

    private lateinit var etProjectName: TextInputEditText
    private lateinit var tilProjectName: TextInputLayout
    private lateinit var etProjectDescription: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var progressBar: ProgressBar

    private var repoOwner: String? = null
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_project_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etProjectName = view.findViewById(R.id.et_project_name)
        tilProjectName = view.findViewById(R.id.til_project_name)
        etProjectDescription = view.findViewById(R.id.et_project_description)
        btnSave = view.findViewById(R.id.btn_save)
        btnCancel = view.findViewById(R.id.btn_cancel)
        progressBar = view.findViewById(R.id.form_progress_bar)

        arguments?.let {
            isEditMode = true
            repoOwner = it.getString("repoOwner")
            etProjectName.setText(it.getString("repoName"))
            etProjectDescription.setText(it.getString("repoDescription"))
            etProjectName.isEnabled = false
        }

        btnSave.setOnClickListener {
            handleSaveClick()
        }

        btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleSaveClick() {
        val name = etProjectName.text.toString().trim()
        val description = etProjectDescription.text.toString().trim()

        if (name.isEmpty()) {
            tilProjectName.error = "El nombre es obligatorio"
            return
        } else {
            tilProjectName.error = null
        }

        setLoading(true)

        val request = CreateRepoRequest(name = name, description = description)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                if (isEditMode) {
                    updateRepository(repoOwner!!, name, request)
                } else {
                    createRepository(request)
                }
            } catch (e: Exception) {
                Log.e("ProjectFormFragment", "Excepción al guardar repo", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                setLoading(false)
            }
        }
    }

    private suspend fun createRepository(request: CreateRepoRequest) {
        val response = RetrofitClient.gitHubApiService.createRepo(request)
        if (response.isSuccessful) {
            Toast.makeText(requireContext(), "Repositorio creado", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else {
            Toast.makeText(requireContext(), "Error al crear", Toast.LENGTH_SHORT).show()
        }
        setLoading(false)
    }

    private suspend fun updateRepository(owner: String, repoName: String, request: CreateRepoRequest) {
        val response = RetrofitClient.gitHubApiService.updateRepo(owner, repoName, request)
        if (response.isSuccessful) {
            Toast.makeText(requireContext(), "Repositorio actualizado", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else {
            Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
        setLoading(false)
    }

    private fun setLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnSave.isEnabled = !isLoading
        btnCancel.isEnabled = !isLoading
    }
}