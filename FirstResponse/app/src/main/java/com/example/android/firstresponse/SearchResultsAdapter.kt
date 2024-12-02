package com.example.android.firstresponse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class SearchResultsAdapter(private val context: Context, private val dataSource: Array<String>) : BaseAdapter() {

    private val iconsMap = mapOf(
        "burns" to R.drawable.burns,
        "choking" to R.drawable.choking,
        "cpr" to R.drawable.cpr,
        "splints" to R.drawable.sling,
        "seizures" to R.drawable.seizure,
        "shock" to R.drawable.shock,
        "bleeding" to R.drawable.bleeding,
        "snakebite" to R.drawable.snake_bite,
        "bruises" to R.drawable.bruise,
        "sprains" to R.drawable.sprain,
        "strains" to R.drawable.strains,
        "nosebleeds" to R.drawable.nosebleeds,
        "allergic reaction" to R.drawable.allergic_reactions,
        "headaches" to R.drawable.headaches,
        "minor concussions" to R.drawable.minor_concussions,
        "muscle cramps" to R.drawable.cramp,
        "blisters" to R.drawable.blisters,
        "acute grief" to R.drawable.acute_grief,
        "trauma informed care" to R.drawable.trauma_informed_care,
        "panic attack response" to R.drawable.panic_attack_response,
        "grounding techniques" to R.drawable.grounding_techniques,
        "stress reduction" to R.drawable.stress_reduction,
        "floods" to R.drawable.floods,
        "anxiety management" to R.drawable.anxiety_management,
        "volcanic eruption" to R.drawable.volcanic,
        "epidemic" to R.drawable.epidemic,
        "earthquake" to R.drawable.earthquake,
        "water safety" to R.drawable.watersafety,
        "road safety" to R.drawable.roadsafety,
        "daily food safety" to R.drawable.dailyfood,
        "emergency food safety" to R.drawable.emergency,
        "heatwave" to R.drawable.heatwave
    )


    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_search_result, parent, false)

        val titleTextView = rowView.findViewById<TextView>(R.id.titleTextView)
        val iconImageView = rowView.findViewById<ImageView>(R.id.topicIcon)

        val searchItem = getItem(position) as String
        titleTextView.text = searchItem

        // Set the icon based on the search item
        val iconResId = iconsMap[searchItem] ?: R.drawable.ic_default
        iconImageView.setImageResource(iconResId)

        return rowView
    }
}
