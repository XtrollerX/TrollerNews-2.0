package com.example.newstest.UI

import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappenqueue.Adapters.NewsAdapter
import com.example.newstest.Architecture.NewsViewModel


import com.example.newstest.Constants.INITIAL_POSITION
import com.example.newstest.Constants.NEWS_CONTENT
import com.example.newstest.Constants.NEWS_DESCRIPTION
import com.example.newstest.Constants.NEWS_IMAGE_URL
import com.example.newstest.Constants.NEWS_PUBLICATION_TIME
import com.example.newstest.Constants.NEWS_SOURCE
import com.example.newstest.Constants.NEWS_TITLE
import com.example.newstest.Constants.NEWS_URL
import com.example.newstest.Constants.TOP_HEADLINES_COUNT
import com.example.newstest.MainActivity
import com.example.newstest.R
import com.example.newstest.retrofit.Article
import com.example.newstest.retrofit.NewsModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

import com.squareup.picasso.Picasso

class GeneralFragment : Fragment(R.layout.fragment_general) {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: NewsAdapter

    private lateinit var Dialog: AppCompatImageButton

    private lateinit var bottomSheetDialog:BottomSheetDialog

    private lateinit var viewModel: NewsViewModel

    //var newsDatafordown changed from List<NewsModel>
    private lateinit var newsDataForDown: List<Article>
    var position = INITIAL_POSITION


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        viewModel = (activity as MainActivity).viewModel

        // Setting recyclerViews adapter
        //for the newsDatafordown add .slice(TOP_HEADLINES_COUNT until MainActivity.generalNews.size - TOP_HEADLINES_COUNT)
//        newsDataForDown = MainActivity.generalNews.slice(TOP_HEADLINES_COUNT until MainActivity.generalNews.size - TOP_HEADLINES_COUNT)
        adapter = NewsAdapter()
        recyclerView.adapter = adapter
        adapter.differ.submitList(MainActivity.generalNews)

        Initialising_Dialog()
        RecyclerView_OnClickListener()
    }

    fun Initialising_Dialog(){
        adapter.setOnDialogListener(){ Article,UI ->
            Dialog = UI.Dialog
            Dialog.setOnClickListener {
                bottomSheetDialog = BottomSheetDialog(requireContext())
                bottomSheetDialog.setContentView(R.layout.dialog)
                bottomSheetDialog.show()
                val button1 = bottomSheetDialog.findViewById<MaterialButton>(R.id.SaveButton)
                val button2 = bottomSheetDialog.findViewById<MaterialButton>(R.id.ShareButton)
                if (button1 != null) {
                    button1.setOnClickListener{
                        Toast.makeText(activity,"Article Saved", Toast.LENGTH_SHORT).show()
                        viewModel.insertNews(Article)
                        bottomSheetDialog.dismiss()
                    }
                }
                if (button2 != null) {
                    button2.setOnClickListener {
                        Toast.makeText(activity,"Sharing Article", Toast.LENGTH_SHORT).show()
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.putExtra(Intent.EXTRA_TEXT, "Hey, checkout this news : " + Article.url)
                        intent.type = "text/plain"
                        startActivity(Intent.createChooser(intent, "Share with :"))
                        bottomSheetDialog.dismiss()
                    }
                }
            }
        }
    }

    fun RecyclerView_OnClickListener(){
        adapter.setOnItemClickListener {NewsModel->
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            context?.let { customTabsIntent.launchUrl(it, Uri.parse(NewsModel.url)) }
        }
    }
}

