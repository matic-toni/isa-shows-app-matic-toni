package com.example.shows_tonimatic

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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

        dialogBinding.profilePicture.setImageResource(R.drawable.ic_profile_placeholder)
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
        }
        dialog?.show()
    }

    private fun openCamera() {
        var file = FileUtil.getImageFile(context)
        if (file == null) {
            file = context?.let { it1 -> FileUtil.createImageFile(it1) }
        }

        if (file != null) {
            val avatarUri = context?.let { it1 -> FileProvider.getUriForFile(it1, it1.applicationContext.packageName.toString() + ".fileprovider", file) }
            val intent = Intent("android.media.action.IMAGE_CAPTURE", avatarUri)
            startActivity(intent)

            val dialogBinding = DialogProfileBinding.inflate(layoutInflater)
            Glide.with(this).load(avatarUri).into(dialogBinding.profilePicture)
        }
    }
}
