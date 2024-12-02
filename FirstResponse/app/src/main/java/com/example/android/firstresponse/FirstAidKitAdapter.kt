package com.example.android.firstresponse

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.animation.ScaleAnimation

class FirstAidKitAdapter(private val firstAidKits: List<FirstAidKit>) : RecyclerView.Adapter<FirstAidKitAdapter.FirstAidKitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstAidKitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_first_aid_kit, parent, false)
        return FirstAidKitViewHolder(view)
    }

    override fun onBindViewHolder(holder: FirstAidKitViewHolder, position: Int) {
        val firstAidKit = firstAidKits[position]
        holder.bind(firstAidKit)
    }

    override fun getItemCount(): Int = firstAidKits.size

    inner class FirstAidKitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val kitImage: ImageView = itemView.findViewById(R.id.kitImage)
        private val kitTitle: TextView = itemView.findViewById(R.id.kitTitle)
        private val kitDescription: TextView = itemView.findViewById(R.id.kitDescription)
        private val shopeeButton: ImageButton = itemView.findViewById(R.id.shopeeButton)
        private val lazadaButton: ImageButton = itemView.findViewById(R.id.lazadaButton)
        private val temuButton: ImageButton = itemView.findViewById(R.id.temuButton)

        fun bind(firstAidKit: FirstAidKit) {
            kitImage.setImageResource(firstAidKit.imageResId)
            kitTitle.text = firstAidKit.title
            kitDescription.setText(firstAidKit.descriptionResId)

            // Button touch handlers
            shopeeButton.setOnTouchListener { _, event ->
                handleButtonTouch(event, shopeeButton, firstAidKit.shopeeUrl)
            }

            lazadaButton.setOnTouchListener { _, event ->
                handleButtonTouch(event, lazadaButton, firstAidKit.lazadaUrl)
            }

            temuButton.setOnTouchListener { _, event ->
                handleButtonTouch(event, temuButton, firstAidKit.temuUrl)
            }
        }

        private fun handleButtonTouch(event: MotionEvent, button: ImageButton, url: String): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    scaleButton(button, 0.9f) // Scale down
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    scaleButton(button, 1.0f) // Scale back to original size
                    openUrl(url)
                    return true
                }
                MotionEvent.ACTION_CANCEL -> {
                    scaleButton(button, 1.0f) // Reset scale
                    return true
                }
            }
            return false
        }

        private fun scaleButton(button: ImageButton, scale: Float) {
            button.scaleX = scale
            button.scaleY = scale
        }

        private fun openUrl(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            itemView.context.startActivity(intent)
        }
    }
}
