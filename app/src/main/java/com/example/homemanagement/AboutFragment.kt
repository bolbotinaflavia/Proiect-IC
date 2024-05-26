import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.homemanagement.ContactFragment
import com.example.homemanagement.R
import com.example.homemanagement.RateExperienceFragment
import com.example.homemanagement.WriteReviewFragment

class AboutFragment : Fragment() {
    private lateinit var contactButton:View
    private lateinit var writeReviewButton:View
    private lateinit var sendNotificationsButton:View
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
        sendNotificationsButton = view.findViewById<View>(R.id.sendNotificationsButton)
        rateExperienceButton = view.findViewById<View>(R.id.rateExperienceButton)
        showAllButtons()

        // Set click listeners for each button
        contactButton.setOnClickListener {
            loadFragment(ContactFragment())
        }

        writeReviewButton.setOnClickListener {
            loadFragment(WriteReviewFragment())
        }

        sendNotificationsButton.setOnClickListener {
            loadFragment(ContactFragment())
        }

        rateExperienceButton.setOnClickListener {
            loadFragment(RateExperienceFragment())
        }

        return view
    }

    private fun loadFragment(fragment: Fragment) {
        hideAllButtons()
        childFragmentManager.commit {
            replace(R.id.about_fragments, fragment)
        }
    }
    private fun hideAllButtons() {
        contactButton.visibility = View.GONE;
        writeReviewButton.visibility = View.GONE;
        sendNotificationsButton.visibility = View.GONE;
        rateExperienceButton.visibility = View.GONE;

    }

    private fun showAllButtons() {
        contactButton.visibility = View.VISIBLE;
        writeReviewButton.visibility = View.VISIBLE;
        sendNotificationsButton.visibility = View.VISIBLE;
        rateExperienceButton.visibility = View.VISIBLE;
    }

}
