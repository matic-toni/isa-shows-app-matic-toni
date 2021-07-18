package com.example.shows_tonimatic

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_tonimatic.databinding.ActivityShowsBinding
import com.example.shows_tonimatic.model.Show

class ShowsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityShowsBinding

    companion object {

        fun buildIntent(activity: Activity) : Intent {
            val intent = Intent(activity, ShowsActivity::class.java)
            return intent
        }

        private val shows = listOf(
            Show("the_office", "The Office", R.drawable.ic_the_office),
            Show("stranger_things", "Stranger Things", R.drawable.ic_stranger_things),
            Show("krv_nije_voda", "Krv Nije Voda", R.drawable.ic_krv_nije_voda)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        initRecycleView()
    }

    private fun initRecycleView() {
        binding.showsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = ShowsAdapter(shows) { item ->
            val intent =  ShowDetailsActivity.buildIntent(this, item)
            startActivity(intent)
        }
    }


}