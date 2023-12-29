package com.example.authentication

import android.net.Uri
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authentication.Model.PhotoModel
import com.example.authentication.databinding.FragmentHomeBinding
import com.example.authentication.databinding.FragmentImagesBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage


class ImagesFragment : Fragment() {
    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!
    private val args: ImagesFragmentArgs by navArgs()


    private val photoList = mutableListOf<PhotoModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.frgImgRecyclerview
        // Fetch and display images from Firestore
        recyclerView.layoutManager = LinearLayoutManager(requireContext())






        val firebaseFireStore = FirebaseFirestore.getInstance()

        // Query to fetch images sorted by upload date
        firebaseFireStore.collection("images")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val imageUrl = document.getString("pic")
                    val userName = document.getString("userName")
                    val comment = document.getString("yorum")

                    val photoModel =
                        PhotoModel(imageUrl.orEmpty(), userName.orEmpty(), comment.orEmpty())
                    photoList.add(photoModel)
                }
                val adapter = PhotoAdapter(requireContext(), photoList)
                recyclerView.adapter = adapter
                Log.d("photo", photoList.size.toString())
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ${exception.message}", exception)
            }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


