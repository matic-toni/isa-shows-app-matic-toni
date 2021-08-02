package com.example.shows_tonimatic

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shows_tonimatic.adapter.ShowsAdapter
import com.example.shows_tonimatic.databinding.DialogProfileBinding
import com.example.shows_tonimatic.databinding.FragmentShowsBinding
import com.example.shows_tonimatic.model.Show
import com.example.shows_tonimatic.viewmodel.ShowsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ShowsFragment : Fragment() {

    private lateinit var binding : FragmentShowsBinding
    private val viewModel : ShowsViewModel by viewModels()
    private val cameraPermissionContract = preparePermissionsContract(onPermissionsGranted = {
        openCamera()
    })

    private var avatarUri: Uri? = null
    private lateinit var file: File
    private var userEmail: String? = null

    private val cameraContract = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            FileUtil.getImageFile(context)?.let {
                Toast.makeText(context, "Picture changed", Toast.LENGTH_SHORT).show()
                uploadImage()
            }
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowsBinding.inflate(layoutInflater)
        val view = binding.root

        viewModel.getMeResultLiveData().observe(viewLifecycleOwner, { response ->
            if (response.user.imageUrl != "") {
                avatarUri = response.user.imageUrl?.toUri()
                Glide.with(this).load(avatarUri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(binding.profilePictureButton)
                userEmail = response.user.email
            }
        })

        viewModel.getMe()

        viewModel.getShowsLiveData().observe(viewLifecycleOwner, { response ->
            if (response.shows.isNotEmpty()) {
                initRecycleView(response.shows)
            } else {
                Toast.makeText(context, "There are no shows", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.getShows()

        initEmptyStateButton()
        initProfilePictureButton()


        viewModel.getPostImageResultLiveData().observe(viewLifecycleOwner, { response ->
            if (response.user.imageUrl != "") {
                avatarUri = response.user.imageUrl?.toUri()
                Glide.with(this).load(avatarUri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(binding.profilePictureButton)
            } else {
                binding.showsRecycler.isVisible = false
                binding.emptyStateImage.isVisible = true
                binding.emptyStateText.isVisible = true
            }
        })

        return view
    }

    private fun initRecycleView(shows: List<Show>) {
        binding.showsRecycler.layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = ShowsAdapter(shows) {
            val action = ShowsFragmentDirections.actionShowsToShowDetails()
            findNavController().navigate(action)
        }
    }

    private fun initProfilePictureButton() {
        binding.profilePictureButton.setOnClickListener {
            showProfileDialog()
        }
    }

    private fun initEmptyStateButton() {
        binding.emptyStateButton.setOnClickListener {
            if(binding.showsRecycler.isVisible) {
                binding.showsRecycler.isVisible = false
                binding.emptyStateImage.isVisible = true
                binding.emptyStateText.isVisible = true
            } else {
                binding.showsRecycler.isVisible = true
                binding.emptyStateImage.isVisible = false
                binding.emptyStateText.isVisible = false
            }
        }
    }

    private fun showProfileDialog() {
        val dialog = view?.let { BottomSheetDialog(it.context) }
        val dialogBinding = DialogProfileBinding.inflate(layoutInflater)

        if (avatarUri == null) {
            dialogBinding.profilePicture.setImageResource(R.drawable.ic_profile_placeholder)
        } else {
            Glide.with(this).load(avatarUri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(dialogBinding.profilePicture)
        }

        dialogBinding.email.text = userEmail

        dialog?.setContentView(dialogBinding.root)

        dialogBinding.logout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to log out?")

            val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
                when(which){
                    DialogInterface.BUTTON_POSITIVE -> {
                        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
                        if (prefs != null) {
                            with (prefs.edit()) {
                                putBoolean(LoginFragment.REMEMBER_ME, false)
                                apply()
                            }
                        }
                        val action = ShowsFragmentDirections.actionShowsToLogin()
                        findNavController().navigate(action)
                        dialog?.dismiss()
                    }
                }
            }

            builder.setPositiveButton("YES",dialogClickListener)
            builder.setNegativeButton("NO",dialogClickListener)

            val dialogAlert = builder.create()
            dialogAlert.show()
        }

        dialogBinding.changeProfilePicture.setOnClickListener {
            cameraPermissionContract.launch(arrayOf(Manifest.permission.CAMERA))
            dialog?.dismiss()
        }
        dialog?.show()
    }


    private fun openCamera() {
        file = FileUtil.createImageFile(requireContext())!!

        avatarUri = FileProvider.getUriForFile(requireContext(), context?.applicationContext?.packageName.toString() + ".fileprovider", file)
        cameraContract.launch(avatarUri)
    }

    private fun uploadImage() {
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name.trim(), requestFile)

        viewModel.postImage(body)
    }
}
