package com.example.shows_tonimatic

import android.content.Context
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            with (binding.triangle) {
                translationY = -800f
                animate()
                    .translationY(0f)
                    .setDuration(800)
                    .setInterpolator(BounceInterpolator())
                    .start()
            }

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
        },
            100)

        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val rememberMe = prefs?.getBoolean(LoginFragment.REMEMBER_ME, false)!!

        handler.postDelayed({
            if (rememberMe) {
                val action = SplashFragmentDirections.splashToShows()
                findNavController().navigate(action)
            } else {
                val action = SplashFragmentDirections.splashToLogin()
                findNavController().navigate(action)
            }
        },
            3000)
    }
}
