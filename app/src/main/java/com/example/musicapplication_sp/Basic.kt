//package com.example.musicapplication_sp
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.ImageView
//import android.widget.PopupMenu
//import android.widget.Toast
//import java.lang.Exception
//
//class Basic {
//    fun popupMenu() {
//
//        val imageView = findViewById<ImageView>(R.id.iv_image)
//        imageView.setOnClickListener {
//
//            val popupMenu : PopupMenu = PopupMenu(this, imageView)
//            popupMenu.menuInflater.inflate(R.menu.menu,popupMenu.menu)
//
//            popupMenu.setOnMenuItemClickListener {
//                when (it.itemId) {
//                    R.id.item1 -> {
//                        Toast.makeText(applicationContext, "Press 1", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    R.id.item2 -> {
//                        Toast.makeText(applicationContext, "Press 2", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    R.id.item3 -> {
//                        Toast.makeText(applicationContext, "Press 3", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    else -> true
//                }
//            }
//
//            //hold on the image
//            imageView.setOnClickListener() {
//
//                try {
//
//                    val popup = PopupMenu::class.java.getDeclaredField("mPopup")
//                    popup.isAccessible = true
//                    val menu = popup.get(popupMenu)
//                    menu.javaClass
//                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
//                        .invoke(menu, true)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                } finally {
//                    popupMenu.show()
//                }
//                true
//            }
//        }
//    }
//}