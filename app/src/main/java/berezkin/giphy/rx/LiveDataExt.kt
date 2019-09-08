package berezkin.giphy.rx

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> LiveData<T>.debounce(duration: Long) = MediatorLiveData<T>().also { mld ->
  val source = this
  val handler = Handler()
  val runnable = Runnable {
    mld.value = source.value
  }
  mld.addSource(source) {
    handler.removeCallbacks(runnable)
    handler.postDelayed(runnable, duration)
  }
}
