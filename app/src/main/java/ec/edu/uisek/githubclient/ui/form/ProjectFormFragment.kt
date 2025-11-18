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
                val response = RetrofitClient.gitHubApiService.createRepo(request)

                if (response.isSuccessful) {
                    val newRepo = response.body()
                    Toast.makeText(
                        requireContext(),
                        "Repositorio '${newRepo?.name}' creado con éxito",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ProjectFormFragment", "Error API ${response.code()}: $errorBody")
                    Toast.makeText(
                        requireContext(),
                        "Error al crear: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                // Manejar error de red u otro
                Log.e("ProjectFormFragment", "Excepción al crear repo", e)
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnSave.isEnabled = !isLoading
        btnCancel.isEnabled = !isLoading
    }
}