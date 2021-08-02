package com.example.shows_tonimatic

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shows_tonimatic.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var binding : FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding.triangle) {
            translationY = -500f
            animate()
                .translationY(0f)
                .setDuration(800)
                .setInterpolator(BounceInterpolator())
                .start()
        }

        val handler = Handler(Looper.getMainLooper())

            with (binding.showsText) {
                scaleX = 0f
                scaleY = 0f
                handler.postDelayed({
                animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(750)
                    .setInterpolator(BounceInterpolator())
                    .start()
                },
                    900)
        }

        handler.postDelayed({
            val action = SplashFragmentDirections.splashToLogin()
            findNavController().navigate(action)
        },
        3000)
    }
}
