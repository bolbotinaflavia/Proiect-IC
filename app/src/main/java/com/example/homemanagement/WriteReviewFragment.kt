package com.example.homemanagement

import AboutFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class WriteReviewFragment(aboutFragment: AboutFragment) : Fragment() {
    private lateinit var reviewEditText: EditText
    private lateinit var submitReviewButton: Button
    private lateinit var backButton: View

    private var parentFrag = aboutFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_write_review, container, false)
        reviewEditText = view.findViewById(R.id.reviewEditText)
        submitReviewButton = view.findViewById(R.id.submitReviewButton)
        backButton = view.findViewById(R.id.backButton)

        submitReviewButton.setOnClickListener {
            val reviewText = reviewEditText.text.toString()
            // Handle review submission logic here
        }
        backButton.setOnClickListener{
            returnToAboutPage()
        }
        return view
    }
    private fun returnToAboutPage() {
        backButton.visibility = View.GONE
        parentFrag.showAllButtons()
        parentFragmentManager.popBackStack()
    }
}
