package com.example.musicapplication_sp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView

class PlaylistAdapter(context: Context, playList: MutableList<PlaylistModel>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val itemList = playList
    private val playlistUpdateDelete:PlaylistUpdateDelete = context as PlaylistUpdateDelete

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    override fun getCount(): Int {
        return itemList.size
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    override fun getItem(position: Int): Any {
        return itemList.get(position)
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * [android.view.LayoutInflater.inflate]
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     * we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     * is non-null and of an appropriate type before using. If it is not possible to convert
     * this view to display the correct data, this method can create a new view.
     * Heterogeneous lists can specify their number of view types, so that this View is
     * always of the right type (see [.getViewTypeCount] and
     * [.getItemViewType]).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val UID: String = itemList.get(position).UID as String
        val itemTextData = itemList.get(position).itemDatatext as String
        val delete: Boolean = itemList.get(position).delete as Boolean

        val view: View
        val viewHolder: ListViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_playlist, parent, false)
            viewHolder = ListViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ListViewHolder
        }

        viewHolder.textLabel.text = itemTextData
        viewHolder.isDone.isChecked = delete

        viewHolder.isDone.setOnClickListener{
            playlistUpdateDelete.modifyItem(UID, !delete)
        }

        viewHolder.isDeleted.setOnClickListener {
            playlistUpdateDelete.onItemDelete(UID)
        }

        return view
    }

    private class ListViewHolder(row: View?) {
        val textLabel: TextView = row!!.findViewById(R.id.itemTextview) as TextView
        val isDone: CheckBox = row!!.findViewById(R.id.cbDelete) as CheckBox
        val isDeleted: ImageButton = row!!.findViewById(R.id.ibDelete) as ImageButton
    }
}