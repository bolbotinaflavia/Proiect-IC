package com.example.homemanagement

import AboutFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class RateExperienceFragment : Fragment() {

    private lateinit var stars: List<ImageView>
    private lateinit var backButton: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rate_experience, container, false)
        stars = listOf(
            view.findViewById(R.id.star1),
            view.findViewById(R.id.star2),
            view.findViewById(R.id.star3),
            view.findViewById(R.id.star4),
            view.findViewById(R.id.star5)
        )
        backButton = view.findViewById(R.id.backButton)
        showAllButtons()

        for (i in stars.indices) {
            stars[i].setOnClickListener {
                setRating(i + 1)
            }
        }
        backButton.setOnClickListener{
            hideAllButtons()
            returnToAboutPage()
        }
        return view
    }

    private fun setRating(rating: Int) {
        for (i in stars.indices) {
            stars[i].setImageResource(if (i < rating) R.drawable.star_field else R.drawable.star_border)
        }
    }

    private fun returnToAboutPage() {
        childFragmentManager.commit {
            replace(R.id.rate_page, AboutFragment())
        }
    }

    private fun hideAllButtons() {
        for (i in stars.indices) {
            stars[i].visibility = View.GONE;
        }
        backButton.visibility = View.GONE;
    }

    private fun showAllButtons() {
        for (i in stars.indices) {
            stars[i].visibility = View.VISIBLE;
        }
        backButton.visibility = View.VISIBLE;
    }
}
