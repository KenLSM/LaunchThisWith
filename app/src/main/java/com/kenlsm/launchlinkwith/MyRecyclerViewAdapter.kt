package com.kenlsm.launchlinkwith

import android.content.pm.PackageManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.pm.ResolveInfo
import android.widget.ImageView


internal class MyRecycleViewAdapter(private val myDataset: List<ResolveInfo>) :
        RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder>() {


    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var subtitle: TextView = view.findViewById(R.id.subtitle)
        var icon: ImageView = view.findViewById(R.id.icon)
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyRecycleViewAdapter.MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_view, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val data = myDataset[position]
        holder.title.text = data.activityInfo.packageName
        holder.subtitle.text = data.activityInfo.name
//        holder.icon.
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}