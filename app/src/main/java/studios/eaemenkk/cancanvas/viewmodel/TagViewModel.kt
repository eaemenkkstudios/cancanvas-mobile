package studios.eaemenkk.cancanvas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import studios.eaemenkk.cancanvas.interactor.TagInteractor

class TagViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = TagInteractor(app.applicationContext)

    val tagList = MutableLiveData<ArrayList<String>>()

    fun getTags() {
        interactor.getTags { tags -> tagList.postValue(tags) }
    }
}