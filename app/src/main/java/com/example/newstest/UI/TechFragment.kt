package com.example.newstest.UI

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.browser.customtabs.CustomTabsIntent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappenqueue.Adapters.NewsAdapter
import com.example.newstest.Architecture.NewsViewModel
import com.example.newstest.Constants
import com.example.newstest.ExtraPackage.ErrorHandling
import com.example.newstest.MainActivity
import com.example.newstest.R
import com.example.newstest.retrofit.Article
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class TechFragment: Fragment(R.layout.techfragment) {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: NewsAdapter

    private lateinit var Dialog: AppCompatImageButton
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var viewModel: NewsViewModel

    //var newsDatafordown changed from List<NewsModel>
    private lateinit var newsDataForDownTech: List<Article>
    var position = Constants.INITIAL_POSITION

    lateinit var ProgressBar:ProgressBar

    lateinit var errorDialog: ConstraintLayout
    lateinit var SocketErrorButton: Button


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        viewModel = (activity as MainActivity).viewModel
        Log.e("Tech", "Tech is working")
        recyclerView.layoutManager = layoutManager
        ProgressBar = view.findViewById(R.id.progresBar)
        errorDialog = view.findViewById(R.id.SocketError)
        SocketErrorButton = view.findViewById(R.id.refreshbutton)


        adapter = NewsAdapter()
        recyclerView.adapter = adapter
        adapter.differ.submitList( MainActivity.TechNews)

        Initialising_Dialog()
        RecyclerView_OnClickListener()
        TechNewsObserver()
    }

    fun TechNewsObserver(){
        Log.d("MainActivity"," General " )
        viewModel.TechNews.observe(viewLifecycleOwner, Observer {
            if(it is ErrorHandling.Success){
                it.data?.let {
                    adapter.differ.submitList(it.articles)
                    ProgressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    errorDialog.visibility = View.GONE

                }
            }
            else if (it is ErrorHandling.Error){
                ProgressBar.visibility = View.GONE
                recyclerView.visibility = View.GONE
                errorDialog.visibility = View.VISIBLE
                SocketErrorButton.setOnClickListener {
                    Toast.makeText(requireActivity(),"Reloading Requests",Toast.LENGTH_LONG).show()

                }
            }
        })
    }

    fun Initialising_Dialog() {
        adapter.setOnDialogListener() { Article, UI ->
            Dialog = UI.Dialog
            Dialog.setOnClickListener {
                bottomSheetDialog = BottomSheetDialog(requireContext())
                bottomSheetDialog.setContentView(R.layout.dialog)
                bottomSheetDialog.show()
                val button1 = bottomSheetDialog.findViewById<MaterialButton>(R.id.SaveButton)
                val button2 = bottomSheetDialog.findViewById<MaterialButton>(R.id.ShareButton)
                if (button1 != null) {
                    button1.setOnClickListener {
                        Toast.makeText(activity, "Article Saved", Toast.LENGTH_SHORT).show()
                        viewModel.insertNews(Article)
                        bottomSheetDialog.dismiss()
                    }
                }
                if (button2 != null) {
                    button2.setOnClickListener {
                        Toast.makeText(activity, "Sharing Article", Toast.LENGTH_SHORT).show()
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Hey, checkout this news : " + Article.url
                        )
                        intent.type = "text/plain"
                        startActivity(Intent.createChooser(intent, "Share with :"))
                        bottomSheetDialog.dismiss()
                    }
                }
            }
        }
    }

    fun RecyclerView_OnClickListener() {
        adapter.setOnItemClickListener { NewsModel ->
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            context?.let { customTabsIntent.launchUrl(it, Uri.parse(NewsModel.url)) }
        }
    }
}