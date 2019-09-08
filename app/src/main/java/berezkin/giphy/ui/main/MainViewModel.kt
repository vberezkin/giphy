package berezkin.giphy.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import berezkin.giphy.repo.MainRepository
import berezkin.giphy.rx.debounce

class MainViewModel : ViewModel() {
  private val repo = MainRepository()
  val query = MutableLiveData<String>().apply { value = "" }
  val items = Transformations.switchMap(query.debounce(500)) { repo.items(it, 20) }
  //  TODO Loading progress
}
