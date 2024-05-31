package com.example.homemanagement

import AboutFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class RateExperienceFragment(aboutFragment: AboutFragment) : Fragment() {

    private lateinit var view: View
    private lateinit var stars: List<ImageView>
    private lateinit var backButton: View

    private var parentFrag = aboutFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_rate_experience, container, false)
        stars = listOf(
            view.findViewById(R.id.star1),
            view.findViewById(R.id.star2),
            view.findViewById(R.id.star3),
            view.findViewById(R.id.star4),
            view.findViewById(R.id.star5)
        )
        backButton = view.findViewById(R.id.backButton)

        for (i in stars.indices) {
            stars[i].setOnClickListener {
                setRating(i + 1)
            }
        }
        backButton.setOnClickListener{
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
        backButton.visibility = View.GONE
        parentFrag.showAllButtons()
        parentFragmentManager.popBackStack()
    }
}
