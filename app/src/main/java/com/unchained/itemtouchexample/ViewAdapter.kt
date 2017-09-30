package acerezo.android.itemtouchexample

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.domain.ViewModel
import java.util.*

class ViewAdapter(private val items: MutableList<ViewModel>) : RecyclerView.Adapter<ItemViewHolder>(),
        ItemTouchHelperCallback.ItemTouchHelperAdapter {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
            holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(parent.inflate(R.layout.item_view))

    override fun onItemMoved(fromPosition: Int, toPosition: Int) = swapItems(fromPosition, toPosition)

    override fun onItemDismiss(position: Int) = deleteItem(position)

    private fun deleteItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun swapItems(positionFrom: Int, positionTo: Int) {
        Collections.swap(items, positionFrom, positionTo)
        notifyItemMoved(positionFrom, positionTo)
    }

    fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

}