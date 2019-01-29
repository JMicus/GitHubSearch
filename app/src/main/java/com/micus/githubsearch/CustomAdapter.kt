package com.micus.githubsearch

import android.content.Context
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


class CustomAdapter(private val mainActivity: MainActivity, private val dataSource: MutableList<Repository>) : BaseAdapter(), AdapterView.OnItemClickListener {

    val TAG = "CustomAdapter"

    private val inflater: LayoutInflater = mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item

        val rowView = inflater.inflate(R.layout.listview_item, parent, false)

        val tvTitle = rowView.findViewById(R.id.tvItemTitle) as TextView

        val tvLanguage = rowView.findViewById(R.id.tvItemLanguage) as TextView

        val tvDescription = rowView.findViewById(R.id.tvItemDescription) as TextView

        val repository = getItem(position) as Repository

        tvTitle.text = repository.title
        tvLanguage.text = repository.language
        tvDescription.text = repository.description

        return rowView
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        Log.v(TAG, "clicked: "+position)

        val selectedRepository = getItem(position) as Repository

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedRepository.link))
        mainActivity.startActivity(browserIntent)
    }
}