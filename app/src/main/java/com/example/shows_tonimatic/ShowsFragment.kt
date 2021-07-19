package com.example.shows_tonimatic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_tonimatic.databinding.FragmentShowsBinding
import com.example.shows_tonimatic.model.Show

class ShowsFragment : Fragment() {

    private lateinit var binding : FragmentShowsBinding

    private val args: ShowsFragmentArgs by navArgs()

    companion object {
        private val shows = listOf(
            Show("the_office", "The Office", R.drawable.ic_the_office),
            Show("stranger_things", "Stranger Things", R.drawable.ic_stranger_things),
            Show("krv_nije_voda", "Krv Nije Voda", R.drawable.ic_krv_nije_voda)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowsBinding.inflate(layoutInflater)
        val view = binding.root
        initRecycleView()
        initLogoutButton()
        return view
    }

    private fun initRecycleView() {
        binding.showsRecycler.layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = ShowsAdapter(shows) {
            val action = ShowsFragmentDirections.actionShowsToShowDetails(args.username, it.id, it.name, it.description, it.imageResourceId)
            findNavController().navigate(action)
        }
    }

    private fun initLogoutButton() {
        binding.logoutButton.setOnClickListener {
            val action = ShowsFragmentDirections.actionShowsToLogin()
            findNavController().navigate(action)
        }
    }
}