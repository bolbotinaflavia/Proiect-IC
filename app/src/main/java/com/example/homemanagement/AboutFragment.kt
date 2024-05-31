import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.homemanagement.ContactFragment
import com.example.homemanagement.R
import com.example.homemanagement.RateExperienceFragment
import com.example.homemanagement.WriteReviewFragment

class AboutFragment : Fragment() {
    private lateinit var contactButton:View
    private lateinit var writeReviewButton:View
    private lateinit var rateExperienceButton:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        // Find each button by their respective IDs
        contactButton = view.findViewById<View>(R.id.contactButton)
        writeReviewButton = view.findViewById<View>(R.id.writeReviewButton)
        rateExperienceButton = view.findViewById<View>(R.id.rateExperienceButton)
        showAllButtons()

        // Set click listeners for each button
        contactButton.setOnClickListener {
            loadFragment(ContactFragment(this))
        }

        writeReviewButton.setOnClickListener {
            loadFragment(WriteReviewFragment(this))
        }

        rateExperienceButton.setOnClickListener {
            loadFragment(RateExperienceFragment(this))
        }

        return view
    }

    private fun loadFragment(fragment: Fragment) {
        hideAllButtons()
        parentFragmentManager.beginTransaction()
            .replace(R.id.about_fragments, fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun hideAllButtons() {
        contactButton.visibility = View.GONE;
        writeReviewButton.visibility = View.GONE;
        rateExperienceButton.visibility = View.GONE;

    }

    fun showAllButtons() {
        contactButton.visibility = View.VISIBLE;
        writeReviewButton.visibility = View.VISIBLE;
        rateExperienceButton.visibility = View.VISIBLE;
    }

}
