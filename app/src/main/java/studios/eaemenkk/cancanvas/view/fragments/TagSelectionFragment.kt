package studios.eaemenkk.cancanvas.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_tag_selection.*
import studios.eaemenkk.cancanvas.R

class TagSelectionFragment : Fragment() {
    private var lastRow: LinearLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tag_selection, container, false)
        addTag("Ubuntu")
        addTag("Ubuntu")
        addTag("Ubuntu")
        addTag("Ubuntu")
        addTag("Ubuntu")
        addTag("Ubuntu")
        return view
    }

    private fun getWidth(v: View): Int {
        v.measure(
            View.MeasureSpec.makeMeasureSpec(
                llTags.width,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
        )
        return v.measuredWidth
    }

    private fun addRow() {
        lastRow = LinearLayout(context)
        llTags.addView(lastRow)
    }

    fun addTag(tagName: String) {
        // Creates fragment
        val tag = TagFragment(tagName)
        // Gets screen size
        val density = resources.displayMetrics.density
        val dpWidth = resources.displayMetrics.widthPixels / density

        // Adds tag to scroll view
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        ft.add(R.id.llTags, tag)
        ft.commit()

        // Waits for the tag to be rendered
        tag.viewAvailable.observe(viewLifecycleOwner, Observer{view ->
            val globalLayoutListener = object: ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    println("batata frita ${dpWidth}")

                    // If there are no rows, adds one and removes the view from the scroll view
                    if(lastRow == null) addRow()
                    (view.parent as ViewGroup).removeView(view)
                    println("amoeba ${view.width} ${lastRow?.width}")
                    // If it's offscreen, adds a new row
                    if(view.width + view.left >= dpWidth) {
                        addRow()
                    }

                    // Adds the tag to the last row available
                    lastRow?.addView(view)

                    // Properly renders the tag
                    view.visibility = View.VISIBLE
                }
            }
            view.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        })
    }
}