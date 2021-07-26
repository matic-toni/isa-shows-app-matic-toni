package com.example.shows_tonimatic

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shows_tonimatic.adapter.ShowsAdapter
import com.example.shows_tonimatic.databinding.DialogProfileBinding
import com.example.shows_tonimatic.databinding.FragmentShowsBinding
import com.example.shows_tonimatic.model.Show
import com.example.shows_tonimatic.viewmodel.ShowsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowsFragment : Fragment() {

    private lateinit var binding : FragmentShowsBinding
    private val args: ShowsFragmentArgs by navArgs()
    private val viewModel : ShowsViewModel by viewModels()
    private val cameraPermissionContract = preparePermissionsContract(onPermissionsGranted = {
        openCamera()
    })

    private var avatarUri: Uri? = null

    private val cameraContract = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            FileUtil.getImageFile(context)?.let {
                Glide.with(this).load(avatarUri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(binding.profilePictureButton)
                Toast.makeText(context, "Picture changed", Toast.LENGTH_SHORT).show()
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
        return view
    }

    private fun initRecycleView(shows: List<Show>) {
        binding.showsRecycler.layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = ShowsAdapter(shows) {
            val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
            if (prefs != null) {
                with (prefs.edit()) {
                    putString("id", it.id)
                    putString("title", it.title)
                    putString("description", it.description)
                    putInt("no of reviews", it.noOfReviews)
                    putInt("average rating", it.averageRating ?: 0)
                    putString("image url", it.imageUrl)
                    apply()
                }
            }
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
        dialogBinding.email.text = args.username

        dialog?.setContentView(dialogBinding.root)

        dialogBinding.logout.setOnClickListener {

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

        dialogBinding.changeProfilePicture.setOnClickListener {
            cameraPermissionContract.launch(arrayOf(Manifest.permission.CAMERA))
            dialog?.dismiss()
        }
        dialog?.show()
    }


    private fun openCamera() {
        val file = FileUtil.createImageFile(requireContext())

        avatarUri = FileProvider.getUriForFile(requireContext(), context?.applicationContext?.packageName.toString() + ".fileprovider", file!!)
        cameraContract.launch(avatarUri)
    }
}
