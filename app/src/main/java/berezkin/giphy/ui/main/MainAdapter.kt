package berezkin.giphy.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import berezkin.giphy.R
import berezkin.giphy.repo.MainItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.main_item.view.*

class MainViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
  fun bind(item: MainItem?) {
    view.title.text = item?.title
    val thumbnail = Glide.with(view).load(item?.stillUrl)
    thumbnail.into(view.image)
    view.image.setOnClickListener {
      Glide.with(view).load(item?.url).thumbnail(thumbnail).into(view.image)
    }
  }
}

class MainAdapter() : PagedListAdapter<MainItem, MainViewHolder>(comparator) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
    MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false))

  override fun onBindViewHolder(holder: MainViewHolder, position: Int) =
    holder.bind(getItem(position))

  companion object {
    val comparator = object : DiffUtil.ItemCallback<MainItem>() {
      override fun areItemsTheSame(oldItem: MainItem, newItem: MainItem): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: MainItem, newItem: MainItem): Boolean =
        oldItem == newItem
    }
  }
}
