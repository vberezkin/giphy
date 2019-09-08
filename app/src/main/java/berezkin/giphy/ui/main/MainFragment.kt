package berezkin.giphy.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import berezkin.giphy.R
import berezkin.giphy.repo.MainItem
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

  companion object {
    fun newInstance() = MainFragment()
  }

  private val adapter = MainAdapter()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.main_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    list.layoutManager = LinearLayoutManager(context)
    list.adapter = adapter
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    viewModel.items.observe(this, Observer<PagedList<MainItem>> { adapter.submitList(it) })
  }
}
