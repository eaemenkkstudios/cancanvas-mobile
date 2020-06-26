package studios.eaemenkk.cancanvas.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_tag.view.*
import kotlinx.android.synthetic.main.fragment_tag_selection.*
import studios.eaemenkk.cancanvas.R


class TagSelectionFragment: Fragment() {
    private var lastRow: LinearLayout? = null
    private var elementsSize: Float = 0f
    var selectedTags = MutableLiveData<ArrayList<String>>()
    var searchable = true
    var selectable = false
    var maxSelections = -1
    var currentSelections = 0

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        val a = requireActivity().obtainStyledAttributes(attrs, R.styleable.TagSelectionFragment)

        selectable = a.getBoolean(R.styleable.TagSelectionFragment_selectable, false)
        maxSelections = a.getInteger(R.styleable.TagSelectionFragment_max_selectable, -1)
        searchable = a.getBoolean(R.styleable.TagSelectionFragment_selectable, true)
        a.recycle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tag_selection, container, false)
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
        elementsSize = 0f
        llTags.addView(lastRow)
    }

    fun addTag(tagName: String) {
        // Creates fragment
        val tag = TagFragment(this, tagName)
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

                    // If there are no rows, adds one and removes the view from the scroll view
                    if(lastRow == null) addRow()
                    (view.parent as ViewGroup).removeView(view)

                    // If it's offscreen, adds a new row
                    val dpViewWidth = (view.measuredWidth + view.marginLeft + view.marginRight) / density
                    if(dpViewWidth + elementsSize >= dpWidth) {
                        addRow()
                    }
                    elementsSize += dpViewWidth

                    // Adds the tag to the last row available
                    lastRow?.addView(view)

                    // Properly renders the tag
                    view.visibility = View.VISIBLE

                    // Sets click listener to search
                    if(searchable) view.cvTag.setOnClickListener{tag.searchTag()}

                    // Sets click listener to select
                    if(selectable) view.cvTag.setOnClickListener{tag.selectTag()}
                }
            }
            view.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        })
    }
}