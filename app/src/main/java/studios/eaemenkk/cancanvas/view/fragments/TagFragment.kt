package studios.eaemenkk.cancanvas.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.fragment_tag.*
import kotlinx.android.synthetic.main.fragment_tag.view.*
import kotlinx.android.synthetic.main.fragment_tag.view.cvTag
import studios.eaemenkk.cancanvas.R

class TagFragment(private  val tagSelector: TagSelectionFragment, private  val tagName: String) : Fragment() {
    private var selected = false
    private var hashedColor = 0

    val viewAvailable = MutableLiveData<View>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tag, container, false)

        view.tvTag.text = tagName
        view.visibility = View.INVISIBLE

        // Gets main theme color
        val typedValue = TypedValue()
        val theme = requireContext().theme
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        val color = typedValue.data

        // Transforms color into HSV
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)

        // Makes random color in range
        val hueRange = 40F
        val text = view.tvTag.text
        var digest = 0
        for(i in text.indices) digest += (digest * (i + 1)) xor text[i].toInt() / text.length
        hsv[0] += (digest * 123456789) % hueRange - hueRange / 2
        hashedColor = Color.HSVToColor(hsv)

        // Sets random color
        view.cvTag.setCardBackgroundColor(hashedColor);
        viewAvailable.value = view

        // Inflates the layout for this fragment
        return view
    }

    fun selectTag() {
        if(!selected
            && tagSelector.selectable
            && (tagSelector.currentSelections < tagSelector.maxSelections
                    || tagSelector.maxSelections < 0)) {
            val typedValue = TypedValue()
            val theme = requireContext().theme
            theme.resolveAttribute(R.attr.colorError, typedValue, true)
            cvTag.setCardBackgroundColor(typedValue.data)
            tagSelector.currentSelections++
            val tagList = ArrayList<String>()
            tagSelector.selectedTags.value?.forEach { t -> tagList.add(t) }
            tagList.add(tagName)
            tagSelector.selectedTags.value = tagList
        } else {
            tagSelector.currentSelections--
            tagSelector.selectedTags.value?.remove(tagName)
            tagSelector.selectedTags.value = tagSelector.selectedTags.value
            cvTag.setCardBackgroundColor(hashedColor)
        }
        selected = !selected
    }

    fun searchTag() {

    }
}