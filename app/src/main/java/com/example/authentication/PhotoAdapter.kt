package com.example.authentication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.authentication.Model.PhotoModel
import com.example.authentication.databinding.ItemListBinding

class PhotoAdapter(var nContext: Context, var photoList: List<PhotoModel>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(var tasarim: ItemListBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(nContext), parent, false)
        return  PhotoViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val app = photoList.get(position)
        val t = holder.tasarim

        t.imageView.setImageResource(
            nContext.resources.getIdentifier(app.photoUri.toString(),"drawable",nContext.packageName))
        t.txtUserName.text=app.userName
        t.txtYorum.text=app.userComment


    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}
