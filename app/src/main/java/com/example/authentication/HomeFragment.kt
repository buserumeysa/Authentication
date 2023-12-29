package com.example.authentication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.authentication.Model.PhotoModel
import com.example.authentication.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var photoModel: PhotoModel? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            handleMediaPickerResult(uri)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView.setOnClickListener {
            pickMedia.launch("image/*")
        }

        binding.btnUpload.setOnClickListener {
            val userComment = binding.edtTxtComment.text.toString()
            uploadImageToFirebase(
                photoModel?.photoUri?.toUri(),
                photoModel?.userName,
                userComment
            )
        }

        binding.btnSave.setOnClickListener {
            navigateToPhotoPicker()
        }
    }

    private fun handleMediaPickerResult(uri: Uri?) {
        if (uri != null) {
            displaySelectedMedia(uri)
            val userEmail = Firebase.auth.currentUser?.email
            val comment = photoModel?.userComment
            photoModel = PhotoModel(
                photoUri = uri.toString(),
                userName = userEmail.orEmpty(),
                userComment = comment
            )
        } else {
            Toast.makeText(requireContext(), "No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    fun uploadImageToFirebase(imageUri: Uri?, userName: String?, userComment: String?) {
        // Firebase Storage ve Firestore bağlantılarını başlat
        val storage = Firebase.storage
        val storageRef: StorageReference = storage.reference
        val firebaseFireStore = FirebaseFirestore.getInstance()

        // Yüklenen dosyanın bulunduğu klasörü belirle
        val imagesRef: StorageReference =
            storageRef.child("images/${System.currentTimeMillis()}.jpg")

        // Fotoğrafı Storage'a yükle
        imageUri?.let {
            imagesRef.putFile(it)
                .addOnSuccessListener { taskSnapshot ->
                    // Yükleme başarılıysa, fotoğrafın URL'sini al
                    imagesRef.downloadUrl.addOnSuccessListener { uri ->
                        // userName ve userComment değerlerini kullanarak map oluştur
                        val map = hashMapOf(
                            "pic" to uri.toString(),
                            "userName" to userName.orEmpty(),
                            "yorum" to userComment.orEmpty() // userComment değerini ekleyin
                        )

                        firebaseFireStore.collection("images")
                            .add(map)
                            .addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    showToast("Uploaded Successfully")
                                } else {
                                    showToast(firestoreTask.exception?.message!!)
                                }
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseStorage", "Upload failed. ${exception.message}", exception)
                    showToast("Upload failed. ${exception.message}")
                }
        } ?: showToast("No image selected.")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToPhotoPicker() {
        val action =
            HomeFragmentDirections.actionHomeFragmentToImagesFragment(photoModel = photoModel)
        findNavController().navigate(action)
    }

    private fun displaySelectedMedia(uri: Uri) {
        Log.d("HomeFragment", "Displaying media: $uri")

        binding.imageView.setImageURI(uri)

    }


}