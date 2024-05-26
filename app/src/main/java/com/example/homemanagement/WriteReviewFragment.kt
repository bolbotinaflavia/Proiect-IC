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

class WriteReviewFragment : Fragment() {
    private lateinit var reviewEditText: EditText
    private lateinit var submitReviewButton: Button
    private lateinit var backButton: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_write_review, container, false)
        reviewEditText = view.findViewById(R.id.reviewEditText)
        submitReviewButton = view.findViewById(R.id.submitReviewButton)
        backButton = view.findViewById(R.id.backButton)
        showAllButtons()
        submitReviewButton.setOnClickListener {
            val reviewText = reviewEditText.text.toString()
            // Handle review submission logic here
        }
        backButton.setOnClickListener{
            hideAllButtons()
            returnToAboutPage()
        }
        return view
    }
    private fun returnToAboutPage() {
        childFragmentManager.commit {
            replace(R.id.review_page, AboutFragment())
        }
    }

    private fun hideAllButtons() {
        reviewEditText.visibility = View.GONE;
        submitReviewButton.visibility = View.GONE;
        backButton.visibility = View.GONE;
    }

    private fun showAllButtons() {
        reviewEditText.visibility = View.VISIBLE;
        submitReviewButton.visibility = View.VISIBLE;
        backButton.visibility = View.VISIBLE;
    }
}
